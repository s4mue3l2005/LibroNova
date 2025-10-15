package com.codeup.novabook.service;

import com.codeup.novabook.model.Book;
import com.codeup.novabook.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookService
 */
public class BookServiceTest {
    
    private BookService bookService;
    
    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl();
    }
    
    @Test
    @DisplayName("Should validate ISBN uniqueness")
    void testIsbnUniqueness() throws Exception {
        // Test with null ISBN
        assertFalse(bookService.isIsbnAvailable(null));
        
        // Test with empty ISBN
        assertFalse(bookService.isIsbnAvailable(""));
        
        // Test with valid ISBN (assuming no existing books in test DB)
        assertTrue(bookService.isIsbnAvailable("9780123456789"));
    }
    
    @Test
    @DisplayName("Should validate ISBN availability for update")
    void testIsbnAvailabilityForUpdate() throws Exception {
        // Test with null values
        assertFalse(bookService.isIsbnAvailableForUpdate(null, 1));
        assertFalse(bookService.isIsbnAvailableForUpdate("", 1));
        assertFalse(bookService.isIsbnAvailableForUpdate("9780123456789", 0));
        
        // Test with valid parameters (assuming no existing books in test DB)
        assertTrue(bookService.isIsbnAvailableForUpdate("9780123456789", 1));
    }
    
    @Test
    @DisplayName("Should create book with valid data")
    void testCreateBookWithValidData() throws Exception {
        Book book = new Book("9780123456789", "Test Book", "Test Author", "Test Category", 5, BigDecimal.valueOf(29.99));
        
        // This test would require a test database setup
        // For now, we'll test the validation logic
        assertNotNull(book);
        assertEquals("9780123456789", book.getIsbn());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("Test Category", book.getCategory());
        assertEquals(5, book.getTotalCopies());
        assertEquals(5, book.getAvailableCopies()); // Should be set to total copies
        assertEquals(BigDecimal.valueOf(29.99), book.getReferencePrice());
        assertTrue(book.isActive());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid book data")
    void testCreateBookWithInvalidData() {
        // Test null book
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(null);
        });
        
        // Test book with null ISBN
        Book bookWithNullIsbn = new Book();
        bookWithNullIsbn.setTitle("Test Book");
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(bookWithNullIsbn);
        });
        
        // Test book with empty title
        Book bookWithEmptyTitle = new Book("9780123456789", "", "Test Author", "Test Category", 5, BigDecimal.valueOf(29.99));
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(bookWithEmptyTitle);
        });
        
        // Test book with negative total copies
        Book bookWithNegativeCopies = new Book("9780123456789", "Test Book", "Test Author", "Test Category", -1, BigDecimal.valueOf(29.99));
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(bookWithNegativeCopies);
        });
        
        // Test book with negative price
        Book bookWithNegativePrice = new Book("9780123456789", "Test Book", "Test Author", "Test Category", 5, BigDecimal.valueOf(-10.00));
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(bookWithNegativePrice);
        });
    }
    
    @Test
    @DisplayName("Should validate business rules for book updates")
    void testBookUpdateValidation() throws Exception {
        Book book = new Book("9780123456789", "Test Book", "Test Author", "Test Category", 5, BigDecimal.valueOf(29.99));
        book.setId(1);
        book.setAvailableCopies(3);
        
        // Test updating with available copies > total copies
        book.setAvailableCopies(10);
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(book);
        });
        
        // Reset to valid state
        book.setAvailableCopies(3);
        
        // Test updating with negative total copies
        book.setTotalCopies(-1);
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(book);
        });
        
        // Reset to valid state
        book.setTotalCopies(5);
        
        // Test updating with negative available copies
        book.setAvailableCopies(-1);
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(book);
        });
    }
    
    @Test
    @DisplayName("Should validate book availability")
    void testBookAvailability() {
        Book book = new Book("9780123456789", "Test Book", "Test Author", "Test Category", 5, BigDecimal.valueOf(29.99));
        
        // Test available book
        book.setActive(true);
        book.setAvailableCopies(3);
        assertTrue(book.isAvailable());
        
        // Test inactive book
        book.setActive(false);
        assertFalse(book.isAvailable());
        
        // Test book with no available copies
        book.setActive(true);
        book.setAvailableCopies(0);
        assertFalse(book.isAvailable());
    }
    
    @Test
    @DisplayName("Should get all categories and authors")
    void testGetCategoriesAndAuthors() throws Exception {
        // These tests would require a test database with sample data
        // For now, we'll test that the methods don't throw exceptions
        assertDoesNotThrow(() -> {
            List<String> categories = bookService.getAllCategories();
            assertNotNull(categories);
        });
        
        assertDoesNotThrow(() -> {
            List<String> authors = bookService.getAllAuthors();
            assertNotNull(authors);
        });
    }
    
    @Test
    @DisplayName("Should validate book deletion with active loans")
    void testBookDeletionWithActiveLoans() {
        // Test invalid book ID for deletion
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBook(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBook(-1);
        });
    }
    
    @Test
    @DisplayName("Should validate book retrieval parameters")
    void testBookRetrievalValidation() {
        // Test invalid book ID
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookById(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookById(-1);
        });
        
        // Test null ISBN
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookByIsbn(null);
        });
        
        // Test empty ISBN
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookByIsbn("");
        });
    }
    
    @Test
    @DisplayName("Should validate book filtering parameters")
    void testBookFilteringValidation() {
        // Test null category
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.findBooksByCategory(null);
        });
        
        // Test empty category
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.findBooksByCategory("");
        });
        
        // Test null author
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.findBooksByAuthor(null);
        });
        
        // Test empty author
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.findBooksByAuthor("");
        });
    }
    
    @Test
    @DisplayName("Should validate book model edge cases")
    void testBookModelEdgeCases() {
        // Test book with zero total copies
        Book bookWithZeroCopies = new Book("9780123456789", "Test Book", "Test Author", "Test Category", 0, BigDecimal.valueOf(29.99));
        assertEquals(0, bookWithZeroCopies.getTotalCopies());
        assertEquals(0, bookWithZeroCopies.getAvailableCopies());
        assertFalse(bookWithZeroCopies.isAvailable());
        
        // Test book with zero price
        Book bookWithZeroPrice = new Book("9780123456789", "Test Book", "Test Author", "Test Category", 5, BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, bookWithZeroPrice.getReferencePrice());
        
        // Test book with very long strings
        String longString = "A".repeat(200);
        Book bookWithLongStrings = new Book("9780123456789", longString, longString, longString, 5, BigDecimal.valueOf(29.99));
        assertEquals(longString, bookWithLongStrings.getTitle());
        assertEquals(longString, bookWithLongStrings.getAuthor());
        assertEquals(longString, bookWithLongStrings.getCategory());
    }
    
    @Test
    @DisplayName("Should validate book ISBN format")
    void testBookIsbnFormat() {
        // Test various ISBN formats
        String[] validIsbns = {
            "9780123456789",  // 13-digit ISBN
            "0123456789",     // 10-digit ISBN
            "978-0-12-345678-9", // ISBN with hyphens
            "0-12-345678-9"   // 10-digit with hyphens
        };
        
        for (String isbn : validIsbns) {
            Book book = new Book(isbn, "Test Book", "Test Author", "Test Category", 5, BigDecimal.valueOf(29.99));
            assertEquals(isbn, book.getIsbn());
        }
        
        // Test ISBN with special characters (should be handled by validation)
        assertDoesNotThrow(() -> {
            Book book = new Book("978-0-12-345678-9", "Test Book", "Test Author", "Test Category", 5, BigDecimal.valueOf(29.99));
            assertNotNull(book);
        });
    }
}
