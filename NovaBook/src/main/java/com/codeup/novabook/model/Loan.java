package com.codeup.novabook.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Loan entity representing book loans
 */
public class Loan {
    
    private int id;
    private int partnerId;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for display purposes
    private String partnerName;
    private String bookTitle;
    private String bookIsbn;
    
    public enum LoanStatus {
        ACTIVO, DEVUELTO, VENCIDO
    }
    
    // Constructors
    public Loan() {}
    
    public Loan(int partnerId, int bookId, LocalDate loanDate, LocalDate dueDate) {
        this.partnerId = partnerId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.fineAmount = BigDecimal.ZERO;
        this.status = LoanStatus.ACTIVO;
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
    
    public int getPartnerId() {
        return partnerId;
    }
    
    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public LocalDate getLoanDate() {
        return loanDate;
    }
    
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public BigDecimal getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public LoanStatus getStatus() {
        return status;
    }
    
    public void setStatus(LoanStatus status) {
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
    
    public String getPartnerName() {
        return partnerName;
    }
    
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getBookIsbn() {
        return bookIsbn;
    }
    
    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }
    
    public boolean isOverdue() {
        return status == LoanStatus.ACTIVO && LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }
    
    @Override
    public String toString() {
        return String.format("Loan{id=%d, partnerId=%d, bookId=%d, loanDate=%s, dueDate=%s, " +
                           "returnDate=%s, status=%s, fineAmount=%s}", 
                           id, partnerId, bookId, loanDate, dueDate, returnDate, status, fineAmount);
    }
}
