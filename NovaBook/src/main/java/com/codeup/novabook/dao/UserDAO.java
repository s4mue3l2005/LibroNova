package com.codeup.novabook.dao;

import com.codeup.novabook.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User entity
 */
public interface UserDAO {
    
    /**
     * Create a new user
     */
    User create(User user);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(int id);
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Get all users
     */
    List<User> findAll();
    
    /**
     * Update an existing user
     */
    User update(User user);
    
    /**
     * Delete a user by ID
     */
    boolean delete(int id);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
