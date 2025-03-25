package com.mycompany.masterclass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
public class register {
   public static void main(String[] args){
        Scanner obj=new Scanner(System.in);
        String uname="";
        String pass="";
        int userid=0;
        boolean tr=true;
        boolean tr1=true;
        boolean tr2=true;
        while(tr2)
        {
        System.out.print("Enter user id:");
        userid=obj.nextInt();
        if (userid>= 200000000 && userid <= 225999999) {
            tr2=false;
        }
        System.out.println("invalid input");
        }
 
        while(tr1)
        {
        System.out.print("\nEnter username:");
        uname=obj.nextLine();
        if (uname.length() >= 3 && uname.length() <= 12) {
            tr1=false;
        }
        System.out.println("invalid input");
        }
        while(tr)
        {
        System.out.print("\nEnter user password:");
        pass=obj.nextLine();
        if (pass.length() >= 8 && pass.length() <= 12) {
        insertUser(userid,uname,pass);
        tr=false;
        }
         System.out.println("invalid input");
        }
    }
    public static void insertUser(int id,String uname, String password){
        String sql = "INSERT INTO users (userid,username,password) VALUES (?,?,?)";
        DBConnection obj1=new DBConnection();
        try{
            Connection conn=obj1.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.setString(2, uname);
            pstmt.setString(3, password);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0){
                System.err.println("Data inserted successfully!");
            } else {
                System.err.println("No data inserted.");
            }
            pstmt.close();
            conn.close();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
