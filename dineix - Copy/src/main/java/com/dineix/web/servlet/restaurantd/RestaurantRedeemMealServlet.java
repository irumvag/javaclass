package com.dineix.web.servlet.restaurantd;

import com.dineix.web.utils.NotificationUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

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
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        // Check if user is logged in and has RESTAURANT_OWNER role
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        if (userId == null || !"RESTAURANT_OWNER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get redemption code
        String redemptionCode = request.getParameter("redemptionCode");
        if (redemptionCode == null || redemptionCode.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Redemption code is required.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        // Validate and redeem meal
        try (Connection conn = dataSource.getConnection()) {
            // Fetch restaurant ID
            int restaurantId = 0;
            String restaurantSql = "SELECT restaurant_id FROM restaurants WHERE owner_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(restaurantSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    restaurantId = rs.getInt("restaurant_id");
                } else {
                    request.setAttribute("errorMessage", "No restaurant found for this owner.");
                    request.getRequestDispatcher("dashboard").forward(request, response);
                    return;
                }
            }

            // Validate meal package status and redemption
            String validationSql = "SELECT mp.status, mp.expiration_date, ump.meals_remaining, mp.restaurant_id "
                    + "FROM meal_packages mp "
                    + "JOIN user_meal_purchases ump ON mp.package_id = ump.package_id "
                    + "WHERE ump.redemption_code = ? AND mp.status = 'ACTIVE'"
                    + "AND mp.expiration_date > NOW() AND ump.meals_remaining > 0";

            String purchaseValidationSql = "SELECT ump.purchase_id, ump.user_id, ump.meals_remaining, mp.restaurant_id "
                    + "FROM user_meal_purchases ump "
                    + "JOIN meal_packages mp ON ump.package_id = mp.package_id "
                    + "WHERE ump.redemption_code = ? AND mp.restaurant_id = ?";
            
            try (PreparedStatement validationStmt = conn.prepareStatement(validationSql)) {
                validationStmt.setString(1, redemptionCode);
                ResultSet rs = validationStmt.executeQuery();
                
                if (!rs.next()) {
                    request.setAttribute("errorMessage", "Invalid or expired redemption code");
                    request.getRequestDispatcher("dashboard").forward(request, response);
                    return;
                }
                
                if (rs.getInt("restaurant_id") != restaurantId) {
                    request.setAttribute("errorMessage", "Code not valid for this restaurant");
                    request.getRequestDispatcher("dashboard").forward(request, response);
                    return;
                }
            }
            int purchaseId = 0;
            int mealsRemaining = 0;
            int targetUserId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(purchaseValidationSql)) {
                stmt.setString(1, redemptionCode);
                stmt.setInt(2, restaurantId);
                stmt.setString(1, redemptionCode);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    purchaseId = rs.getInt("purchase_id");
                    mealsRemaining = rs.getInt("meals_remaining");
                    targetUserId = rs.getInt("user_id");
                    if (rs.getInt("restaurant_id") != restaurantId) {
                        request.setAttribute("errorMessage", "This redemption code is not valid for your restaurant.");
                        request.getRequestDispatcher("dashboard").forward(request, response);
                        return;
                    }
                } else {
                    request.setAttribute("errorMessage", "Invalid redemption code.");
                    request.getRequestDispatcher("dashboard").forward(request, response);
                    return;
                }
            }

            if (mealsRemaining <= 0) {
                request.setAttribute("errorMessage", "No meals remaining in this package.");
                request.getRequestDispatcher("dashboard").forward(request, response);
                return;
            }

            // Atomic transaction for redemption
            conn.setAutoCommit(false);
            try {
                // Update meals remaining
                String updateSql = "UPDATE user_meal_purchases "
                        + "SET meals_remaining = meals_remaining - 1 "
                        + "WHERE purchase_id = ? AND meals_remaining > 0";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, purchaseId);
                    int affectedRows = stmt.executeUpdate();
                    
                    if (affectedRows == 0) {
                        throw new SQLException("No meals remaining");
                    }
                }

                // Record redemption with timestamp
                String redemptionSql = "INSERT INTO meal_redemptions "
                        + "(purchase_id, restaurant_id, user_id, redeemed_by_user_id, redeemed_at) "
                        + "VALUES (?, ?, ?, ?, NOW())";
                try (PreparedStatement stmt = conn.prepareStatement(redemptionSql)) {
                    stmt.setInt(1, purchaseId);
                    stmt.setInt(2, restaurantId);
                    stmt.setInt(3, targetUserId);
                    stmt.setInt(4, userId);
                    stmt.executeUpdate();
                }

                conn.commit();
                // After successful redemption
                String userMessage = "Your meal was redeemed using code: " + redemptionCode;
                String ownerMessage = "You redeemed a meal for code: " + redemptionCode;
                NotificationUtil.createNotification(dataSource, targetUserId, userMessage);
                NotificationUtil.createNotification(dataSource, userId, ownerMessage);
                request.setAttribute("successMessage", "Meal redeemed successfully with code: " + redemptionCode);
                request.getRequestDispatcher("dashboard").forward(request, response);
            } catch (SQLException e) {
                conn.rollback();
                request.setAttribute("errorMessage", "An error occurred while redeeming the meal.");
                e.printStackTrace();
                request.getRequestDispatcher("dashboard").forward(request, response);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "An error occurred while processing the redemption.");
            e.printStackTrace();
            request.getRequestDispatcher("dashboard").forward(request, response);
        }
    }
}
