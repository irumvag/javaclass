package com.dineix.web.servlet.admin;

import com.dineix.web.models.Analytics;
import com.dineix.web.models.User;
import com.dineix.web.models.home.Restaurant;
import com.dineix.web.models.home.RoleRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
import javax.sql.DataSource;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();

            // Role Requests
            List<RoleRequest> roleRequests = getRoleRequests(conn);
            request.setAttribute("roleRequests", roleRequests);

            // Users
            List<User> users = getUsers(conn);
            request.setAttribute("users", users);

            // Restaurants
            List<Restaurant> restaurants = getRestaurants(conn);
            request.setAttribute("restaurants", restaurants);

            // Analytics
            Analytics analytics = getAnalytics(conn);
            request.setAttribute("analytics", analytics);

            // CSRF Token
            request.setAttribute("csrfToken", generateCsrfToken(request));

            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<RoleRequest> getRoleRequests(Connection conn) throws Exception {
        List<RoleRequest> roleRequests = new ArrayList<>();
        String query = "SELECT rr.request_id, u.email, u.full_name, rr.requested_role, rr.status, " +
                      "rr.request_date, rr.restaurant_name, rr.restaurant_address, rr.restaurant_contact " +
                      "FROM role_requests rr JOIN users u ON rr.user_id = u.user_id " +
                      "WHERE rr.status = 'PENDING'";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));

                RoleRequest roleRequest = new RoleRequest();
                roleRequest.setRequestId(rs.getInt("request_id"));
                roleRequest.setUser(user);
                roleRequest.setRequestedRole(rs.getString("requested_role"));
                roleRequest.setStatus(rs.getString("status"));
                roleRequest.setRequestDate(rs.getTimestamp("request_date"));
                roleRequest.setRestaurantName(rs.getString("restaurant_name"));
                roleRequest.setRestaurantAddress(rs.getString("restaurant_address"));
                roleRequest.setRestaurantContact(rs.getString("restaurant_contact"));
                roleRequests.add(roleRequest);
            }
        }
        return roleRequests;
    }

    private List<User> getUsers(Connection conn) throws Exception {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, full_name, email, role, registration_date, theme_preference, email_verified " +
                      "FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setRegistrationDate(rs.getTimestamp("registration_date"));
                user.setThemePreference(rs.getString("theme_preference"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                users.add(user);
            }
        }
        return users;
    }

    private List<Restaurant> getRestaurants(Connection conn) throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT restaurant_id, owner_id, name, address, contact_number, logo_url, description " +
                      "FROM restaurants";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setRestaurantId(rs.getInt("restaurant_id"));
                restaurant.setOwnerId(rs.getInt("owner_id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setContactNumber(rs.getString("contact_number"));
                restaurant.setLogoUrl(rs.getString("logo_url"));
                restaurant.setDescription(rs.getString("description"));
                restaurants.add(restaurant);
            }
        }
        return restaurants;
    }

    private Analytics getAnalytics(Connection conn) throws Exception {
        Analytics analytics = new Analytics();

        // Total Users
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                analytics.setTotalUsers(rs.getInt(1));
            }
        }

        // Total Restaurants
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM restaurants");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                analytics.setTotalRestaurants(rs.getInt(1));
            }
        }

        // Total Revenue
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COALESCE(SUM(mp.price), 0) FROM user_meal_purchases pmp " +
                "JOIN meal_packages mp ON pmp.package_id = mp.package_id");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                analytics.setTotalRevenue(rs.getDouble(1));
            }
        }

        // Monthly User Registrations
        Map<String, Integer> monthlyUsers = new HashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT DATE_FORMAT(registration_date, '%Y-%m') AS month, COUNT(*) AS count " +
                "FROM users GROUP BY DATE_FORMAT(registration_date, '%Y-%m') ORDER BY month");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                monthlyUsers.put(rs.getString("month"), rs.getInt("count"));
            }
        }
        analytics.setMonthlyUsers(monthlyUsers);

        // Revenue per Restaurant
        Map<String, Double> restaurantRevenue = new HashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT r.name, COALESCE(SUM(mp.price), 0) AS revenue " +
                "FROM restaurants r " +
                "LEFT JOIN meal_packages mp ON r.restaurant_id = mp.restaurant_id " +
                "LEFT JOIN user_meal_purchases pmp ON mp.package_id = pmp.package_id " +
                "GROUP BY r.restaurant_id, r.name");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                restaurantRevenue.put(rs.getString("name"), rs.getDouble("revenue"));
            }
        }
        analytics.setRestaurantRevenue(restaurantRevenue);

        // Meal Packages per Restaurant
        Map<String, Integer> mealPackagesPerRestaurant = new HashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT r.name, COUNT(mp.package_id) AS package_count " +
                "FROM restaurants r " +
                "LEFT JOIN meal_packages mp ON r.restaurant_id = mp.restaurant_id " +
                "GROUP BY r.restaurant_id, r.name");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mealPackagesPerRestaurant.put(rs.getString("name"), rs.getInt("package_count"));
            }
        }
        analytics.setMealPackagesPerRestaurant(mealPackagesPerRestaurant);

        // Monthly Purchases per Restaurant
        Map<String, Map<String, Integer>> monthlyPurchasesPerRestaurant = new HashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT r.name, DATE_FORMAT(pmp.purchase_date, '%Y-%m') AS month, COUNT(pmp.purchase_id) AS purchase_count " +
                "FROM restaurants r " +
                "LEFT JOIN meal_packages mp ON r.restaurant_id = mp.restaurant_id " +
                "LEFT JOIN user_meal_purchases pmp ON mp.package_id = pmp.package_id " +
                "WHERE pmp.purchase_id IS NOT NULL " +
                "GROUP BY r.restaurant_id, r.name, DATE_FORMAT(pmp.purchase_date, '%Y-%m') " +
                "ORDER BY r.name, month");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String restaurantName = rs.getString("name");
                String month = rs.getString("month");
                int purchaseCount = rs.getInt("purchase_count");
                monthlyPurchasesPerRestaurant.computeIfAbsent(restaurantName, k -> new HashMap<>()).put(month, purchaseCount);
            }
        }
        analytics.setMonthlyPurchasesPerRestaurant(monthlyPurchasesPerRestaurant);

        return analytics;
    }

    private String generateCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("csrfToken");
        if (token == null) {
            token = java.util.UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
        }
        return token;
    }
}
