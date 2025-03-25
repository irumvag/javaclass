package com.mycompany.socket;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class client {
    public static void main(String[] args) throws IOException {
        Scanner obj=new Scanner(System.in);
        String serverAddress = "localhost"; // Server IP or hostname
        int port = 22222; // Port to connect to

        Socket socket = new Socket(serverAddress, port);
        System.out.println("Connected to server");

        // Get input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send message to server
        System.out.print("Enter message:");
        String msg=obj.nextLine();
        out.println(msg);

        // Read response from server
        String response = in.readLine();
        System.out.println("Received from server: " + response);
        // Close resources
        socket.close();
    }

    private static String readline() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
