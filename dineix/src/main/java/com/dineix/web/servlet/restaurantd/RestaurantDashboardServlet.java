package com.dineix.web.servlet.restaurantd;

import com.dineix.web.models.home.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantDashboardServlet extends HttpServlet {

    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check session for logged-in user and role
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        if (userId == null || !"RESTAURANT_OWNER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Generate CSRF token
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());

        try (Connection conn = dataSource.getConnection()) {
            int restaurantId = 0;

            // Step 1: Get restaurant ID for the logged-in owner
            String restaurantSql = "SELECT restaurant_id FROM restaurants WHERE owner_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(restaurantSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        restaurantId = rs.getInt("restaurant_id");
                    } else {
                        request.setAttribute("errorMessage", "No restaurant found for this owner.");
                        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Step 2: Load restaurant statistics
            RestaurantStats stats = new RestaurantStats();

            String followersSql = "SELECT COUNT(*) FROM user_restaurant_follows WHERE restaurant_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(followersSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.setTotalFollowers(rs.getInt(1));
                    }
                }
            }

            String purchasesSql = "SELECT COUNT(*) FROM user_meal_purchases ump " +
                    "JOIN meal_packages mp ON ump.package_id = mp.package_id WHERE mp.restaurant_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(purchasesSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.setTotalPurchases(rs.getInt(1));
                    }
                }
            }

            String revenueSql = "SELECT SUM(mp.price) FROM user_meal_purchases ump " +
                    "JOIN meal_packages mp ON ump.package_id = mp.package_id WHERE mp.restaurant_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(revenueSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.setTotalRevenue(rs.getDouble(1));
                    }
                }
            }

            request.setAttribute("stats", stats);

            // Step 3: Load meal packages for the restaurant
            List<MealPackage> restaurantPackages = new ArrayList<>();
            String packageSql = "SELECT package_id, package_name, description, number_of_meals, price " +
                    "FROM meal_packages WHERE restaurant_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        restaurantPackages.add(new MealPackage(
                                rs.getInt("package_id"),
                                rs.getString("package_name"),
                                rs.getString("description"),
                                rs.getInt("number_of_meals"),
                                rs.getDouble("price"),
                                null // restaurant_name not required here
                        ));
                    }
                }
            }

            request.setAttribute("restaurantPackages", restaurantPackages);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "An error occurred while loading the dashboard.");
            e.printStackTrace();
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
