package com.mycompany.assingment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.assingment.model.course;
import com.mycompany.assingment.utils.DBConnect;

public class CourseDao {
	Connection conn;
	
	public CourseDao()
	{
		conn=DBConnect.getConnection();
		
	}
	public void addCourse(course c)
	{
		try {
			PreparedStatement pst=conn.prepareStatement("INSERT INTO course(coursename,fees,category,duration) values(?,?,?,?)");
			pst.setString(1,c.getCoursename());
			pst.setDouble(2, c.getFees());
			pst.setString(3,c.getCategory());
			pst.setInt(4, c.getDuration());
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<course> getAllCourseces()
	{
		List<course> cs=new ArrayList<course>();
		try
		{
			Statement stm=conn.createStatement();
			ResultSet rs=stm.executeQuery("SELECT * FROM `course`");
			while(rs.next())
			{
				course c=new course();
				c.setCoursename(rs.getString("coursename"));
				c.setCourseid(rs.getInt("courseid"));
				c.setCategory(rs.getString("category"));
				c.setDuration(rs.getInt("duration"));
				c.setFees(rs.getDouble("fees"));
				cs.add(c);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		return cs;
	}
}
