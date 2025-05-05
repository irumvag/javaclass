
package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.SQLException;
import javax.naming.NamingException;

public class ThemeServlet extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String theme = request.getParameter("theme");
        response.addCookie(new Cookie("theme", theme));
        
        if(user != null) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.saveThemePreference(user.getUserId(), theme);
            } catch (Exception e) {
                // Log error but don't break functionality
            }
        }
        Cookie themeCookie = new Cookie("theme", theme);
        themeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        response.addCookie(themeCookie);
        response.sendRedirect(request.getHeader("referer"));
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
