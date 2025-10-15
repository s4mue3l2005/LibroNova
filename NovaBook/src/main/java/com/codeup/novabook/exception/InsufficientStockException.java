package com.codeup.novabook.exception;

/**
 * Exception thrown when trying to loan a book with insufficient available copies
 */
public class InsufficientStockException extends LibroNovaException {
    
    public InsufficientStockException(String isbn, int requested, int available) {
        super(String.format("Insufficient stock for book %s. Requested: %d, Available: %d", 
                          isbn, requested, available));
    }
}
