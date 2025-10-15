package com.codeup.novabook.service;

import com.codeup.novabook.model.Loan;
import java.util.List;

/**
 * Service interface for Loan operations
 */
public interface LoanService {
    
    /**
     * Create a new loan
     */
    Loan createLoan(int partnerId, int bookId) throws Exception;
    
    /**
     * Get loan by ID
     */
    Loan getLoanById(int id) throws Exception;
    
    /**
     * Get all loans
     */
    List<Loan> getAllLoans() throws Exception;
    
    /**
     * Get active loans
     */
    List<Loan> getActiveLoans() throws Exception;
    
    /**
     * Get overdue loans
     */
    List<Loan> getOverdueLoans() throws Exception;
    
    /**
     * Get loans by partner ID
     */
    List<Loan> getLoansByPartnerId(int partnerId) throws Exception;
    
    /**
     * Get loans by book ID
     */
    List<Loan> getLoansByBookId(int bookId) throws Exception;
    
    /**
     * Get all loans with partner and book details
     */
    List<Loan> getAllLoansWithDetails() throws Exception;
    
    /**
     * Get overdue loans with partner and book details
     */
    List<Loan> getOverdueLoansWithDetails() throws Exception;
    
    /**
     * Return a book
     */
    Loan returnBook(int loanId) throws Exception;
    
    /**
     * Calculate fine for overdue loan
     */
    double calculateFine(Loan loan) throws Exception;
    
    /**
     * Update loan information
     */
    Loan updateLoan(Loan loan) throws Exception;
    
    /**
     * Delete loan by ID
     */
    boolean deleteLoan(int id) throws Exception;
    
    /**
     * Check if partner can borrow books (is active and has no overdue books)
     */
    boolean canPartnerBorrow(int partnerId) throws Exception;
    
    /**
     * Check if book is available for loan
     */
    boolean isBookAvailable(int bookId) throws Exception;
}
