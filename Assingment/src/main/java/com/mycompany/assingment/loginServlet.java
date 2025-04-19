package com.mycompany.assingment;
import com.mycompany.assingment.utils.DBconnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class loginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBconnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                // Verify password (using BCrypt)
                if (BCrypt.checkpw(password, storedHash)) {
                    // Correct password
                    HttpSession session = request.getSession();
                    session.setAttribute("user", username);
                    response.sendRedirect("dashboard");
                    return;
                } else {
                    // Incorrect password
                    System.out.println("Password failed");
                    request.setAttribute("error", "Invalid password");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return; // Don't continue execution
                }
            } else {
                // Invalid credentials
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Redirect failed");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Redirect failed");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
