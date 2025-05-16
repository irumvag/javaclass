//package com.mycompany.duneix.web.servlet.restaurantd;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class RestaurantRedeemMealServlet extends HttpServlet {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/dineix";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "";
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // CSRF token validation
//        String csrfToken = request.getParameter("csrfToken");
//        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
//        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
//            request.setAttribute("errorMessage", "Invalid CSRF token.");
//            request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//            return;
//        }
//
//        // Check if user is logged in and has RESTAURANT_OWNER role
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//        String role = (String) request.getSession().getAttribute("role");
//        if (userId == null || !"RESTAURANT_OWNER".equals(role)) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        // Get parameters
//        String userEmail = request.getParameter("userEmail");
//        String purchaseIdStr = request.getParameter("purchaseId");
//        int purchaseId;
//        try {
//            purchaseId = Integer.parseInt(purchaseIdStr);
//        } catch (NumberFormatException e) {
//            request.setAttribute("errorMessage", "Invalid purchase ID.");
//            request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//            return;
//        }
//
//        // Validate and redeem meal
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            // Fetch user ID by email
//            int targetUserId = 0;
//            String userSql = "SELECT user_id FROM users WHERE email = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
//                stmt.setString(1, userEmail);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    targetUserId = rs.getInt("user_id");
//                } else {
//                    request.setAttribute("errorMessage", "User not found.");
//                    request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//                    return;
//                }
//            }
//
//            // Fetch restaurant ID
//            int restaurantId = 0;
//            String restaurantSql = "SELECT restaurant_id FROM restaurants WHERE owner_id = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(restaurantSql)) {
//                stmt.setInt(1, userId);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    restaurantId = rs.getInt("restaurant_id");
//                } else {
//                    request.setAttribute("errorMessage", "No restaurant found for this owner.");
//                    request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//                    return;
//                }
//            }
//
//            // Check purchase and meals remaining
//            String purchaseSql = "SELECT ump.meals_remaining, mp.restaurant_id " +
//                                "FROM user_meal_purchases ump " +
//                                "JOIN meal_packages mp ON ump.package_id = mp.package_id " +
//                                "WHERE ump.purchase_id = ? AND ump.user_id = ?";
//            int mealsRemaining = 0;
//            try (PreparedStatement stmt = conn.prepareStatement(purchaseSql)) {
//                stmt.setInt(1, purchaseId);
//                stmt.setInt(2, targetUserId);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    mealsRemaining = rs.getInt("meals_remaining");
//                    if (rs.getInt("restaurant_id") != restaurantId) {
//                        request.setAttribute("errorMessage", "This purchase is not valid for your restaurant.");
//                        request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//                        return;
//                    }
//                } else {
//                    request.setAttribute("errorMessage", "Invalid purchase ID.");
//                    request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//                    return;
//                }
//            }
//
//            if (mealsRemaining <= 0) {
//                request.setAttribute("errorMessage", "No meals remaining in this package.");
//                request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//                return;
//            }
//
//            // Update meals remaining and record redemption
//            conn.setAutoCommit(false);
//            try {
//                String updateSql = "UPDATE user_meal_purchases SET meals_remaining = meals_remaining - 1 WHERE purchase_id = ?";
//                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
//                    stmt.setInt(1, purchaseId);
//                    stmt.executeUpdate();
//                }
//
//                String redemptionSql = "UPDATE meal_redemptions SET redeemed_by_user_id = ? WHERE purchase_id = ? AND user_id = ? AND redemption_date = (SELECT MAX(redemption_date) FROM meal_redemptions WHERE purchase_id = ?)";
//                try (PreparedStatement stmt = conn.prepareStatement(redemptionSql)) {
//                    stmt.setInt(1, userId);
//                    stmt.setInt(2, purchaseId);
//                    stmt.setInt(3, targetUserId);
//                    stmt.setInt(4, purchaseId);
//                    int rows = stmt.executeUpdate();
//                    if (rows == 0) {
//                        // Insert new redemption if no pending one exists
//                        String insertRedemptionSql = "INSERT INTO meal_redemptions (purchase_id, restaurant_id, user_id, redeemed_by_user_id) VALUES (?, ?, ?, ?)";
//                        try (PreparedStatement insertStmt = conn.prepareStatement(insertRedemptionSql)) {
//                            insertStmt.setInt(1, purchaseId);
//                            insertStmt.setInt(2, restaurantId);
//                            insertStmt.setInt(3, targetUserId);
//                            insertStmt.setInt(4, userId);
//                            insertStmt.executeUpdate();
//                        }
//                    }
//                }
//
//                conn.commit();
//                request.setAttribute("successMessage", "Meal redeemed successfully.");
//                request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//            } catch (SQLException e) {
//                conn.rollback();
//                request.setAttribute("errorMessage", "An error occurred while redeeming the meal.");
//                e.printStackTrace();
//                request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//            } finally {
//                conn.setAutoCommit(true);
//            }
//        } catch (SQLException e) {
//            request.setAttribute("errorMessage", "An error occurred while processing the redemption.");
//            e.printStackTrace();
//            request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//        }
//    }
//}
