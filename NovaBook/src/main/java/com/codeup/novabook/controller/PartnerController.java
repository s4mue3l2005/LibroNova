package com.codeup.novabook.controller;

import com.codeup.novabook.model.Partner;
import com.codeup.novabook.service.PartnerService;
import com.codeup.novabook.service.impl.PartnerServiceImpl;
import com.codeup.novabook.util.TableFormatter;

import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for Partner operations with JOptionPane interface
 */
public class PartnerController {
    
    private static final Logger logger = Logger.getLogger(PartnerController.class.getName());
    private final PartnerService partnerService;
    
    public PartnerController() {
        this.partnerService = new PartnerServiceImpl();
    }
    
    /**
     * Show partner management menu
     */
    public void showPartnerMenu() {
        String[] options = {
            "1. List all partners",
            "2. List active partners",
            "3. Find partner by document",
            "4. Add new partner",
            "5. Update partner",
            "6. Delete partner",
            "7. Back to main menu"
        };
        
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Partner Management\n\nSelect an option:",
                "LibroNova - Partner Management",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice == null || choice.contains("7")) {
                break;
            }
            
            try {
                if (choice.contains("1")) {
                    listAllPartners();
                } else if (choice.contains("2")) {
                    listActivePartners();
                } else if (choice.contains("3")) {
                    findPartnerByDocument();
                } else if (choice.contains("4")) {
                    addNewPartner();
                } else if (choice.contains("5")) {
                    updatePartner();
                } else if (choice.contains("6")) {
                    deletePartner();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Error in partner management: " + e.getMessage());
            }
        }
    }
    
    /**
     * List all partners
     */
    private void listAllPartners() throws Exception {
        List<Partner> partners = partnerService.getAllPartners();
        String table = TableFormatter.formatPartnersTable(partners);
        
        JOptionPane.showMessageDialog(null, 
            "All Partners:\n\n" + table, 
            "All Partners", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * List active partners
     */
    private void listActivePartners() throws Exception {
        List<Partner> partners = partnerService.getActivePartners();
        String table = TableFormatter.formatPartnersTable(partners);
        
        JOptionPane.showMessageDialog(null, 
            "Active Partners:\n\n" + table, 
            "Active Partners", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Find partner by document number
     */
    private void findPartnerByDocument() throws Exception {
        String documentNumber = JOptionPane.showInputDialog(null, 
            "Enter document number:", 
            "Find Partner", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (documentNumber != null && !documentNumber.trim().isEmpty()) {
            try {
                Partner partner = partnerService.getPartnerByDocumentNumber(documentNumber);
                
                JOptionPane.showMessageDialog(null, 
                    "Partner Found:\n\n" +
                    "ID: " + partner.getId() + "\n" +
                    "Document: " + partner.getDocumentNumber() + "\n" +
                    "Name: " + partner.getFullName() + "\n" +
                    "Email: " + partner.getEmail() + "\n" +
                    "Phone: " + (partner.getPhone() != null ? partner.getPhone() : "N/A") + "\n" +
                    "Address: " + (partner.getAddress() != null ? partner.getAddress() : "N/A") + "\n" +
                    "Status: " + partner.getStatus(), 
                    "Partner Details", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Partner not found with document number: " + documentNumber, 
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    /**
     * Add new partner
     */
    private void addNewPartner() throws Exception {
        String documentNumber = JOptionPane.showInputDialog(null, "Enter document number:", "Add New Partner", JOptionPane.QUESTION_MESSAGE);
        if (documentNumber == null || documentNumber.trim().isEmpty()) return;
        
        String firstName = JOptionPane.showInputDialog(null, "Enter first name:", "Add New Partner", JOptionPane.QUESTION_MESSAGE);
        if (firstName == null || firstName.trim().isEmpty()) return;
        
        String lastName = JOptionPane.showInputDialog(null, "Enter last name:", "Add New Partner", JOptionPane.QUESTION_MESSAGE);
        if (lastName == null || lastName.trim().isEmpty()) return;
        
        String email = JOptionPane.showInputDialog(null, "Enter email:", "Add New Partner", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;
        
        String phone = JOptionPane.showInputDialog(null, "Enter phone (optional):", "Add New Partner", JOptionPane.QUESTION_MESSAGE);
        String address = JOptionPane.showInputDialog(null, "Enter address (optional):", "Add New Partner", JOptionPane.QUESTION_MESSAGE);
        
        Partner partner = new Partner(documentNumber, firstName, lastName, email);
        partner.setPhone(phone);
        partner.setAddress(address);
        
        Partner createdPartner = partnerService.createPartner(partner);
        
        JOptionPane.showMessageDialog(null, 
            "Partner created successfully!\n\n" +
            "ID: " + createdPartner.getId() + "\n" +
            "Document: " + createdPartner.getDocumentNumber() + "\n" +
            "Name: " + createdPartner.getFullName() + "\n" +
            "Email: " + createdPartner.getEmail() + "\n" +
            "Status: " + createdPartner.getStatus(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Update partner
     */
    private void updatePartner() throws Exception {
        String idStr = JOptionPane.showInputDialog(null, "Enter partner ID to update:", "Update Partner", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid partner ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Partner partner = partnerService.getPartnerById(id);
        
        String documentNumber = (String) JOptionPane.showInputDialog(null, "Enter document number:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, null, partner.getDocumentNumber());
        if (documentNumber == null || documentNumber.trim().isEmpty()) return;
        
        String firstName = (String) JOptionPane.showInputDialog(null, "Enter first name:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, null, partner.getFirstName());
        if (firstName == null || firstName.trim().isEmpty()) return;
        
        String lastName = (String) JOptionPane.showInputDialog(null, "Enter last name:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, null, partner.getLastName());
        if (lastName == null || lastName.trim().isEmpty()) return;
        
        String email = (String) JOptionPane.showInputDialog(null, "Enter email:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, null, partner.getEmail());
        if (email == null || email.trim().isEmpty()) return;
        
        String phone = (String) JOptionPane.showInputDialog(null, "Enter phone:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, null, partner.getPhone() != null ? partner.getPhone() : "");
        String address = (String) JOptionPane.showInputDialog(null, "Enter address:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, null, partner.getAddress() != null ? partner.getAddress() : "");
        
        String[] statusOptions = {"ACTIVO", "INACTIVO"};
        String statusChoice = (String) JOptionPane.showInputDialog(null, "Select status:", "Update Partner", JOptionPane.QUESTION_MESSAGE, null, statusOptions, partner.getStatus().name());
        if (statusChoice == null) return;
        
        partner.setDocumentNumber(documentNumber);
        partner.setFirstName(firstName);
        partner.setLastName(lastName);
        partner.setEmail(email);
        partner.setPhone(phone);
        partner.setAddress(address);
        partner.setStatus(Partner.PartnerStatus.valueOf(statusChoice));
        
        Partner updatedPartner = partnerService.updatePartner(partner);
        
        JOptionPane.showMessageDialog(null, 
            """
            Partner updated successfully!
            
            ID: """ + updatedPartner.getId() + "\n" +
            "Document: " + updatedPartner.getDocumentNumber() + "\n" +
            "Name: " + updatedPartner.getFullName() + "\n" +
            "Email: " + updatedPartner.getEmail() + "\n" +
            "Status: " + updatedPartner.getStatus(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Delete partner
     */
    private void deletePartner() throws Exception {
        String idStr = JOptionPane.showInputDialog(null, "Enter partner ID to delete:", "Delete Partner", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid partner ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Partner partner = partnerService.getPartnerById(id);
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            """
            Are you sure you want to delete this partner?
            
            Document: """ + partner.getDocumentNumber() + "\n" +
            "Name: " + partner.getFullName() + "\n" +
            "Email: " + partner.getEmail(), 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = partnerService.deletePartner(id);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Partner deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete partner.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
