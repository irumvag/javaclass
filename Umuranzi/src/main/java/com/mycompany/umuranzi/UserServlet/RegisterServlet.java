package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            if (new UserDAO().emailExists(email)) {
                request.setAttribute("errorMessage", "Email already registered");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            // Add to RegistrationServlet before emailExists check
            if (!newUser.isValidEmail()) {
                request.setAttribute("errorMessage",
                        "<strong>Invalid Email!</strong> Please enter a valid email address.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            newUser.setPassword(password);
            newUser.setRole(User.UserRole.valueOf("USER"));

            new UserDAO().createUser(newUser);
            request.setAttribute("successMessage",
                    "<strong>Registration Successful!</strong> You can now login with your credentials.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage",
                    "<strong>Registration Failed!</strong> Please try again later.");
            throw new ServletException("Registration failed", e);
        }
    }
}
