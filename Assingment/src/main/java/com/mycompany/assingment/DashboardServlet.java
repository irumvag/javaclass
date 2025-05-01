
package com.mycompany.assingment;

import com.mycompany.assingment.dao.User;
import com.mycompany.assingment.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList;
        userList = UserDao.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList;
        String search = request.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
           userList = UserDao.searchUsers(search);
        } else {
           userList = UserDao.getAllUsers();
        }
        request.setAttribute("userList", userList);
        request.setAttribute("search", search);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
