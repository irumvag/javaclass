package com.mycompany.umuranzi.models;

import java.time.LocalDateTime;

public class Redemption {
    private int redemptionId;
    private int ticketId;
    private int restaurantId;
    private LocalDateTime redemptionTime;

    // Constructors
    public Redemption() {}

    public Redemption(int ticketId, int restaurantId) {
        this.ticketId = ticketId;
        this.restaurantId = restaurantId;
    }

    // Getters and Setters
    public int getRedemptionId() { return redemptionId; }
    public void setRedemptionId(int redemptionId) { this.redemptionId = redemptionId; }
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public LocalDateTime getRedemptionTime() { return redemptionTime; }
    public void setRedemptionTime(LocalDateTime redemptionTime) { this.redemptionTime = redemptionTime; }
}