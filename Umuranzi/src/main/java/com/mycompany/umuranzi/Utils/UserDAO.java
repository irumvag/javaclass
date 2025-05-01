package com.mycompany.umuranzi.Utils;
import com.mycompany.umuranzi.Utils.DBConnection;
import com.mycompany.umuranzi.models.User;
import java.sql.*;
import javax.naming.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    // Check if email exists
    public boolean emailExists(String email) throws SQLException, NamingException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Create new user
    public boolean createUser(User user) throws SQLException, NamingException {
        String sql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(4, user.getRole().name()); // Convert enum to string
            
            return stmt.executeUpdate() > 0;
        }
    }

    // Authentication method with enum conversion
    public User authenticate(String email, String password) throws SQLException, NamingException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(User.UserRole.valueOf(rs.getString("role"))); // Convert string to enum
                    user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
                    return user;
                }
            }
        }
        return null;
    }
}