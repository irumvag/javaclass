package game.component;

import game.component.SoundManager;
import game.object.Bullet;
import game.object.Rocket;
import game.object.player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComponent;

public class PanelGame extends JComponent {

    //game FPS stands for Frames per second
    private final int FPS = 60;
    private int width;
    private int height;
    private int shotTime;
    private Graphics2D g2;
    private BufferedImage image;
    private Thread thread;
    private boolean start = true;
    private final int TARGET_TIME = 1000000000 / 60;
    private player player1;
    private key k1;
    private List<Bullet> bullets;
    private List<Rocket> rockets;
    public int marks=0;
    public int lost;

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
        SoundManager.playSound("/game/sound/game.wav");
        thread.start();
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
        player1.changeLocation(150, 150);
        rockets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    addRocket();
                    sleep(3000);
                }
            }
        }).start();
    }

    private void initKeyBoard() {
        k1 = new key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    k1.setKey_left(true);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    k1.setKey_right(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    k1.setKey_space(true);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    k1.setKey_j(true);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    k1.setKey_k(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    k1.setKey_left(false);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    k1.setKey_right(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    k1.setKey_space(false);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    k1.setKey_j(false);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    k1.setKey_k(false);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (start) {
                    float angle = player1.getAngle();
                    if (k1.isKey_left()) {
                        angle -= s;
                    }
                    if (k1.isKey_right()) {
                        angle += s;
                    }
                    if (k1.isKey_j() || k1.isKey_k()) {
                        if (shotTime == 0) {
                            if (k1.isKey_j()) {
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
                    if (k1.isKey_space()) {
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
                                lost++;
                            }
                        }
                    }
                    sleep(5);
                }
            }
        }).start();
    }

    private void initBullet() {
        bullets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < bullets.size(); i++) {
                        Bullet bullet = bullets.get(i);
                        if (bullet != null) {
                            bullet.update();
                            checkBullets(bullet);
                            if (!bullet.check(width, height)) {
                                bullets.remove(bullet);
                            }
                        } else {
                            bullets.remove(bullet);

                        }
                    }
                    sleep(1);
                }
            }
        }).start();
    }
    private void checkBullets(Bullet bullet)
    {
        for(int i=0;i<rockets.size();i++)
        {
            Rocket rocket=rockets.get(i);
            if(rocket!=null)
            {
                Area area=new Area(bullet.getShape());
                area.intersect(rocket.getShape());
                if(!area.isEmpty())
                {
                    rockets.remove(rocket);
                    bullets.remove(bullet);
                    marks++;
                    SoundManager.playSound("/game/sound/enemyHit.wav");
                }
            }
        }
    }
    private void drawBackground() {
        g2.setColor(new Color(255, 208, 0));
        g2.fillRect(0, 0, width, height);
    }

    private void drawGame() {
        player1.draw(g2);
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
}
