package com.dineix.web.servlet.userd;

import com.dineix.web.utils.NotificationUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        String requestedRole = request.getParameter("requestedRole");
        String restaurantName = request.getParameter("restaurantName");
        String restaurantAddress = request.getParameter("restaurantAddress");
        String restaurantContact = request.getParameter("restaurantContact");
        String reason = request.getParameter("reason");

        // Validate inputs
        if (userId == null) {
            request.setAttribute("errorMessage", "Please log in to request a role.");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        if (requestedRole == null || !requestedRole.equals("RESTAURANT_OWNER") ||
            restaurantName == null || restaurantName.trim().isEmpty() ||
            restaurantAddress == null || restaurantAddress.trim().isEmpty() ||
            restaurantContact == null || restaurantContact.trim().isEmpty() ||
            reason == null || reason.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required and role must be RESTAURANT_OWNER.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO role_requests (user_id, requested_role, restaurant_name, restaurant_address, restaurant_contact, reason, status, request_date) VALUES (?, ?, ?, ?, ?, ?, 'PENDING', ?)";
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
            NotificationUtil.createNotification(dataSource, userId, "Your restaurant owner role request for " + restaurantName + " has been submitted.");

            request.setAttribute("successMessage", "Role request submitted successfully.");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error submitting role request: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("dashboard").forward(request, response);
    }
}