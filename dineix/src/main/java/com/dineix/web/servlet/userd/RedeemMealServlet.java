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
            String checkSql;
            PreparedStatement stmt;
            int purchaseId = 0;

            // Try redemption code first
            if (redemptionCode != null && !redemptionCode.trim().isEmpty()) {
                if (!redemptionCode.matches("\\d{6}")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\": false, \"message\": \"Valid 6-digit redemption code is required.\"}");
                    return;
                }
                checkSql = "SELECT ump.purchase_id, ump.meals_remaining, ump.redemption_code, mp.package_name, mp.description, r.name AS restaurant_name " +
                           "FROM user_meal_purchases ump " +
                           "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                           "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id " +
                           "WHERE ump.redemption_code = ? AND ump.user_id = ?";
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
                checkSql = "SELECT ump.purchase_id, ump.meals_remaining, ump.redemption_code, mp.package_name, mp.description, r.name AS restaurant_name " +
                           "FROM user_meal_purchases ump " +
                           "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
                           "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id " +
                           "WHERE ump.purchase_id = ? AND ump.user_id = ?";
                stmt = conn.prepareStatement(checkSql);
                stmt.setInt(1, purchaseId);
                stmt.setInt(2, userId);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Redemption code or purchase ID is required.\"}");
                return;
            }

            // Execute query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int mealsRemaining = rs.getInt("meals_remaining");
                    if (mealsRemaining <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"success\": false, \"message\": \"No meals remaining in this package.\"}");
                        return;
                    }

                    // Prepare response data
                    purchaseId = rs.getInt("purchase_id");
                    String packageName = rs.getString("package_name");
                    String description = rs.getString("description");
                    String restaurantName = rs.getString("restaurant_name");
                    String usedRedemptionCode = rs.getString("redemption_code");
                    String responseJson = String.format(
                        "{\"success\": true, \"message\": \"Please provide this %s to the restaurant: %s\", " +
                        "\"details\": {\"redemptionCode\": \"%s\", \"purchaseId\": %d, \"packageName\": \"%s\", \"description\": \"%s\", " +
                        "\"mealsRemaining\": %d, \"restaurantName\": \"%s\"}}",
                        usedRedemptionCode != null ? "redemption code" : "purchase ID",
                        usedRedemptionCode != null ? usedRedemptionCode : purchaseId,
                        usedRedemptionCode != null ? usedRedemptionCode : "",
                        purchaseId, packageName, description, mealsRemaining, restaurantName
                    );
                    out.print(responseJson);

                    // Notify user
                    NotificationUtil.createNotification(dataSource, userId, 
                        "Redemption " + (usedRedemptionCode != null ? "code " + usedRedemptionCode : "for purchase ID " + purchaseId) + 
                        " requested for " + packageName + " at " + restaurantName);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\": false, \"message\": \"Invalid redemption code or purchase ID.\"}");
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error processing request: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}