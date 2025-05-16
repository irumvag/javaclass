package com.dineix.web.models.home;

import com.dineix.web.models.User;
import java.sql.Timestamp;

public class RoleRequest {
    private int requestId;
    private User user;
    private String requestedRole;
    private String status;
    private Timestamp requestDate;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantContact;

    // Constructor
    public RoleRequest() {
    }
     public RoleRequest(String status,String requestDate) {
         this.status=status;
         this.requestDate=Timestamp.valueOf(requestDate);
    }

    // Getters
    public int getRequestId() {
        return requestId;
    }

    public User getUser() {
        return user;
    }

    public String getRequestedRole() {
        return requestedRole;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantContact() {
        return restaurantContact;
    }

    // Setters
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequestedRole(String requestedRole) {
        this.requestedRole = requestedRole;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantContact(String restaurantContact) {
        this.restaurantContact = restaurantContact;
    }
}
