package com.mycompany.socket;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class server {
    public static void main(String[] args) throws IOException {
        Scanner obj = new Scanner(System.in);
        int port = 22222;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String message;
        while (true) {
            // Read message from client
            message = in.readLine();
            if (message == null || "exit".equalsIgnoreCase(message)) {
                System.out.println("Client has ended the conversation");
                break;
            }
            System.out.println("Received from client: " + message);

            // Send response to client
            System.out.print("Enter message (or 'C' to 'q' ): ");
            String msg = obj.nextLine();
            
            if ("c".equalsIgnoreCase(msg)||"q".equalsIgnoreCase(msg)) {
                out.println(msg); // Notify client we're exiting
                break;
            }
            
            out.println(msg);
        }

        // Close resources
        socket.close();
        serverSocket.close();
        obj.close();
        System.out.println("Server shutdown");
    }
}