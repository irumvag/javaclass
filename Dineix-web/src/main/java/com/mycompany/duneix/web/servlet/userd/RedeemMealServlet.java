//package com.mycompany.duneix.web.servlet.userd;
//
//import com.mycompany.duneix.web.servlet.home.LoginServlet;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class RedeemMealServlet extends HttpServlet {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/dineix";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "";
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        try {
//            // CSRF token validation
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        // CSRF token validation
//        String csrfToken = request.getParameter("csrfToken");
//        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
//        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
//            out.print("{\"success\": false, \"message\": \"Invalid CSRF token.\"}");
//            out.flush();
//            return;
//        }
//
//        // Check if user is logged in
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//        if (userId == null) {
//            out.print("{\"success\": false, \"message\": \"Please log in.\"}");
//            out.flush();
//            return;
//        }
//
//        // Get parameters
//        String purchaseIdStr = request.getParameter("purchaseId");
//        int purchaseId;
//        try {
//            purchaseId = Integer.parseInt(purchaseIdStr);
//        } catch (NumberFormatException e) {
//            out.print("{\"success\": false, \"message\": \"Invalid purchase ID.\"}");
//            out.flush();
//            return;
//        }
//
//        // Validate and redeem meal
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            // Check purchase and meals remaining
//            String purchaseSql = "SELECT meals_remaining, package_id FROM user_meal_purchases WHERE purchase_id = ? AND user_id = ?";
//            int mealsRemaining = 0;
//            int packageId = 0;
//            try (PreparedStatement stmt = conn.prepareStatement(purchaseSql)) {
//                stmt.setInt(1, purchaseId);
//                stmt.setInt(2, userId);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    mealsRemaining = rs.getInt("meals_remaining");
//                    packageId = rs.getInt("package_id");
//                } else {
//                    out.print("{\"success\": false, \"message\": \"Invalid purchase ID.\"}");
//                    out.flush();
//                    return;
//                }
//            }
//
//            if (mealsRemaining <= 0) {
//                out.print("{\"success\": false, \"message\": \"No meals remaining in this package.\"}");
//                out.flush();
//                return;
//            }
//
//            // Get restaurant ID
//            int restaurantId = 0;
//            String packageSql = "SELECT restaurant_id FROM meal_packages WHERE package_id = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
//                stmt.setInt(1, packageId);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    restaurantId = rs.getInt("restaurant_id");
//                }
//            }
//
//            // Insert redemption request
//            String redemptionSql = "INSERT INTO meal_redemptions (purchase_id, restaurant_id, user_id, redemption_date) VALUES (?, ?, ?, NOW())";
//            try (PreparedStatement stmt = conn.prepareStatement(redemptionSql)) {
//                stmt.setInt(1, purchaseId);
//                stmt.setInt(2, restaurantId);
//                stmt.setInt(3, userId);
//                stmt.executeUpdate();
//
//                out.print("{\"success\": true}");
//                out.flush();
//            }
//        } catch (SQLException e) {
//            out.print("{\"success\": false, \"message\": \"An error occurred.\"}");
//            e.printStackTrace();
//            out.flush();
//        }
//    }
//}