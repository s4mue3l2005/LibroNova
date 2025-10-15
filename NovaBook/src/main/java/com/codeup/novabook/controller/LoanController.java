package com.codeup.novabook.controller;

import com.codeup.novabook.exception.InsufficientStockException;
import com.codeup.novabook.exception.InvalidLoanException;
import com.codeup.novabook.model.Loan;
import com.codeup.novabook.model.Partner;
import com.codeup.novabook.service.LoanService;
import com.codeup.novabook.service.PartnerService;
import com.codeup.novabook.service.impl.LoanServiceImpl;
import com.codeup.novabook.service.impl.PartnerServiceImpl;
import com.codeup.novabook.util.TableFormatter;

import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for Loan operations with JOptionPane interface
 */
public class LoanController {
    
    private static final Logger logger = Logger.getLogger(LoanController.class.getName());
    private final LoanService loanService;
    private final PartnerService partnerService;
    
    public LoanController() {
        this.loanService = new LoanServiceImpl();
        this.partnerService = new PartnerServiceImpl();
    }
    
    /**
     * Show loan management menu
     */
    public void showLoanMenu() {
        String[] options = {
            "1. List all loans",
            "2. List active loans",
            "3. List overdue loans",
            "4. Find loans by partner",
            "5. Create new loan",
            "6. Return book",
            "7. Back to main menu"
        };
        
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Loan Management\n\nSelect an option:",
                "LibroNova - Loan Management",
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
                    listAllLoans();
                } else if (choice.contains("2")) {
                    listActiveLoans();
                } else if (choice.contains("3")) {
                    listOverdueLoans();
                } else if (choice.contains("4")) {
                    findLoansByPartner();
                } else if (choice.contains("5")) {
                    createNewLoan();
                } else if (choice.contains("6")) {
                    returnBook();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Error in loan management: " + e.getMessage());
            }
        }
    }
    
    /**
     * List all loans
     */
    private void listAllLoans() throws Exception {
        List<Loan> loans = loanService.getAllLoansWithDetails();
        String table = TableFormatter.formatLoansTable(loans);
        
        JOptionPane.showMessageDialog(null, 
            "All Loans:\n\n" + table, 
            "All Loans", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * List active loans
     */
    private void listActiveLoans() throws Exception {
        List<Loan> loans = loanService.getActiveLoans();
        String table = TableFormatter.formatLoansTable(loans);
        
        JOptionPane.showMessageDialog(null, 
            "Active Loans:\n\n" + table, 
            "Active Loans", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * List overdue loans
     */
    private void listOverdueLoans() throws Exception {
        List<Loan> loans = loanService.getOverdueLoansWithDetails();
        String table = TableFormatter.formatLoansTable(loans);
        
        JOptionPane.showMessageDialog(null, 
            "Overdue Loans:\n\n" + table, 
            "Overdue Loans", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Find loans by partner
     */
    private void findLoansByPartner() throws Exception {
        String documentNumber = JOptionPane.showInputDialog(null, 
            "Enter partner document number:", 
            "Find Loans by Partner", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (documentNumber != null && !documentNumber.trim().isEmpty()) {
            try {
                Partner partner = partnerService.getPartnerByDocumentNumber(documentNumber);
                List<Loan> loans = loanService.getLoansByPartnerId(partner.getId());
                String table = TableFormatter.formatLoansTable(loans);
                
                JOptionPane.showMessageDialog(null, 
                    "Loans for partner " + partner.getFullName() + ":\n\n" + table, 
                    "Partner Loans", 
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
     * Create new loan
     */
    private void createNewLoan() throws Exception {
        // Get partner
        String documentNumber = JOptionPane.showInputDialog(null, 
            "Enter partner document number:", 
            "Create New Loan", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (documentNumber == null || documentNumber.trim().isEmpty()) return;
        
        Partner partner;
        try {
            partner = partnerService.getPartnerByDocumentNumber(documentNumber);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Partner not found with document number: " + documentNumber, 
                "Not Found", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if partner can borrow
        if (!loanService.canPartnerBorrow(partner.getId())) {
            JOptionPane.showMessageDialog(null, 
                "Partner cannot borrow books. Check for overdue books.", 
                "Cannot Borrow", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get book ISBN
        String isbn = JOptionPane.showInputDialog(null, 
            "Enter book ISBN:", 
            "Create New Loan", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (isbn == null || isbn.trim().isEmpty()) return;
        
        // For simplicity, we'll use a mock book ID
        // In a real application, you'd look up the book by ISBN
        String bookIdStr = JOptionPane.showInputDialog(null, 
            "Enter book ID:", 
            "Create New Loan", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) return;
        
        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid book ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if book is available
        if (!loanService.isBookAvailable(bookId)) {
            JOptionPane.showMessageDialog(null, 
                "Book is not available for loan.", 
                "Not Available", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Loan loan = loanService.createLoan(partner.getId(), bookId);
            
            JOptionPane.showMessageDialog(null, 
                "Loan created successfully!\n\n" +
                "Loan ID: " + loan.getId() + "\n" +
                "Partner: " + partner.getFullName() + "\n" +
                "Book ISBN: " + isbn + "\n" +
                "Loan Date: " + loan.getLoanDate() + "\n" +
                "Due Date: " + loan.getDueDate(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (InsufficientStockException e) {
            JOptionPane.showMessageDialog(null, 
                "Insufficient stock: " + e.getMessage(), 
                "Insufficient Stock", 
                JOptionPane.WARNING_MESSAGE);
        } catch (InvalidLoanException e) {
            JOptionPane.showMessageDialog(null, 
                "Invalid loan: " + e.getMessage(), 
                "Invalid Loan", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Return book
     */
    private void returnBook() throws Exception {
        String loanIdStr = JOptionPane.showInputDialog(null, 
            "Enter loan ID to return:", 
            "Return Book", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (loanIdStr == null || loanIdStr.trim().isEmpty()) return;
        
        int loanId;
        try {
            loanId = Integer.parseInt(loanIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid loan ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Loan loan = loanService.getLoanById(loanId);
            
            if (loan.getStatus() != Loan.LoanStatus.ACTIVO) {
                JOptionPane.showMessageDialog(null, 
                    "This loan is not active.", 
                    "Not Active", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Calculate fine if overdue
            double fine = loanService.calculateFine(loan);
            String fineMessage = "";
            if (fine > 0) {
                fineMessage = "\n\nFine for overdue return: $" + fine;
            }
            
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Confirm return of this loan?\n\n" +
                "Loan ID: " + loan.getId() + "\n" +
                "Due Date: " + loan.getDueDate() + fineMessage, 
                "Confirm Return", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Loan returnedLoan = loanService.returnBook(loanId);
                
                String returnMessage = "Book returned successfully!\n\n" +
                    "Loan ID: " + returnedLoan.getId() + "\n" +
                    "Return Date: " + returnedLoan.getReturnDate();
                
                if (returnedLoan.getFineAmount().doubleValue() > 0) {
                    returnMessage += "\nFine Amount: $" + returnedLoan.getFineAmount();
                }
                
                JOptionPane.showMessageDialog(null, 
                    returnMessage, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InvalidLoanException e) {
            JOptionPane.showMessageDialog(null, 
                "Invalid loan: " + e.getMessage(), 
                "Invalid Loan", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}
