//package com.mycompany.duneix.web.servlet.userd;
//
//import com.mycompany.duneix.web.servlet.home.LoginServlet;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//
//public class RequestRoleServlet extends HttpServlet {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/dineix";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "";
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // CSRF token validation
//        try {
//            // CSRF token validation
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
//        // Get parameters
//        String requestedRole = request.getParameter("requestedRole");
//        String reason = request.getParameter("reason");
//
//        // Validate input
//        if (!"RESTAURANT_OWNER".equals(requestedRole) || reason == null || reason.trim().isEmpty()) {
//            request.setAttribute("errorMessage", "Invalid role request data.");
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            return;
//        }
//
//        // Check if a pending request exists
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            String checkSql = "SELECT COUNT(*) FROM role_requests WHERE user_id = ? AND requested_role = ? AND status = 'PENDING'";
//            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
//                stmt.setInt(1, userId);
//                stmt.setString(2, requestedRole);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next() && rs.getInt(1) > 0) {
//                    request.setAttribute("errorMessage", "You already have a pending role request.");
//                    request.getRequestDispatcher("/user/dashboard").forward(request, response);
//                    return;
//                }
//            }
//
//            // Insert role request
//            String insertSql = "INSERT INTO role_requests (user_id, requested_role, status, reason, request_date) VALUES (?, ?, 'PENDING', ?, NOW())";
//            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
//                stmt.setInt(1, userId);
//                stmt.setString(2, requestedRole);
//                stmt.setString(3, reason);
//                stmt.executeUpdate();
//
//                request.setAttribute("successMessage", "Role request submitted successfully.");
//                request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            }
//        } catch (SQLException e) {
//            request.setAttribute("errorMessage", "An error occurred while submitting your role request.");
//            e.printStackTrace();
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//        }
//    }
//}