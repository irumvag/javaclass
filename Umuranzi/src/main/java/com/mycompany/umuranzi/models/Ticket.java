package com.mycompany.umuranzi.models;

import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int purchaseId;
    private int restaurantId;
    private String ticketCode;
    private LocalDateTime generatedAt;
    private LocalDateTime redeemedAt;
    private TicketStatus status;
    private byte[] qrCode;

    public enum TicketStatus {
        ACTIVE, USED, EXPIRED
    }

    // Constructors
    public Ticket() {}

    public Ticket(int purchaseId, int restaurantId, String ticketCode) {
        this.purchaseId = purchaseId;
        this.restaurantId = restaurantId;
        this.ticketCode = ticketCode;
    }

    // Getters and Setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public int getPurchaseId() { return purchaseId; }
    public void setPurchaseId(int purchaseId) { this.purchaseId = purchaseId; }
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public LocalDateTime getRedeemedAt() { return redeemedAt; }
    public void setRedeemedAt(LocalDateTime redeemedAt) { this.redeemedAt = redeemedAt; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    public byte[] getQrCode() { return qrCode; }
    public void setQrCode(byte[] qrCode) { this.qrCode = qrCode; }
}
