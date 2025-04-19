package com.mycompany.assingment.utils;

import java.sql.*;

public class DBconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/servlet";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        System.out.println("Loading MySQL driver...");
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Connecting to: " + URL);
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
