package game.main;

import game.component.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.mindrot.jbcrypt.BCrypt;

public class loginFrame extends javax.swing.JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton guestButton;
    private JButton loginbutton;

    public loginFrame() {
        initComponents();
        setTitle("Login");
        setSize(1366, 769);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        // Background panel
        BackgroundPanel background = new BackgroundPanel();
        setContentPane(background);
        background.setLayout(null);  // Important: Set layout so we can place components manually

        // Login Panel (glass effect)
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 255, 255, 200)); // semi-transparent white
        loginPanel.setBounds(450, 150, 460, 450);
        loginPanel.setLayout(null);
        background.add(loginPanel);

        JLabel titleLabel = new JLabel("🚀 Welcome to Airplane Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(30, 20, 400, 50);
        loginPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setBounds(50, 100, 100, 30);
        loginPanel.add(usernameLabel);

        usernameField = new RoundedTextField(20);
        usernameField.setBounds(50, 140, 360, 40);
        loginPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setBounds(50, 200, 100, 30);
        loginPanel.add(passwordLabel);

        passwordField = new RoundedPasswordField(20);
        passwordField.setBounds(50, 240, 360, 40);
        loginPanel.add(passwordField);

        loginButton = new RoundedButton("Login");
        loginButton.setBounds(50, 320, 150, 50);
        loginPanel.add(loginButton);

        guestButton = new RoundedButton("Guest");
        guestButton.setBounds(260, 320, 150, 50);
        loginPanel.add(guestButton);

        loginbutton = new RoundedButton("Register");
        loginbutton.setBounds(150, 390, 150, 50);
        loginPanel.add(loginbutton);

        loginButton.addActionListener(e -> login());
        guestButton.addActionListener(e -> guestLogin());
        loginbutton.addActionListener(e -> openRegisterPage());
        setVisible(true);
    }

    // Custom rounded text field
    class RoundedTextField extends JTextField {

        private int arcSize;

        public RoundedTextField(int arcSize) {
            super();
            this.arcSize = arcSize;
            setOpaque(false);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Custom rounded password field
    class RoundedPasswordField extends JPasswordField {

        private int arcSize;

        public RoundedPasswordField(int arcSize) {
            super();
            this.arcSize = arcSize;
            setOpaque(false);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Custom rounded button
    class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setFont(new Font("Arial", Font.BOLD, 18));
            setForeground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Background with stars
    class BackgroundPanel extends JPanel {

        private List<Point> stars;

        public BackgroundPanel() {
            stars = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                stars.add(new Point((int) (Math.random() * 1366), (int) (Math.random() * 769)));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, new Color(25, 15, 65), 0, getHeight(), new Color(12, 7, 32)));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(Color.WHITE);
            for (Point star : stars) {
                g2.fillOval(star.x, star.y, 2, 2);
            }
        }
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill username and password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ?")) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                // Verify password (using BCrypt)
                if (BCrypt.checkpw(password, storedHash)) {
                    // Correct password
                    JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new Instruction(rs.getInt("id"),username).setVisible(true);
                    rs.close();
                    dispose();
                    return;
                } else {
                    // Incorrect password
                    JOptionPane.showMessageDialog(this, "Invalid password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    rs.close();
                    return; // Don't continue execution
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username and password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void guestLogin() {
        new Instruction(5,"Guests").setVisible(true);
        dispose();
    }

    private void openRegisterPage() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        //JOptionPane.showMessageDialog(this, "Register page coming soon!");
        // Here you will call new RegisterPage();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DBConnection.getConnection();) {
            // Check if username or email already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username Exists!", "Register Failed", JOptionPane.ERROR_MESSAGE);
                rs.close();
                return;
            }
            // Insert user into DB
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)"
            );
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);

            int result = insertStmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Register Successful!\n  Login...", "Success", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");               
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Register failed!", "Register Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1366, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 769, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new loginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
