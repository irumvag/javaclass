package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import javax.naming.NamingException;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = new UserDAO().authenticate(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if (user.getRole().equals("RESTAURANT_OWNER")) {
                    response.sendRedirect("restaurant-dashboard.jsp");
                } else {
                    request.setAttribute("user", user);
                    response.sendRedirect("user-dashboard.jsp");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage",
                    "<strong>Login Failed!</strong> Please try again later.");
            throw new ServletException("Database error", e);
        }
    }
}
