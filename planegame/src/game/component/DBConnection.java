package game.component;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/schoollab";
    private static final String USER = "root";
    private static final String PASSWORD ="";
    
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
            
    }
    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { /* Ignored */ }
        }
    }
}
