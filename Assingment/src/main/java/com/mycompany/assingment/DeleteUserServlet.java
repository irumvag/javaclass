package com.mycompany.assingment;

import com.mycompany.assingment.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            UserDao dao = new UserDao();
            dao.deleteUser(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("dashboard");
    }
}
