/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dineix.web.servlet.admin;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/admin/delete-user")
public class DeleteUserServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer adminUserId = (Integer) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        String userId = request.getParameter("userId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Validate session and CSRF token
        if (adminUserId == null || !"ADMIN".equals(role)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\": false, \"message\": \"Unauthorized access.\"}");
            return;
        }

        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print("{\"success\": false, \"message\": \"Invalid CSRF token.\"}");
            return;
        }

        if (userId == null || !userId.matches("\\d+")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Invalid user ID.\"}");
            return;
        }

        // Prevent admin from deleting themselves
        if (Integer.parseInt(userId) == adminUserId) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Cannot delete your own account.\"}");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try {
                // Delete dependent records
                String[] deleteQueries = {
                    "DELETE FROM user_meal_purchases WHERE user_id = ?",
                    "DELETE FROM role_requests WHERE user_id = ?",
                    "DELETE FROM notifications WHERE user_id = ?",
                    "DELETE FROM user_restaurant_follows WHERE user_id = ?",
                    // Nullify restaurant owner_id to avoid foreign key violation
                    "UPDATE restaurants SET owner_id = NULL WHERE owner_id = ?"
                };

                for (String query : deleteQueries) {
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, Integer.parseInt(userId));
                        stmt.executeUpdate();
                    }
                }

                // Delete user
                String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                    stmt.setInt(1, Integer.parseInt(userId));
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 0) {
                        conn.rollback();
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"success\": false, \"message\": \"User not found.\"}");
                        return;
                    }
                }

                conn.commit();
                out.print("{\"success\": true, \"message\": \"User deleted successfully.\"}");
            } catch (SQLException e) {
                conn.rollback();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error deleting user: " + e.getMessage() + "\"}");
                log("SQLException in DeleteUserServlet: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Database connection error.\"}");
            log("SQLException in DeleteUserServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }
}