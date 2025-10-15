package com.codeup.novabook.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for custom exceptions
 */
public class ExceptionTest {
    
    @Test
    @DisplayName("Should create LibroNovaException with message")
    void testLibroNovaExceptionWithMessage() {
        String message = "Test error message";
        LibroNovaException exception = new LibroNovaException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    @DisplayName("Should create LibroNovaException with message and cause")
    void testLibroNovaExceptionWithMessageAndCause() {
        String message = "Test error message";
        RuntimeException cause = new RuntimeException("Root cause");
        LibroNovaException exception = new LibroNovaException(message, cause);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("Should create DuplicateISBNException with ISBN")
    void testDuplicateISBNException() {
        String isbn = "9780123456789";
        DuplicateISBNException exception = new DuplicateISBNException(isbn);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(isbn));
        assertTrue(exception.getMessage().contains("already exists"));
        assertNull(exception.getCause());
    }
    
    @Test
    @DisplayName("Should create InsufficientStockException with details")
    void testInsufficientStockException() {
        String isbn = "9780123456789";
        int requested = 5;
        int available = 2;
        
        InsufficientStockException exception = new InsufficientStockException(isbn, requested, available);
        
        assertNotNull(exception);
        String message = exception.getMessage();
        assertTrue(message.contains(isbn));
        assertTrue(message.contains(String.valueOf(requested)));
        assertTrue(message.contains(String.valueOf(available)));
        assertTrue(message.contains("Insufficient stock"));
        assertNull(exception.getCause());
    }
    
    @Test
    @DisplayName("Should create InvalidLoanException with message")
    void testInvalidLoanException() {
        String message = "Loan is not active";
        InvalidLoanException exception = new InvalidLoanException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    @DisplayName("Should create AuthenticationException with message")
    void testAuthenticationException() {
        String message = "Invalid credentials";
        AuthenticationException exception = new AuthenticationException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    @DisplayName("Should test exception inheritance hierarchy")
    void testExceptionInheritance() {
        String message = "Test message";
        
        // Test that all custom exceptions extend LibroNovaException
        assertTrue(new DuplicateISBNException("9780123456789") instanceof LibroNovaException);
        assertTrue(new InsufficientStockException("9780123456789", 1, 0) instanceof LibroNovaException);
        assertTrue(new InvalidLoanException(message) instanceof LibroNovaException);
        assertTrue(new AuthenticationException(message) instanceof LibroNovaException);
        
        // Test that LibroNovaException extends Exception
        assertTrue(new LibroNovaException(message) instanceof Exception);
    }
    
    @Test
    @DisplayName("Should test exception message formatting")
    void testExceptionMessageFormatting() {
        // Test DuplicateISBNException message format
        String isbn = "978-0-12-345678-9";
        DuplicateISBNException duplicateException = new DuplicateISBNException(isbn);
        String expectedMessage = "A book with ISBN " + isbn + " already exists in the catalog";
        assertEquals(expectedMessage, duplicateException.getMessage());
        
        // Test InsufficientStockException message format
        InsufficientStockException stockException = new InsufficientStockException("9780123456789", 10, 3);
        String stockMessage = stockException.getMessage();
        assertTrue(stockMessage.contains("Insufficient stock for book 9780123456789. Requested: 10, Available: 3"));
        
        // Test that messages are properly formatted
        assertFalse(stockMessage.contains("null"));
        assertFalse(stockMessage.contains("undefined"));
    }
    
    @Test
    @DisplayName("Should handle edge cases in exception creation")
    void testExceptionEdgeCases() {
        // Test with null ISBN
        DuplicateISBNException nullIsbnException = new DuplicateISBNException(null);
        assertNotNull(nullIsbnException);
        assertTrue(nullIsbnException.getMessage().contains("null"));
        
        // Test with empty message
        AuthenticationException emptyMessageException = new AuthenticationException("");
        assertNotNull(emptyMessageException);
        assertEquals("", emptyMessageException.getMessage());
        
        // Test with zero values in InsufficientStockException
        InsufficientStockException zeroStockException = new InsufficientStockException("9780123456789", 0, 0);
        assertNotNull(zeroStockException);
        assertTrue(zeroStockException.getMessage().contains("Requested: 0"));
        assertTrue(zeroStockException.getMessage().contains("Available: 0"));
    }
    
    @Test
    @DisplayName("Should test exception serialization compatibility")
    void testExceptionSerialization() {
        // Test that exceptions can be serialized (basic check)
        String message = "Test message";
        
        LibroNovaException baseException = new LibroNovaException(message);
        DuplicateISBNException duplicateException = new DuplicateISBNException("9780123456789");
        InsufficientStockException stockException = new InsufficientStockException("9780123456789", 1, 0);
        InvalidLoanException loanException = new InvalidLoanException(message);
        AuthenticationException authException = new AuthenticationException(message);
        
        // Verify all exceptions can be instantiated and have messages
        assertNotNull(baseException.getMessage());
        assertNotNull(duplicateException.getMessage());
        assertNotNull(stockException.getMessage());
        assertNotNull(loanException.getMessage());
        assertNotNull(authException.getMessage());
        
        // Verify message persistence
        assertEquals(message, baseException.getMessage());
        assertEquals(message, loanException.getMessage());
        assertEquals(message, authException.getMessage());
    }
}
