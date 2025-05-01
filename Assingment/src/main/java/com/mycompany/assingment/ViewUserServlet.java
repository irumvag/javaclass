package com.mycompany.assingment;

import com.mycompany.assingment.dao.UserDao;
import com.mycompany.assingment.dao.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String id = request.getParameter("id");

        UserDao userDao = new UserDao();
        User user;
        try {
            user = userDao.selectUser(Integer.parseInt(id));
            request.setAttribute("user", user);
        } catch (Exception ex) {
            Logger.getLogger(ViewUserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }  
        RequestDispatcher dispatcher = request.getRequestDispatcher("view-user.jsp");
        dispatcher.forward(request, response);
    }
}
