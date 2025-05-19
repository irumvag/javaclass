package com.dineix.web.servlet.userd;

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

@WebServlet("/user/payment-details")
public class PaymentDetailsServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        
        try {
            // Validate session
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                jsonResponse.put("success", false)
                           .put("message", "Please log in to view payment details.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            String transactionId = request.getParameter("transactionId");
            if (transactionId == null) {
                jsonResponse.put("success", false)
                           .put("message", "Missing transaction ID.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            try (Connection conn = dataSource.getConnection()) {
                String sql = """
                    SELECT pt.*, mp.name as package_name 
                    FROM payment_transactions pt 
                    JOIN meal_packages mp ON pt.package_id = mp.package_id 
                    WHERE pt.transaction_id = ? AND pt.user_id = ?
                    """;
                
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, transactionId);
                    stmt.setInt(2, userId);
                    
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            JSONObject details = new JSONObject()
                                .put("transactionId", rs.getString("transaction_id"))
                                .put("packageName", rs.getString("package_name"))
                                .put("amount", rs.getDouble("amount"))
                                .put("currency", rs.getString("currency"))
                                .put("phoneNumber", rs.getString("phone_number"))
                                .put("status", rs.getString("status"))
                                .put("referenceId", rs.getString("reference_id"))
                                .put("createdAt", rs.getTimestamp("created_at").toString());
                            
                            jsonResponse.put("success", true)
                                       .put("details", details);
                        } else {
                            jsonResponse.put("success", false)
                                       .put("message", "Payment not found.");
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            jsonResponse.put("success", false)
                       .put("message", "Error fetching payment details: " + e.getMessage());
        }
        
        response.getWriter().write(jsonResponse.toString());
    }
}