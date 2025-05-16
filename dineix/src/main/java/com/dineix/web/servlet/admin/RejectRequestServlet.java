package com.dineix.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import jakarta.annotation.Resource;

@WebServlet("/admin/reject-request")
public class RejectRequestServlet extends HttpServlet {
    @Resource(name = "jdbc/dineix")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("requestId");
        String csrfToken = request.getParameter("csrfToken");
        response.setContentType("application/json");

        if (!isValidCsrfToken(request, csrfToken)) {
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid CSRF token\"}");
            return;
        }

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            String query = "UPDATE role_requests SET status = 'REJECTED' WHERE request_id = ? AND status = 'PENDING'";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(requestId));
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Request rejected\"}");
                } else {
                    response.getWriter().write("{\"success\": false, \"message\": \"Request not found or already processed\"}");
                }
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isValidCsrfToken(HttpServletRequest request, String csrfToken) {
        return csrfToken != null && csrfToken.equals(request.getSession().getAttribute("csrfToken"));
    }
}
