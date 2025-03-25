/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginp;

/**
 *
 * @author Kva
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage  extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
       
        setTitle("Login Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        //components
        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        // Add components to frame
        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(loginButton);

        // Add button click event
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check password validity
                if (password.length() >= 8 && password.length() <= 12 && password.equals(password.toUpperCase())) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    new Feedback(); // Open Feedback form
                    dispose(); // Close LoginPage
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Password! Must be 8-12 characters and uppercase.", "Error", JOptionPane.ERROR_MESSAGE);
                    usernameField.setText(""); // Clear fields
                    passwordField.setText("");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage(); // Run the login page
    }

}
