package com.codeup.novabook.exception;

/**
 * Exception thrown when authentication fails
 */
public class AuthenticationException extends LibroNovaException {
    
    public AuthenticationException(String message) {
        super(message);
    }
}
