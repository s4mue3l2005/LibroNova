package com.codeup.novabook.exception;

/**
 * Base exception class for LibroNova application
 */
public class LibroNovaException extends Exception {
    
    public LibroNovaException(String message) {
        super(message);
    }
    
    public LibroNovaException(String message, Throwable cause) {
        super(message, cause);
    }
}
