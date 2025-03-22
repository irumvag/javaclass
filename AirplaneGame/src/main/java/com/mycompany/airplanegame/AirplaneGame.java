/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.airplanegame;
/**
 *
 * @author Chairman
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
public class AirplaneGame extends JPanel implements KeyListener, Runnable {
    private int airplaneX = 100, airplaneY = 250; // Airplane position
    private int score = 0;
    private int speed = 5;
    private boolean gameOver = false;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();
    public AirplaneGame() {
        JFrame frame = new JFrame("Airplane Game");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);
        // Start the game loop in a separate thread
        new Thread(this).start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the airplane
        g.setColor(Color.BLUE);
        g.fillRect(airplaneX, airplaneY, 50, 50);
        // Draw obstacles
        g.setColor(Color.RED);
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
        // Draw the score and speed
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Speed: " + speed, 10, 40);

        // Display game over message
        if (gameOver) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", 300, 250);
            g.drawString("Final Score: " + score, 280, 300);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            airplaneY -= speed; // Move airplane up
        } else if (key == KeyEvent.VK_DOWN) {
            airplaneY += speed; // Move airplane down
        } else if (key == KeyEvent.VK_LEFT) {
            airplaneX -= speed; // Move airplane left
        } else if (key == KeyEvent.VK_RIGHT) {
            airplaneX += speed; // Move airplane right
        } else if (key == KeyEvent.VK_U) {
            speed += 2; // Increase speed
        } else if (key == KeyEvent.VK_D) {
            speed -= 2; // Decrease speed
            if (speed < 1) speed = 1;
        }
        repaint(); // Redraw the game
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void run() {
        // Game loop
        while (!gameOver) {
            // Add new obstacles randomly
            if (random.nextInt(100) < 10) { // 10% chance to spawn an obstacle
                obstacles.add(new Obstacle(800, random.nextInt(550), 30, 30));
            }
            // Move obstacles
            for (Obstacle obstacle : obstacles) {
                obstacle.x -= speed; // Move obstacle left
                // Check for collisions
                if (airplaneX < obstacle.x + obstacle.width &&
                    airplaneX + 50 > obstacle.x &&
                    airplaneY < obstacle.y + obstacle.height &&
                    airplaneY + 50 > obstacle.y) {
                    gameOver = true; // End the game
                }
            }
            // Remove obstacles that are off-screen
            obstacles.removeIf(obstacle -> obstacle.x + obstacle.width < 0);
            // Increment score
            score += 1;
            // Repaint the game
            repaint();
            // Pause for a short time to control the game speed
            try {
                Thread.sleep(30); // Adjust this value to control game speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new AirplaneGame();
    }
}
class Obstacle {
    int x, y, width, height;
    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}