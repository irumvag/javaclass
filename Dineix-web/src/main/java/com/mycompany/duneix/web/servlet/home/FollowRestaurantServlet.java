package com.mycompany.duneix.web.servlet.home;

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
import org.json.JSONObject;

@WebServlet("/follow-restaurant")
public class FollowRestaurantServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        // CSRF token validation
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            jsonResponse.put("success", false).put("message", "Invalid CSRF token.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        // Check if user is logged in
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            jsonResponse.put("success", false).put("message", "User not logged in.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        // Get parameters
        String restaurantIdStr = request.getParameter("restaurantId");
        String action = request.getParameter("action");
        int restaurantId;
        try {
            restaurantId = Integer.parseInt(restaurantIdStr);
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false).put("message", "Invalid restaurant ID.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql;
            if ("follow".equals(action)) {
                sql = "INSERT INTO user_restaurant_follows (user_id, restaurant_id) VALUES (?, ?)";
            } else if ("unfollow".equals(action)) {
                sql = "DELETE FROM user_restaurant_follows WHERE user_id = ? AND restaurant_id = ?";
            } else {
                jsonResponse.put("success", false).put("message", "Invalid action.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, restaurantId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false).put("message", "Action failed.");
                }
            }
        } catch (SQLException e) {
            jsonResponse.put("success", false).put("message", "Database error.");
        }

        response.getWriter().write(jsonResponse.toString());
    }
}