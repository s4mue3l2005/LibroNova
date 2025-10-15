package com.codeup.novabook.dao.impl;

import com.codeup.novabook.dao.LoanDAO;
import com.codeup.novabook.model.Loan;
import com.codeup.novabook.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * JDBC implementation of LoanDAO
 */
public class LoanDAOImpl implements LoanDAO {
    
    private static final Logger logger = Logger.getLogger(LoanDAOImpl.class.getName());
    
    @Override
    public Loan create(Loan loan) {
        String sql = "INSERT INTO loans (partner_id, book_id, loan_date, due_date, return_date, " +
                    "fine_amount, status, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, loan.getPartnerId());
            statement.setInt(2, loan.getBookId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, Date.valueOf(loan.getDueDate()));
            
            if (loan.getReturnDate() != null) {
                statement.setDate(5, Date.valueOf(loan.getReturnDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }
            
            statement.setBigDecimal(6, loan.getFineAmount());
            statement.setString(7, loan.getStatus().name());
            statement.setTimestamp(8, Timestamp.valueOf(loan.getCreatedAt()));
            statement.setTimestamp(9, Timestamp.valueOf(loan.getUpdatedAt()));
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating loan failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating loan failed, no ID obtained.");
                }
            }
            
            logger.info("Loan created successfully: ID " + loan.getId());
            return loan;
            
        } catch (SQLException e) {
            logger.severe("Error creating loan: " + e.getMessage());
            throw new RuntimeException("Error creating loan", e);
        }
    }
    
    @Override
    public Optional<Loan> findById(int id) {
        String sql = "SELECT * FROM loans WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToLoan(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding loan by ID: " + e.getMessage());
            throw new RuntimeException("Error finding loan", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Loan> findAll() {
        String sql = "SELECT * FROM loans ORDER BY loan_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding all loans: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public List<Loan> findActive() {
        String sql = "SELECT * FROM loans WHERE status = 'ACTIVO' ORDER BY due_date";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding active loans: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public List<Loan> findOverdue() {
        String sql = "SELECT * FROM loans WHERE status = 'ACTIVO' AND due_date < CURDATE() ORDER BY due_date";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding overdue loans: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public List<Loan> findByPartnerId(int partnerId) {
        String sql = "SELECT * FROM loans WHERE partner_id = ? ORDER BY loan_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, partnerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    loans.add(mapResultSetToLoan(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding loans by partner ID: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public List<Loan> findByBookId(int bookId) {
        String sql = "SELECT * FROM loans WHERE book_id = ? ORDER BY loan_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, bookId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    loans.add(mapResultSetToLoan(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding loans by book ID: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public List<Loan> findAllWithDetails() {
        String sql = "SELECT l.*, p.first_name, p.last_name, b.title, b.isbn " +
                    "FROM loans l " +
                    "JOIN partners p ON l.partner_id = p.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "ORDER BY l.loan_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Loan loan = mapResultSetToLoan(resultSet);
                loan.setPartnerName(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                loan.setBookTitle(resultSet.getString("title"));
                loan.setBookIsbn(resultSet.getString("isbn"));
                loans.add(loan);
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding loans with details: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public List<Loan> findOverdueWithDetails() {
        String sql = "SELECT l.*, p.first_name, p.last_name, b.title, b.isbn " +
                    "FROM loans l " +
                    "JOIN partners p ON l.partner_id = p.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "WHERE l.status = 'ACTIVO' AND l.due_date < CURDATE() " +
                    "ORDER BY l.due_date";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Loan loan = mapResultSetToLoan(resultSet);
                loan.setPartnerName(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                loan.setBookTitle(resultSet.getString("title"));
                loan.setBookIsbn(resultSet.getString("isbn"));
                loans.add(loan);
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding overdue loans with details: " + e.getMessage());
            throw new RuntimeException("Error finding loans", e);
        }
        
        return loans;
    }
    
    @Override
    public Loan update(Loan loan) {
        String sql = "UPDATE loans SET partner_id = ?, book_id = ?, loan_date = ?, due_date = ?, " +
                    "return_date = ?, fine_amount = ?, status = ?, updated_at = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, loan.getPartnerId());
            statement.setInt(2, loan.getBookId());
            statement.setDate(3, Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, Date.valueOf(loan.getDueDate()));
            
            if (loan.getReturnDate() != null) {
                statement.setDate(5, Date.valueOf(loan.getReturnDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }
            
            statement.setBigDecimal(6, loan.getFineAmount());
            statement.setString(7, loan.getStatus().name());
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(9, loan.getId());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating loan failed, no rows affected.");
            }
            
            loan.setUpdatedAt(LocalDateTime.now());
            logger.info("Loan updated successfully: ID " + loan.getId());
            return loan;
            
        } catch (SQLException e) {
            logger.severe("Error updating loan: " + e.getMessage());
            throw new RuntimeException("Error updating loan", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            logger.info("Loan deleted: " + (affectedRows > 0));
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.severe("Error deleting loan: " + e.getMessage());
            throw new RuntimeException("Error deleting loan", e);
        }
    }
    
    private Loan mapResultSetToLoan(ResultSet resultSet) throws SQLException {
        Loan loan = new Loan();
        loan.setId(resultSet.getInt("id"));
        loan.setPartnerId(resultSet.getInt("partner_id"));
        loan.setBookId(resultSet.getInt("book_id"));
        loan.setLoanDate(resultSet.getDate("loan_date").toLocalDate());
        loan.setDueDate(resultSet.getDate("due_date").toLocalDate());
        
        Date returnDate = resultSet.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }
        
        loan.setFineAmount(resultSet.getBigDecimal("fine_amount"));
        loan.setStatus(Loan.LoanStatus.valueOf(resultSet.getString("status")));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            loan.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            loan.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return loan;
    }
}
