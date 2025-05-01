package game.test;
import game.component.DBConnection;
import game.main.play;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class testlogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    
    public testlogin() {
        setTitle("Game Login");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);
        
        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        
        btnLogin.addActionListener(this::performLogin);
        btnRegister.addActionListener(e -> showRegisterForm());
        
        panel.add(btnLogin);
        panel.add(btnRegister);
        
        add(panel, BorderLayout.CENTER);
    }

    private void performLogin(ActionEvent e){
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        try (var conn = DBConnection.getConnection()) {
            String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                SwingUtilities.invokeLater(() -> {
                    new play(userId).setVisible(true);
                    dispose();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showRegisterForm() {
        JTextField regUser = new JTextField();
        JPasswordField regPass = new JPasswordField();
        
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Username:"));
        panel.add(regUser);
        panel.add(new JLabel("Password:"));
        panel.add(regPass);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Register", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String username = regUser.getText();
            String password = new String(regPass.getPassword());
            
            try (var conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new testlogin().setVisible(true));
    }
}