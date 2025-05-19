package com.dineix.web.servlet.userd;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.dineix.web.utils.NotificationUtil;

@WebServlet("/user/redeem-meal")
public class RedeemMealServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        String redemptionCode = request.getParameter("redemptionCode");
        String purchaseIdStr = request.getParameter("purchaseId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Validate session and CSRF token
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\": false, \"message\": \"Please log in to view redemption details.\"}");
            return;
        }

        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print("{\"success\": false, \"message\": \"Invalid CSRF token.\"}");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            
            String checkSql;
            PreparedStatement stmt;
            int purchaseId = 0;
            int mealsRemaining = 0;
            String packageName = "";
            String restaurantName = "";

            // Try redemption code first
            if (redemptionCode != null && !redemptionCode.trim().isEmpty()) {
                if (!redemptionCode.matches("\\d{6}")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\": false, \"message\": \"Valid 6-digit redemption code is required.\"}");
                    return;
                }
                
                checkSql = "SELECT ump.purchase_id, ump.meals_remaining, ump.redemption_code, mp.package_name, r.name AS restaurant_name " +
                         "FROM user_meal_purchases ump " +
                         "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                         "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id " +
                         "WHERE ump.redemption_code = ? AND ump.user_id = ? FOR UPDATE";
                
                stmt = conn.prepareStatement(checkSql);
                stmt.setString(1, redemptionCode);
                stmt.setInt(2, userId);
            } else if (purchaseIdStr != null && !purchaseIdStr.trim().isEmpty()) {
                try {
                    purchaseId = Integer.parseInt(purchaseIdStr);
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\": false, \"message\": \"Invalid purchase ID.\"}");
                    return;
                }
                
                checkSql = "SELECT ump.purchase_id, ump.meals_remaining, ump.redemption_code, mp.package_name, r.name AS restaurant_name " +
                         "FROM user_meal_purchases ump " +
                         "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                         "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id " +
                         "WHERE ump.purchase_id = ? AND ump.user_id = ? FOR UPDATE";
                
                stmt = conn.prepareStatement(checkSql);
                stmt.setInt(1, purchaseId);
                stmt.setInt(2, userId);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Redemption code or purchase ID is required.\"}");
                return;
            }

            // Execute query to check purchase
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    purchaseId = rs.getInt("purchase_id");
                    mealsRemaining = rs.getInt("meals_remaining");
                    packageName = rs.getString("package_name");
                    restaurantName = rs.getString("restaurant_name");
                    
                    if (mealsRemaining <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"success\": false, \"message\": \"No meals remaining in this package.\"}");
                        return;
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\": false, \"message\": \"Invalid redemption code or purchase ID.\"}");
                    return;
                }
            }

            // Update meals remaining
            String updateSql = "UPDATE user_meal_purchases SET meals_remaining = meals_remaining - 1 WHERE purchase_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, purchaseId);
                int rowsUpdated = updateStmt.executeUpdate();
                
                if (rowsUpdated == 0) {
                    conn.rollback();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"success\": false, \"message\": \"Failed to update meal count.\"}");
                    return;
                }
            }

            // Record redemption in logs
            String logSql = "INSERT INTO redemption_logs (purchase_id, restaurant_id, redemption_code, success, message) " +
                           "VALUES (?, (SELECT restaurant_id FROM meal_packages WHERE package_id = " +
                           "(SELECT package_id FROM user_meal_purchases WHERE purchase_id = ?)), ?, 1, 'Meal redeemed successfully')";
            
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, purchaseId);
                logStmt.setInt(2, purchaseId);
                logStmt.setString(3, redemptionCode);
                logStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            
            // Prepare success response
            String responseJson = String.format(
                "{\"success\": true, \"message\": \"Meal redeemed successfully for %s at %s\", " +
                "\"details\": {\"redemptionCode\": \"%s\", \"purchaseId\": %d, \"packageName\": \"%s\", " +
                "\"mealsRemaining\": %d, \"restaurantName\": \"%s\"}}",
                packageName, restaurantName,
                redemptionCode != null ? redemptionCode : "",
                purchaseId, packageName, mealsRemaining - 1, restaurantName
            );
            
            out.print(responseJson);

            // Notify user
            NotificationUtil.createNotification(dataSource, userId, 
                "Meal redeemed for " + packageName + " at " + restaurantName + 
                ". Remaining meals: " + (mealsRemaining - 1));

        } catch (SQLException e) {
            try(Connection conn = dataSource.getConnection()) {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error processing request: " + e.getMessage() + "\"}");
            e.printStackTrace();
        } finally {
            try (Connection conn = dataSource.getConnection()){
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}