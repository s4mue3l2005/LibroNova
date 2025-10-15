package com.codeup.novabook.service.impl;

import com.codeup.novabook.dao.BookDAO;
import com.codeup.novabook.dao.LoanDAO;
import com.codeup.novabook.dao.PartnerDAO;
import com.codeup.novabook.dao.impl.BookDAOImpl;
import com.codeup.novabook.dao.impl.LoanDAOImpl;
import com.codeup.novabook.dao.impl.PartnerDAOImpl;
import com.codeup.novabook.exception.InsufficientStockException;
import com.codeup.novabook.exception.InvalidLoanException;
import com.codeup.novabook.model.Book;
import com.codeup.novabook.model.Loan;
import com.codeup.novabook.model.Partner;
import com.codeup.novabook.service.LoanService;
import com.codeup.novabook.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service implementation for Loan operations with transaction support
 */
public class LoanServiceImpl implements LoanService {
    
    private static final Logger logger = Logger.getLogger(LoanServiceImpl.class.getName());
    private final LoanDAO loanDAO;
    private final BookDAO bookDAO;
    private final PartnerDAO partnerDAO;
    
    public LoanServiceImpl() {
        this.loanDAO = new LoanDAOImpl();
        this.bookDAO = new BookDAOImpl();
        this.partnerDAO = new PartnerDAOImpl();
    }
    
    @Override
    public Loan createLoan(int partnerId, int bookId) throws Exception {
        logger.info("POST /loans - Creating new loan for partner: " + partnerId + ", book: " + bookId);
        
        // Validate input parameters
        if (partnerId <= 0) {
            throw new IllegalArgumentException("Valid partner ID is required");
        }
        
        if (bookId <= 0) {
            throw new IllegalArgumentException("Valid book ID is required");
        }
        
        // Validate partner and book exist
        Optional<Partner> partnerOptional = partnerDAO.findById(partnerId);
        if (partnerOptional.isEmpty()) {
            throw new IllegalArgumentException("Partner not found with ID: " + partnerId);
        }
        
        Optional<Book> bookOptional = bookDAO.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }
        
        Partner partner = partnerOptional.get();
        Book book = bookOptional.get();
        
        // Validate business rules
        if (partner.getStatus() != Partner.PartnerStatus.ACTIVO) {
            throw new InvalidLoanException("Partner is not active");
        }
        
        if (!book.isActive()) {
            throw new InvalidLoanException("Book is not active");
        }
        
        if (book.getAvailableCopies() <= 0) {
            throw new InsufficientStockException(book.getIsbn(), 1, book.getAvailableCopies());
        }
        
        // Check if partner can borrow (no overdue books)
        if (!canPartnerBorrow(partnerId)) {
            throw new InvalidLoanException("Partner has overdue books and cannot borrow more");
        }
        
        // Create loan with transaction
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction
            
            // Calculate loan dates
            LocalDate loanDate = LocalDate.now();
            int loanDays = Integer.parseInt(DatabaseConnection.getProperty("diasPrestamo", "7"));
            LocalDate dueDate = loanDate.plusDays(loanDays);
            
            // Create loan
            Loan loan = new Loan(partnerId, bookId, loanDate, dueDate);
            loan.setCreatedAt(LocalDateTime.now());
            loan.setUpdatedAt(LocalDateTime.now());
            
            Loan createdLoan = loanDAO.create(loan);
            
            // Update book available copies
            int newAvailableCopies = book.getAvailableCopies() - 1;
            bookDAO.updateAvailableCopies(bookId, newAvailableCopies);
            
            // Commit transaction
            connection.commit();
            logger.info("Loan created successfully: ID " + createdLoan.getId() + 
                       ", Book: " + book.getTitle() + ", Partner: " + partner.getFullName());
            
            return createdLoan;
            
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    logger.severe("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    logger.severe("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Error creating loan", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    logger.warning("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    @Override
    public Loan returnBook(int loanId) throws Exception {
        logger.info("POST /loans/" + loanId + "/return - Returning book");
        
        if (loanId <= 0) {
            throw new IllegalArgumentException("Valid loan ID is required");
        }
        
        // Get loan
        Optional<Loan> loanOptional = loanDAO.findById(loanId);
        if (loanOptional.isEmpty()) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }
        
        Loan loan = loanOptional.get();
        
        if (loan.getStatus() != Loan.LoanStatus.ACTIVO) {
            throw new InvalidLoanException("Loan is not active");
        }
        
        // Get book
        Optional<Book> bookOptional = bookDAO.findById(loan.getBookId());
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        
        Book book = bookOptional.get();
        
        // Process return with transaction
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction
            
            // Calculate fine if overdue
            LocalDate returnDate = LocalDate.now();
            double fineAmount = 0.0;
            
            if (returnDate.isAfter(loan.getDueDate())) {
                fineAmount = calculateFine(loan);
            }
            
            // Update loan
            loan.setReturnDate(returnDate);
            loan.setStatus(Loan.LoanStatus.DEVUELTO);
            loan.setFineAmount(BigDecimal.valueOf(fineAmount));
            loan.setUpdatedAt(LocalDateTime.now());
            
            loanDAO.update(loan);
            
            // Restore book available copies
            int newAvailableCopies = book.getAvailableCopies() + 1;
            bookDAO.updateAvailableCopies(book.getId(), newAvailableCopies);
            
            // Commit transaction
            connection.commit();
            logger.info("Book returned successfully: Loan ID " + loanId + 
                       ", Fine: $" + fineAmount);
            
            return loan;
            
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    logger.severe("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    logger.severe("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Error returning book", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    logger.warning("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    @Override
    public double calculateFine(Loan loan) throws Exception {
        if (loan.getStatus() != Loan.LoanStatus.ACTIVO) {
            return 0.0;
        }
        
        LocalDate currentDate = LocalDate.now();
        if (!currentDate.isAfter(loan.getDueDate())) {
            return 0.0;
        }
        
        long daysOverdue = currentDate.toEpochDay() - loan.getDueDate().toEpochDay();
        double finePerDay = Double.parseDouble(DatabaseConnection.getProperty("multaPorDia", "1500"));
        
        return daysOverdue * finePerDay;
    }
    
    @Override
    public Loan getLoanById(int id) throws Exception {
        logger.info("GET /loans/" + id + " - Retrieving loan by ID");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid loan ID is required");
        }
        
        Optional<Loan> loanOptional = loanDAO.findById(id);
        if (loanOptional.isEmpty()) {
            throw new IllegalArgumentException("Loan not found with ID: " + id);
        }
        
        return loanOptional.get();
    }
    
    @Override
    public List<Loan> getAllLoans() throws Exception {
        logger.info("GET /loans - Retrieving all loans");
        
        return loanDAO.findAll();
    }
    
    @Override
    public List<Loan> getActiveLoans() throws Exception {
        logger.info("GET /loans/active - Retrieving active loans");
        
        return loanDAO.findActive();
    }
    
    @Override
    public List<Loan> getOverdueLoans() throws Exception {
        logger.info("GET /loans/overdue - Retrieving overdue loans");
        
        return loanDAO.findOverdue();
    }
    
    @Override
    public List<Loan> getLoansByPartnerId(int partnerId) throws Exception {
        logger.info("GET /loans/partner/" + partnerId + " - Retrieving loans by partner");
        
        if (partnerId <= 0) {
            throw new IllegalArgumentException("Valid partner ID is required");
        }
        
        return loanDAO.findByPartnerId(partnerId);
    }
    
    @Override
    public List<Loan> getLoansByBookId(int bookId) throws Exception {
        logger.info("GET /loans/book/" + bookId + " - Retrieving loans by book");
        
        if (bookId <= 0) {
            throw new IllegalArgumentException("Valid book ID is required");
        }
        
        return loanDAO.findByBookId(bookId);
    }
    
    @Override
    public List<Loan> getAllLoansWithDetails() throws Exception {
        logger.info("GET /loans/details - Retrieving all loans with details");
        
        return loanDAO.findAllWithDetails();
    }
    
    @Override
    public List<Loan> getOverdueLoansWithDetails() throws Exception {
        logger.info("GET /loans/overdue/details - Retrieving overdue loans with details");
        
        return loanDAO.findOverdueWithDetails();
    }
    
    @Override
    public Loan updateLoan(Loan loan) throws Exception {
        
        if (loan == null) {
            throw new IllegalArgumentException("Loan is required");
        }

        logger.info("PATCH /loans/" + loan.getId() + " - Updating loan");
        
        if (loan.getId() <= 0) {
            throw new IllegalArgumentException("Valid loan ID is required");
        }
        
        // Check if loan exists
        Optional<Loan> existingLoanOptional = loanDAO.findById(loan.getId());
        if (existingLoanOptional.isEmpty()) {
            throw new IllegalArgumentException("Loan not found with ID: " + loan.getId());
        }
        
        loan.setUpdatedAt(LocalDateTime.now());
        Loan updatedLoan = loanDAO.update(loan);
        logger.info("Loan updated successfully: ID " + updatedLoan.getId());
        
        return updatedLoan;
    }
    
    @Override
    public boolean deleteLoan(int id) throws Exception {
        logger.info("DELETE /loans/" + id + " - Deleting loan");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid loan ID is required");
        }
        
        // Check if loan exists
        Optional<Loan> loanOptional = loanDAO.findById(id);
        if (loanOptional.isEmpty()) {
            throw new IllegalArgumentException("Loan not found with ID: " + id);
        }
        
        Loan loan = loanOptional.get();
        
        // Only allow deletion of completed loans
        if (loan.getStatus() == Loan.LoanStatus.ACTIVO) {
            throw new InvalidLoanException("Cannot delete active loan. Please return the book first.");
        }
        
        boolean deleted = loanDAO.delete(id);
        if (deleted) {
            logger.info("Loan deleted successfully: ID " + id);
        } else {
            logger.warning("Failed to delete loan: ID " + id);
        }
        
        return deleted;
    }
    
    @Override
    public boolean canPartnerBorrow(int partnerId) throws Exception {
        if (partnerId <= 0) {
            throw new IllegalArgumentException("Partner ID must be positive");
        }
        List<Loan> activeLoans = loanDAO.findByPartnerId(partnerId);
        
        // Check if partner has any overdue books
        for (Loan loan : activeLoans) {
            if (loan.getStatus() == Loan.LoanStatus.ACTIVO && loan.isOverdue()) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isBookAvailable(int bookId) throws Exception {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        Optional<Book> bookOptional = bookDAO.findById(bookId);
        if (bookOptional.isEmpty()) {
            return false;
        }
        
        Book book = bookOptional.get();
        return book.isActive() && book.getAvailableCopies() > 0;
    }
}
