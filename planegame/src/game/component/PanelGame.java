package game.component;

import game.component.SoundManager;
import game.object.Effect;
import game.object.Bullet;
import game.object.Rocket;
import game.object.player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.sql.*;
import java.util.Iterator;
import javax.swing.SwingWorker;

public class PanelGame extends JComponent {

    //game FPS stands for Frames per second
    private int userId; // Add this
    private boolean gameOver = false;
    private final int FPS = 60;
    private int width;
    private int height;
    private int shotTime;
    private Graphics2D g2;
    public BufferedImage image;
    private Thread thread;
    private boolean start = true;
    private final int TARGET_TIME = 1000000000 / 60;
    private player player1;
    private key k1;
    private List<Bullet> bullets = new java.util.concurrent.CopyOnWriteArrayList<>();
    private List<Rocket> rockets = new java.util.concurrent.CopyOnWriteArrayList<>();
    public int marks = 0;
    public int lost;
    private float starTwinklePhase = 0;
    private final Color SPACE_COLOR = new Color(12, 7, 32); // Deep space purple
    private final Color NEBULA_COLOR_1 = new Color(70, 40, 110, 150); // Purple
    private final Color NEBULA_COLOR_2 = new Color(40, 70, 110, 100); // Blue
    // Add these class variables at the top
    private List<Point> foregroundStars;
    private List<Point> mediumStars;
    private List<Point> distantStars;
    private static final int MAX_LOST = 5;
    private List<Effect> boomEffects;
    private int score = 0;
    private String username;
    private boolean paused = false;
    private boolean showScoreTable = false;
    private Rectangle pauseButtonBounds;
    private Rectangle scoresButtonBounds;
    private List<Object[]> scoreData;

    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();
                    drawBackground();
                    drawGame();
                    if (!gameOver && !paused) {
                        checkPlayerCollision();
                        updateGameState();
                        checkGameOver();
                    }
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1000000;
                        sleep(sleep);
                        System.out.println(sleep);
                    }
                }
            }
        });

        initObjectGame();
        initKeyBoard();
        initBullet();
        initStars();
        initUI();

        if (!SoundManager.isGameSoundPlaying()) { 
            SoundManager.playSound("/game/sound/game.wav");
        }
        thread.start();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        initStars();  // Regenerate stars when window size changes
    }

    private void initUI() {
        // Initialize button positions
        pauseButtonBounds = new Rectangle(10, 10, 80, 30);
        scoresButtonBounds = new Rectangle(100, 10, 120, 30);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleUIClicks(e.getPoint());
            }
        });
    }

    private void handleUIClicks(Point clickPoint) {
        if (pauseButtonBounds.contains(clickPoint)) {
            togglePause();
        } else if (scoresButtonBounds.contains(clickPoint)) {
            showScoreTable = !showScoreTable;
            if (showScoreTable) {
                fetchScores();
            }
        }
    }

    private void drawUI(Graphics2D g2) {
        // Draw username
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Player: " + username, 20, height - 30);

        // Draw pause button
        drawButton(g2, pauseButtonBounds, paused ? "RESUME" : "PAUSE");

        // Draw scores button
        drawButton(g2, scoresButtonBounds, "HIGH SCORES");

        // Draw overlays
        if (paused) {
            drawPauseOverlay(g2);
        }
        if (showScoreTable) {
            drawScoreTable(g2);
        }
    }

    private void updateGameState() {
        // Existing game state updates (player, bullets, rockets)
        player1.update();
        updateBullets();
        updateRockets();
        checkCollisions();
    }

    private void updateBullets() {
        bullets.removeIf(bullet -> {
            bullet.update();
            return !bullet.check(width, height);
        });
    }

    private void updateRockets() {
        rockets.removeIf(rocket -> {
            rocket.update();
            if (!rocket.check(width, height)) {
                lost++;
                if (lost >= MAX_LOST) {
                    gameOver = true;
                    saveScore();
                    showGameOver();
                }
                return true; // Remove the rocket
            }
            return false; // Keep the rocket
        });
    }

    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Rocket> rocketsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            Area bulletArea = new Area(bullet.getShape());

            for (Rocket rocket : rockets) {
                Area rocketArea = new Area(rocket.getShape());

                try {
                    rocketArea.intersect(bulletArea);
                    if (!rocketArea.isEmpty()) {
                        bulletsToRemove.add(bullet);
                        rocketsToRemove.add(rocket);
                        marks += 10;
                        SoundManager.playSound("/game/sound/enemyHit.wav");
                        break;
                    }
                } catch (Exception e) {
                    // Handle exceptions
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        rockets.removeAll(rocketsToRemove);
    }

    private void drawButton(Graphics2D g2, Rectangle bounds, String text) {
        g2.setColor(new Color(30, 30, 30, 200));
        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textX = bounds.x + (bounds.width - fm.stringWidth(text)) / 2;
        int textY = bounds.y + (bounds.height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    private void drawPauseOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        String text = "PAUSED";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, width / 2 - textWidth / 2, height / 2);
    }

    private void drawScoreTable(Graphics2D g2) {
        // Semi-transparent background
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(width / 2 - 200, 50, 400, 300, 20, 20);

        // Table header
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Top Scores", width / 2 - 50, 80);

        // Draw scores
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        if (scoreData != null) {
            int yPos = 120;
            for (Object[] row : scoreData) {
                String scoreLine = String.format("%-15s %-6d %tF",
                        row[0], row[1], row[2]);
                g2.drawString(scoreLine, width / 2 - 180, yPos);
                yPos += 30;
            }
        }
    }

    private void fetchScores() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                String query = "SELECT u.username, s.score, s.played_at "
                        + "FROM scores s "
                        + "JOIN users u ON s.user_id = u.id "
                        + "ORDER BY s.score DESC LIMIT 6";

                List<Object[]> results = new ArrayList<>();
                try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                    while (rs.next()) {
                        results.add(new Object[]{
                            rs.getString("username"),
                            rs.getInt("score"),
                            rs.getTimestamp("played_at")
                        });
                    }
                }
                return results;
            }

            @Override
            protected void done() {
                try {
                    scoreData = get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    public void togglePause() {
        paused = !paused;
        if (paused) {
            SoundManager.pauseAll();
        } else {
            SoundManager.resumeAll();
        }

    }

    private void addRocket() {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Rocket rocket = new Rocket();
        rocket.changeLocation(0, locationY);
        rocket.changeAngle(0);
        rockets.add(rocket);
        int locationY2 = ran.nextInt(height - 50) + 25;
        Rocket rocket2 = new Rocket();
        rocket2.changeLocation(width, locationY2);
        rocket2.changeAngle(180);
        rockets.add(rocket2);
    }

    private void initObjectGame() {
        player1 = new player();
        player1.changeLocation(1366/2, 769/2);
        rockets = new ArrayList<>();
        boomEffects = new ArrayList<>();
        lost = 0; // Initialize lost counter

        new Thread(() -> {
            while (start && !gameOver) { // Add gameOver check
                if (!paused) {
                    addRocket();
                }
                sleep(3000);
            }
        }).start();
    }

    private void initKeyBoard() {
        k1 = new key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    k1.setKey_left(true);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    k1.setKey_right(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    k1.setKey_space(true);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    k1.setKey_w(true);
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    k1.setKey_e(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    k1.setKey_left(false);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    k1.setKey_right(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    k1.setKey_space(false);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    k1.setKey_w(false);
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    k1.setKey_e(false);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (start) {
                    if (!paused) { // Add this check
                        float angle = player1.getAngle();
                        if (k1.isKey_left()) {
                            angle -= s;
                        }
                        if (k1.isKey_right()) {
                            angle += s;
                        }
                        if (k1.isKey_w() || k1.isKey_e()) {
                            if (shotTime == 0) {
                                if (k1.isKey_w()) {
                                    bullets.add(0, new Bullet(player1.getX(), player1.getY(), player1.getAngle(), 5, 3f));
                                    SoundManager.playSound("/game/sound/shoot.wav");
                                } else {
                                    bullets.add(0, new Bullet(player1.getX(), player1.getY(), player1.getAngle(), 15, 3f));
                                    SoundManager.playSound("/game/sound/shoot.wav");
                                }
                            }
                            shotTime++;
                            if (shotTime == 15) {
                                shotTime = 0;
                            }
                        } else {
                            shotTime = 0;
                        }
                        if (k1.isKey_space() && !paused) {
                            player1.speedUp();
                        } else {
                            player1.speedDown();
                        }
                        player1.update();
                        player1.changeAngle(angle);
                        for (int i = 0; i < rockets.size(); i++) {
                            Rocket rocket = rockets.get(i);
                            if (rocket != null) {
                                rocket.update();
                                if (!rocket.check(width, height)) {
                                    rockets.remove(rocket);
                                } else {
                                    if (player1.isAlive()) {
                                        checkPlayer(rocket);
                                    }
                                }
                            }
                        }
                    }
                    sleep(5);
                }
            }
        }
        ).start();
    }

    private void checkPlayer(Rocket rocket) {
        if (rocket != null) {
            Area area = new Area(player1.getShape());
            area.intersect(rocket.getShape());
            if (!area.isEmpty()) {
                double rocketHp = rocket.getHP();
                if (!rocket.updateHP(player1.getHP())) {
                    rockets.remove(rocket);
                    //sound.soundDestroy();
                    double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                    double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                }
                if (!player1.updateHP(rocketHp)) {
                    player1.setAlive(false);
                    //sound.soundDestroy();
                    double x = player1.getX() + player1.PLAYER_SIZE / 2;
                    double y = player1.getY() + player1.PLAYER_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                }

            }
        }

    }

    private void initBullet() {
        bullets = new java.util.concurrent.CopyOnWriteArrayList<>();
        new Thread(() -> {
            while (start) {
                bullets.removeIf(bullet -> {
                    bullet.update();
                    return !bullet.check(width, height);
                });
                sleep(1);
            }
        }).start();
    }

// Add this initialization method
    private void initStars() {
        foregroundStars = generateStarPositions(150);
        mediumStars = generateStarPositions(300);
        distantStars = generateStarPositions(450);
    }

    private List<Point> generateStarPositions(int count) {
        List<Point> stars = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            stars.add(new Point(
                    (int) (Math.random() * width),
                    (int) (Math.random() * height)
            ));
        }
        return stars;
    }

    private void drawStars(Graphics2D g2) {
        // Reduced twinkle speed by 50%
        starTwinklePhase += 0.01f;  // Slowed down from 0.02f
        drawStarLayer(g2, foregroundStars, 1.5f, 0.9f);  // Foreground
        drawStarLayer(g2, mediumStars, 1.0f, 0.6f);       // Medium
        drawStarLayer(g2, distantStars, 0.5f, 0.3f);      // Distant
    }

    private void drawStarLayer(Graphics2D g2, List<Point> stars, float size, float alpha) {
        for (Point star : stars) {
            float pulse = (float) (Math.sin(starTwinklePhase + star.x + star.y) * 0.2f + 0.8f);
            Color starColor = new Color(1, 1, 1, Math.min(1, alpha * pulse));

            g2.setColor(starColor);
            g2.fillRect(star.x, star.y, (int) size, (int) size);
        }
    }

    private void drawBackground() {
        // Reinitialize stars if window size changes
        if (foregroundStars == null || width != getWidth() || height != getHeight()) {
            width = getWidth();
            height = getHeight();
            initStars();
        }
        GradientPaint gradient = new GradientPaint(
                width / 2f, 0, new Color(25, 15, 65),
                width / 2f, height, SPACE_COLOR
        );

        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        drawStars(g2);
    }

    private void drawGame() {
        player1.draw(g2);
        drawUI(g2);
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet != null) {
                bullet.draw(g2);
            }
        }
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                rocket.draw(g2);
            }
        }
        if (gameOver) {
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.setColor(Color.RED);
            String text = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, width / 2 - textWidth / 2, height / 2);
        }
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + marks, width - 150, 30);
        g2.drawString("Missed: " + lost + "/" + MAX_LOST, width - 150, 60);

        // Draw game over text if needed
        if (gameOver) {
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.setColor(Color.RED);
            String text = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, width / 2 - textWidth / 2, height / 2);
        }
        if (paused) {
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            String text = "PAUSED";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, width / 2 - textWidth / 2, height / 2);
        }

    }

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            System.out.print(e);
        }

    }
// Modify constructor to accept user ID

    public PanelGame(int userId, String uname) {
        this.userId = userId;
        this.username = uname;
    }
// Add this method to check player collision

    private void checkPlayerCollision() {
        Area playerArea = new Area(new Ellipse2D.Double(
                player1.getX(), player1.getY(),
                player.PLAYER_SIZE, player.PLAYER_SIZE
        ));

        // Use thread-safe iteration
        rockets.forEach(rocket -> {
            if (rocket != null && playerArea.intersects(rocket.getShape().getBounds2D())) {
                gameOver = true;
                saveScore();
                showGameOver();
            }
        });
    }

    private void saveScore() {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO scores (user_id, score) VALUES (?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, marks);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
// Add this method to show game over screen

    private void checkGameOver() {
        if (lost >= MAX_LOST) {
            gameOver = true;
            SoundManager.pauseAll();
            saveScore();
            showGameOver();
        }
    }

    private void showGameOver() {
        start = false; // Stop game loop
        javax.swing.SwingUtilities.invokeLater(() -> {
            JPanel gameOverPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;

                    // Dark overlay
                    g2.setColor(new Color(0, 0, 0, 220));
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Main game over text
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 48));
                    String gameOverText = "GAME OVER";
                    int gameOverWidth = g2.getFontMetrics().stringWidth(gameOverText);
                    g2.drawString(gameOverText, getWidth() / 2 - gameOverWidth / 2, getHeight() / 2 - 80);

                    // Score display
                    g2.setFont(new Font("Arial", Font.BOLD, 32));
                    String scoreText = "Your Score: " + marks;
                    int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
                    g2.drawString(scoreText, getWidth() / 2 - scoreWidth / 2, getHeight() / 2 - 20);

                    // Missed rockets
                    g2.setFont(new Font("Arial", Font.PLAIN, 24));
                    String missedText = "Missed Rockets: " + lost + "/5";
                    int missedWidth = g2.getFontMetrics().stringWidth(missedText);
                    g2.drawString(missedText, getWidth() / 2 - missedWidth / 2, getHeight() / 2 + 20);

                    // Thank you message
                    g2.setFont(new Font("Arial", Font.ITALIC, 22));
                    String thanksText = "Thank you for playing our game!";
                    int thanksWidth = g2.getFontMetrics().stringWidth(thanksText);
                    g2.drawString(thanksText, getWidth() / 2 - thanksWidth / 2, getHeight() / 2 + 80);

                    // Restart instruction
                    g2.setFont(new Font("Arial", Font.PLAIN, 18));
                    String restartText = "Click anywhere to play again";
                    int restartWidth = g2.getFontMetrics().stringWidth(restartText);
                    g2.drawString(restartText, getWidth() / 2 - restartWidth / 2, getHeight() / 2 + 120);
                }
            };

            gameOverPanel.setBounds(0, 0, width, height);
            gameOverPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    remove(gameOverPanel);
                    resetGame();
                    repaint();
                }
            });
            add(gameOverPanel);
            repaint();
        });
    }

    private void resetGame() {
        marks = 0;
        lost = 0;
        gameOver = false;
        bullets.clear();
        rockets.clear();
        player1.changeLocation(150, 150);
        start = true;
        thread = new Thread(() -> {
            start();
            // ... existing run() code
        });
        thread.start();
    }
}
