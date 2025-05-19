package com.dineix.web.servlet.userd;

import com.dineix.web.payment.MTNMomoService;
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
import java.util.UUID;
import org.json.JSONObject;

@WebServlet("/user/process-mtn-payment")
public class ProcessMTNPaymentServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;
    private MTNMomoService momoService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        momoService = new MTNMomoService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        
        try {
            // Validate session and CSRF
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            String csrfToken = request.getParameter("csrfToken");
            String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
            
            if (userId == null) {
                jsonResponse.put("success", false)
                           .put("message", "Please log in to make a payment.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
                jsonResponse.put("success", false)
                           .put("message", "Invalid CSRF token.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            // Get payment details
            String packageIdStr = request.getParameter("packageId");
            String phoneNumber = request.getParameter("phoneNumber");
            String amountStr = request.getParameter("amount");
            
            if (packageIdStr == null || phoneNumber == null || amountStr == null) {
                jsonResponse.put("success", false)
                           .put("message", "Missing required payment information.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            int packageId = Integer.parseInt(packageIdStr);
            double amount = Double.parseDouble(amountStr);
            
            // Format phone number (ensure it starts with 250 for Rwanda)
            phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
            if (!phoneNumber.startsWith("250")) {
                phoneNumber = "250" + phoneNumber;
            }
            
            // Generate transaction ID
            String transactionId = UUID.randomUUID().toString();
            
            // Initialize payment in database
            try (Connection conn = dataSource.getConnection()) {
                String sql = "INSERT INTO payment_transactions (transaction_id, user_id, package_id, amount, phone_number, status, reference_id) VALUES (?, ?, ?, ?, ?, ?, ?)";;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, transactionId);
                    stmt.setInt(2, userId);
                    stmt.setInt(3, packageId);
                    stmt.setDouble(4, amount);
                    stmt.setString(5, phoneNumber);
                    stmt.setString(6, "PENDING");
                    stmt.setString(7, ""); // Will be updated after MTN API call
                    stmt.executeUpdate();
                }
            }
            
            // Initiate MTN MOMO payment
            String referenceId = momoService.initiatePayment(phoneNumber, amount, "RWF");
            
            // Update transaction with reference ID
            try (Connection conn = dataSource.getConnection()) {
                String sql = "UPDATE payment_transactions SET reference_id = ? WHERE transaction_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, referenceId);
                    stmt.setString(2, transactionId);
                    stmt.executeUpdate();
                }
            }
            
            // Create notification
            NotificationUtil.createNotification(dataSource, userId, 
                "Payment initiated. Please check your phone to complete the transaction.");
            
            jsonResponse.put("success", true)
                       .put("message", "Payment initiated successfully. Please check your phone to complete the transaction.")
                       .put("transactionId", transactionId)
                       .put("referenceId", referenceId);
            
        } catch (Exception e) {
            jsonResponse.put("success", false)
                       .put("message", "Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.getWriter().write(jsonResponse.toString());
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        
        try {
            String transactionId = request.getParameter("transactionId");
            String referenceId = request.getParameter("referenceId");
            
            if (transactionId == null || referenceId == null) {
                jsonResponse.put("success", false)
                           .put("message", "Missing transaction information.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            // Check payment status from MTN
            String paymentStatus = momoService.getPaymentStatus(referenceId);
            
            // Update transaction status
            try (Connection conn = dataSource.getConnection()) {
                String sql = "UPDATE payment_transactions SET status = ? WHERE transaction_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, paymentStatus);
                    stmt.setString(2, transactionId);
                    stmt.executeUpdate();
                }
                
                // Record status history
                sql = "INSERT INTO payment_status_history (transaction_id, status) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, transactionId);
                    stmt.setString(2, paymentStatus);
                    stmt.executeUpdate();
                }
            }
            
            jsonResponse.put("success", true)
                       .put("status", paymentStatus);
            
        } catch (Exception e) {
            jsonResponse.put("success", false)
                       .put("message", "Error checking payment status: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.getWriter().write(jsonResponse.toString());
    }
}