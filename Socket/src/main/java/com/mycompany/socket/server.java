package com.mycompany.socket;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class server {
    public static void main(String[] args) throws IOException{
        Scanner obj=new Scanner(System.in);
        int port=22222;
        ServerSocket serverSocket= new ServerSocket(port);
        System.out.println("Server is listerning on port "+port);
        
        Socket socket = serverSocket.accept();
        System.out.println("Client connected");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        String message = in.readLine();
        System.out.println("Received from client: "+ message);
        System.out.print("Enter message:");
        String msg=obj.nextLine();
        out.println(msg);
        socket.close();
        serverSocket.close();
    }
}
