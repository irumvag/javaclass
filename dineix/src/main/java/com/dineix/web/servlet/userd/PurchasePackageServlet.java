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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

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
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        String packageIdStr = request.getParameter("packageId");

        if (userId == null) {
            request.setAttribute("errorMessage", "Please log in to purchase a package.");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        if (packageIdStr == null) {
            request.setAttribute("errorMessage", "Package ID is required.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        int packageId;
        try {
            packageId = Integer.parseInt(packageIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid package ID.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            // Fetch number of meals
            String packageSql = "SELECT number_of_meals FROM meal_packages WHERE package_id = ?";
            int numberOfMeals = 0;
            try (PreparedStatement stmt = conn.prepareStatement(packageSql)) {
                stmt.setInt(1, packageId);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    numberOfMeals = rs.getInt("number_of_meals");
                } else {
                    request.setAttribute("errorMessage", "Package not found.");
                    request.getRequestDispatcher("dashboard").forward(request, response);
                    return;
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