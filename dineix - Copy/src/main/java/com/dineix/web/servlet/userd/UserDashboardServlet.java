package com.dineix.web.servlet.userd;

import com.dineix.web.models.home.MealPackage;
import com.dineix.web.models.home.PurchasedPackage;
import com.dineix.web.models.home.Restaurant;
import com.dineix.web.models.home.RoleRequest;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
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
import java.util.*;
import java.util.UUID;

public class UserDashboardServlet extends HttpServlet {

    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
            // Get user ID from session
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            // Generate CSRF token
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());

        // Initialize error messages
        List<String> errorMessages = new ArrayList<>();
            // Fetch payment analytics
            try (Connection conn = dataSource.getConnection()) {
                // Get total spent
                String spentSql = "SELECT COALESCE(SUM(amount), 0) as total_spent FROM payment_transactions WHERE user_id = ? AND status = 'SUCCESSFUL'";
                try (PreparedStatement stmt = conn.prepareStatement(spentSql)) {
                    stmt.setInt(1, userId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            request.setAttribute("totalSpent", rs.getDouble("total_spent"));
                        }
                    }
                }
                // Get payment counts by status
                String countSql = "SELECT status, COUNT(*) as count FROM payment_transactions WHERE user_id = ? GROUP BY status";
                try (PreparedStatement stmt = conn.prepareStatement(countSql);
                     ResultSet rs = stmt.executeQuery()) {
                    stmt.setInt(1, userId);
                    int successful = 0, pending = 0, failed = 0;
                    while (rs.next()) {
                        String status = rs.getString("status");
                        int count = rs.getInt("count");
                        switch (status) {
                            case "SUCCESSFUL" -> successful = count;
                            case "PENDING" -> pending = count;
                            case "FAILED" -> failed = count;
                        }
                    }
                    request.setAttribute("successfulPayments", successful);
                    request.setAttribute("pendingPayments", pending);
                    request.setAttribute("failedPayments", failed);
                }

                // Get payment history
                String historySql = """
                    SELECT pt.*, mp.name as package_name 
                    FROM payment_transactions pt 
                    JOIN meal_packages mp ON pt.package_id = mp.package_id 
                    WHERE pt.user_id = ? 
                    ORDER BY pt.created_at DESC
                    """;
                try (PreparedStatement stmt = conn.prepareStatement(historySql)) {
                    stmt.setInt(1, userId);
                    try (ResultSet rs = stmt.executeQuery()) {
                    stmt.setInt(1, userId);
                    List<Map<String, Object>> paymentHistory = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> payment = new HashMap<>();
                        payment.put("transactionId", rs.getString("transaction_id"));
                        payment.put("packageName", rs.getString("package_name"));
                        payment.put("amount", rs.getDouble("amount"));
                        payment.put("status", rs.getString("status"));
                        payment.put("referenceId", rs.getString("reference_id"));
                        payment.put("createdAt", rs.getTimestamp("created_at"));
                        paymentHistory.add(payment);
                    }
                    request.setAttribute("paymentHistory", paymentHistory);
                }
            }
            }
            catch(Exception e) {
                    errorMessages.add("Error loading payment history: " + e.getMessage());
                    e.printStackTrace();
                }

            // Fetch dashboard data
        try (Connection conn = dataSource.getConnection()) {
            // Fetch user details
            String userSql = "SELECT full_name, email, theme_preference FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    request.getSession().setAttribute("fullName", rs.getString("full_name"));
                    request.getSession().setAttribute("email", rs.getString("email"));
                    request.getSession().setAttribute("themePreference", rs.getString("theme_preference"));
                }
            } catch (SQLException e) {
                errorMessages.add("Error loading user details: " + e.getMessage());
            }
            List<Map<String, Object>> notifications = new ArrayList<>();
            String notificationSql = "SELECT notification_id, message, is_read, created_at FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
            try (PreparedStatement stmt = conn.prepareStatement(notificationSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> notification = new HashMap<>();
                    notification.put("id", rs.getInt("notification_id"));
                    notification.put("message", rs.getString("message"));
                    notification.put("isRead", rs.getBoolean("is_read"));
                    notification.put("createdAt", rs.getTimestamp("created_at").toString());
                    notifications.add(notification);
                }
            }
            request.setAttribute("notifications", notifications);
            // Fetch total meals remaining
            int totalMealsRemaining = 0;
            String mealsSql = "SELECT SUM(meals_remaining) AS total FROM user_meal_purchases WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(mealsSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalMealsRemaining = rs.getInt("total");
                }
            } catch (SQLException e) {
                errorMessages.add("Error loading total meals remaining: " + e.getMessage());
            }
            request.setAttribute("totalMealsRemaining", totalMealsRemaining);

            // Fetch followed restaurants
            List<Restaurant> followedRestaurants = new ArrayList<>();
            String followSql = "SELECT r.restaurant_id, r.name, r.logo_url, r.description "
                    + "FROM user_restaurant_follows urf "
                    + "JOIN restaurants r ON urf.restaurant_id = r.restaurant_id "
                    + "WHERE urf.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(followSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    followedRestaurants.add(new Restaurant(
                            rs.getInt("restaurant_id"),
                            0, // owner_id not needed
                            rs.getString("name"),
                            null, // address not needed
                            null, // contact_number not needed
                            rs.getString("logo_url"),
                            rs.getString("description")
                    ));
                }
            } catch (SQLException e) {
                errorMessages.add("Error loading followed restaurants: " + e.getMessage());
            }
            request.setAttribute("followedRestaurants", followedRestaurants);

            // Fetch available meal packages
            List<MealPackage> availablePackages = new ArrayList<>();
            String packageSql = "SELECT mp.package_id, mp.package_name, mp.description, mp.number_of_meals, mp.price, r.name AS restaurant_name "
                    + "FROM meal_packages mp JOIN restaurants r ON mp.restaurant_id = r.restaurant_id";
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    availablePackages.add(new MealPackage(
                            rs.getInt("package_id"),
                            rs.getString("package_name"),
                            rs.getString("description"),
                            rs.getInt("number_of_meals"),
                            rs.getDouble("price"),
                            rs.getString("restaurant_name")
                    ));
                }
            } catch (SQLException e) {
                errorMessages.add("Error loading available packages: " + e.getMessage());
            }
            request.setAttribute("availablePackages", availablePackages);

            // Fetch purchased packages
            List<PurchasedPackage> purchasedPackages = new ArrayList<>();
            String purchaseSql = "SELECT ump.purchase_id, mp.package_name, mp.description, ump.meals_remaining, ump.purchase_date, ump.redemption_code "
                    + "FROM user_meal_purchases ump "
                    + "JOIN meal_packages mp ON ump.package_id = mp.package_id "
                    + "WHERE ump.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(purchaseSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    purchasedPackages.add(new PurchasedPackage(
                            rs.getInt("purchase_id"),
                            rs.getString("package_name"),
                            rs.getString("description"),
                            rs.getInt("meals_remaining"),
                            rs.getTimestamp("purchase_date").toString(),
                            rs.getString("redemption_code")
                    ));
                }
            } catch (SQLException e) {
                errorMessages.add("Error loading purchased packages: " + e.getMessage());
            }
            request.setAttribute("purchasedPackages", purchasedPackages);

            // Fetch role request
            String roleRequestSql = "SELECT status, request_date FROM role_requests WHERE user_id = ? AND requested_role = 'RESTAURANT_OWNER'";
            try (PreparedStatement stmt = conn.prepareStatement(roleRequestSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    RoleRequest roleRequest = new RoleRequest(
                            rs.getString("status"),
                            rs.getTimestamp("request_date").toString()
                    );
                    request.setAttribute("roleRequest", roleRequest);
                }
            } catch (SQLException e) {
                errorMessages.add("Error loading role request: " + e.getMessage());
            }

            // Set error messages if any
            if (!errorMessages.isEmpty()) {
                request.setAttribute("errorMessage", String.join("; ", errorMessages));
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database connection error: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("/user/userdashboard.jsp").forward(request, response);
    }
}
