package com.dineix.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the session, if it exists
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Invalidate the session to clear all attributes
            session.invalidate();
        }

        // Redirect to the home page
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests the same as GET for simplicity
        doGet(request, response);
    }
}
