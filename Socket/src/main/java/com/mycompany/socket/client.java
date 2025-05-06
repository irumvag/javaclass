package com.mycompany.socket;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {
        Scanner obj = new Scanner(System.in);
        String serverAddress = "localhost"; // Server IP or hostname
        int port = 22222; // Port to connect to

        Socket socket = new Socket(serverAddress, port);
        System.out.println("Connected to server");

        // Get input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String msg;
        while (true) {
            // Send message to server
            System.out.print("Enter message (or 'c' to 'q'): ");
            msg = obj.nextLine();
            
            if ("c".equalsIgnoreCase(msg)||"q".equalsIgnoreCase(msg)) {
                out.println(msg); // Notify server we're exiting
                break;
            }
            
            out.println(msg);

            // Read response from server
            String response = in.readLine();
            if (response == null || "c".equalsIgnoreCase(response)) {
                System.out.println("Server has ended the conversation");
                break;
            }
            System.out.println("Received from server: " + response);
        }

        // Close resources
        socket.close();
        obj.close();
        System.out.println("Connection closed");
    }
}