/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import org.json.JSONObject;

@WebServlet("/api/redeem-meal")
public class RedeemMealApiServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        Integer restaurantId = (Integer) request.getSession().getAttribute("restaurantId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (restaurantId == null || csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            jsonResponse.put("success", false).put("message", "Invalid request.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        String redemptionCode = request.getParameter("redemptionCode");
        String purchaseIdStr = request.getParameter("purchaseId");

        if ((redemptionCode == null || redemptionCode.trim().isEmpty()) && (purchaseIdStr == null || purchaseIdStr.trim().isEmpty())) {
            jsonResponse.put("success", false).put("message", "Redemption code or purchase ID is required.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql;
            PreparedStatement stmt;
            if (redemptionCode != null && !redemptionCode.trim().isEmpty()) {
                sql = "SELECT pmp.purchase_id, pmp.meals_remaining, mp.restaurant_id " +
                      "FROM purchased_meal_packages pmp " +
                      "JOIN meal_packages mp ON pmp.package_id = mp.package_id " +
                      "WHERE pmp.redemption_code = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, redemptionCode);
            } else {
                int purchaseId = Integer.parseInt(purchaseIdStr);
                sql = "SELECT pmp.purchase_id, pmp.meals_remaining, mp.restaurant_id " +
                      "FROM purchased_meal_packages pmp " +
                      "JOIN meal_packages mp ON pmp.package_id = mp.package_id " +
                      "WHERE pmp.purchase_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, purchaseId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int purchaseId = rs.getInt("purchase_id");
                    int mealsRemaining = rs.getInt("meals_remaining");
                    int packageRestaurantId = rs.getInt("restaurant_id");
                    if (packageRestaurantId != restaurantId) {
                        jsonResponse.put("success", false).put("message", "This package does not belong to your restaurant.");
                    } else if (mealsRemaining <= 0) {
                        jsonResponse.put("success", false).put("message", "No meals remaining in this package.");
                    } else {
                        String updateSql = "UPDATE purchased_meal_packages SET meals_remaining = meals_remaining - 1, redeemed_meals = redeemed_meals + 1 WHERE purchase_id = ? AND meals_remaining > 0";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, purchaseId);
                            int rows = updateStmt.executeUpdate();
                            if (rows > 0) {
                                jsonResponse.put("success", true).put("message", "Meal redeemed successfully. Meals remaining: " + (mealsRemaining - 1));
                            } else {
                                jsonResponse.put("success", false).put("message", "Failed to redeem meal. No meals remaining.");
                            }
                        }
                    }
                } else {
                    jsonResponse.put("success", false).put("message", "Invalid redemption code or purchase ID.");
                }
            }
        } catch (Exception e) {
            jsonResponse.put("success", false).put("message", "An error occurred: " + e.getMessage());
        }
        response.getWriter().write(jsonResponse.toString());
    }
}