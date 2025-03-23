package com.mycompany.masterclass;
import java.io.*;
import java.sql.*;
public class DBConnection {
    public  Connection con=null;
    public  Statement stm=null;
    public  ResultSet rset=null;
    public  PreparedStatement pstmt=null;
    private static final String user="root",passd="";
    public Connection getConnection()
    {
        String url = "jdbc:mysql://localhost:3306/schoollab?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String passd = ""; 
        try {
            // Load MySQL JDBC Driver (Some versions auto-load, but better to include)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish Connection
            Connection con = DriverManager.getConnection(url, user, passd);
            System.out.println("Connected to database!");
            // Close connection
           // con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
      return con;
    }
}
