package com.codeup.novabook.exception;

/**
 * Exception thrown when trying to perform an invalid loan operation
 */
public class InvalidLoanException extends LibroNovaException {
    
    public InvalidLoanException(String message) {
        super(message);
    }
}
