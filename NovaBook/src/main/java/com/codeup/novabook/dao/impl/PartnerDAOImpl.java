package com.codeup.novabook.dao.impl;

import com.codeup.novabook.dao.PartnerDAO;
import com.codeup.novabook.model.Partner;
import com.codeup.novabook.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * JDBC implementation of PartnerDAO
 */
public class PartnerDAOImpl implements PartnerDAO {
    
    private static final Logger logger = Logger.getLogger(PartnerDAOImpl.class.getName());
    
    @Override
    public Partner create(Partner partner) {
        String sql = "INSERT INTO partners (document_number, first_name, last_name, email, " +
                    "phone, address, status, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, partner.getDocumentNumber());
            statement.setString(2, partner.getFirstName());
            statement.setString(3, partner.getLastName());
            statement.setString(4, partner.getEmail());
            statement.setString(5, partner.getPhone());
            statement.setString(6, partner.getAddress());
            statement.setString(7, partner.getStatus().name());
            statement.setTimestamp(8, Timestamp.valueOf(partner.getCreatedAt()));
            statement.setTimestamp(9, Timestamp.valueOf(partner.getUpdatedAt()));
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating partner failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    partner.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating partner failed, no ID obtained.");
                }
            }
            
            logger.info("Partner created successfully: " + partner.getFullName());
            return partner;
            
        } catch (SQLException e) {
            logger.severe("Error creating partner: " + e.getMessage());
            throw new RuntimeException("Error creating partner", e);
        }
    }
    
    @Override
    public Optional<Partner> findById(int id) {
        String sql = "SELECT * FROM partners WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToPartner(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding partner by ID: " + e.getMessage());
            throw new RuntimeException("Error finding partner", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Partner> findByDocumentNumber(String documentNumber) {
        String sql = "SELECT * FROM partners WHERE document_number = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, documentNumber);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToPartner(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding partner by document number: " + e.getMessage());
            throw new RuntimeException("Error finding partner", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Partner> findByEmail(String email) {
        String sql = "SELECT * FROM partners WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToPartner(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding partner by email: " + e.getMessage());
            throw new RuntimeException("Error finding partner", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Partner> findAll() {
        String sql = "SELECT * FROM partners ORDER BY first_name, last_name";
        List<Partner> partners = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                partners.add(mapResultSetToPartner(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding all partners: " + e.getMessage());
            throw new RuntimeException("Error finding partners", e);
        }
        
        return partners;
    }
    
    @Override
    public List<Partner> findActive() {
        String sql = "SELECT * FROM partners WHERE status = 'ACTIVO' ORDER BY first_name, last_name";
        List<Partner> partners = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                partners.add(mapResultSetToPartner(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding active partners: " + e.getMessage());
            throw new RuntimeException("Error finding partners", e);
        }
        
        return partners;
    }
    
    @Override
    public Partner update(Partner partner) {
        String sql = "UPDATE partners SET document_number = ?, first_name = ?, last_name = ?, " +
                    "email = ?, phone = ?, address = ?, status = ?, updated_at = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, partner.getDocumentNumber());
            statement.setString(2, partner.getFirstName());
            statement.setString(3, partner.getLastName());
            statement.setString(4, partner.getEmail());
            statement.setString(5, partner.getPhone());
            statement.setString(6, partner.getAddress());
            statement.setString(7, partner.getStatus().name());
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(9, partner.getId());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating partner failed, no rows affected.");
            }
            
            partner.setUpdatedAt(LocalDateTime.now());
            logger.info("Partner updated successfully: " + partner.getFullName());
            return partner;
            
        } catch (SQLException e) {
            logger.severe("Error updating partner: " + e.getMessage());
            throw new RuntimeException("Error updating partner", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM partners WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            logger.info("Partner deleted: " + (affectedRows > 0));
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.severe("Error deleting partner: " + e.getMessage());
            throw new RuntimeException("Error deleting partner", e);
        }
    }
    
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        String sql = "SELECT COUNT(*) FROM partners WHERE document_number = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, documentNumber);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error checking document number existence: " + e.getMessage());
            throw new RuntimeException("Error checking document number", e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM partners WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error checking email existence: " + e.getMessage());
            throw new RuntimeException("Error checking email", e);
        }
        
        return false;
    }
    
    private Partner mapResultSetToPartner(ResultSet resultSet) throws SQLException {
        Partner partner = new Partner();
        partner.setId(resultSet.getInt("id"));
        partner.setDocumentNumber(resultSet.getString("document_number"));
        partner.setFirstName(resultSet.getString("first_name"));
        partner.setLastName(resultSet.getString("last_name"));
        partner.setEmail(resultSet.getString("email"));
        partner.setPhone(resultSet.getString("phone"));
        partner.setAddress(resultSet.getString("address"));
        partner.setStatus(Partner.PartnerStatus.valueOf(resultSet.getString("status")));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            partner.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            partner.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return partner;
    }
}
