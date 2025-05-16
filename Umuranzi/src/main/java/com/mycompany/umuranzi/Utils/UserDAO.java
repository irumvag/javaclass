package com.mycompany.umuranzi.Utils;

import com.mycompany.umuranzi.Utils.DBConnection;
import com.mycompany.umuranzi.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.naming.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    // In UserDAO.java
    public User getUserById(int userId) throws SQLException, NamingException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(User.UserRole.valueOf(rs.getString("role")));
                    user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
                    return user;
                }
            }
        }
        throw new SQLException("User not found with ID: " + userId);
    }

    public boolean verifyToken(String token) throws SQLException, NamingException {
        String sql = "SELECT u.user_id, expiry_date FROM verification_tokens vt "
                + "JOIN users u ON vt.user_id = u.user_id "
                + "WHERE token = ? AND u.email_verified = false";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp expiryDate = rs.getTimestamp("expiry_date");
                    if (expiryDate.after(new Timestamp(System.currentTimeMillis()))) {
                        // Update verification status
                        String updateSql = "UPDATE users SET email_verified = true WHERE user_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, rs.getInt("user_id"));
                            updateStmt.executeUpdate();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Check if email exists
    public void saveThemePreference(int userId, String theme) throws SQLException, NamingException {
        String sql = "UPDATE users SET theme_preference = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, theme);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public String getThemePreference(int userId) throws SQLException, NamingException {
        String sql = "SELECT theme_preference FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("theme_preference") : "light";
            }
        }
    }

    public boolean emailExists(String email) throws SQLException, NamingException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    public boolean updateUser(User user) throws SQLException, NamingException {
        String sql = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getUserId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean verifyPassword(int userId, String plainPassword)
            throws SQLException, NamingException {
        String sql = "SELECT password FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BCrypt.checkpw(plainPassword, hashedPassword);
                }
            }
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword)
            throws SQLException, NamingException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        }
    }

    public void deleteUser(int userId) throws SQLException, NamingException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void updateThemePreference(int userId, String theme) throws SQLException, NamingException {
        String sql = "UPDATE users SET theme_preference = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, theme);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException, NamingException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, full_name, email, role, registration_date FROM users ORDER BY registration_date DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(User.UserRole.valueOf(rs.getString("role")));
                user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
                users.add(user);
            }
        }
        return users;
    }

    public void updateUserRole(int userId, String role) throws SQLException, NamingException {
        // Validate role
        if (!Arrays.asList("USER", "RESTAURANT_OWNER", "ADMIN").contains(role)) {
            throw new IllegalArgumentException("Invalid role specified: " + role);
        }

        String sql = "UPDATE users SET role = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No user found with ID: " + userId);
            }
        }
    }
}
