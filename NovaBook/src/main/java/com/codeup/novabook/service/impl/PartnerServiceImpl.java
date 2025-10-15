package com.codeup.novabook.service.impl;

import com.codeup.novabook.dao.PartnerDAO;
import com.codeup.novabook.dao.impl.PartnerDAOImpl;
import com.codeup.novabook.model.Partner;
import com.codeup.novabook.service.PartnerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service implementation for Partner operations
 */
public class PartnerServiceImpl implements PartnerService {
    
    private static final Logger logger = Logger.getLogger(PartnerServiceImpl.class.getName());
    private final PartnerDAO partnerDAO;
    
    public PartnerServiceImpl() {
        this.partnerDAO = new PartnerDAOImpl();
    }
    
    @Override
    public Partner createPartner(Partner partner) throws Exception {

        
        // Validate input parameters
        if (partner == null) {
            throw new IllegalArgumentException("Partner is required");
        }

        logger.info("POST /partners - Creating new partner: " + partner.getFullName());
        
        if (partner.getDocumentNumber() == null || partner.getDocumentNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Document number is required");
        }
        
        if (partner.getFirstName() == null || partner.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (partner.getLastName() == null || partner.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        if (partner.getEmail() == null || partner.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Check if document number already exists
        if (partnerDAO.existsByDocumentNumber(partner.getDocumentNumber())) {
            throw new IllegalArgumentException("Document number already exists: " + partner.getDocumentNumber());
        }
        
        // Check if email already exists
        if (partnerDAO.existsByEmail(partner.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + partner.getEmail());
        }
        
        // Set default values (decorator pattern)
        partner.setStatus(Partner.PartnerStatus.ACTIVO);
        partner.setCreatedAt(LocalDateTime.now());
        partner.setUpdatedAt(LocalDateTime.now());
        
        Partner createdPartner = partnerDAO.create(partner);
        logger.info("Partner created successfully: " + createdPartner.getFullName());
        
        return createdPartner;
    }
    
    @Override
    public Partner getPartnerById(int id) throws Exception {
        logger.info("GET /partners/" + id + " - Retrieving partner by ID");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid partner ID is required");
        }
        
        Optional<Partner> partnerOptional = partnerDAO.findById(id);
        if (partnerOptional.isEmpty()) {
            throw new IllegalArgumentException("Partner not found with ID: " + id);
        }
        
        return partnerOptional.get();
    }
    
    @Override
    public Partner getPartnerByDocumentNumber(String documentNumber) throws Exception {
        logger.info("GET /partners/document/" + documentNumber + " - Retrieving partner by document number");
        
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Document number is required");
        }
        
        Optional<Partner> partnerOptional = partnerDAO.findByDocumentNumber(documentNumber);
        if (partnerOptional.isEmpty()) {
            throw new IllegalArgumentException("Partner not found with document number: " + documentNumber);
        }
        
        return partnerOptional.get();
    }
    
    @Override
    public List<Partner> getAllPartners() throws Exception {
        logger.info("GET /partners - Retrieving all partners");
        
        return partnerDAO.findAll();
    }
    
    @Override
    public List<Partner> getActivePartners() throws Exception {
        logger.info("GET /partners/active - Retrieving active partners");
        
        return partnerDAO.findActive();
    }
    
    @Override
    public Partner updatePartner(Partner partner) throws Exception {
        
        
        if (partner == null) {
            throw new IllegalArgumentException("Partner is required");
        }

        logger.info("PATCH /partners/" + partner.getId() + " - Updating partner");
        
        if (partner.getId() <= 0) {
            throw new IllegalArgumentException("Valid partner ID is required");
        }
        
        // Check if partner exists
        Optional<Partner> existingPartnerOptional = partnerDAO.findById(partner.getId());
        if (existingPartnerOptional.isEmpty()) {
            throw new IllegalArgumentException("Partner not found with ID: " + partner.getId());
        }
        
        Partner existingPartner = existingPartnerOptional.get();
        
        // Check document number uniqueness if changed
        if (!existingPartner.getDocumentNumber().equals(partner.getDocumentNumber()) && 
            partnerDAO.existsByDocumentNumber(partner.getDocumentNumber())) {
            throw new IllegalArgumentException("Document number already exists: " + partner.getDocumentNumber());
        }
        
        // Check email uniqueness if changed
        if (!existingPartner.getEmail().equals(partner.getEmail()) && 
            partnerDAO.existsByEmail(partner.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + partner.getEmail());
        }
        
        partner.setUpdatedAt(LocalDateTime.now());
        Partner updatedPartner = partnerDAO.update(partner);
        logger.info("Partner updated successfully: " + updatedPartner.getFullName());
        
        return updatedPartner;
    }
    
    @Override
    public boolean deletePartner(int id) throws Exception {
        logger.info("DELETE /partners/" + id + " - Deleting partner");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid partner ID is required");
        }
        
        // Check if partner exists
        Optional<Partner> partnerOptional = partnerDAO.findById(id);
        if (partnerOptional.isEmpty()) {
            throw new IllegalArgumentException("Partner not found with ID: " + id);
        }
        
        // Check if partner has active loans
        Partner partner = partnerOptional.get();
        if (partner.getStatus() == Partner.PartnerStatus.ACTIVO) {
            // Additional validation could be added here to check for active loans
            // For now, we'll allow deletion but set status to INACTIVO instead
            partner.setStatus(Partner.PartnerStatus.INACTIVO);
            partnerDAO.update(partner);
            logger.info("Partner status set to INACTIVO: " + partner.getFullName());
            return true;
        }
        
        boolean deleted = partnerDAO.delete(id);
        if (deleted) {
            logger.info("Partner deleted successfully: ID " + id);
        } else {
            logger.warning("Failed to delete partner: ID " + id);
        }
        
        return deleted;
    }
    
    @Override
    public boolean isDocumentNumberAvailable(String documentNumber) throws Exception {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return false;
        }
        
        return !partnerDAO.existsByDocumentNumber(documentNumber);
    }
    
    @Override
    public boolean isDocumentNumberAvailableForUpdate(String documentNumber, int partnerId) throws Exception {
        if (documentNumber == null || documentNumber.trim().isEmpty() || partnerId <= 0) {
            return false;
        }
        Optional<Partner> existingPartner = partnerDAO.findById(partnerId);
        if (!existingPartner.isPresent()) {
            return false; // Si el partner no existe, no debe estar disponible
        }
        if (existingPartner.get().getDocumentNumber().equals(documentNumber)) {
            return true; // Mismo documento, no hay conflicto
        }
        return !partnerDAO.existsByDocumentNumber(documentNumber);
    }
    
    @Override
    public boolean isEmailAvailable(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return !partnerDAO.existsByEmail(email);
    }
    
    @Override
    public boolean isEmailAvailableForUpdate(String email, int partnerId) throws Exception {
        if (email == null || email.trim().isEmpty() || partnerId <= 0) {
            return false;
        }
        Optional<Partner> existingPartner = partnerDAO.findById(partnerId);
        if (!existingPartner.isPresent()) {
            return false; // Si el partner no existe, no debe estar disponible
        }
        if (existingPartner.get().getEmail().equals(email)) {
            return true; // Mismo email, no hay conflicto
        }
        return !partnerDAO.existsByEmail(email);
    }
}
