package com.dineix.web.servlet.userd;

import com.dineix.web.utils.NotificationUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/user/request-role")
public class RequestRoleServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Read JSON body
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        JSONObject jsonBody = new JSONObject(sb.toString());

        // Validate CSRF token
        String csrfToken = request.getHeader("X-CSRF-Token");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid CSRF token");
            return;
        }

        // Validate user session
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not logged in");
            return;
        }

        // Get and validate parameters
        String requestedRole = jsonBody.optString("requestedRole");
        String restaurantName = jsonBody.optString("restaurantName");
        String restaurantAddress = jsonBody.optString("restaurantAddress");
        String restaurantContact = jsonBody.optString("restaurantContact");
        String reason = jsonBody.optString("reason");

        if (!requestedRole.equals("RESTAURANT_OWNER") ||
            restaurantName == null || restaurantName.trim().isEmpty() ||
            restaurantAddress == null || restaurantAddress.trim().isEmpty() ||
            restaurantContact == null || restaurantContact.trim().isEmpty() ||
            reason == null || reason.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("All fields are required and role must be RESTAURANT_OWNER");
            return;
        }

        // Process request
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO role_requests (user_id, requested_role, restaurant_name, restaurant_address, restaurant_contact, ReasonforRequest, status, request_date) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 'PENDING', ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, requestedRole);
                stmt.setString(3, restaurantName);
                stmt.setString(4, restaurantAddress);
                stmt.setString(5, restaurantContact);
                stmt.setString(6, reason);
                stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                stmt.executeUpdate();
            }

            // Create notification
            NotificationUtil.createNotification(dataSource, userId, 
                "Your restaurant owner role request for " + restaurantName + " has been submitted.");

            response.getWriter().write("Role request submitted successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error submitting role request: " + e.getMessage());
            e.printStackTrace();
        }
    }
}