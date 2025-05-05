package com.mycompany.umuranzi.Utils;

import com.mycompany.umuranzi.models.MealPackage;
import com.mycompany.umuranzi.models.Purchase;
import com.mycompany.umuranzi.models.Restaurant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class PurchaseDAO {

public List<Purchase> getActivePurchases(int userId) throws SQLException, NamingException {
    List<Purchase> purchases = new ArrayList<>();
    String sql = "SELECT p.purchase_id, p.user_id, p.package_id, p.purchase_date, "
               + "p.quantity, p.total_price, p.remaining_meals, "
               + "mp.package_name, mp.number_of_meals, r.name AS restaurant_name "
               + "FROM purchases p "
               + "JOIN meal_packages mp ON p.package_id = mp.package_id "
               + "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id "
               + "WHERE p.user_id = ? AND p.remaining_meals > 0";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            // Initialize Purchase with core fields
            Purchase purchase = new Purchase();
            purchase.setPurchaseId(rs.getInt("purchase_id"));
            purchase.setUserId(rs.getInt("user_id"));
            purchase.setPackageId(rs.getInt("package_id"));
            purchase.setPurchaseDate(rs.getObject("purchase_date", LocalDateTime.class));
            purchase.setQuantity(rs.getInt("quantity"));
            purchase.setTotalPrice(rs.getDouble("total_price"));
            purchase.setRemainingMeals(rs.getInt("remaining_meals"));

            // Add MealPackage details
            MealPackage mealPackage = new MealPackage();
            mealPackage.setPackageId(rs.getInt("package_id"));
            mealPackage.setPackageName(rs.getString("package_name"));
            mealPackage.setNumberOfMeals(rs.getInt("number_of_meals"));
            purchase.setMealPackage(mealPackage);

            // Add Restaurant details
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantId(rs.getInt("restaurant_id"));  // Added from join
            restaurant.setName(rs.getString("restaurant_name"));
            purchase.setRestaurant(restaurant);

            purchases.add(purchase);
        }
    }
    return purchases;
}

    public List<Purchase> getUserPurchases(int userId) throws SQLException, NamingException {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT p.*, mp.number_of_meals, r.name AS restaurant_name "
                + "FROM purchases p "
                + "JOIN meal_packages mp ON p.package_id = mp.package_id "
                + "JOIN restaurants r ON mp.restaurant_id = r.restaurant_id "
                + "WHERE p.user_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Purchase purchase = new Purchase();
                // Set basic purchase info
                purchase.setPurchaseId(rs.getInt("purchase_id"));
                purchase.setUserId(rs.getInt("user_id"));
                purchase.setPackageId(rs.getInt("package_id"));
                purchase.setPurchaseDate(rs.getObject("purchase_date", LocalDateTime.class));
                purchase.setQuantity(rs.getInt("quantity"));
                purchase.setTotalPrice(rs.getDouble("total_price"));
                purchase.setRemainingMeals(rs.getInt("remaining_meals"));

                // Calculate total meals (quantity * meals per package)
                int mealsPerPackage = rs.getInt("number_of_meals");
                purchase.setTotalMeals(purchase.getQuantity() * mealsPerPackage);

                // Set restaurant name as additional info
                purchase.setRestaurantName(rs.getString("restaurant_name"));

                purchases.add(purchase);
            }
        }
        return purchases;
    }
}
