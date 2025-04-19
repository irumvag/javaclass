package com.mycompany.assingment.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.assingment.utils.DBconnection;

public class UserDao {

    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password, phone, email, country) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE users SET username = ?, password = ?, phone = ?, email = ?, country = ? WHERE id = ?";

    protected Connection getConnection() throws SQLException, ClassNotFoundException {
        return DBconnection.getConnection();
    }

    public void insertUser(User user) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getCountry());
            ps.executeUpdate();
        }
    }

    public User selectUser(int id) throws SQLException, ClassNotFoundException {
        User user = null;
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("country")
                );
            }
        }
        return user;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS)) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPhone(rs.getString("phone"));
                u.setEmail(rs.getString("email"));
                u.setCountry(rs.getString("country"));
                users.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getCountry());
            ps.setInt(6, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteUser(int id) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ? OR email LIKE ? OR country LIKE ?";
        try (Connection conn = DBconnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            stmt.setString(1, q);
            stmt.setString(2, q);
            stmt.setString(3, q);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCountry(rs.getString("country"));
                user.setPhone(rs.getString("phone"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

}
