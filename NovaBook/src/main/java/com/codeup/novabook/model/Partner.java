package com.codeup.novabook.model;

import java.time.LocalDateTime;

/**
 * Partner entity representing library members
 */
public class Partner {
    
    private int id;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private PartnerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum PartnerStatus {
        ACTIVO, INACTIVO
    }
    
    // Constructors
    public Partner() {}
    
    public Partner(String documentNumber, String firstName, String lastName, String email) {
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = PartnerStatus.ACTIVO;
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
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public PartnerStatus getStatus() {
        return status;
    }
    
    public void setStatus(PartnerStatus status) {
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
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return String.format("Partner{id=%d, documentNumber='%s', fullName='%s %s', email='%s', status=%s}", 
                           id, documentNumber, firstName, lastName, email, status);
    }
}
