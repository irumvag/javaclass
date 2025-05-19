package com.dineix.web.servlet.userd;

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

public class PurchaseHistoryServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Map<String, Object>> purchaseHistory = new ArrayList<>();
        List<Map<String, Object>> redemptionHistory = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            // Fetch purchase history
            String purchaseSql = "SELECT ump.purchase_id, mp.package_name, mp.price, r.name AS restaurant_name, " +
                    "ump.purchase_date, ump.meals_remaining, mp.number_of_meals " +
                    "FROM user_meal_purchases ump " +
                    "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                    "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id " +
                    "WHERE ump.user_id = ? " +
                    "ORDER BY ump.purchase_date DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(purchaseSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> purchase = new HashMap<>();
                        purchase.put("purchaseId", rs.getInt("purchase_id"));
                        purchase.put("packageName", rs.getString("package_name"));
                        purchase.put("price", rs.getDouble("price"));
                        purchase.put("restaurantName", rs.getString("restaurant_name"));
                        purchase.put("purchaseDate", rs.getTimestamp("purchase_date").toString());
                        purchase.put("mealsRemaining", rs.getInt("meals_remaining"));
                        purchase.put("totalMeals", rs.getInt("number_of_meals"));
                        purchase.put("mealsUsed", rs.getInt("number_of_meals") - rs.getInt("meals_remaining"));
                        purchaseHistory.add(purchase);
                    }
                }
            }

            // Fetch redemption history
            String redemptionSql = "SELECT rl.log_id, rl.purchase_id, rl.redemption_code, " +
                    "rl.timestamp, rl.success, rl.message, r.name AS restaurant_name, mp.package_name " +
                    "FROM redemption_logs rl " +
                    "JOIN user_meal_purchases ump ON rl.purchase_id = ump.purchase_id " +
                    "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                    "JOIN restaurants r ON rl.restaurant_id = r.restaurant_id " +
                    "WHERE ump.user_id = ? " +
                    "ORDER BY rl.timestamp DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(redemptionSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> redemption = new HashMap<>();
                        redemption.put("logId", rs.getInt("log_id"));
                        redemption.put("purchaseId", rs.getInt("purchase_id"));
                        redemption.put("redemptionCode", rs.getString("redemption_code"));
                        redemption.put("timestamp", rs.getTimestamp("timestamp").toString());
                        redemption.put("success", rs.getBoolean("success"));
                        redemption.put("message", rs.getString("message"));
                        redemption.put("restaurantName", rs.getString("restaurant_name"));
                        redemption.put("packageName", rs.getString("package_name"));
                        redemptionHistory.add(redemption);
                    }
                }
            }

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("purchaseHistory", purchaseHistory);
        request.setAttribute("redemptionHistory", redemptionHistory);
        request.getRequestDispatcher("purchase-history.jsp").forward(request, response);
    }
}