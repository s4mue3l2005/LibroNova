package com.codeup.novabook.service.impl;

import com.codeup.novabook.dao.BookDAO;
import com.codeup.novabook.dao.impl.BookDAOImpl;
import com.codeup.novabook.exception.DuplicateISBNException;
import com.codeup.novabook.model.Book;
import com.codeup.novabook.service.BookService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service implementation for Book operations
 */
public class BookServiceImpl implements BookService {
    
    private static final Logger logger = Logger.getLogger(BookServiceImpl.class.getName());
    private final BookDAO bookDAO;
    
    public BookServiceImpl() {
        this.bookDAO = new BookDAOImpl();
    }
    
    @Override
    public Book createBook(Book book) throws Exception {
        if (book == null) {
            throw new IllegalArgumentException("Book is required");
        }
        logger.info("POST /books - Creating new book: " + book.getIsbn());

        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        
        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
        
        if (book.getTotalCopies() <= 0) {
            throw new IllegalArgumentException("Total copies must be greater than 0");
        }
        
        if (book.getReferencePrice() == null || book.getReferencePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Reference price must be non-negative");
        }
        
        // Validate ISBN uniqueness
        if (bookDAO.existsByIsbn(book.getIsbn())) {
            throw new DuplicateISBNException(book.getIsbn());
        }
        
        // Set default values
        book.setAvailableCopies(book.getTotalCopies());
        book.setActive(true);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        
        Book createdBook = bookDAO.create(book);
        logger.info("Book created successfully: " + createdBook.getIsbn() + " - " + createdBook.getTitle());
        
        return createdBook;
    }
    
    @Override
    public Book getBookById(int id) throws Exception {
        logger.info("GET /books/" + id + " - Retrieving book by ID");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid book ID is required");
        }
        
        Optional<Book> bookOptional = bookDAO.findById(id);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + id);
        }
        
        return bookOptional.get();
    }
    
    @Override
    public Book getBookByIsbn(String isbn) throws Exception {
        logger.info("GET /books/isbn/" + isbn + " - Retrieving book by ISBN");
        
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        
        Optional<Book> bookOptional = bookDAO.findByIsbn(isbn);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ISBN: " + isbn);
        }
        
        return bookOptional.get();
    }
    
    @Override
    public List<Book> getAllBooks() throws Exception {
        logger.info("GET /books - Retrieving all books");
        
        return bookDAO.findAll();
    }
    
    @Override
    public List<Book> getActiveBooks() throws Exception {
        logger.info("GET /books/active - Retrieving active books");
        
        return bookDAO.findActive();
    }
    
    @Override
    public List<Book> findBooksByCategory(String category) throws Exception {
        logger.info("GET /books/category/" + category + " - Finding books by category");
        
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
        
        return bookDAO.findByCategory(category);
    }
    
    @Override
    public List<Book> findBooksByAuthor(String author) throws Exception {
        logger.info("GET /books/author/" + author + " - Finding books by author");
        
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        
        return bookDAO.findByAuthor(author);
    }
    
    @Override
    public Book updateBook(Book book) throws Exception {
        logger.info("PATCH /books/" + book.getId() + " - Updating book");
        
        if (book == null) {
            throw new IllegalArgumentException("Book is required");
        }
        
        if (book.getId() <= 0) {
            throw new IllegalArgumentException("Valid book ID is required");
        }
        
        // Check if book exists
        Optional<Book> existingBookOptional = bookDAO.findById(book.getId());
        if (existingBookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + book.getId());
        }
        
        Book existingBook = existingBookOptional.get();
        
        // Validate ISBN uniqueness if changed
        if (!existingBook.getIsbn().equals(book.getIsbn()) && 
            bookDAO.existsByIsbn(book.getIsbn())) {
            throw new DuplicateISBNException(book.getIsbn());
        }
        
        // Validate business rules
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException("Available copies cannot exceed total copies");
        }
        
        if (book.getTotalCopies() < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }
        
        if (book.getAvailableCopies() < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }
        
        book.setUpdatedAt(LocalDateTime.now());
        Book updatedBook = bookDAO.update(book);
        logger.info("Book updated successfully: " + updatedBook.getIsbn() + " - " + updatedBook.getTitle());
        
        return updatedBook;
    }
    
    @Override
    public boolean deleteBook(int id) throws Exception {
        logger.info("DELETE /books/" + id + " - Deleting book");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid book ID is required");
        }
        
        // Check if book exists
        Optional<Book> bookOptional = bookDAO.findById(id);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + id);
        }
        
        // Check if book has active loans
        Book book = bookOptional.get();
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            throw new IllegalArgumentException("Cannot delete book with active loans");
        }
        
        boolean deleted = bookDAO.delete(id);
        if (deleted) {
            logger.info("Book deleted successfully: ID " + id);
        } else {
            logger.warning("Failed to delete book: ID " + id);
        }
        
        return deleted;
    }
    
    @Override
    public boolean isIsbnAvailable(String isbn) throws Exception {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        
        return !bookDAO.existsByIsbn(isbn);
    }
    
    @Override
    public boolean isIsbnAvailableForUpdate(String isbn, int bookId) throws Exception {
        if (isbn == null || isbn.trim().isEmpty() || bookId <= 0) {
            return false;
        }
        
        Optional<Book> existingBook = bookDAO.findById(bookId);
        if (existingBook.isPresent() && existingBook.get().getIsbn().equals(isbn)) {
            return true; // Same ISBN, no conflict
        }
        
        return !bookDAO.existsByIsbn(isbn);
    }
    
    @Override
    public List<String> getAllCategories() throws Exception {
        logger.info("GET /books/categories - Retrieving all categories");
        
        List<Book> allBooks = bookDAO.findAll();
        return allBooks.stream()
                .map(Book::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllAuthors() throws Exception {
        logger.info("GET /books/authors - Retrieving all authors");
        
        List<Book> allBooks = bookDAO.findAll();
        return allBooks.stream()
                .map(Book::getAuthor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
