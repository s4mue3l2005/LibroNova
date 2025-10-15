package com.codeup.novabook.service;

import com.codeup.novabook.model.Book;
import java.util.List;

/**
 * Service interface for Book operations
 */
public interface BookService {
    
    /**
     * Create a new book
     * @param book
     * @return 
     * @throws java.lang.Exception
     */
    Book createBook(Book book) throws Exception;
    
    /**
     * Get book by ID
     * @param id
     * @return 
     * @throws java.lang.Exception
     */
    Book getBookById(int id) throws Exception;
    
    /**
     * Get book by ISBN
     */
    Book getBookByIsbn(String isbn) throws Exception;
    
    /**
     * Get all books
     */
    List<Book> getAllBooks() throws Exception;
    
    /**
     * Get active books only
     */
    List<Book> getActiveBooks() throws Exception;
    
    /**
     * Find books by category
     */
    List<Book> findBooksByCategory(String category) throws Exception;
    
    /**
     * Find books by author
     */
    List<Book> findBooksByAuthor(String author) throws Exception;
    
    /**
     * Update book information
     */
    Book updateBook(Book book) throws Exception;
    
    /**
     * Delete book by ID
     */
    boolean deleteBook(int id) throws Exception;
    
    /**
     * Check if ISBN is available
     */
    boolean isIsbnAvailable(String isbn) throws Exception;
    
    /**
     * Check if ISBN is available for update (excluding current book)
     */
    boolean isIsbnAvailableForUpdate(String isbn, int bookId) throws Exception;
    
    /**
     * Get all available categories
     */
    List<String> getAllCategories() throws Exception;
    
    /**
     * Get all authors
     */
    List<String> getAllAuthors() throws Exception;
}
