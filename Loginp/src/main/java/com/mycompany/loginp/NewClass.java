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
import java.awt.event.*;

// NewClass containing reusable functions
public class NewClass {



    public static boolean checkEmail(String email) {
        return email.matches(".*@(gmail\\.com|yahoo\\.com|hotmail\\.com)");
    }

    public static int getStringLength(String txt) {
        return txt.length();
    }

    public static String changeToUpper(String txt) {
        return txt.toUpperCase();
    }
}

// LoginPage GUI
class LoginPage extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(300, 200);
        setLayout(new GridLayout(4, 1, 5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);

        setLocationRelativeTo(null); // Center window
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String password = new String(passwordField.getPassword());
        if (password.length() < 8 || password.length() > 12 || !password.matches("[A-Z]+")) {
            JOptionPane.showMessageDialog(this, "Invalid password! Must be 8-12 uppercase letters.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            new Feedback().setVisible(true);
            this.dispose();
        }
        usernameField.setText("");
        passwordField.setText("");
    }
}

// Feedback GUI
class Feedback extends JFrame implements ActionListener {
    JTextField emailField, firstNameField, lastNameField;
    JTextArea commentArea;
    JButton submitButton, displayButton;
    JComboBox<String> genderBox;

    public Feedback() {
        setTitle("Feedback Form");
        setSize(400, 450);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        emailField = new JTextField(20);
        emailField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                submitButton.setEnabled(NewClass.checkEmail(emailField.getText()));
            }
        });
        add(emailField, gbc);

        gbc.gridy++;
        add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        firstNameField = new JTextField(15);
        add(firstNameField, gbc);

        gbc.gridy++;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        lastNameField = new JTextField(15);
        add(lastNameField, gbc);

        gbc.gridy++;
        add(new JLabel("Gender:"), gbc);
        gbc.gridy++;
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        add(genderBox, gbc);

        gbc.gridy++;
        add(new JLabel("Comment:"), gbc);
        gbc.gridy++;
        commentArea = new JTextArea(4, 20);
        add(new JScrollPane(commentArea), gbc);

        gbc.gridy++;
        submitButton = new JButton("Submit");
        submitButton.setEnabled(false);
        submitButton.addActionListener(this);
        add(submitButton, gbc);

        gbc.gridy++;
        displayButton = new JButton("DISPLAY RESULT");
        displayButton.setEnabled(false);
        displayButton.addActionListener(this);
        add(displayButton, gbc);

        setLocationRelativeTo(null); // Center window
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (emailField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || commentArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (commentArea.getText().length() < 50 || commentArea.getText().length() > 100) {
                JOptionPane.showMessageDialog(this, "Comment must be 50-100 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                displayButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Submission successful! You can now display the result.");
            }
        } else if (e.getSource() == displayButton) {
            new ResultPage(emailField.getText(), firstNameField.getText(), lastNameField.getText(), (String) genderBox.getSelectedItem(), commentArea.getText()).setVisible(true);
            this.dispose();
        }
    }
}

// ResultPage GUI
class ResultPage extends JFrame {
    public ResultPage(String email, String firstName, String lastName, String gender, String comment) {
        setTitle("Result Page");
        setSize(300, 300);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Email: " + email));
        add(new JLabel("First Name: " + firstName));
        add(new JLabel("Last Name: " + lastName));
        add(new JLabel("Gender: " + gender));
        add(new JLabel("Comment: " + comment));

        setLocationRelativeTo(null); // Center window
    }
}
