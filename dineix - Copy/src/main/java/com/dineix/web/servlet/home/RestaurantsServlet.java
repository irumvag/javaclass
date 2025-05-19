package com.dineix.web.servlet.home;

import com.dineix.web.models.home.Restaurant;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

public class RestaurantsServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Generate CSRF token
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());

        // Get search and sort parameters
        String searchQuery = request.getParameter("searchQuery");
        String sort = request.getParameter("sort");
        if (sort == null) sort = "name";

        // Validate searchQuery
        boolean hasValidSearchQuery = searchQuery != null && !searchQuery.trim().isEmpty() && !searchQuery.equals("&searchQuery=");
        if (!hasValidSearchQuery) {
            searchQuery = null; // Clear invalid search query
        } else {
            searchQuery = searchQuery.trim();
        }

        // Fetch restaurants
        List<Restaurant> restaurants = new ArrayList<>();
        Map<Integer, Boolean> follows = new HashMap<>();
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        try (Connection conn = dataSource.getConnection()) {
            // Build SQL query
            StringBuilder sql = new StringBuilder(
                "SELECT restaurant_id, owner_id, name, address, contact_number, logo_url, description " +
                "FROM restaurants WHERE 1=1"
            );
            List<String> params = new ArrayList<>();
            if (hasValidSearchQuery) {
                sql.append(" AND (name LIKE ? OR description LIKE ?)");
                params.add("%" + searchQuery + "%");
                params.add("%" + searchQuery + "%");
            }
            if (sort.equals("popular")) {
                sql.append(" ORDER BY (SELECT COUNT(*) FROM user_restaurant_follows urf WHERE urf.restaurant_id = restaurants.restaurant_id) DESC");
            } else {
                sql.append(" ORDER BY name");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setString(i + 1, params.get(i));
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Restaurant restaurant = new Restaurant(
                        rs.getInt("restaurant_id"),
                        rs.getInt("owner_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact_number"),
                        rs.getString("logo_url"),
                        rs.getString("description")
                    );
                    restaurants.add(restaurant);
                }
            }

            // Fetch follow status for logged-in user
            if (userId != null) {
                String followSql = "SELECT restaurant_id FROM user_restaurant_follows WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(followSql)) {
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        follows.put(rs.getInt("restaurant_id"), true);
                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred while fetching restaurants.");
        }

        // Set attributes
        request.setAttribute("restaurants", restaurants);
        request.setAttribute("follows", follows);
        request.setAttribute("searchQuery", searchQuery);
        request.getRequestDispatcher("restaurants.jsp").forward(request, response);
    }
}