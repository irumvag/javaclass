package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.RoleRequestDAO;
import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.RoleRequest;
import com.mycompany.umuranzi.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;

public class adminselvet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verify admin role
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Get pending requests
            RoleRequestDAO roleRequestDAO = new RoleRequestDAO();
            List<RoleRequest> requests = roleRequestDAO.getPendingRequests();
            
            // Get all users
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();
            
            request.setAttribute("roleRequests", requests);
            request.setAttribute("users", users);
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
            
        } catch (SQLException | NamingException e) {
            request.setAttribute("error", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
