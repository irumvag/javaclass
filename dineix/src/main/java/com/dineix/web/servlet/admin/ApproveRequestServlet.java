package com.dineix.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import jakarta.annotation.Resource;
import java.sql.ResultSet;

@WebServlet("/admin/approve-request")
public class ApproveRequestServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("requestId");
        String csrfToken = request.getParameter("csrfToken");
        response.setContentType("application/json");

        if (!isValidCsrfToken(request, csrfToken)) {
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid CSRF token\"}");
            return;
        }

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // Get role request details
            String selectQuery = "SELECT user_id, restaurant_name, restaurant_address, restaurant_contact " +
                                "FROM role_requests WHERE request_id = ? AND status = 'PENDING'";
            int userId;
            String restaurantName, restaurantAddress, restaurantContact;
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, Integer.parseInt(requestId));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        response.getWriter().write("{\"success\": false, \"message\": \"Request not found or already processed\"}");
                        return;
                    }
                    userId = rs.getInt("user_id");
                    restaurantName = rs.getString("restaurant_name");
                    restaurantAddress = rs.getString("restaurant_address");
                    restaurantContact = rs.getString("restaurant_contact");
                }
            }

            // Update user role
            String updateUserQuery = "UPDATE users SET role = 'RESTAURANT_OWNER' WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateUserQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Insert restaurant
            String insertRestaurantQuery = "INSERT INTO restaurants (owner_id, name, address, contact_number) " +
                                         "VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertRestaurantQuery)) {
                stmt.setInt(1, userId);
                stmt.setString(2, restaurantName);
                stmt.setString(3, restaurantAddress);
                stmt.setString(4, restaurantContact);
                stmt.executeUpdate();
            }

            // Update role request status
            String updateRequestQuery = "UPDATE role_requests SET status = 'APPROVED' WHERE request_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateRequestQuery)) {
                stmt.setInt(1, Integer.parseInt(requestId));
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    response.getWriter().write("{\"success\": true, \"message\": \"Request approved and restaurant created\"}");
                } else {
                    conn.rollback();
                    response.getWriter().write("{\"success\": false, \"message\": \"Request not found\"}");
                }
            }
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            response.getWriter().write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isValidCsrfToken(HttpServletRequest request, String csrfToken) {
        return csrfToken != null && csrfToken.equals(request.getSession().getAttribute("csrfToken"));
    }
}