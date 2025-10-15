package com.codeup.novabook;

import com.codeup.novabook.controller.BookController;
import com.codeup.novabook.controller.ExportController;
import com.codeup.novabook.controller.LoanController;
import com.codeup.novabook.controller.PartnerController;
import com.codeup.novabook.controller.UserController;
import com.codeup.novabook.model.User;
import com.codeup.novabook.util.DatabaseConnection;
import com.codeup.novabook.util.LoggerUtil;
import java.awt.HeadlessException;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Main application class for LibroNova Library Management System
 */
public class NovaBook {
    
    private static final Logger logger = Logger.getLogger(NovaBook.class.getName());
    private final UserController userController;
    private final BookController bookController;
    private final PartnerController partnerController;
    private final LoanController loanController;
    private final ExportController exportController;
    
    public NovaBook() {
        this.userController = new UserController();
        this.bookController = new BookController();
        this.partnerController = new PartnerController();
        this.loanController = new LoanController();
        this.exportController = new ExportController();
    }
    
    /**
     * Main method to start the application
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Setup logging
            LoggerUtil.setupLogging();
            logger.info("Starting LibroNova Library Management System");
            
            // Test database connection
            if (!DatabaseConnection.testConnection()) {
                JOptionPane.showMessageDialog(null, 
                    "Cannot connect to database. Please check your configuration.", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Database connection failed");
                return;
            }
            
            // Start application
            NovaBook app = new NovaBook();
            app.run();
            
        } catch (HeadlessException e) {
            logger.severe(() -> "Fatal error starting application: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Fatal error: " + e.getMessage(), 
                "Application Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Main application loop
     */
    public void run() {
        User currentUser = null;
        
        // Login loop
        while (currentUser == null) {
            currentUser = userController.showLoginDialog();
            if (currentUser == null) {
                int choice = JOptionPane.showConfirmDialog(null, 
                    "Login cancelled. Do you want to exit the application?", 
                    "Exit Application", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    logger.info("Application terminated by user");
                    return;
                }
            }
        }
        
        // Main menu loop
        while (currentUser != null) {
            try {
                currentUser = showMainMenu(currentUser);
            } catch (Exception e) {
                logger.severe(() -> "Error in main menu: " + e.getMessage());
                JOptionPane.showMessageDialog(null, 
                    "Error: " + e.getMessage(), 
                    "Application Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        logger.info("Application closed");
    }
    
    /**
     * Show main menu and handle user selection
     */
    private User showMainMenu(User currentUser) {
        String[] options = {
            "1. Book Management",
            "2. Partner Management", 
            "3. Loan Management",
            "4. Export Data",
            "5. User Management",
            "6. Logout",
            "7. Exit Application"
        };
        
        // Filter options based on user role
        if (currentUser.getRole() != User.UserRole.ADMIN) {
            options = new String[]{
                "1. Book Management",
                "2. Partner Management", 
                "3. Loan Management",
                "4. Export Data",
                "5. Logout",
                "6. Exit Application"
            };
        }
        
        String choice = (String) JOptionPane.showInputDialog(
            null,
            """
            LibroNova Library Management System
            
            Welcome, """ + currentUser.getUsername() + " (" + currentUser.getRole() + ")\n\n" +
            "Select an option:",
            "LibroNova - Main Menu",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice == null) {
            return currentUser; // User cancelled, stay in menu
        }
        
        try {
            if (choice.contains("1")) {
                bookController.showBookMenu();
            } else if (choice.contains("2")) {
                partnerController.showPartnerMenu();
            } else if (choice.contains("3")) {
                loanController.showLoanMenu();
            } else if (choice.contains("4")) {
                exportController.showExportMenu();
            } else if (choice.contains("5")) {
                if (currentUser.getRole() == User.UserRole.ADMIN) {
                    userController.showUserMenu();
                } else {
                    // Regular user logout
                    userController.logout();
                    return null;
                }
            } else if (choice.contains("6")) {
                if (currentUser.getRole() == User.UserRole.ADMIN) {
                    userController.logout();
                    return null;
                } else {
                    // Regular user exit
                    return exitApplication();
                }
            } else if (choice.contains("7")) {
                return exitApplication();
            }
        } catch (Exception e) {
            logger.severe(() -> "Error in menu selection: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return currentUser;
    }
    
    /**
     * Handle application exit
     */
    private User exitApplication() {
        int choice = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to exit the application?", 
            "Exit Application", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            logger.info("Application exit confirmed by user");
            return null;
        }
        
        return userController.getCurrentUser();
    }
}