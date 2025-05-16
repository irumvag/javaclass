package com.dineix.web.servlet.home;

import com.dineix.web.utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;



public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid login token.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean rememberMe = "on".equals(request.getParameter("rememberMe"));

        // Server-side validation
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        // Database authentication
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT user_id, full_name, password, role, email_verified FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        if (rs.getInt("email_verified") == 0) {
                            request.setAttribute("errorMessage", "Please verify your email before logging in.");
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                            return;
                        }

                        // Successful login
                        HttpSession session = request.getSession();
                        session.setAttribute("userId", rs.getInt("user_id"));
                        session.setAttribute("fullName", rs.getString("full_name"));
                        session.setAttribute("role", rs.getString("role"));

                        // Handle "Remember Me" (extend session timeout or use cookies)
                        if (rememberMe) {
                            session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
                        } else {
                            session.setMaxInactiveInterval(30 * 60); // 30 minutes
                        }

                        // Generate new CSRF token
                        session.setAttribute("csrfToken", UUID.randomUUID().toString());

                        // Redirect based on role
                        String role = rs.getString("role");
                        switch (role) {
                            case "ADMIN":
                                response.sendRedirect("admin/dashboard");
                                break;
                            case "RESTAURANT_OWNER":
                                response.sendRedirect(request.getContextPath() + "/restaurant/dashboard");
                                break;
                            default: // USER
                                response.sendRedirect(request.getContextPath() + "/user/dashboard");
                                break;
                        }
                    } else {
                        request.setAttribute("errorMessage", "Invalid email or password.");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "Invalid email or password.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            e.printStackTrace();
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Generate CSRF token for the form
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}