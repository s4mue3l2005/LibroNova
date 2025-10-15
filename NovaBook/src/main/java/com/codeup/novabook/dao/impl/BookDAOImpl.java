package com.codeup.novabook.dao.impl;

import com.codeup.novabook.dao.BookDAO;
import com.codeup.novabook.model.Book;
import com.codeup.novabook.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * JDBC implementation of BookDAO
 */
public class BookDAOImpl implements BookDAO {
    
    private static final Logger logger = Logger.getLogger(BookDAOImpl.class.getName());
    
    @Override
    public Book create(Book book) {
        String sql = "INSERT INTO books (isbn, title, author, category, total_copies, " +
                    "available_copies, reference_price, is_active, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setInt(5, book.getTotalCopies());
            statement.setInt(6, book.getAvailableCopies());
            statement.setBigDecimal(7, book.getReferencePrice());
            statement.setBoolean(8, book.isActive());
            statement.setTimestamp(9, Timestamp.valueOf(book.getCreatedAt()));
            statement.setTimestamp(10, Timestamp.valueOf(book.getUpdatedAt()));
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
            
            logger.info("Book created successfully: " + book.getIsbn());
            return book;
            
        } catch (SQLException e) {
            logger.severe("Error creating book: " + e.getMessage());
            throw new RuntimeException("Error creating book", e);
        }
    }
    
    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToBook(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding book by ID: " + e.getMessage());
            throw new RuntimeException("Error finding book", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, isbn);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToBook(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding book by ISBN: " + e.getMessage());
            throw new RuntimeException("Error finding book", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding all books: " + e.getMessage());
            throw new RuntimeException("Error finding books", e);
        }
        
        return books;
    }
    
    @Override
    public List<Book> findActive() {
        String sql = "SELECT * FROM books WHERE is_active = true ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding active books: " + e.getMessage());
            throw new RuntimeException("Error finding books", e);
        }
        
        return books;
    }
    
    @Override
    public List<Book> findByCategory(String category) {
        String sql = "SELECT * FROM books WHERE category = ? AND is_active = true ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, category);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapResultSetToBook(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding books by category: " + e.getMessage());
            throw new RuntimeException("Error finding books", e);
        }
        
        return books;
    }
    
    @Override
    public List<Book> findByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author = ? AND is_active = true ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, author);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapResultSetToBook(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding books by author: " + e.getMessage());
            throw new RuntimeException("Error finding books", e);
        }
        
        return books;
    }
    
    @Override
    public Book update(Book book) {
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, category = ?, " +
                    "total_copies = ?, available_copies = ?, reference_price = ?, is_active = ?, " +
                    "updated_at = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getCategory());
            statement.setInt(5, book.getTotalCopies());
            statement.setInt(6, book.getAvailableCopies());
            statement.setBigDecimal(7, book.getReferencePrice());
            statement.setBoolean(8, book.isActive());
            statement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(10, book.getId());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating book failed, no rows affected.");
            }
            
            book.setUpdatedAt(LocalDateTime.now());
            logger.info("Book updated successfully: " + book.getIsbn());
            return book;
            
        } catch (SQLException e) {
            logger.severe("Error updating book: " + e.getMessage());
            throw new RuntimeException("Error updating book", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            logger.info("Book deleted: " + (affectedRows > 0));
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.severe("Error deleting book: " + e.getMessage());
            throw new RuntimeException("Error deleting book", e);
        }
    }
    
    @Override
    public boolean existsByIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, isbn);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error checking ISBN existence: " + e.getMessage());
            throw new RuntimeException("Error checking ISBN", e);
        }
        
        return false;
    }
    
    @Override
    public void updateAvailableCopies(int bookId, int newAvailableCopies) {
        String sql = "UPDATE books SET available_copies = ?, updated_at = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, newAvailableCopies);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, bookId);
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating available copies failed, no rows affected.");
            }
            
            logger.info("Available copies updated for book ID: " + bookId);
            
        } catch (SQLException e) {
            logger.severe("Error updating available copies: " + e.getMessage());
            throw new RuntimeException("Error updating available copies", e);
        }
    }
    
    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setCategory(resultSet.getString("category"));
        book.setTotalCopies(resultSet.getInt("total_copies"));
        book.setAvailableCopies(resultSet.getInt("available_copies"));
        book.setReferencePrice(resultSet.getBigDecimal("reference_price"));
        book.setActive(resultSet.getBoolean("is_active"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            book.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            book.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return book;
    }
}
