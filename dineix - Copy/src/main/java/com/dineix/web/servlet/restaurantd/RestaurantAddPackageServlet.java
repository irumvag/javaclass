package com.dineix.web.servlet.restaurantd;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


public class RestaurantAddPackageServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

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

        // Get parameters
        String packageName = request.getParameter("packageName");
        String description = request.getParameter("description");
        String numberOfMealsStr = request.getParameter("numberOfMeals");
        String priceStr = request.getParameter("price");

        // Validate input
        int numberOfMeals;
        double price;
        try {
            numberOfMeals = Integer.parseInt(numberOfMealsStr);
            price = Double.parseDouble(priceStr);
            if (numberOfMeals <= 0 || price < 0 || packageName == null || packageName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid input.");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid package details.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        // Fetch restaurant ID
        try (Connection conn = dataSource.getConnection()) {
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

            // Insert package
            String packageSql = "INSERT INTO meal_packages (restaurant_id, package_name, description, number_of_meals, price) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                stmt.setInt(1, restaurantId);
                stmt.setString(2, packageName);
                stmt.setString(3, description != null ? description : "");
                stmt.setInt(4, numberOfMeals);
                stmt.setDouble(5, price);
                stmt.executeUpdate();

                request.setAttribute("successMessage", "Meal package added successfully.");
                request.getRequestDispatcher("dashboard").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "An error occurred while adding the package.");
            e.printStackTrace();
            request.getRequestDispatcher("dashboard").forward(request, response);
        }
    }
}
