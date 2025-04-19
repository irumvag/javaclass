package com.mycompany.assingment;

import com.mycompany.assingment.dao.User;
import com.mycompany.assingment.dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class EditUserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            UserDao dao = new UserDao();
            User user = dao.selectUser(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("editUser.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard");
            return;
        }
    }
}
