package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.RestaurantDAO;
import com.mycompany.umuranzi.models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.sql.SQLException;
import javax.naming.NamingException;



public class FollowServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if(user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
            boolean follow = Boolean.parseBoolean(request.getParameter("follow"));
            
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            
            if(follow) {
                restaurantDAO.followRestaurant(user.getUserId(), restaurantId);
            } else {
                restaurantDAO.unfollowRestaurant(user.getUserId(), restaurantId);
            }
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
            
        } catch (SQLException | NamingException | NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
