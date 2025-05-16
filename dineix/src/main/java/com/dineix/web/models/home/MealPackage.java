/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dineix.web.models.home;

public class MealPackage {
    private int packageId;
    private String packageName;
    private String description;
    private int numberOfMeals;
    private double price;
    private String restaurantName;

    public MealPackage(int packageId, String packageName, String description, int numberOfMeals, double price, String restaurantName) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.description = description;
        this.numberOfMeals = numberOfMeals;
        this.price = price;
        this.restaurantName = restaurantName;
    }

    // Getters and Setters
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfMeals() {
        return numberOfMeals;
    }

    public void setNumberOfMeals(int numberOfMeals) {
        this.numberOfMeals = numberOfMeals;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
