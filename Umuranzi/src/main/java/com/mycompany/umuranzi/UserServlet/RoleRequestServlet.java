package com.mycompany.umuranzi.userservlet;

import com.mycompany.umuranzi.Utils.RoleRequestDAO;
import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.RoleRequest;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100)  // 100 MB
public class RoleRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String action = request.getParameter("action");
            
            RoleRequestDAO roleRequestDAO = new RoleRequestDAO();
            UserDAO userDAO = new UserDAO();
            
            // Process request
            if ("approve".equals(action)) {
                RoleRequest roleRequest = roleRequestDAO.getRequest(requestId);
                userDAO.updateUserRole(roleRequest.getUserId(), "RESTAURANT_OWNER");
            }
            
            roleRequestDAO.updateRequestStatus(requestId, action.toUpperCase());
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}