package com.dineix.web.servlet.userd;

import com.dineix.web.models.User;
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
import java.sql.*;
import java.util.*;

public class UserDashboardServlet extends HttpServlet {

    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());

        List<String> errorMessages = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            User user = new User();
            String userSql = "SELECT user_id, full_name, email, role, registration_date, theme_preference, email_verified FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        user.setUserId(rs.getInt("user_id"));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setRole(rs.getString("role"));
                        user.setRegistrationDate(rs.getTimestamp("registration_date"));
                        user.setThemePreference(rs.getString("theme_preference"));
                        user.setEmailVerified(rs.getBoolean("email_verified"));

                        request.getSession().setAttribute("fullName", user.getFullName());
                        request.getSession().setAttribute("email", user.getEmail());
                        request.getSession().setAttribute("themePreference", user.getThemePreference());
                    }
                }
            }

            List<Map<String, Object>> notifications = new ArrayList<>();
            String notificationSql = "SELECT notification_id, message, is_read, created_at FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
            try (PreparedStatement stmt = conn.prepareStatement(notificationSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> notification = new HashMap<>();
                        notification.put("id", rs.getInt("notification_id"));
                        notification.put("message", rs.getString("message"));
                        notification.put("isRead", rs.getBoolean("is_read"));
                        notification.put("createdAt", rs.getTimestamp("created_at").toString());
                        notifications.add(notification);
                    }
                }
            }
            request.setAttribute("notifications", notifications);

            int totalMealsRemaining = 0;
            String mealsSql = "SELECT SUM(meals_remaining) AS total FROM user_meal_purchases WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(mealsSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        totalMealsRemaining = rs.getInt("total");
                        if (rs.wasNull()) totalMealsRemaining = 0;
                    }
                }
            }
            request.setAttribute("totalMealsRemaining", totalMealsRemaining);

            List<Restaurant> followedRestaurants = new ArrayList<>();
            String followSql = "SELECT r.restaurant_id, r.name, r.logo_url, r.description, r.contact_number, r.address " +
                    "FROM user_restaurant_follows urf " +
                    "JOIN restaurants r ON urf.restaurant_id = r.restaurant_id " +
                    "WHERE urf.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(followSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        followedRestaurants.add(new Restaurant(
                                rs.getInt("restaurant_id"),
                                0,
                                rs.getString("name"),
                                rs.getString("address"),
                                rs.getString("contact_number"),
                                rs.getString("logo_url"),
                                rs.getString("description")
                        ));
                    }
                }
            }
            request.setAttribute("followedRestaurants", followedRestaurants);

            List<MealPackage> availablePackages = new ArrayList<>();
            String packageSql = "SELECT mp.package_id, mp.package_name, mp.description, mp.number_of_meals, mp.price, r.name AS restaurant_name " +
                    "FROM meal_packages mp JOIN restaurants r ON mp.restaurant_id = r.restaurant_id";
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                try (ResultSet rs = stmt.executeQuery()) {
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
                }
            }
            request.setAttribute("availablePackages", availablePackages);

            List<PurchasedPackage> purchasedPackages = new ArrayList<>();
            String purchaseSql = "SELECT ump.purchase_id, mp.package_name, mp.description, ump.meals_remaining, ump.purchase_date, ump.redemption_code " +
                    "FROM user_meal_purchases ump " +
                    "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                    "WHERE ump.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(purchaseSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
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
                }
            }
            request.setAttribute("purchasedPackages", purchasedPackages);

            String roleRequestSql = "SELECT * FROM role_requests WHERE user_id = ? AND requested_role = 'RESTAURANT_OWNER'";
            try (PreparedStatement stmt = conn.prepareStatement(roleRequestSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        RoleRequest roleRequest = new RoleRequest(
                                rs.getInt("request_id"),
                                user,
                                rs.getString("requested_role"),
                                rs.getString("status"),
                                rs.getString("request_date"),
                                rs.getString("restaurant_name"),
                                rs.getString("restaurant_address"),
                                rs.getString("restaurant_contact")
                        );
                        request.setAttribute("roleRequest", roleRequest);
                    }
                }
            }

            if (!errorMessages.isEmpty()) {
                request.setAttribute("errorMessage", String.join("; ", errorMessages));
            }

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("userdashboard.jsp").forward(request, response);
    }
}
