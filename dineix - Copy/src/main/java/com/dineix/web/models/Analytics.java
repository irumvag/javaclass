package com.dineix.web.models;

import java.util.Map;

public class Analytics {
    private int totalUsers;
    private int totalRestaurants;
    private double totalRevenue;
    private Map<String, Integer> monthlyUsers; // Key: "YYYY-MM", Value: Count
    private Map<String, Double> restaurantRevenue; // Key: Restaurant Name, Value: Revenue
    private Map<String, Integer> mealPackagesPerRestaurant; // Key: Restaurant Name, Value: Meal Package Count
    private Map<String, Map<String, Integer>> monthlyPurchasesPerRestaurant; // Key: Restaurant Name, Value: Map of "YYYY-MM" to Purchase Count

    // Constructor
    public Analytics() {
    }

    // Getters
    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalRestaurants() {
        return totalRestaurants;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public Map<String, Integer> getMonthlyUsers() {
        return monthlyUsers;
    }

    public Map<String, Double> getRestaurantRevenue() {
        return restaurantRevenue;
    }

    public Map<String, Integer> getMealPackagesPerRestaurant() {
        return mealPackagesPerRestaurant;
    }

    public Map<String, Map<String, Integer>> getMonthlyPurchasesPerRestaurant() {
        return monthlyPurchasesPerRestaurant;
    }

    // Setters
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setTotalRestaurants(int totalRestaurants) {
        this.totalRestaurants = totalRestaurants;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setMonthlyUsers(Map<String, Integer> monthlyUsers) {
        this.monthlyUsers = monthlyUsers;
    }

    public void setRestaurantRevenue(Map<String, Double> restaurantRevenue) {
        this.restaurantRevenue = restaurantRevenue;
    }

    public void setMealPackagesPerRestaurant(Map<String, Integer> mealPackagesPerRestaurant) {
        this.mealPackagesPerRestaurant = mealPackagesPerRestaurant;
    }

    public void setMonthlyPurchasesPerRestaurant(Map<String, Map<String, Integer>> monthlyPurchasesPerRestaurant) {
        this.monthlyPurchasesPerRestaurant = monthlyPurchasesPerRestaurant;
    }
}
