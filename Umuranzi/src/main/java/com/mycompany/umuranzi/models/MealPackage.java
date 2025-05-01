package com.mycompany.umuranzi.models;

import java.time.LocalDateTime;

public class MealPackage {
    private int packageId;
    private int restaurantId;
    private String packageName;
    private String description;
    private int numberOfMeals;
    private double price;
    private LocalDateTime createdAt;

    // Constructors
    public MealPackage() {}

    public MealPackage(int restaurantId, String packageName, int numberOfMeals, double price) {
        this.restaurantId = restaurantId;
        this.packageName = packageName;
        this.numberOfMeals = numberOfMeals;
        this.price = price;
    }

    // Getters and Setters
    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getNumberOfMeals() { return numberOfMeals; }
    public void setNumberOfMeals(int numberOfMeals) { this.numberOfMeals = numberOfMeals; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
