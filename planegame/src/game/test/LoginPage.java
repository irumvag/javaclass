package game.test;

import game.component.DBConnection;
import game.main.play;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login Page");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background panel
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(25, 15, 65),
                        0, getHeight(), new Color(12, 7, 32)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(null);
        setContentPane(background);

        // Login Form Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(450, 150, 450, 500);
        loginPanel.setBackground(new Color(0, 0, 0, 150));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        background.add(loginPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to Space Battle");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Space

        // Username Field
        usernameField = new JTextField();
        styleField(usernameField, "Username");
        loginPanel.add(usernameField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password Field
        passwordField = new JPasswordField();
        styleField(passwordField, "Password");
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Guest Button
        JButton guestButton = new JButton("Guest");
        styleButton(guestButton);
        loginPanel.add(guestButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Register Button
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        loginPanel.add(registerButton);

        // Action Listeners
        loginButton.addActionListener(e -> login());
        guestButton.addActionListener(e -> guestLogin());
        registerButton.addActionListener(e -> openRegisterPage());

        setVisible(true);
    }

    private void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBackground(new Color(255, 255, 255, 80));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setOpaque(false);
        field.putClientProperty("JTextField.placeholderText", placeholder);  // requires Java 9+
    }

    private void styleButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setBackground(new Color(0, 0, 0, 150));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 60));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0, 150));
            }
        });
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new play(100).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void guestLogin() {
        new play(100).setVisible(true);
        dispose();
    }

    private void openRegisterPage() {
        JOptionPane.showMessageDialog(this, "Register page coming soon!");
        // Here you will call new RegisterPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
