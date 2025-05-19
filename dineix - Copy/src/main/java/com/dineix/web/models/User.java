package com.dineix.web.models;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private Timestamp registrationDate;
    private String themePreference;
    private boolean emailVerified;

    // Constructor
    public User() {
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public String getThemePreference() {
        return themePreference;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setThemePreference(String themePreference) {
        this.themePreference = themePreference;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
