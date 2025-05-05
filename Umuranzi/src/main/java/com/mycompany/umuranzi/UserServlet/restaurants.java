package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.RestaurantDAO;
import com.mycompany.umuranzi.models.Restaurant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class restaurants extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();
            request.setAttribute("restaurants", restaurants);
            request.getRequestDispatcher("restaurants.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
 
}
