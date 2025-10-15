package com.codeup.novabook.dao;

import com.codeup.novabook.model.Partner;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Partner entity
 */
public interface PartnerDAO {
    
    /**
     * Create a new partner
     */
    Partner create(Partner partner);
    
    /**
     * Find partner by ID
     */
    Optional<Partner> findById(int id);
    
    /**
     * Find partner by document number
     */
    Optional<Partner> findByDocumentNumber(String documentNumber);
    
    /**
     * Find partner by email
     */
    Optional<Partner> findByEmail(String email);
    
    /**
     * Get all partners
     */
    List<Partner> findAll();
    
    /**
     * Get active partners only
     */
    List<Partner> findActive();
    
    /**
     * Update an existing partner
     */
    Partner update(Partner partner);
    
    /**
     * Delete a partner by ID
     */
    boolean delete(int id);
    
    /**
     * Check if document number exists
     */
    boolean existsByDocumentNumber(String documentNumber);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
