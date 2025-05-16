package com.mycompany.duneix.web.servlet.home;

import com.mycompany.duneix.web.models.home.Restaurant;
import com.mycompany.duneix.web.utils.DBConnection;
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

        // Fetch restaurants
        List<Restaurant> restaurants = new ArrayList<>();
        Map<Integer, Boolean> follows = new HashMap<>();
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        try (Connection conn = dataSource.getConnection()) {
            // Build SQL query
            String sql = "SELECT restaurant_id, owner_id, name, address, contact_number, logo_url, description " +
                         "FROM restaurants WHERE 1=1";
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                sql += " AND (name LIKE ? OR description LIKE ?)";
            }
            if (sort.equals("popular")) {
                sql += " ORDER BY (SELECT COUNT(*) FROM user_restaurant_follows urf WHERE urf.restaurant_id = restaurants.restaurant_id) DESC";
            } else {
                sql += " ORDER BY name";
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    stmt.setString(1, "%" + searchQuery + "%");
                    stmt.setString(2, "%" + searchQuery + "%");
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
        request.getRequestDispatcher("/restaurants.jsp").forward(request, response);
    }
}