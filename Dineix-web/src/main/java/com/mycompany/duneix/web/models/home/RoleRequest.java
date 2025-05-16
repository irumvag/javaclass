package com.mycompany.duneix.web.models.home;


public class RoleRequest {
    private String status;
    private String requestDate;

    public RoleRequest(String status, String requestDate) {
        this.status = status;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}