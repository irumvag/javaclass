package com.mycompany.assingment;

import com.mycompany.assingment.dao.User;
import com.mycompany.assingment.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

public class UpdateUserServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String country = request.getParameter("country");

            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

            User user = new User(id, username, hashed, phone, email, country);
            UserDao dao = new UserDao();
            dao.updateUser(user);

            response.sendRedirect("dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard");
        }
    }
}
