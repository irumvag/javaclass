package com.dineix.web.servlet.home;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.json.JSONObject;

public class FollowRestaurantServlet extends HttpServlet {
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

        // Get data from JSON
        String csrfToken = request.getHeader("X-CSRF-Token");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid CSRF token.");
            return;
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not logged in.");
            return;
        }

        int restaurantId = jsonBody.optInt("restaurantId", -1);
        String action = jsonBody.optString("action");

        if (restaurantId <= 0 || action == null || action.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid parameters.");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql;
            if ("follow".equals(action)) {
                sql = "INSERT IGNORE INTO user_restaurant_follows (user_id, restaurant_id) VALUES (?, ?)";
            } else if ("unfollow".equals(action)) {
                sql = "DELETE FROM user_restaurant_follows WHERE user_id = ? AND restaurant_id = ?";
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid action.");
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, restaurantId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0 || "follow".equals(action)) {
                    response.getWriter().write("Success");
                } else {
                    response.getWriter().write("No change.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error.");
        }
    }
}
