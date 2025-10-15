package com.codeup.novabook.model;

import java.time.LocalDateTime;

/**
 * User entity representing system users (ADMIN/ASISTENTE)
 */
public class User {
    
    private int id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum UserRole {
        ADMIN, ASISTENTE
    }
    
    public enum UserStatus {
        ACTIVO, INACTIVO
    }
    
    // Constructors
    public User() {}
    
    public User(String username, String password, String email, UserRole role, UserStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s', role=%s, status=%s}", 
                           id, username, email, role, status);
    }
}
