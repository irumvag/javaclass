/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.duneix.web.servlet.home;

import com.mycompany.duneix.web.utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;


public class RegisterServlet extends HttpServlet {
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CSRF token validation
        String csrfToken = request.getParameter("csrfToken");
        String sessionCsrfToken = (String) request.getSession().getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("errorMessage", "Invalid register token.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Get form parameters
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Server-side validation
        if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*].*")) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters, including 1 number and 1 special character.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Hash password with BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Database insertion
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (full_name, email, password, role, theme_preference, email_verified) VALUES (?, ?, ?, 'USER', 'light', 0)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fullName);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();

                // Generate new CSRF token for next form
                request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());
                request.setAttribute("successMessage", "Registration successful! Please check your email to verify your account.");
                request.getRequestDispatcher("login").forward(request, response);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry (email)
                request.setAttribute("errorMessage", "Email already registered.");
            } else {
                request.setAttribute("errorMessage", "An error occurred during registration. Please try again.");
                e.printStackTrace();
            }
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
        catch(Exception ex)
        {
           request.setAttribute("errorMessage", "Something went wrong!."); 
           request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Generate CSRF token for the form
        request.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}