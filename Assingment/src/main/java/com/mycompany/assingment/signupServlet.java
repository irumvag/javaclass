
package com.mycompany.assingment;

import com.mycompany.assingment.utils.DBconnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class signupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        // Encrypt password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DBconnection.getConnection()) {
            // Check if username or email already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? OR email = ?");
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                request.setAttribute("error", "Username or Email already exists!");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
                return;
            }
            // Insert user into DB
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO users (username, password, phone, email, country) VALUES (?, ?, ?, ?, ?)"
            );
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            insertStmt.setString(3, phone);
            insertStmt.setString(4, email);
            insertStmt.setString(5, country);

            int result = insertStmt.executeUpdate();

            if (result > 0) {
                // Registration successful, redirect to login
                response.sendRedirect("index.jsp");
            } else {
                request.setAttribute("error", "Registration failed!");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Internal server error!");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }
}

