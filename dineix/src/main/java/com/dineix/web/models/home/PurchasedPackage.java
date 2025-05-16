/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dineix.web.models.home;

public class PurchasedPackage {
    private int purchaseId;
    private String packageName;
    private String description;
    private int mealsRemaining;
    private String purchaseDate;
    private String redemptionCode; // New field

    public PurchasedPackage(int purchaseId, String packageName, String description, int mealsRemaining, String purchaseDate, String redemptionCode) {
        this.purchaseId = purchaseId;
        this.packageName = packageName;
        this.description = description;
        this.mealsRemaining = mealsRemaining;
        this.purchaseDate = purchaseDate;
        this.redemptionCode = redemptionCode;
    }

    // Getters and setters
    public String getRedemptionCode() { return redemptionCode; }
    public void setRedemptionCode(String redemptionCode) { this.redemptionCode = redemptionCode; }
    
    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
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

    public int getMealsRemaining() {
        return mealsRemaining;
    }

    public void setMealsRemaining(int mealsRemaining) {
        this.mealsRemaining = mealsRemaining;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}