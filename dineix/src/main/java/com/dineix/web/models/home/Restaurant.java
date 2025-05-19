/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dineix.web.models.home;

public class Restaurant {
    private int restaurantId;
    private int ownerId;
    private String name;
    private String address;
    private String contactNumber;
    
    public Restaurant(){}
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    private String logoUrl;
    private String description;

    public Restaurant(int restaurantId, int ownerId, String name, String address, String contactNumber, String logoUrl, String description) {
        this.restaurantId = restaurantId;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public int getRestaurantId() { return restaurantId; }
    public int getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
    public String getLogoUrl() { return logoUrl; }
    public String getDescription() { return description; }
}



