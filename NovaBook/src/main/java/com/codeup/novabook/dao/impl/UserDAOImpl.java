package com.codeup.novabook.dao.impl;

import com.codeup.novabook.dao.UserDAO;
import com.codeup.novabook.model.User;
import com.codeup.novabook.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * JDBC implementation of UserDAO
 */
public class UserDAOImpl implements UserDAO {
    
    private static final Logger logger = Logger.getLogger(UserDAOImpl.class.getName());
    
    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (username, password, email, role, status, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole().name());
            statement.setString(5, user.getStatus().name());
            statement.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));
            statement.setTimestamp(7, Timestamp.valueOf(user.getUpdatedAt()));
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            logger.info("User created successfully: " + user.getUsername());
            return user;
            
        } catch (SQLException e) {
            logger.severe("Error creating user: " + e.getMessage());
            throw new RuntimeException("Error creating user", e);
        }
    }
    
    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding user by ID: " + e.getMessage());
            throw new RuntimeException("Error finding user", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding user by username: " + e.getMessage());
            throw new RuntimeException("Error finding user", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding user by email: " + e.getMessage());
            throw new RuntimeException("Error finding user", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
            
        } catch (SQLException e) {
            logger.severe("Error finding all users: " + e.getMessage());
            throw new RuntimeException("Error finding users", e);
        }
        
        return users;
    }
    
    @Override
    public User update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ?, " +
                    "status = ?, updated_at = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole().name());
            statement.setString(5, user.getStatus().name());
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(7, user.getId());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            logger.info("User updated successfully: " + user.getUsername());
            return user;
            
        } catch (SQLException e) {
            logger.severe("Error updating user: " + e.getMessage());
            throw new RuntimeException("Error updating user", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            logger.info("User deleted: " + (affectedRows > 0));
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.severe("Error deleting user: " + e.getMessage());
            throw new RuntimeException("Error deleting user", e);
        }
    }
    
    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error checking username existence: " + e.getMessage());
            throw new RuntimeException("Error checking username", e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
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
    
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setRole(User.UserRole.valueOf(resultSet.getString("role")));
        user.setStatus(User.UserStatus.valueOf(resultSet.getString("status")));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}
