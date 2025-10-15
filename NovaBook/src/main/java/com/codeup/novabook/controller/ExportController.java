package com.codeup.novabook.controller;

import com.codeup.novabook.service.BookService;
import com.codeup.novabook.service.LoanService;
import com.codeup.novabook.service.impl.BookServiceImpl;
import com.codeup.novabook.service.impl.LoanServiceImpl;
import com.codeup.novabook.util.CSVExporter;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for export operations with JOptionPane interface
 */
public class ExportController {
    
    private static final Logger logger = Logger.getLogger(ExportController.class.getName());
    private final BookService bookService;
    private final LoanService loanService;
    
    public ExportController() {
        this.bookService = new BookServiceImpl();
        this.loanService = new LoanServiceImpl();
    }
    
    /**
     * Show export menu
     */
    public void showExportMenu() {
        String[] options = {
            "1. Export all books to CSV",
            "2. Export overdue loans to CSV",
            "3. Export all loans to CSV",
            "4. Back to main menu"
        };
        
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Export Data\n\nSelect an option:",
                "LibroNova - Export Data",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice == null || choice.contains("4")) {
                break;
            }
            
            try {
                if (choice.contains("1")) {
                    exportAllBooks();
                } else if (choice.contains("2")) {
                    exportOverdueLoans();
                } else if (choice.contains("3")) {
                    exportAllLoans();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error during export: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Error during export: " + e.getMessage());
            }
        }
    }
    
    /**
     * Export all books to CSV
     */
    private void exportAllBooks() throws Exception {
        try {
            List<com.codeup.novabook.model.Book> books = bookService.getAllBooks();
            
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No books to export.", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String filename = CSVExporter.generateTimestampedFilename("libros_export", "csv");
            CSVExporter.exportBooksToCSV(books, filename);
            
            JOptionPane.showMessageDialog(null, 
                "Books exported successfully!\n\n" +
                "File: " + filename + "\n" +
                "Records: " + books.size(), 
                "Export Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            logger.info("Books exported to: " + filename + " (" + books.size() + " records)");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error writing CSV file: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
    
    /**
     * Export overdue loans to CSV
     */
    private void exportOverdueLoans() throws Exception {
        try {
            List<com.codeup.novabook.model.Loan> overdueLoans = loanService.getOverdueLoansWithDetails();
            
            if (overdueLoans.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No overdue loans to export.", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String filename = CSVExporter.generateTimestampedFilename("prestamos_vencidos", "csv");
            CSVExporter.exportOverdueLoansToCSV(overdueLoans, filename);
            
            JOptionPane.showMessageDialog(null, 
                "Overdue loans exported successfully!\n\n" +
                "File: " + filename + "\n" +
                "Records: " + overdueLoans.size(), 
                "Export Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            logger.info("Overdue loans exported to: " + filename + " (" + overdueLoans.size() + " records)");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error writing CSV file: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
    
    /**
     * Export all loans to CSV
     */
    private void exportAllLoans() throws Exception {
        try {
            List<com.codeup.novabook.model.Loan> loans = loanService.getAllLoansWithDetails();
            
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No loans to export.", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String filename = CSVExporter.generateTimestampedFilename("prestamos_export", "csv");
            CSVExporter.exportLoansToCSV(loans, filename);
            
            JOptionPane.showMessageDialog(null, 
                "All loans exported successfully!\n\n" +
                "File: " + filename + "\n" +
                "Records: " + loans.size(), 
                "Export Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            logger.info("All loans exported to: " + filename + " (" + loans.size() + " records)");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error writing CSV file: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
}
