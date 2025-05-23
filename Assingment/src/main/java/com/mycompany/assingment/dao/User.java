package com.mycompany.assingment.dao;
public class User {
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String country;

    public User(int id, String username, String password, String phone, String email, String country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.country = country;
    }

    User() {
        //user
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public User(String username, String password, String phone, String email, String country) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.country = country;
    }
}
