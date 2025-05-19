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
import java.sql.SQLException;

@WebServlet("/user/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");

        if (userId == null) {
            request.setAttribute("errorMessage", "Please log in to update your profile.");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        String fullName = request.getParameter("fullName");
        String themePreference = request.getParameter("themePreference");

        if (fullName == null || fullName.trim().isEmpty() || themePreference == null) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("dashboard").forward(request, response);
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE users SET full_name = ?, theme_preference = ? WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fullName);
                stmt.setString(2, themePreference);
                stmt.setInt(3, userId);
                stmt.executeUpdate();
            }

            // Update session attributes
            request.getSession().setAttribute("fullName", fullName);
            request.getSession().setAttribute("themePreference", themePreference);
            request.setAttribute("successMessage", "Profile updated successfully.");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        request.getRequestDispatcher("dashboard").forward(request, response);
    }
}