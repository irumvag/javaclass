package com.mycompany.umuranzi.Utils;

import java.util.List;
import com.mycompany.umuranzi.models.RoleRequest;
import com.mycompany.umuranzi.models.User;
import java.sql.*;
import javax.naming.NamingException;

public class RoleRequestDAO {
    public boolean hasPendingRequest(int userId) throws SQLException, NamingException {
        String sql = "SELECT COUNT(*) FROM role_requests WHERE user_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void createRequest(RoleRequest request) throws SQLException, NamingException {
        String sql = "INSERT INTO role_requests (user_id, document_path, message) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, request.getUserId());
            stmt.setString(2, request.getDocumentPath());
            stmt.setString(3, request.getMessage());
            
            stmt.executeUpdate();
        }
    }
        public List<RoleRequest> getPendingRequests() throws SQLException, NamingException {
        List<RoleRequest> requests = new ArrayList<>();
        String sql = "SELECT r.request_id, r.user_id, r.requested_role, r.status, r.created_at, u.email "
                   + "FROM role_requests r "
                   + "JOIN users u ON r.user_id = u.user_id "
                   + "WHERE r.status = 'PENDING' "
                   + "ORDER BY r.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RoleRequest request = new RoleRequest();
                request.setRequestId(rs.getInt("request_id"));
                request.setUserId(rs.getInt("user_id"));
                request.setRequestedRole(rs.getString("requested_role"));
                request.setStatus(rs.getString("status"));
                request.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                // Create temporary user object for email
                User user = new User();
                user.setEmail(rs.getString("email"));
                request.setUser(user);
                
                requests.add(request);
            }
        }
        return requests;
    }

    public void updateRequestStatus(int requestId, String status) throws SQLException, NamingException {
        String sql = "UPDATE role_requests SET status = ? WHERE request_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        }
    }

    public void createRequest(int userId) throws SQLException, NamingException {
        String sql = "INSERT INTO role_requests (user_id, requested_role, status) "
                   + "VALUES (?, 'RESTAURANT_OWNER', 'PENDING')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}