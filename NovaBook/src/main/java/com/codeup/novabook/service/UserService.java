package com.codeup.novabook.service;

import com.codeup.novabook.model.User;
import java.util.List;

/**
 * Service interface for User operations
 */
public interface UserService {
    
    /**
     * Authenticate user with username and password
     */
    User authenticate(String username, String password) throws Exception;
    
    /**
     * Create a new user with default properties
     */
    User createUser(String username, String password, String email, User.UserRole role) throws Exception;
    
    /**
     * Get user by ID
     */
    User getUserById(int id) throws Exception;
    
    /**
     * Get user by username
     */
    User getUserByUsername(String username) throws Exception;
    
    /**
     * Get all users
     */
    List<User> getAllUsers() throws Exception;
    
    /**
     * Update user information
     */
    User updateUser(User user) throws Exception;
    
    /**
     * Delete user by ID
     */
    boolean deleteUser(int id) throws Exception;
    
    /**
     * Check if username is available
     */
    boolean isUsernameAvailable(String username) throws Exception;
    
    /**
     * Check if email is available
     */
    boolean isEmailAvailable(String email) throws Exception;
}
