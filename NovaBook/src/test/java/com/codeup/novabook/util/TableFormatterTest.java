package com.codeup.novabook.util;

import com.codeup.novabook.model.Book;
import com.codeup.novabook.model.Loan;
import com.codeup.novabook.model.Partner;
import com.codeup.novabook.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TableFormatter utility
 */
public class TableFormatterTest {
    
    @Test
    @DisplayName("Should format empty book list")
    void testFormatEmptyBooksList() {
        List<Book> emptyList = new ArrayList<>();
        String result = TableFormatter.formatBooksTable(emptyList);
        
        assertNotNull(result);
        assertEquals("No books found.", result);
    }
    
    @Test
    @DisplayName("Should format books table correctly")
    void testFormatBooksTable() {
        List<Book> books = new ArrayList<>();
        
        Book book1 = new Book("9780123456789", "Test Book 1", "Test Author 1", "Programming", 5, BigDecimal.valueOf(29.99));
        book1.setId(1);
        book1.setAvailableCopies(3);
        book1.setActive(true);
        book1.setCreatedAt(LocalDateTime.now());
        book1.setUpdatedAt(LocalDateTime.now());
        books.add(book1);
        
        Book book2 = new Book("9780123456790", "Test Book 2", "Test Author 2", "Fiction", 3, BigDecimal.valueOf(19.99));
        book2.setId(2);
        book2.setAvailableCopies(0);
        book2.setActive(false);
        book2.setCreatedAt(LocalDateTime.now());
        book2.setUpdatedAt(LocalDateTime.now());
        books.add(book2);
        
        String result = TableFormatter.formatBooksTable(books);
        
        assertNotNull(result);
        assertTrue(result.contains("ID"));
        assertTrue(result.contains("ISBN"));
        assertTrue(result.contains("Title"));
        assertTrue(result.contains("Author"));
        assertTrue(result.contains("Category"));
        assertTrue(result.contains("Copies"));
        assertTrue(result.contains("Available"));
        assertTrue(result.contains("Price"));
        assertTrue(result.contains("Status"));
        assertTrue(result.contains("Test Book 1"));
        assertTrue(result.contains("Test Book 2"));
        assertTrue(result.contains("[ACTIVO]"));
        assertTrue(result.contains("[INACTIVO]"));
    }
    
    @Test
    @DisplayName("Should format empty partner list")
    void testFormatEmptyPartnersList() {
        List<Partner> emptyList = new ArrayList<>();
        String result = TableFormatter.formatPartnersTable(emptyList);
        
        assertNotNull(result);
        assertEquals("No partners found.", result);
    }
    
    @Test
    @DisplayName("Should format partners table correctly")
    void testFormatPartnersTable() {
        List<Partner> partners = new ArrayList<>();
        
        Partner partner1 = new Partner("12345678", "John", "Doe", "john@test.com");
        partner1.setId(1);
        partner1.setPhone("3001234567");
        partner1.setAddress("Calle 123");
        partner1.setStatus(Partner.PartnerStatus.ACTIVO);
        partner1.setCreatedAt(LocalDateTime.now());
        partner1.setUpdatedAt(LocalDateTime.now());
        partners.add(partner1);
        
        Partner partner2 = new Partner("87654321", "Jane", "Smith", "jane@test.com");
        partner2.setId(2);
        partner2.setStatus(Partner.PartnerStatus.INACTIVO);
        partner2.setCreatedAt(LocalDateTime.now());
        partner2.setUpdatedAt(LocalDateTime.now());
        partners.add(partner2);
        
        String result = TableFormatter.formatPartnersTable(partners);
        
        assertNotNull(result);
        assertTrue(result.contains("ID"));
        assertTrue(result.contains("Document"));
        assertTrue(result.contains("Full Name"));
        assertTrue(result.contains("Email"));
        assertTrue(result.contains("Phone"));
        assertTrue(result.contains("Status"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Jane Smith"));
        assertTrue(result.contains("12345678"));
        assertTrue(result.contains("87654321"));
        assertTrue(result.contains("[ACTIVO]"));
        assertTrue(result.contains("[INACTIVO]"));
    }
    
    @Test
    @DisplayName("Should format empty user list")
    void testFormatEmptyUsersList() {
        List<User> emptyList = new ArrayList<>();
        String result = TableFormatter.formatUsersTable(emptyList);
        
        assertNotNull(result);
        assertEquals("No users found.", result);
    }
    
    @Test
    @DisplayName("Should format users table correctly")
    void testFormatUsersTable() {
        List<User> users = new ArrayList<>();
        
        User user1 = new User("admin", "password", "admin@test.com", User.UserRole.ADMIN, User.UserStatus.ACTIVO);
        user1.setId(1);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());
        users.add(user1);
        
        User user2 = new User("assistant", "password", "assistant@test.com", User.UserRole.ASISTENTE, User.UserStatus.INACTIVO);
        user2.setId(2);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());
        users.add(user2);
        
        String result = TableFormatter.formatUsersTable(users);
        
        assertNotNull(result);
        assertTrue(result.contains("ID"));
        assertTrue(result.contains("Username"));
        assertTrue(result.contains("Email"));
        assertTrue(result.contains("Role"));
        assertTrue(result.contains("Status"));
        assertTrue(result.contains("Created"));
        assertTrue(result.contains("admin"));
        assertTrue(result.contains("assistant"));
        assertTrue(result.contains("ADMIN"));
        assertTrue(result.contains("ASISTENTE"));
        assertTrue(result.contains("[ACTIVO]"));
        assertTrue(result.contains("[INACTIVO]"));
    }
    
    @Test
    @DisplayName("Should format empty loan list")
    void testFormatEmptyLoansList() {
        List<Loan> emptyList = new ArrayList<>();
        String result = TableFormatter.formatLoansTable(emptyList);
        
        assertNotNull(result);
        assertEquals("No loans found.", result);
    }
    
    @Test
    @DisplayName("Should format loans table correctly")
    void testFormatLoansTable() {
        List<Loan> loans = new ArrayList<>();
        
        Loan loan1 = new Loan(1, 1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(2));
        loan1.setId(1);
        loan1.setPartnerName("John Doe");
        loan1.setBookTitle("Test Book 1");
        loan1.setBookIsbn("9780123456789");
        loan1.setStatus(Loan.LoanStatus.ACTIVO);
        loan1.setFineAmount(BigDecimal.ZERO);
        loans.add(loan1);
        
        Loan loan2 = new Loan(2, 2, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));
        loan2.setId(2);
        loan2.setReturnDate(LocalDate.now());
        loan2.setPartnerName("Jane Smith");
        loan2.setBookTitle("Test Book 2");
        loan2.setBookIsbn("9780123456790");
        loan2.setStatus(Loan.LoanStatus.DEVUELTO);
        loan2.setFineAmount(BigDecimal.valueOf(1500.0));
        loans.add(loan2);
        
        String result = TableFormatter.formatLoansTable(loans);
        
        assertNotNull(result);
        assertTrue(result.contains("ID"));
        assertTrue(result.contains("Partner"));
        assertTrue(result.contains("Book"));
        assertTrue(result.contains("Loan Date"));
        assertTrue(result.contains("Due Date"));
        assertTrue(result.contains("Return Date"));
        assertTrue(result.contains("Fine"));
        assertTrue(result.contains("Status"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Jane Smith"));
        assertTrue(result.contains("Test Book 1"));
        assertTrue(result.contains("Test Book 2"));
        assertTrue(result.contains("ACTIVO"));
        assertTrue(result.contains("DEVUELTO"));
    }
    
    @Test
    @DisplayName("Should handle long text truncation")
    void testLongTextTruncation() {
        List<Book> books = new ArrayList<>();
        // Create book with very long title and author
        String longTitle = "A".repeat(50);
        String longAuthor = "B".repeat(30);
        String longCategory = "C".repeat(25);
        Book book = new Book("9780123456789", longTitle, longAuthor, longCategory, 5, BigDecimal.valueOf(29.99));
        book.setId(1);
        book.setAvailableCopies(3);
        book.setActive(true);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        books.add(book);

        String result = TableFormatter.formatBooksTable(books);
        assertNotNull(result);
        // Verifica que los campos truncados terminan en "..." y tienen la longitud correcta
        String truncatedTitle = TableFormatter.truncate(longTitle, 32);
        String truncatedAuthor = TableFormatter.truncate(longAuthor, 18);
        String truncatedCategory = TableFormatter.truncate(longCategory, 11);
        assertTrue(truncatedTitle.endsWith("..."));
        assertEquals(32, truncatedTitle.length());
        assertTrue(truncatedAuthor.endsWith("..."));
        assertEquals(18, truncatedAuthor.length());
        assertTrue(truncatedCategory.endsWith("..."));
        assertEquals(11, truncatedCategory.length());
        // Verifica que el resultado contiene los valores truncados
        assertTrue(result.contains(truncatedTitle));
        assertTrue(result.contains(truncatedAuthor));
        assertTrue(result.contains(truncatedCategory));
    }
    
    @Test
    @DisplayName("Should handle null values in loan display fields")
    void testNullValuesInLoans() {
        List<Loan> loans = new ArrayList<>();
        
        Loan loan = new Loan(1, 1, LocalDate.now(), LocalDate.now().plusDays(7));
        loan.setId(1);
        // Leave partnerName, bookTitle, and bookIsbn as null
        loan.setStatus(Loan.LoanStatus.ACTIVO);
        loan.setFineAmount(BigDecimal.ZERO);
        loans.add(loan);
        
        String result = TableFormatter.formatLoansTable(loans);
        
        assertNotNull(result);
        assertTrue(result.contains("N/A")); // Should display N/A for null values
    }
}
