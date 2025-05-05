package com.mycompany.umuranzi.models;

import java.time.LocalDateTime;

public class RoleRequest {
    private int requestId;
    private int userId;
    private String documentPath;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    public RoleRequest() {
    }

    public RoleRequest(int requestId, int userId, String documentPath, String status, LocalDateTime createdAt) {
        this.requestId = requestId;
        this.userId = userId;
        this.documentPath = documentPath;
        this.status = status;
        this.createdAt = createdAt;
    }

    public RoleRequest(int requestId, int userId, String documentPath, String message, String status, LocalDateTime createdAt) {
        this.requestId = requestId;
        this.userId = userId;
        this.documentPath = documentPath;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    
    // Getters and setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}