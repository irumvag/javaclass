/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package books;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Chairman
 */
public class calcurator {
    private static JTextField display;
    private static String currentInput = "";
    private static double firstNumber = 0;
    private static String operator = "";

    public static void main(String[] args) {
        // 1. Set up the frame
        JFrame frame = new JFrame("Calculator");
        frame.setLayout(new BorderLayout());

        // 2. Create the display
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false); // Prevent typing
        frame.add(display, BorderLayout.NORTH);

        // 3. Create buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4));
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Handle button clicks
    static class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                    currentInput += command;
                    display.setText(currentInput);
                    break;

                case "+": case "-": case "*": case "/":
                    if (!currentInput.isEmpty()) {
                        firstNumber = Double.parseDouble(currentInput);
                        operator = command;
                        currentInput = "";
                    }
                    break;

                case "=":
                    if (!currentInput.isEmpty() && !operator.isEmpty()) {
                        double secondNumber = Double.parseDouble(currentInput);
                        double result = calculate(firstNumber, secondNumber, operator);
                        display.setText(String.valueOf(result));
                        currentInput = String.valueOf(result);
                        operator = "";
                    }
                    break;

                case "C":
                    currentInput = "";
                    firstNumber = 0;
                    operator = "";
                    display.setText("");
                    break;
            }
        }
    }

    // Perform calculations
    private static double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            default: return 0;
        }
    }
}
    
