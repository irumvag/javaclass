
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
import java.util.Random;
import org.json.JSONObject;

@WebServlet("/user/complete-purchase")
public class CompletePurchaseServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    private String generateRedemptionCode(Connection conn) throws SQLException {
        Random random = new Random();
        String code;
        boolean exists;
        do {
            code = String.format("%06d", random.nextInt(1000000));
            String checkSql = "SELECT COUNT(*) FROM purchased_meal_packages WHERE redemption_code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, code);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    exists = rs.getInt(1) > 0;
                }
            }
        } while (exists);
        return code;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        String transactionId = request.getParameter("transactionId");

        if (userId == null || csrfToken == null || !csrfToken.equals(sessionCsrfToken) || transactionId == null) {
            jsonResponse.put("success", false).put("message", "Invalid request.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            // Check payment status
            String paymentSql = "SELECT user_id, package_id, status FROM payment_transactions WHERE transaction_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(paymentSql)) {
                stmt.setString(1, transactionId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        if (!"SUCCESSFUL".equals(rs.getString("status"))) {
                            jsonResponse.put("success", false).put("message", "Payment not successful.");
                            response.getWriter().write(jsonResponse.toString());
                            return;
                        }
                        int paymentUserId = rs.getInt("user_id");
                        if (paymentUserId != userId) {
                            jsonResponse.put("success", false).put("message", "Unauthorized.");
                            response.getWriter().write(jsonResponse.toString());
                            return;
                        }
                        int packageId = rs.getInt("package_id");
                        // Check if purchase already completed
                        String checkPurchaseSql = "SELECT COUNT(*) FROM purchased_meal_packages WHERE transaction_id = ?";
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkPurchaseSql)) {
                            checkStmt.setString(1, transactionId);
                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                checkRs.next();
                                if (checkRs.getInt(1) > 0) {
                                    jsonResponse.put("success", false).put("message", "Purchase already completed.");
                                    response.getWriter().write(jsonResponse.toString());
                                    return;
                                }
                            }
                        }
                        // Get number of meals
                        String packageSql = "SELECT number_of_meals FROM meal_packages WHERE package_id = ?";
                        int numberOfMeals = 0;
                        try (PreparedStatement packageStmt = conn.prepareStatement(packageSql)) {
                            packageStmt.setInt(1, packageId);
                            try (ResultSet packageRs = packageStmt.executeQuery()) {
                                if (packageRs.next()) {
                                    numberOfMeals = packageRs.getInt("number_of_meals");
                                } else {
                                    jsonResponse.put("success", false).put("message", "Package not found.");
                                    response.getWriter().write(jsonResponse.toString());
                                    return;
                                }
                            }
                        }
                        // Generate redemption code
                        String redemptionCode = generateRedemptionCode(conn);
                        // Insert purchase
                        String insertSql = "INSERT INTO purchased_meal_packages (user_id, package_id, purchase_date, redemption_code, meals_remaining, redeemed_meals, transaction_id) VALUES (?, ?, NOW(), ?, ?, 0, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setInt(1, userId);
                            insertStmt.setInt(2, packageId);
                            insertStmt.setString(3, redemptionCode);
                            insertStmt.setInt(4, numberOfMeals);
                            insertStmt.setString(5, transactionId);
                            insertStmt.executeUpdate();
                        }
                        // Create notification
                        NotificationUtil.createNotification(dataSource, userId, "You purchased a meal package. Redemption code: " + redemptionCode);
                        jsonResponse.put("success", true).put("message", "Purchase completed successfully.");
                    } else {
                        jsonResponse.put("success", false).put("message", "Transaction not found.");
                    }
                }
            }
        } catch (SQLException e) {
            jsonResponse.put("success", false).put("message", "Database error: " + e.getMessage());
        }
        response.getWriter().write(jsonResponse.toString());
    }
}
