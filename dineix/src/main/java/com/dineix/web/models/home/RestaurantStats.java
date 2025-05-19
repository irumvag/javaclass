package com.dineix.web.models.home;
import java.util.Map;


public class RestaurantStats {
    private int totalFollowers;
    private int totalPurchases;
    private double totalRevenue;
    private Map<String, Integer> monthlyPurchases; // Key: "YYYY-MM", Value: count
    private Map<String, Double> monthlyRevenue;    // Key: "YYYY-MM", Value: amount
    private Map<String, Integer> packagePopularity; // Key: package name, Value: purchases
    private Map<String, Integer> followerGrowth;   // Key: "YYYY-MM", Value: count

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
      // Getters and setters for all fields
    
    public Map<String, Integer> getMonthlyPurchases() { return monthlyPurchases; }
    public void setMonthlyPurchases(Map<String, Integer> monthlyPurchases) { this.monthlyPurchases = monthlyPurchases; }
    
    public Map<String, Double> getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(Map<String, Double> monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }
    
    public Map<String, Integer> getPackagePopularity() { return packagePopularity; }
    public void setPackagePopularity(Map<String, Integer> packagePopularity) { this.packagePopularity = packagePopularity; }
    
    public Map<String, Integer> getFollowerGrowth() { return followerGrowth; }
    public void setFollowerGrowth(Map<String, Integer> followerGrowth) { this.followerGrowth = followerGrowth; }
}

