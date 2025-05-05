package com.mycompany.umuranzi.models;

import java.time.LocalDateTime;

public class Purchase {

    private int purchaseId;
    private int userId;
    private int packageId;
    private LocalDateTime purchaseDate;
    private int quantity;
    private double totalPrice;
    private int remainingMeals;
    private int totalMeals;
    private String restaurantName;
    private MealPackage mealPackage;
    private Restaurant restaurant;

    public void setMealPackage(MealPackage mealPackage) {
        this.mealPackage = mealPackage;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getTotalMeals() {
        return totalMeals;
    }

    public void setTotalMeals(int totalMeals) {
        this.totalMeals = totalMeals;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    // Constructors
    public Purchase() {
    }

    public Purchase(int userId, int packageId, int quantity) {
        this.userId = userId;
        this.packageId = packageId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRemainingMeals() {
        return remainingMeals;
    }

    public void setRemainingMeals(int remainingMeals) {
        this.remainingMeals = remainingMeals;
    }
}
