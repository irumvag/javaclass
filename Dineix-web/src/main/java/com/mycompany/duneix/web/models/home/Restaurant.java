/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.duneix.web.models.home;

public class Restaurant {
    private int restaurantId;
    private int ownerId;
    private String name;
    private String address;
    private String contactNumber;
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
