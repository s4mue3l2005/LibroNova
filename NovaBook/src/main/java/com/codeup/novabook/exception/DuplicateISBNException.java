package com.codeup.novabook.exception;

/**
 * Exception thrown when trying to register a book with an existing ISBN
 */
public class DuplicateISBNException extends LibroNovaException {
    
    public DuplicateISBNException(String isbn) {
        super("A book with ISBN " + isbn + " already exists in the catalog");
    }
}
