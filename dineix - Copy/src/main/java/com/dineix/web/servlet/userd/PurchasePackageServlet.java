package com.dineix.web.servlet.userd;

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
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;
import org.json.JSONObject;

@WebServlet("/user/purchase-package")
public class PurchasePackageServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    private String generateRedemptionCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        String packageIdStr = request.getParameter("packageId");

        if (userId == null) {
            jsonResponse.put("success", false)
                       .put("message", "Please log in to purchase a package.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            jsonResponse.put("success", false)
                       .put("message", "Invalid CSRF token.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        if (packageIdStr == null) {
            jsonResponse.put("success", false)
                       .put("message", "Package ID is required.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        int packageId;
        try {
            packageId = Integer.parseInt(packageIdStr);
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false)
                       .put("message", "Invalid package ID.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            // Fetch package details
            String packageSql = "SELECT number_of_meals, price FROM meal_packages WHERE package_id = ?";
            int numberOfMeals = 0;
            double price = 0.0;
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                stmt.setInt(1, packageId);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    numberOfMeals = rs.getInt("number_of_meals");
                    price = rs.getDouble("price");
                } else {
                    jsonResponse.put("success", false)
                               .put("message", "Package not found.");
                    response.getWriter().write(jsonResponse.toString());
                    return;
                }
            }

            // Return package details for payment processing
            jsonResponse.put("success", true)
                       .put("packageId", packageId)
                       .put("amount", price)
                       .put("numberOfMeals", numberOfMeals);
            response.getWriter().write(jsonResponse.toString());
            
        } catch (SQLException e) {
            jsonResponse.put("success", false)
                       .put("message", "Error retrieving package details: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Generate CSRF token for the form
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());
        request.getRequestDispatcher("/user/dashboard").forward(request, response);
    }
    
    public void completePurchase(HttpServletRequest request, HttpServletResponse response, int packageId) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        int numberOfMeals = 0;
        
        try (Connection conn = dataSource.getConnection()) {
            // Fetch package details to get number of meals
            String packageSql = "SELECT number_of_meals FROM meal_packages WHERE package_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                stmt.setInt(1, packageId);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    numberOfMeals = rs.getInt("number_of_meals");
                }
            }
            
            // Generate unique redemption code
            String redemptionCode;
            boolean codeExists;
            do {
                redemptionCode = generateRedemptionCode();
                String checkSql = "SELECT COUNT(*) FROM user_meal_purchases WHERE redemption_code = ?";
                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                    stmt.setString(1, redemptionCode);
                    var rs = stmt.executeQuery();
                    rs.next();
                    codeExists = rs.getInt(1) > 0;
                }
            } while (codeExists);

            // Insert purchase
            String sql = "INSERT INTO user_meal_purchases (user_id, package_id, meals_remaining, purchase_date, redemption_code) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, packageId);
                stmt.setInt(3, numberOfMeals);
                stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                stmt.setString(5, redemptionCode);
                stmt.executeUpdate();
            }
            // After successful purchase
            NotificationUtil.createNotification(dataSource, userId, "You purchased a meal package. Redemption code: " + redemptionCode);
            request.setAttribute("successMessage", "Package purchased successfully. Redemption code: " + redemptionCode);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error purchasing package: " + e.getMessage());
        }

        request.getRequestDispatcher("dashboard").forward(request, response);
    }
}