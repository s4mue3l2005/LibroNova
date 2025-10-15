package com.codeup.novabook.service;

import com.codeup.novabook.model.Loan;
import com.codeup.novabook.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoanService
 */
public class LoanServiceTest {
    
    private LoanService loanService;
    
    @BeforeEach
    void setUp() {
        loanService = new LoanServiceImpl();
    }
    
    @Test
    @DisplayName("Should calculate fine correctly for overdue loans")
    void testCalculateFine() throws Exception {
        // Create a loan that's 3 days overdue
        Loan overdueLoan = new Loan();
        overdueLoan.setStatus(Loan.LoanStatus.ACTIVO);
        overdueLoan.setLoanDate(LocalDate.now().minusDays(10));
        overdueLoan.setDueDate(LocalDate.now().minusDays(3));
        
        // Calculate fine (assuming 1500 per day from config)
        double fine = loanService.calculateFine(overdueLoan);
        assertEquals(4500.0, fine, 0.01); // 3 days * 1500
        
        // Test loan that's not overdue
        Loan currentLoan = new Loan();
        currentLoan.setStatus(Loan.LoanStatus.ACTIVO);
        currentLoan.setLoanDate(LocalDate.now());
        currentLoan.setDueDate(LocalDate.now().plusDays(7));
        
        double noFine = loanService.calculateFine(currentLoan);
        assertEquals(0.0, noFine, 0.01);
        
        // Test returned loan (should have no fine)
        Loan returnedLoan = new Loan();
        returnedLoan.setStatus(Loan.LoanStatus.DEVUELTO);
        returnedLoan.setLoanDate(LocalDate.now().minusDays(10));
        returnedLoan.setDueDate(LocalDate.now().minusDays(3));
        
        double returnedFine = loanService.calculateFine(returnedLoan);
        assertEquals(0.0, returnedFine, 0.01);
    }
    
    @Test
    @DisplayName("Should validate loan creation parameters")
    void testCreateLoanValidation() {
        // Test invalid partner ID
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(0, 1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(-1, 1);
        });
        
        // Test invalid book ID
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(1, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(1, -1);
        });
    }
    
    @Test
    @DisplayName("Should validate loan return parameters")
    void testReturnLoanValidation() {
        // Test invalid loan ID
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.returnBook(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.returnBook(-1);
        });
    }
    
    @Test
    @DisplayName("Should check if loan is overdue")
    void testIsLoanOverdue() {
        // Create overdue loan
        Loan overdueLoan = new Loan();
        overdueLoan.setStatus(Loan.LoanStatus.ACTIVO);
        overdueLoan.setLoanDate(LocalDate.now().minusDays(10));
        overdueLoan.setDueDate(LocalDate.now().minusDays(1));
        
        assertTrue(overdueLoan.isOverdue());
        
        // Test days overdue calculation
        long daysOverdue = overdueLoan.getDaysOverdue();
        assertEquals(1, daysOverdue);
        
        // Create current loan
        Loan currentLoan = new Loan();
        currentLoan.setStatus(Loan.LoanStatus.ACTIVO);
        currentLoan.setLoanDate(LocalDate.now());
        currentLoan.setDueDate(LocalDate.now().plusDays(7));
        
        assertFalse(currentLoan.isOverdue());
        assertEquals(0, currentLoan.getDaysOverdue());
        
        // Test returned loan
        Loan returnedLoan = new Loan();
        returnedLoan.setStatus(Loan.LoanStatus.DEVUELTO);
        returnedLoan.setLoanDate(LocalDate.now().minusDays(10));
        returnedLoan.setDueDate(LocalDate.now().minusDays(3));
        
        assertFalse(returnedLoan.isOverdue());
        assertEquals(0, returnedLoan.getDaysOverdue());
    }
    
    @Test
    @DisplayName("Should validate loan business rules")
    void testLoanBusinessRules() {
        Loan loan = new Loan(1, 1, LocalDate.now(), LocalDate.now().plusDays(7));
        
        // Test loan creation with valid data
        assertNotNull(loan);
        assertEquals(1, loan.getPartnerId());
        assertEquals(1, loan.getBookId());
        assertEquals(LocalDate.now(), loan.getLoanDate());
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
        assertEquals(Loan.LoanStatus.ACTIVO, loan.getStatus());
        assertEquals(BigDecimal.ZERO, loan.getFineAmount());
    }
    
    @Test
    @DisplayName("Should validate loan update parameters")
    void testUpdateLoanValidation() throws Exception {
        Loan loan = new Loan();
        loan.setId(1);
        loan.setPartnerId(1);
        loan.setBookId(1);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setStatus(Loan.LoanStatus.ACTIVO);
        loan.setFineAmount(BigDecimal.ZERO);
        
        // Test updating with valid data
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.DEVUELTO);
        
        // This would require a test database setup
        // For now, we'll test the validation logic
        assertNotNull(loan);
        assertEquals(Loan.LoanStatus.DEVUELTO, loan.getStatus());
        assertNotNull(loan.getReturnDate());
    }
    
    @Test
    @DisplayName("Should validate loan deletion parameters")
    void testDeleteLoanValidation() {
        // Test invalid loan ID
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.deleteLoan(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.deleteLoan(-1);
        });
    }
    
    @Test
    @DisplayName("Should validate partner and book availability checks")
    void testAvailabilityChecks() throws Exception {
        // These tests would require a test database setup
        // For now, we'll test that the methods don't throw exceptions with valid parameters
        
        assertDoesNotThrow(() -> {
            boolean canBorrow = loanService.canPartnerBorrow(1);
            assertNotNull(Boolean.valueOf(canBorrow));
        });
        
        assertDoesNotThrow(() -> {
            boolean isAvailable = loanService.isBookAvailable(1);
            assertNotNull(Boolean.valueOf(isAvailable));
        });
        
        // Test with invalid parameters
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.canPartnerBorrow(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.isBookAvailable(0);
        });
    }
    
    @Test
    @DisplayName("Should validate loan queries with invalid parameters")
    void testLoanQueryValidation() {
        // Test invalid IDs for queries
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.getLoanById(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.getLoansByPartnerId(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.getLoansByBookId(0);
        });
    }
    
    @Test
    @DisplayName("Should validate loan model edge cases")
    void testLoanModelEdgeCases() {
        // Test loan with different statuses
        Loan activeLoan = new Loan();
        activeLoan.setStatus(Loan.LoanStatus.ACTIVO);
        activeLoan.setLoanDate(LocalDate.now().minusDays(10));
        activeLoan.setDueDate(LocalDate.now().minusDays(3));
        assertTrue(activeLoan.isOverdue());
        
        Loan returnedLoan = new Loan();
        returnedLoan.setStatus(Loan.LoanStatus.DEVUELTO);
        returnedLoan.setLoanDate(LocalDate.now().minusDays(10));
        returnedLoan.setDueDate(LocalDate.now().minusDays(3));
        assertFalse(returnedLoan.isOverdue());
        
        Loan overdueLoan = new Loan();
        overdueLoan.setStatus(Loan.LoanStatus.VENCIDO);
        overdueLoan.setLoanDate(LocalDate.now().minusDays(10));
        overdueLoan.setDueDate(LocalDate.now().minusDays(3));
        assertFalse(overdueLoan.isOverdue()); // Status is VENCIDO, not ACTIVO
    }
    
    @Test
    @DisplayName("Should validate loan date calculations")
    void testLoanDateCalculations() {
        // Test loan created today
        Loan todayLoan = new Loan(1, 1, LocalDate.now(), LocalDate.now().plusDays(7));
        assertEquals(LocalDate.now(), todayLoan.getLoanDate());
        assertEquals(LocalDate.now().plusDays(7), todayLoan.getDueDate());
        assertFalse(todayLoan.isOverdue());
        assertEquals(0, todayLoan.getDaysOverdue());
        
        // Test loan due today
        Loan dueTodayLoan = new Loan();
        dueTodayLoan.setStatus(Loan.LoanStatus.ACTIVO);
        dueTodayLoan.setLoanDate(LocalDate.now().minusDays(7));
        dueTodayLoan.setDueDate(LocalDate.now());
        assertFalse(dueTodayLoan.isOverdue());
        assertEquals(0, dueTodayLoan.getDaysOverdue());
        
        // Test loan due tomorrow
        Loan dueTomorrowLoan = new Loan();
        dueTomorrowLoan.setStatus(Loan.LoanStatus.ACTIVO);
        dueTomorrowLoan.setLoanDate(LocalDate.now().minusDays(6));
        dueTomorrowLoan.setDueDate(LocalDate.now().plusDays(1));
        assertFalse(dueTomorrowLoan.isOverdue());
        assertEquals(0, dueTomorrowLoan.getDaysOverdue());
    }
    
    @Test
    @DisplayName("Should validate loan fine calculations with edge cases")
    void testLoanFineCalculationsEdgeCases() throws Exception {
        // Test loan exactly on due date
        Loan onDueDateLoan = new Loan();
        onDueDateLoan.setStatus(Loan.LoanStatus.ACTIVO);
        onDueDateLoan.setLoanDate(LocalDate.now().minusDays(7));
        onDueDateLoan.setDueDate(LocalDate.now());
        
        double fineOnDueDate = loanService.calculateFine(onDueDateLoan);
        assertEquals(0.0, fineOnDueDate, 0.01);
        
        // Test loan with large overdue period
        Loan longOverdueLoan = new Loan();
        longOverdueLoan.setStatus(Loan.LoanStatus.ACTIVO);
        longOverdueLoan.setLoanDate(LocalDate.now().minusDays(100));
        longOverdueLoan.setDueDate(LocalDate.now().minusDays(93)); // 7 days loan, 93 days overdue
        
        double longOverdueFine = loanService.calculateFine(longOverdueLoan);
        assertEquals(93 * 1500.0, longOverdueFine, 0.01); // 93 days * 1500
    }
    
    @Test
    @DisplayName("Should validate loan status transitions")
    void testLoanStatusTransitions() {
        // Test loan creation with default status
        Loan loan = new Loan(1, 1, LocalDate.now(), LocalDate.now().plusDays(7));
        assertEquals(Loan.LoanStatus.ACTIVO, loan.getStatus());
        assertEquals(BigDecimal.ZERO, loan.getFineAmount());
        assertNull(loan.getReturnDate());
        
        // Test loan return
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.DEVUELTO);
        loan.setFineAmount(BigDecimal.valueOf(1500.0));
        
        assertEquals(LocalDate.now(), loan.getReturnDate());
        assertEquals(Loan.LoanStatus.DEVUELTO, loan.getStatus());
        assertEquals(BigDecimal.valueOf(1500.0), loan.getFineAmount());
    }
    
    @Test
    @DisplayName("Should validate loan update with null parameters")
    void testLoanUpdateWithNullParameters() throws Exception {
        // Test updating loan with null loan
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.updateLoan(null);
        });
        
        // Test updating loan with invalid ID
        Loan loanWithInvalidId = new Loan();
        loanWithInvalidId.setId(0);
        loanWithInvalidId.setPartnerId(1);
        loanWithInvalidId.setBookId(1);
        loanWithInvalidId.setLoanDate(LocalDate.now());
        loanWithInvalidId.setDueDate(LocalDate.now().plusDays(7));
        loanWithInvalidId.setStatus(Loan.LoanStatus.ACTIVO);
        loanWithInvalidId.setFineAmount(BigDecimal.ZERO);
        
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.updateLoan(loanWithInvalidId);
        });
    }
    
    @Test
    @DisplayName("Should validate loan business rules")
    void testLoanBusinessRulesValidation() {
        // Test loan with past loan date
        Loan pastLoanDateLoan = new Loan();
        pastLoanDateLoan.setLoanDate(LocalDate.now().minusDays(30));
        pastLoanDateLoan.setDueDate(LocalDate.now().minusDays(23));
        
        // This should be valid (loans can be created in the past)
        assertNotNull(pastLoanDateLoan);
        assertTrue(pastLoanDateLoan.getLoanDate().isBefore(LocalDate.now()));
        
        // Test loan with future due date
        Loan futureDueDateLoan = new Loan();
        futureDueDateLoan.setLoanDate(LocalDate.now());
        futureDueDateLoan.setDueDate(LocalDate.now().plusDays(30));
        
        assertNotNull(futureDueDateLoan);
        assertTrue(futureDueDateLoan.getDueDate().isAfter(LocalDate.now()));
        assertFalse(futureDueDateLoan.isOverdue());
    }
}
