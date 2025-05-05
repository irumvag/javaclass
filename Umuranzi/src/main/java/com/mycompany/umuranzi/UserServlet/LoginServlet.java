package com.mycompany.umuranzi.UserServlet;

import com.mycompany.umuranzi.Utils.PurchaseDAO;
import com.mycompany.umuranzi.Utils.RestaurantDAO;
import com.mycompany.umuranzi.Utils.UserDAO;
import com.mycompany.umuranzi.models.Purchase;
import com.mycompany.umuranzi.models.Restaurant;
import com.mycompany.umuranzi.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import javax.naming.NamingException;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = new UserDAO().authenticate(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                RestaurantDAO restaurantDAO = new RestaurantDAO();
                List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();
                request.setAttribute("restaurants", restaurants);
                // Get user purchases
                PurchaseDAO purchaseDAO = new PurchaseDAO();
                List<Purchase> purchases = purchaseDAO.getUserPurchases(user.getUserId());

                // Calculate statistics
                int totalRestaurants = restaurantDAO.getTotalRestaurants();
                int totalPurchased = purchases.stream().mapToInt(Purchase::getTotalMeals).sum();
                int totalRemaining = purchases.stream().mapToInt(Purchase::getRemainingMeals).sum();

                request.setAttribute("restaurants", restaurants);
                request.setAttribute("purchases", purchases);
                request.setAttribute("totalRestaurants", totalRestaurants);
                request.setAttribute("totalPurchased", totalPurchased);
                
                if (user.getRole() == User.UserRole.RESTAURANT_OWNER) {
                    response.sendRedirect("restaurant-dashboard.jsp");
                } else {
                    request.setAttribute("user", user);
                    response.sendRedirect("user-dashboard.jsp");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage",
                    "<strong>Login Failed!</strong> Please try again later.");
            throw new ServletException("Database error", e);
        }
    }
}
