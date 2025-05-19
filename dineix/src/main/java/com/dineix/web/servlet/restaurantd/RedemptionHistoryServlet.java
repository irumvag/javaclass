package com.dineix.web.servlet.restaurantd;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedemptionHistoryServlet extends HttpServlet {

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

        request.getSession().setAttribute("csrfToken", java.util.UUID.randomUUID().toString());

        try (Connection conn = dataSource.getConnection()) {
            int restaurantId = getRestaurantId(conn, userId);
            if (restaurantId == 0) {
                request.setAttribute("errorMessage", "No restaurant found for this owner.");
                request.getRequestDispatcher("dashboard").forward(request, response);
                return;
            }

            List<Map<String, Object>> redemptionHistory = new ArrayList<>();
            
            // Fetch redemption history for this restaurant
            String redemptionSql = "SELECT rl.log_id, rl.purchase_id, rl.redemption_code, " +
                    "rl.timestamp, rl.success, rl.message, u.full_name AS user_name, u.email AS user_email, " +
                    "mp.package_name, ump.purchase_date " +
                    "FROM redemption_logs rl " +
                    "JOIN user_meal_purchases ump ON rl.purchase_id = ump.purchase_id " +
                    "JOIN users u ON ump.user_id = u.user_id " +
                    "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                    "WHERE rl.restaurant_id = ? " +
                    "ORDER BY rl.timestamp DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(redemptionSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> redemption = new HashMap<>();
                        redemption.put("logId", rs.getInt("log_id"));
                        redemption.put("purchaseId", rs.getInt("purchase_id"));
                        redemption.put("redemptionCode", rs.getString("redemption_code"));
                        redemption.put("timestamp", rs.getTimestamp("timestamp").toString());
                        redemption.put("success", rs.getBoolean("success"));
                        redemption.put("message", rs.getString("message"));
                        redemption.put("userName", rs.getString("user_name"));
                        redemption.put("userEmail", rs.getString("user_email"));
                        redemption.put("packageName", rs.getString("package_name"));
                        redemption.put("purchaseDate", rs.getTimestamp("purchase_date").toString());
                        redemptionHistory.add(redemption);
                    }
                }
            }

            // Get summary statistics
            Map<String, Object> stats = new HashMap<>();
            
            // Total redemptions
            String totalSql = "SELECT COUNT(*) AS total FROM redemption_logs WHERE restaurant_id = ?"; 
            try (PreparedStatement stmt = conn.prepareStatement(totalSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("totalRedemptions", rs.getInt("total"));
                    }
                }
            }
            
            // Successful redemptions
            String successSql = "SELECT COUNT(*) AS total FROM redemption_logs WHERE restaurant_id = ? AND success = true";
            try (PreparedStatement stmt = conn.prepareStatement(successSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("successfulRedemptions", rs.getInt("total"));
                    }
                }
            }
            
            // Failed redemptions
            String failedSql = "SELECT COUNT(*) AS total FROM redemption_logs WHERE restaurant_id = ? AND success = false";
            try (PreparedStatement stmt = conn.prepareStatement(failedSql)) {
                stmt.setInt(1, restaurantId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("failedRedemptions", rs.getInt("total"));
                    }
                }
            }

            request.setAttribute("redemptionHistory", redemptionHistory);
            request.setAttribute("redemptionStats", stats);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading redemption history: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("redemption-history.jsp").forward(request, response);
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
}