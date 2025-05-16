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
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class UpdateProfileServlet extends HttpServlet {
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
//        String fullName = request.getParameter("fullName");
//        String themePreference = request.getParameter("themePreference");
//
//        // Validate input
//        if (fullName == null || fullName.trim().isEmpty() || themePreference == null || (!themePreference.equals("light") && !themePreference.equals("dark"))) {
//            request.setAttribute("errorMessage", "Invalid input data.");
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            return;
//        }
//
//        // Update user profile
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            String sql = "UPDATE users SET full_name = ?, theme_preference = ? WHERE user_id = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, fullName);
//                stmt.setString(2, themePreference);
//                stmt.setInt(3, userId);
//                stmt.executeUpdate();
//
//                // Update session attributes
//                request.getSession().setAttribute("fullName", fullName);
//                request.getSession().setAttribute("themePreference", themePreference);
//
//                request.setAttribute("successMessage", "Profile updated successfully.");
//                request.getRequestDispatcher("/user/dashboard").forward(request, response);
//            }
//        } catch (SQLException e) {
//            request.setAttribute("errorMessage", "An error occurred while updating your profile.");
//            e.printStackTrace();
//            request.getRequestDispatcher("/user/dashboard").forward(request, response);
//        }
//    }
//}