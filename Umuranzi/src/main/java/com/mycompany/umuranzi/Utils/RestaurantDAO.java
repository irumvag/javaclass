package com.mycompany.umuranzi.Utils;

import com.mycompany.umuranzi.models.MealPackage;
import com.mycompany.umuranzi.models.Restaurant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;

public class RestaurantDAO {

    public List<Restaurant> getAllRestaurants() throws SQLException, NamingException {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT * FROM restaurants";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Restaurant r = new Restaurant(rs.getInt("restaurant_id"), rs.getInt("owner_id"), rs.getString("name"), rs.getString("address"), rs.getString("contact_number"), rs.getString("logo_url"), rs.getString("description"));
                    restaurants.add(r);
                }
            }
        }
        return restaurants;
    }

    public int getFollowerCount(int restaurantId) throws SQLException, NamingException {
        String sql = "SELECT COUNT(*) FROM restaurant_follows WHERE restaurant_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Map<Integer, Boolean> getUserFollows(int userId) throws SQLException, NamingException {
        Map<Integer, Boolean> follows = new HashMap<>();
        String sql = "SELECT restaurant_id FROM restaurant_follows WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    follows.put(rs.getInt("restaurant_id"), true);
                }
            }
        }
        return follows;
    }

    public void unfollowRestaurant(int userId, int restaurantId) throws SQLException, NamingException {
        String sql = "DELETE FROM restaurant_follows WHERE user_id = ? AND restaurant_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, restaurantId);
            stmt.executeUpdate();
        }
    }

    public void followRestaurant(int userId, int restaurantId) throws SQLException, NamingException {
        String sql = "INSERT INTO restaurant_follows (user_id, restaurant_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, restaurantId);
            stmt.executeUpdate();
        }
    }

    public List<Restaurant> getAllRestaurantsWithPackages() throws SQLException, NamingException {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT r.restaurant_id, r.name AS restaurant_name, r.logo_url, "
                + "mp.package_id, mp.package_name, mp.number_of_meals, mp.price, mp.description "
                + "FROM restaurants r "
                + "LEFT JOIN meal_packages mp ON r.restaurant_id = mp.restaurant_id "
                + "ORDER BY r.name, mp.price";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            Restaurant currentRestaurant = null;
            int lastRestaurantId = -1;

            while (rs.next()) {
                int restaurantId = rs.getInt("restaurant_id");

                // New restaurant detected
                if (restaurantId != lastRestaurantId) {
                    currentRestaurant = new Restaurant();
                    currentRestaurant.setRestaurantId(restaurantId);
                    currentRestaurant.setName(rs.getString("restaurant_name"));
                    currentRestaurant.setLogoUrl(rs.getString("logo_url"));
                    currentRestaurant.setMealPackages(new ArrayList<>());
                    restaurants.add(currentRestaurant);
                    lastRestaurantId = restaurantId;
                }

                // Add meal package if exists (LEFT JOIN might return nulls)
                if (rs.getObject("package_id") != null) {
                    MealPackage mealPackage = new MealPackage();
                    mealPackage.setPackageId(rs.getInt("package_id"));
                    mealPackage.setPackageName(rs.getString("package_name"));
                    mealPackage.setNumberOfMeals(rs.getInt("number_of_meals"));
                    mealPackage.setPrice(rs.getDouble("price"));
                    mealPackage.setDescription(rs.getString("description"));

                    currentRestaurant.getMealPackages().add(mealPackage);
                }
            }
        }
        return restaurants;
    }

    public int getTotalRestaurants() throws SQLException, NamingException {
        String sql = "SELECT COUNT(*) FROM restaurants";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
