package com.mycompany.umuranzi.models;

public class Restaurant {
    private int restaurantId;
    private int ownerId;
    private String name;
    private String address;
    private String contactNumber;
    private String logoUrl;

    // Constructors
    public Restaurant() {}

    public Restaurant(int restaurantId, String name, int ownerId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.ownerId = ownerId;
    }

    // Getters and Setters
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
