package com.mycompany.umuranzi.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class VerificationServlet extends HttpServlet {

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        try {
            UserDAO userDAO = new UserDAO();
            if(userDAO.verifyToken(token)) {
                request.setAttribute("successMessage", "Email verified successfully!");
            } else {
                request.setAttribute("errorMessage", "Invalid or expired verification link");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Verification error: " + e.getMessage());
        }
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

}
