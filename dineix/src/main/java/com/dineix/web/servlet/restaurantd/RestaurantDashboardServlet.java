package com.dineix.web.servlet.restaurantd;

import com.dineix.web.models.home.MealPackage;
import com.dineix.web.models.home.RestaurantStats;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.UUID;

public class RestaurantDashboardServlet extends HttpServlet {

    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        if (userId == null || !"RESTAURANT_OWNER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());

        try (Connection conn = dataSource.getConnection()) {
            int restaurantId = getRestaurantId(conn, userId);
            if (restaurantId == 0) {
                request.setAttribute("errorMessage", "No restaurant found for this owner.");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }

            RestaurantStats stats = new RestaurantStats();
            loadBasicStats(conn, restaurantId, stats);
            loadAnalyticsData(conn, restaurantId, stats);
            
            request.setAttribute("stats", stats);
            request.setAttribute("restaurantPackages", getRestaurantPackages(conn, restaurantId));

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private int getRestaurantId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT restaurant_id FROM restaurants WHERE owner_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("restaurant_id") : 0;
            }
        }
    }

    private void loadBasicStats(Connection conn, int restaurantId, RestaurantStats stats) throws SQLException {
        // Total Followers
        String followersSql = "SELECT COUNT(*) FROM user_restaurant_follows WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(followersSql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setTotalFollowers(rs.getInt(1));
            }
        }

        // Total Purchases
        String purchasesSql = "SELECT COUNT(*) FROM user_meal_purchases ump " +
                "JOIN meal_packages mp ON ump.package_id = mp.package_id WHERE mp.restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(purchasesSql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setTotalPurchases(rs.getInt(1));
            }
        }

        // Total Revenue
        String revenueSql = "SELECT SUM(mp.price) FROM user_meal_purchases ump " +
                "JOIN meal_packages mp ON ump.package_id = mp.package_id WHERE mp.restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(revenueSql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.setTotalRevenue(rs.getDouble(1));
            }
        }
    }

    private void loadAnalyticsData(Connection conn, int restaurantId, RestaurantStats stats) throws SQLException {
        // Monthly Purchases
        Map<String, Integer> monthlyPurchases = new LinkedHashMap<>();
        String monthlyPurchasesSql = "SELECT DATE_FORMAT(purchase_date, '%Y-%m') AS month, COUNT(*) AS count " +
                "FROM user_meal_purchases ump " +
                "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                "WHERE mp.restaurant_id = ? " +
                "GROUP BY DATE_FORMAT(purchase_date, '%Y-%m') " +
                "ORDER BY month";
        try (PreparedStatement stmt = conn.prepareStatement(monthlyPurchasesSql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    monthlyPurchases.put(rs.getString("month"), rs.getInt("count"));
                }
            }
        }
        stats.setMonthlyPurchases(monthlyPurchases);

        // Monthly Revenue
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        String monthlyRevenueSql = "SELECT DATE_FORMAT(purchase_date, '%Y-%m') AS month, SUM(mp.price) AS revenue " +
                "FROM user_meal_purchases ump " +
                "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                "WHERE mp.restaurant_id = ? " +
                "GROUP BY DATE_FORMAT(purchase_date, '%Y-%m') " +
                "ORDER BY month";
        try (PreparedStatement stmt = conn.prepareStatement(monthlyRevenueSql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    monthlyRevenue.put(rs.getString("month"), rs.getDouble("revenue"));
                }
            }
        }
        stats.setMonthlyRevenue(monthlyRevenue);

        // Package Popularity
        Map<String, Integer> packagePopularity = new LinkedHashMap<>();
        String packagePopularitySql = "SELECT mp.package_name, COUNT(ump.purchase_id) AS purchases " +
                "FROM meal_packages mp " +
                "LEFT JOIN user_meal_purchases ump ON mp.package_id = ump.package_id " +
                "WHERE mp.restaurant_id = ? " +
                "GROUP BY mp.package_id, mp.package_name " +
                "ORDER BY purchases DESC";
        try (PreparedStatement stmt = conn.prepareStatement(packagePopularitySql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    packagePopularity.put(rs.getString("package_name"), rs.getInt("purchases"));
                }
            }
        }
        stats.setPackagePopularity(packagePopularity);

        // Follower Growth
        Map<String, Integer> followerGrowth = new LinkedHashMap<>();
        String followerGrowthSql = "SELECT DATE_FORMAT(follow_date, '%Y-%m') AS month, COUNT(*) AS count " +
                "FROM user_restaurant_follows " +
                "WHERE restaurant_id = ? " +
                "GROUP BY DATE_FORMAT(follow_date, '%Y-%m') " +
                "ORDER BY month";
        try (PreparedStatement stmt = conn.prepareStatement(followerGrowthSql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    followerGrowth.put(rs.getString("month"), rs.getInt("count"));
                }
            }
        }
        stats.setFollowerGrowth(followerGrowth);
    }

    private List<MealPackage> getRestaurantPackages(Connection conn, int restaurantId) throws SQLException {
        List<MealPackage> packages = new ArrayList<>();
        String sql = "SELECT package_id, package_name, description, number_of_meals, price " +
                "FROM meal_packages WHERE restaurant_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    packages.add(new MealPackage(
                            rs.getInt("package_id"),
                            rs.getString("package_name"),
                            rs.getString("description"),
                            rs.getInt("number_of_meals"),
                            rs.getDouble("price"),
                            null
                    ));
                }
            }
        }
        return packages;
    }
}