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
import javax.swing.table.DefaultTableModel;

public class Feedback extends JFrame {
     private JTextField emailField, firstNameField, lastNameField;
    private JTextArea commentField;
    private JComboBox<String> genderBox;
    private JButton submitButton, displayButton;
    private JTable table;
    private DefaultTableModel model;

    public Feedback() {
        setTitle("Feedback Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Create components
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        emailField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        commentField = new JTextArea();

        submitButton = new JButton("Submit");
        displayButton = new JButton("Display Result");
        displayButton.setEnabled(false); // Initially disabled

        // JTable setup
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"First Name", "Last Name", "Email", "Gender", "Comment"});
        table = new JTable(model);

        // Add components
        add(new JLabel("First Name:"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Gender:"));
        add(genderBox);
        add(new JLabel("Comment (50-100 chars):"));
        add(new JScrollPane(commentField));
        add(submitButton);
        add(displayButton);

        // Email validation and enable button
        emailField.addActionListener(e -> {
            if (MasterClass.checkEmail(emailField.getText())) {
                displayButton.setEnabled(true);
            } else {
                displayButton.setEnabled(false);
            }
        });

        // Submit button event
        submitButton.addActionListener(e -> {
            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String gender = (String) genderBox.getSelectedItem();
            String comment = commentField.getText();

            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || comment.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (comment.length() < 50 || comment.length() > 100) {
                JOptionPane.showMessageDialog(null, "Comment must be between 50-100 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            model.addRow(new Object[]{firstName, lastName, email, gender, comment});
        });

        //displayButton.addActionListener(e -> new ResultPage());

        setVisible(true);
    }

}
