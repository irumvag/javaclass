package com.dineix.web.servlet.restaurantd;

import com.dineix.web.utils.NotificationUtil;
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

@WebServlet("/restaurant/redeem-meal")
public class RestaurantRedeemMealServlet extends HttpServlet {

    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CSRF token validation
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            response.sendRedirect("dashboard");
            return;
        }

        // Check if user is logged in and has RESTAURANT_OWNER role
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        if (userId == null || !"RESTAURANT_OWNER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get redemption code or manual entry details
        String redemptionCode = request.getParameter("redemptionCode");
        String userEmail = request.getParameter("userEmail");
        String purchaseIdStr = request.getParameter("purchaseId");

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Fetch restaurant ID
            int restaurantId = getRestaurantId(conn, userId);
            if (restaurantId == 0) {
                request.setAttribute("errorMessage", "No restaurant found for this owner.");
                response.sendRedirect("dashboard");
                return;
            }

            // Process redemption based on input method
            if (redemptionCode != null && redemptionCode.matches("\\d{6}")) {
                processRedemptionByCode(conn, request, response, restaurantId, redemptionCode);
            } else if (userEmail != null && purchaseIdStr != null) {
                processManualRedemption(conn, request, response, restaurantId, userEmail, purchaseIdStr);
            } else {
                request.setAttribute("errorMessage", "Valid redemption code or purchase details required.");
                response.sendRedirect("dashboard");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect("dashboard");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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

    private void processRedemptionByCode(Connection conn, HttpServletRequest request,
            HttpServletResponse response, int restaurantId, String redemptionCode)
            throws SQLException, ServletException, IOException {

        // Check purchase and lock row
        String sql = "SELECT ump.purchase_id, ump.meals_remaining, ump.user_id, "
                + "mp.package_name, r.name AS restaurant_name "
                + "FROM user_meal_purchases ump "
                + "JOIN meal_packages mp ON ump.package_id = mp.package_id "
                + "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id "
                + "WHERE ump.redemption_code = ? AND mp.restaurant_id = ? FOR UPDATE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, redemptionCode);
            stmt.setInt(2, restaurantId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int purchaseId = rs.getInt("purchase_id");
                    int mealsRemaining = rs.getInt("meals_remaining");
                    int targetUserId = rs.getInt("user_id");
                    String packageName = rs.getString("package_name");
                    String restaurantName = rs.getString("restaurant_name");

                    if (mealsRemaining <= 0) {
                        request.setAttribute("errorMessage", "No meals remaining in this package.");
                        conn.rollback();
                        response.sendRedirect("dashboard");
                        return;
                    }

                    // Update meals remaining
                    updateMealsRemaining(conn, purchaseId);

                    // Record redemption
                    recordRedemption(conn, purchaseId, restaurantId, targetUserId,
                            Integer.parseInt((request.getSession().getAttribute("userId").toString())), redemptionCode);

                    // Commit transaction
                    conn.commit();

                    // Send notifications
                    sendRedemptionNotifications(dataSource, targetUserId,
                            Integer.parseInt((request.getSession().getAttribute("userId").toString())),
                            redemptionCode, packageName, restaurantName);

                    request.setAttribute("successMessage",
                            "Meal redeemed successfully for " + packageName);
                } else {
                    request.setAttribute("errorMessage", "Invalid redemption code for your restaurant.");
                    conn.rollback();
                }
            }
        }

        response.sendRedirect("dashboard");
    }

    private void processManualRedemption(Connection conn, HttpServletRequest request,
            HttpServletResponse response, int restaurantId, String userEmail,
            String purchaseIdStr) throws SQLException, ServletException, IOException {

        try {
            int purchaseId = Integer.parseInt(purchaseIdStr);

            // Verify purchase belongs to user and restaurant
            String sql = "SELECT ump.purchase_id, ump.meals_remaining, ump.user_id, "
                    + "mp.package_name, r.name AS restaurant_name "
                    + "FROM user_meal_purchases ump "
                    + "JOIN meal_packages mp ON ump.package_id = mp.package_id "
                    + "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id "
                    + "JOIN users u ON ump.user_id = u.user_id "
                    + "WHERE ump.purchase_id = ? AND mp.restaurant_id = ? "
                    + "AND u.email = ? FOR UPDATE";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, purchaseId);
                stmt.setInt(2, restaurantId);
                stmt.setString(3, userEmail);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int mealsRemaining = rs.getInt("meals_remaining");
                        int targetUserId = rs.getInt("user_id");
                        String packageName = rs.getString("package_name");
                        String restaurantName = rs.getString("restaurant_name");

                        if (mealsRemaining <= 0) {
                            request.setAttribute("errorMessage", "No meals remaining in this package.");
                            conn.rollback();
                            response.sendRedirect("dashboard");
                            return;
                        }

                        // Update meals remaining
                        updateMealsRemaining(conn, purchaseId);

                        // Record redemption
                        recordRedemption(conn, purchaseId, restaurantId, targetUserId,
                                Integer.parseInt((request.getSession().getAttribute("userId").toString())), null);

                        // Commit transaction
                        conn.commit();

                        // Send notifications
                        sendRedemptionNotifications(dataSource, targetUserId,
                                Integer.parseInt((request.getSession().getAttribute("userId").toString())),
                                "purchase ID " + purchaseId, packageName, restaurantName);

                        request.setAttribute("successMessage",
                                "Meal redeemed successfully for " + packageName);
                    } else {
                        request.setAttribute("errorMessage",
                                "No matching purchase found for the provided details.");
                        conn.rollback();
                    }
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid purchase ID format.");
            conn.rollback();
        }

        request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
    }

    private void updateMealsRemaining(Connection conn, int purchaseId) throws SQLException {
        String sql = "UPDATE user_meal_purchases SET meals_remaining = meals_remaining - 1 "
                + "WHERE purchase_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, purchaseId);
            stmt.executeUpdate();
        }
    }

    private void recordRedemption(Connection conn, int purchaseId, int restaurantId,
            int userId, int redeemedByUserId, String redemptionCode) throws SQLException {

        String sql = "INSERT INTO meal_redemptions (purchase_id, restaurant_id, user_id, "
                + "redeemed_by_user_id, redemption_code, redemption_date) "
                + "VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, purchaseId);
            stmt.setInt(2, restaurantId);
            stmt.setInt(3, userId);
            stmt.setInt(4, redeemedByUserId);
            stmt.setString(5, redemptionCode);
            stmt.executeUpdate();
        }
    }

    private void sendRedemptionNotifications(DataSource dataSource, int userId,
            int ownerUserId, String identifier, String packageName, String restaurantName) throws SQLException {

        String userMessage = String.format(
                "Your meal was redeemed (%s) for %s at %s",
                identifier, packageName, restaurantName
        );

        String ownerMessage = String.format(
                "You redeemed a meal (%s) for %s",
                identifier, packageName
        );

        NotificationUtil.createNotification(dataSource, userId, userMessage);
        NotificationUtil.createNotification(dataSource, ownerUserId, ownerMessage);
    }
}
