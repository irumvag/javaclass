//package com.mycompany.duneix.web.servlet.userd;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@WebServlet("/user/purchase-package")
//public class PurchasePackageServlet extends HttpServlet {
//    @Resource(name = "jdbc/dineix")
//    private DataSource dataSource;
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // CSRF token validation
//        String csrfToken = request.getParameter("csrfToken");
//        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
//        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
//            request.setAttribute("errorMessage", "Invalid CSRF token.");
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            return;
//        }
//
//        // Check if user is logged in
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
//        if (userId == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        // Get package ID
//        String packageIdStr = request.getParameter("packageId");
//        int packageId;
//        try {
//            packageId = Integer.parseInt(packageIdStr);
//        } catch (NumberFormatException e) {
//            request.setAttribute("errorMessage", "Invalid package ID.");
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            return;
//        }
//
//        // Fetch number of meals for the package
//        int numberOfMeals = 0;
//        try (Connection conn = dataSource.getConnection()) {
//            String sql = "SELECT number_of_meals FROM meal_packages WHERE package_id = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, packageId);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    numberOfMeals = rs.getInt("number_of_meals");
//                } else {
//                    request.setAttribute("errorMessage", "Meal package not found.");
//                    request.getRequestDispatcher("/user/dashboard").forward(request, response);
//                    return;
//                }
//            }
//        } catch (SQLException e) {
//            request.setAttribute("errorMessage", "Error retrieving package details.");
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            return;
//        }
//
//        // Insert purchase record
//        try (Connection conn = dataSource.getConnection()) {
//            String sql = "INSERT INTO user_meal_purchases (user_id, package_id, meals_remaining) VALUES (?, ?, ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, userId);
//                stmt.setInt(2, packageId);
//                stmt.setInt(3, numberOfMeals);
//                stmt.executeUpdate();
//                request.setAttribute("successMessage", "Package purchased successfully!");
//            }
//        } catch (SQLException e) {
//            request.setAttribute("errorMessage", "Error purchasing package.");
//        }
//
//        // Redirect to dashboard
//        request.getRequestDispatcher("/user/dashboard").forward(request, response);
//    }
//}