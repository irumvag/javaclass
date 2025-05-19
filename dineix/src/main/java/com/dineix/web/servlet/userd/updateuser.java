/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dineix.web.servlet.userd;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Chairman
 */
public class updateuser extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("dashboard").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            response.sendRedirect("dashboard");
            return;
        }

        String fullName = request.getParameter("fullName");
        String themePreference = request.getParameter("themePreference");

        if (fullName == null || fullName.trim().isEmpty() || themePreference == null) {
            request.setAttribute("errorMessage", "All fields are required.");
            response.sendRedirect("dashboard");
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
            request.setAttribute("successMessage", "Profile updated successfully.");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }
        response.sendRedirect("dashboard");
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
