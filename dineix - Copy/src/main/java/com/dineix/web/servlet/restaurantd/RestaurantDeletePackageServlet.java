/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dineix.web.servlet.restaurantd;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/restaurant/delete-package")
public class RestaurantDeletePackageServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // CSRF token validation
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            out.print("{\"success\": false, \"message\": \"Invalid CSRF token.\"}");
            out.flush();
            return;
        }

        // Check if user is logged in and has RESTAURANT_OWNER role
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        if (userId == null || !"RESTAURANT_OWNER".equals(role)) {
            out.print("{\"success\": false, \"message\": \"Unauthorized.\"}");
            out.flush();
            return;
        }

        // Get parameters
        String packageIdStr = request.getParameter("packageId");
        int packageId;
        try {
            packageId = Integer.parseInt(packageIdStr);
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Invalid package ID.\"}");
            out.flush();
            return;
        }

        // Delete package
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM meal_packages WHERE package_id = ? AND restaurant_id IN (SELECT restaurant_id FROM restaurants WHERE owner_id = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, packageId);
                stmt.setInt(2, userId);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"Package not found or you don't have permission to delete it.\"}");
                }
            }
        } catch (SQLException e) {
            out.print("{\"success\": false, \"message\": \"An error occurred.\"}");
            e.printStackTrace();
        }
        out.flush();
    }
}