package com.codeup.novabook.dao;

import com.codeup.novabook.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Book entity
 */
public interface BookDAO {
    
    /**
     * Create a new book
     */
    Book create(Book book);
    
    /**
     * Find book by ID
     */
    Optional<Book> findById(int id);
    
    /**
     * Find book by ISBN
     */
    Optional<Book> findByIsbn(String isbn);
    
    /**
     * Get all books
     */
    List<Book> findAll();
    
    /**
     * Get active books only
     */
    List<Book> findActive();
    
    /**
     * Find books by category
     */
    List<Book> findByCategory(String category);
    
    /**
     * Find books by author
     */
    List<Book> findByAuthor(String author);
    
    /**
     * Update an existing book
     */
    Book update(Book book);
    
    /**
     * Delete a book by ID
     */
    boolean delete(int id);
    
    /**
     * Check if ISBN exists
     */
    boolean existsByIsbn(String isbn);
    
    /**
     * Update available copies count
     */
    void updateAvailableCopies(int bookId, int newAvailableCopies);
}
