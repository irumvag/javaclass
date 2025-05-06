package com.mycompany.assingment.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	static String url="jdbc:mysql://localhost:3306/servlet?";
	static String user="root";
	static String pass="";
	static Connection con=null;
	public static Connection getConnection()
	{
	try {
	Class.forName("com.mysql.cj.jdbc.Driver");
	con=DriverManager.getConnection(url, user, pass);
	return con;
	}catch(Exception e)
	{
		e.printStackTrace();
		return con;
	}
	}

}
