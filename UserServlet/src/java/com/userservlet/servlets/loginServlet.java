package com.userservlet.servlets;

import com.userservlet.utils.DBconnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBconnection.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                // Verify password (using BCrypt)
                if (BCrypt.checkpw(password, storedHash)) {
                    // Create session
                    HttpSession session = request.getSession();
                    session.setAttribute("user", username);
                    //response.sendRedirect("dashboard.jsp");
                    response.sendRedirect("dashboard.jsp");
                    return;
                }
                else
                {
                  System.out.println("Password failed");
                  request.setAttribute("error", "Invalid username or password");
                }
            }
            else
            {
            // Invalid credentials
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            request.setAttribute("error", "Database class name error");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
