package com.codeup.novabook.controller;

import com.codeup.novabook.model.Book;
import com.codeup.novabook.service.BookService;
import com.codeup.novabook.service.impl.BookServiceImpl;
import com.codeup.novabook.util.TableFormatter;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for Book operations with JOptionPane interface
 */
public class BookController {
    
    private static final Logger logger = Logger.getLogger(BookController.class.getName());
    private final BookService bookService;
    
    public BookController() {
        this.bookService = new BookServiceImpl();
    }
    
    /**
     * Show book management menu
     */
    public void showBookMenu() {
        String[] options = {
            "1. List all books",
            "2. List active books",
            "3. Find books by category",
            "4. Find books by author",
            "5. Add new book",
            "6. Update book",
            "7. Delete book",
            "8. Back to main menu"
        };
        
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Book Management\n\nSelect an option:",
                "LibroNova - Book Management",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice == null || choice.contains("8")) {
                break;
            }
            
            try {
                if (choice.contains("1")) {
                    listAllBooks();
                } else if (choice.contains("2")) {
                    listActiveBooks();
                } else if (choice.contains("3")) {
                    findBooksByCategory();
                } else if (choice.contains("4")) {
                    findBooksByAuthor();
                } else if (choice.contains("5")) {
                    addNewBook();
                } else if (choice.contains("6")) {
                    updateBook();
                } else if (choice.contains("7")) {
                    deleteBook();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Error in book management: " + e.getMessage());
            }
        }
    }
    
    /**
     * List all books
     */
    private void listAllBooks() throws Exception {
        List<Book> books = bookService.getAllBooks();
        String table = TableFormatter.formatBooksTable(books);
        
        JOptionPane.showMessageDialog(null, 
            "All Books:\n\n" + table, 
            "All Books", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * List active books
     */
    private void listActiveBooks() throws Exception {
        List<Book> books = bookService.getActiveBooks();
        String table = TableFormatter.formatBooksTable(books);
        
        JOptionPane.showMessageDialog(null, 
            "Active Books:\n\n" + table, 
            "Active Books", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Find books by category
     */
    private void findBooksByCategory() throws Exception {
        List<String> categories = bookService.getAllCategories();
        
        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No categories available.", 
                "No Categories", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String category = (String) JOptionPane.showInputDialog(
            null,
            "Select a category:",
            "Find Books by Category",
            JOptionPane.QUESTION_MESSAGE,
            null,
            categories.toArray(),
            categories.get(0)
        );
        
        if (category != null) {
            List<Book> books = bookService.findBooksByCategory(category);
            String table = TableFormatter.formatBooksTable(books);
            
            JOptionPane.showMessageDialog(null, 
                "Books in category '" + category + "':\n\n" + table, 
                "Books by Category", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Find books by author
     */
    private void findBooksByAuthor() throws Exception {
        List<String> authors = bookService.getAllAuthors();
        
        if (authors.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No authors available.", 
                "No Authors", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String author = (String) JOptionPane.showInputDialog(
            null,
            "Select an author:",
            "Find Books by Author",
            JOptionPane.QUESTION_MESSAGE,
            null,
            authors.toArray(),
            authors.get(0)
        );
        
        if (author != null) {
            List<Book> books = bookService.findBooksByAuthor(author);
            String table = TableFormatter.formatBooksTable(books);
            
            JOptionPane.showMessageDialog(null, 
                "Books by author '" + author + "':\n\n" + table, 
                "Books by Author", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Add new book
     */
    private void addNewBook() throws Exception {
        String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:", "Add New Book", JOptionPane.QUESTION_MESSAGE);
        if (isbn == null || isbn.trim().isEmpty()) return;
        
        String title = JOptionPane.showInputDialog(null, "Enter title:", "Add New Book", JOptionPane.QUESTION_MESSAGE);
        if (title == null || title.trim().isEmpty()) return;
        
        String author = JOptionPane.showInputDialog(null, "Enter author:", "Add New Book", JOptionPane.QUESTION_MESSAGE);
        if (author == null || author.trim().isEmpty()) return;
        
        String category = JOptionPane.showInputDialog(null, "Enter category:", "Add New Book", JOptionPane.QUESTION_MESSAGE);
        if (category == null || category.trim().isEmpty()) return;
        
        String totalCopiesStr = JOptionPane.showInputDialog(null, "Enter total copies:", "Add New Book", JOptionPane.QUESTION_MESSAGE);
        if (totalCopiesStr == null || totalCopiesStr.trim().isEmpty()) return;
        
        int totalCopies;
        try {
            totalCopies = Integer.parseInt(totalCopiesStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format for total copies.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String priceStr = JOptionPane.showInputDialog(null, "Enter reference price:", "Add New Book", JOptionPane.QUESTION_MESSAGE);
        if (priceStr == null || priceStr.trim().isEmpty()) return;
        
        BigDecimal price;
        try {
            price = new BigDecimal(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = new Book(isbn, title, author, category, totalCopies, price);
        Book createdBook = bookService.createBook(book);
        
        JOptionPane.showMessageDialog(null, 
            "Book created successfully!\n\n" +
            "ID: " + createdBook.getId() + "\n" +
            "ISBN: " + createdBook.getIsbn() + "\n" +
            "Title: " + createdBook.getTitle() + "\n" +
            "Author: " + createdBook.getAuthor(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Update book
     */
    private void updateBook() throws Exception {
        String idStr = JOptionPane.showInputDialog(null, "Enter book ID to update:", "Update Book", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid book ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = bookService.getBookById(id);
        
        // Usar JTextField para inicializar el valor
        JTextField isbnField = new JTextField(book.getIsbn());
        int isbnResult = JOptionPane.showConfirmDialog(null, isbnField, "Enter ISBN:", JOptionPane.OK_CANCEL_OPTION);
        if (isbnResult != JOptionPane.OK_OPTION || isbnField.getText().trim().isEmpty()) return;
        String isbn = isbnField.getText();

        JTextField titleField = new JTextField(book.getTitle());
        int titleResult = JOptionPane.showConfirmDialog(null, titleField, "Enter title:", JOptionPane.OK_CANCEL_OPTION);
        if (titleResult != JOptionPane.OK_OPTION || titleField.getText().trim().isEmpty()) return;
        String title = titleField.getText();

        JTextField authorField = new JTextField(book.getAuthor());
        int authorResult = JOptionPane.showConfirmDialog(null, authorField, "Enter author:", JOptionPane.OK_CANCEL_OPTION);
        if (authorResult != JOptionPane.OK_OPTION || authorField.getText().trim().isEmpty()) return;
        String author = authorField.getText();

        JTextField categoryField = new JTextField(book.getCategory());
        int categoryResult = JOptionPane.showConfirmDialog(null, categoryField, "Enter category:", JOptionPane.OK_CANCEL_OPTION);
        if (categoryResult != JOptionPane.OK_OPTION || categoryField.getText().trim().isEmpty()) return;
        String category = categoryField.getText();

        JTextField totalCopiesField = new JTextField(String.valueOf(book.getTotalCopies()));
        int totalCopiesResult = JOptionPane.showConfirmDialog(null, totalCopiesField, "Enter total copies:", JOptionPane.OK_CANCEL_OPTION);
        if (totalCopiesResult != JOptionPane.OK_OPTION || totalCopiesField.getText().trim().isEmpty()) return;
        int totalCopies;
        try {
            totalCopies = Integer.parseInt(totalCopiesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format for total copies.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField availableCopiesField = new JTextField(String.valueOf(book.getAvailableCopies()));
        int availableCopiesResult = JOptionPane.showConfirmDialog(null, availableCopiesField, "Enter available copies:", JOptionPane.OK_CANCEL_OPTION);
        if (availableCopiesResult != JOptionPane.OK_OPTION || availableCopiesField.getText().trim().isEmpty()) return;
        int availableCopies;
        try {
            availableCopies = Integer.parseInt(availableCopiesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format for available copies.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField priceField = new JTextField(book.getReferencePrice().toString());
        int priceResult = JOptionPane.showConfirmDialog(null, priceField, "Enter reference price:", JOptionPane.OK_CANCEL_OPTION);
        if (priceResult != JOptionPane.OK_OPTION || priceField.getText().trim().isEmpty()) return;
        BigDecimal price;
        try {
            price = new BigDecimal(priceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] statusOptions = {"Active", "Inactive"};
        String statusChoice = (String) JOptionPane.showInputDialog(null, "Select status:", "Update Book", JOptionPane.QUESTION_MESSAGE, null, statusOptions, book.isActive() ? "Active" : "Inactive");
        if (statusChoice == null) return;

        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(availableCopies);
        book.setReferencePrice(price);
        book.setActive("Active".equals(statusChoice));

        Book updatedBook = bookService.updateBook(book);

        JOptionPane.showMessageDialog(null, 
            "Book updated successfully!\n\n" +
            "ID: " + updatedBook.getId() + "\n" +
            "ISBN: " + updatedBook.getIsbn() + "\n" +
            "Title: " + updatedBook.getTitle() + "\n" +
            "Author: " + updatedBook.getAuthor(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Delete book
     */
    private void deleteBook() throws Exception {
        String idStr = JOptionPane.showInputDialog(null, "Enter book ID to delete:", "Delete Book", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid book ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = bookService.getBookById(id);
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete this book?\n\n" +
            "ISBN: " + book.getIsbn() + "\n" +
            "Title: " + book.getTitle() + "\n" +
            "Author: " + book.getAuthor(), 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = bookService.deleteBook(id);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
