//package com.mycompany.duneix.web.servlet.restaurantd;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class RestaurantUpdatePackageServlet extends HttpServlet {
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
//        String packageIdStr = request.getParameter("packageId");
//        String packageName = request.getParameter("packageName");
//        String description = request.getParameter("description");
//        String numberOfMealsStr = request.getParameter("numberOfMeals");
//        String priceStr = request.getParameter("price");
//
//        // Validate input
//        int packageId, numberOfMeals;
//        double price;
//        try {
//            packageId = Integer.parseInt(packageIdStr);
//            numberOfMeals = Integer.parseInt(numberOfMealsStr);
//            price = Double.parseDouble(priceStr);
//            if (numberOfMeals <= 0 || price < 0 || packageName == null || packageName.trim().isEmpty()) {
//                throw new IllegalArgumentException("Invalid input.");
//            }
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Invalid package details.");
//            request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//            return;
//        }
//
//        // Update package
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            String sql = "UPDATE meal_packages SET package_name = ?, description = ?, number_of_meals = ?, price = ? WHERE package_id = ? AND restaurant_id IN (SELECT restaurant_id FROM restaurants WHERE owner_id = ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, packageName);
//                stmt.setString(2, description != null ? description : "");
//                stmt.setInt(3, numberOfMeals);
//                stmt.setDouble(4, price);
//                stmt.setInt(5, packageId);
//                stmt.setInt(6, userId);
//                int rows = stmt.executeUpdate();
//                if (rows > 0) {
//                    request.setAttribute("successMessage", "Meal package updated successfully.");
//                } else {
//                    request.setAttribute("errorMessage", "Package not found or you don't have permission to edit it.");
//                }
//                request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//            }
//        } catch (SQLException e) {
//            request.setAttribute("errorMessage", "An error occurred while updating the package.");
//            e.printStackTrace();
//            request.getRequestDispatcher("/restaurant/dashboard").forward(request, response);
//        }
//    }
//}