package com.mycompany.umuranzi.models;

import java.util.List;

public class Restaurant {

    private int restaurantId;
    private int ownerId;
    private String name;
    private String address;
    private String contactNumber;
    private String logoUrl;
    private String description;
    private List<MealPackage> mealPackages;

    public List<MealPackage> getMealPackages() {
        return mealPackages;
    }

    public void setMealPackages(List<MealPackage> mealPackages) {
        this.mealPackages = mealPackages;
    }

    public Restaurant(int restaurantId, int ownerId, String name, String address, String contactNumber, String logoUrl, String description) {
        this.restaurantId = restaurantId;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    // Constructors
    public Restaurant() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and Setters
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
