package com.dineix.web.servlet.restaurantd;

import com.dineix.web.models.home.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RestaurantDashboardServlet extends HttpServlet {

    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check session for logged-in user
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user has RESTAURANT_OWNER role or is switching roles
        String role = (String) request.getSession().getAttribute("role");
        String tempRole = (String) request.getSession().getAttribute("tempRole");
        
        if (!"RESTAURANT_OWNER".equals(role) && !"RESTAURANT_OWNER".equals(tempRole)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Generate CSRF token
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());

        try (Connection conn = dataSource.getConnection()) {
            int restaurantId = 0;

            // Step 1: Get restaurant ID for the logged-in owner
            String restaurantSql = "SELECT restaurant_id FROM restaurants WHERE owner_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(restaurantSql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        restaurantId = rs.getInt("restaurant_id");
                    } else {
                        request.setAttribute("errorMessage", "No restaurant found for this owner.");
                        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Step 2: Load restaurant statistics
            RestaurantStats stats = new RestaurantStats();

            // Restaurant purchase statistics
            String purchaseSql = "SELECT COUNT(*) AS total_purchases, SUM(mp.price) AS total_revenue " +
                "FROM purchased_meal_packages pmp " +
                "JOIN meal_packages mp ON pmp.package_id = mp.package_id " +
                "WHERE mp.restaurant_id = ?";

            // Recent transactions
            String transactionsSql = "SELECT pmp.purchase_id, mp.package_name, pmp.purchase_date, pmp.meals_remaining " +
                "FROM purchased_meal_packages pmp " +
                "JOIN meal_packages mp ON pmp.package_id = mp.package_id " +
                "WHERE mp.restaurant_id = ? ORDER BY pmp.purchase_date DESC LIMIT 5";

            // Package performance
            String packagePerformanceSql = "SELECT mp.package_name, COUNT(pmp.purchase_id) AS sales, SUM(mp.price) AS revenue " +
                "FROM purchased_meal_packages pmp " +
                "JOIN meal_packages mp ON pmp.package_id = mp.package_id " +
                "WHERE mp.restaurant_id = ? GROUP BY mp.package_name";
            // Execute purchase statistics query
            try (PreparedStatement pstmt = conn.prepareStatement(purchaseSql)) {
                pstmt.setInt(1, restaurantId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    request.setAttribute("totalPurchases", rs.getInt("total_purchases"));
                    request.setAttribute("totalRevenue", rs.getDouble("total_revenue"));
                }
            }

            // Get recent transactions
            try (PreparedStatement pstmt = conn.prepareStatement(transactionsSql)) {
                pstmt.setInt(1, restaurantId);
                ResultSet rs = pstmt.executeQuery();
                List<Map<String, Object>> transactions = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> transaction = new HashMap<>();
                    transaction.put("id", rs.getInt("purchase_id"));
                    transaction.put("package", rs.getString("package_name"));
                    transaction.put("date", rs.getTimestamp("purchase_date"));
                    transaction.put("remaining", rs.getInt("meals_remaining"));
                    transactions.add(transaction);
                }
                request.setAttribute("recentTransactions", transactions);
            }

            // Get package performance
            try (PreparedStatement pstmt = conn.prepareStatement(packagePerformanceSql)) {
                pstmt.setInt(1, restaurantId);
                ResultSet rs = pstmt.executeQuery();
                List<Map<String, Object>> performance = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> statss = new HashMap<>();
                    statss.put("package", rs.getString("package_name"));
                    statss.put("sales", rs.getInt("sales"));
                    statss.put("revenue", rs.getDouble("revenue"));
                    performance.add(statss);
                }
                request.setAttribute("packagePerformance", performance);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "An error occurred while loading the dashboard.");
            e.printStackTrace();
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // CSRF token validation
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        // Check if user is logged in
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("errorMessage", "Invalid action.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        switch (action) {
            case "switchRole" -> handleRoleSwitch(request, response);
            case "redeemQR" -> handleQRRedemption(request, response);
            default -> {
                request.setAttribute("errorMessage", "Invalid action.");
                request.getRequestDispatcher("dashboard").forward(request, response);
            }
        }
    }

    private void handleRoleSwitch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String currentRole = (String) request.getSession().getAttribute("role");
        String tempRole = (String) request.getSession().getAttribute("tempRole");

        if ("RESTAURANT_OWNER".equals(currentRole)) {
            // Switch to user role
            request.getSession().setAttribute("tempRole", currentRole);
            request.getSession().setAttribute("role", "USER");
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        } else if ("RESTAURANT_OWNER".equals(tempRole)) {
            // Switch back to restaurant owner role
            request.getSession().setAttribute("role", tempRole);
            request.getSession().removeAttribute("tempRole");
            response.sendRedirect(request.getContextPath() + "/restaurant/dashboard");
        } else {
            request.setAttribute("errorMessage", "Invalid role switch attempt.");
            request.getRequestDispatcher("dashboard").forward(request, response);
        }
    }

    private void handleQRRedemption(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String qrCode = request.getParameter("qrCode");
        if (qrCode == null || qrCode.trim().isEmpty()) {
            request.setAttribute("errorMessage", "QR code is required.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        try {
            // Decode QR code to get redemption code
            String redemptionCode = qrCode.trim();
            
            // Forward to redeem-meal servlet
            request.setAttribute("redemptionCode", redemptionCode);
            request.getRequestDispatcher("/restaurant/redeem-meal").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid QR code format.");
            request.getRequestDispatcher("dashboard").forward(request, response);
        }
    }
}
