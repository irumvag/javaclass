/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.database;

/**
 *
 * @author Chairman
 */
import java.sql.*;
public class testingfile {
    public static void main(String[] args)
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");//registering driver
            String user="root";
            String password="";
            Connection con=null;
            con=DriverManager.getConnection("jdbc:msql://locakhost:3360/schoollab",user,password);//connecting connection
            //DriverManager.registerDriver();
            Statement sttm=con.createStatement();
            String sql="Select * from student";
            ResultSet rl=sttm.executeQuery(sql);
            con.close();//close connection
        }catch(Exception e)
        {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
}
