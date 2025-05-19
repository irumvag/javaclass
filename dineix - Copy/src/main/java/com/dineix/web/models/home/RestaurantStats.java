package com.dineix.web.models.home;

public class RestaurantStats {
    private int totalFollowers;
    private int totalPurchases;
    private double totalRevenue;

    public RestaurantStats(int totalFollowers, int totalPurchases, double totalRevenue) {
        this.totalFollowers = totalFollowers;
        this.totalPurchases = totalPurchases;
        this.totalRevenue = totalRevenue;
    }
    public RestaurantStats()
    {
    }
    // Getters and Setters
    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}