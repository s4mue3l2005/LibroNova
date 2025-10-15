package com.codeup.novabook.service;

import com.codeup.novabook.model.Partner;
import java.util.List;

/**
 * Service interface for Partner operations
 */
public interface PartnerService {
    
    /**
     * Create a new partner
     */
    Partner createPartner(Partner partner) throws Exception;
    
    /**
     * Get partner by ID
     */
    Partner getPartnerById(int id) throws Exception;
    
    /**
     * Get partner by document number
     */
    Partner getPartnerByDocumentNumber(String documentNumber) throws Exception;
    
    /**
     * Get all partners
     */
    List<Partner> getAllPartners() throws Exception;
    
    /**
     * Get active partners only
     */
    List<Partner> getActivePartners() throws Exception;
    
    /**
     * Update partner information
     */
    Partner updatePartner(Partner partner) throws Exception;
    
    /**
     * Delete partner by ID
     */
    boolean deletePartner(int id) throws Exception;
    
    /**
     * Check if document number is available
     */
    boolean isDocumentNumberAvailable(String documentNumber) throws Exception;
    
    /**
     * Check if document number is available for update (excluding current partner)
     */
    boolean isDocumentNumberAvailableForUpdate(String documentNumber, int partnerId) throws Exception;
    
    /**
     * Check if email is available
     */
    boolean isEmailAvailable(String email) throws Exception;
    
    /**
     * Check if email is available for update (excluding current partner)
     */
    boolean isEmailAvailableForUpdate(String email, int partnerId) throws Exception;
}
