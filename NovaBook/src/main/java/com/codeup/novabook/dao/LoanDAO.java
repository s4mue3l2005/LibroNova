package com.codeup.novabook.dao;

import com.codeup.novabook.model.Loan;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Loan entity
 */
public interface LoanDAO {
    
    /**
     * Create a new loan
     */
    Loan create(Loan loan);
    
    /**
     * Find loan by ID
     */
    Optional<Loan> findById(int id);
    
    /**
     * Get all loans
     */
    List<Loan> findAll();
    
    /**
     * Get active loans
     */
    List<Loan> findActive();
    
    /**
     * Get overdue loans
     */
    List<Loan> findOverdue();
    
    /**
     * Get loans by partner ID
     */
    List<Loan> findByPartnerId(int partnerId);
    
    /**
     * Get loans by book ID
     */
    List<Loan> findByBookId(int bookId);
    
    /**
     * Get loans with partner and book details
     */
    List<Loan> findAllWithDetails();
    
    /**
     * Get overdue loans with partner and book details
     */
    List<Loan> findOverdueWithDetails();
    
    /**
     * Update an existing loan
     */
    Loan update(Loan loan);
    
    /**
     * Delete a loan by ID
     */
    boolean delete(int id);
}
