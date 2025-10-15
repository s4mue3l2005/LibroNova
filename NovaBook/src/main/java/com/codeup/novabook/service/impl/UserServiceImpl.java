package com.codeup.novabook.service.impl;

import com.codeup.novabook.dao.UserDAO;
import com.codeup.novabook.dao.impl.UserDAOImpl;
import com.codeup.novabook.exception.AuthenticationException;
import com.codeup.novabook.model.User;
import com.codeup.novabook.service.UserService;
import com.codeup.novabook.util.DatabaseConnection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service implementation for User operations
 */
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User authenticate(String username, String password) throws Exception {
        logger.info("POST /auth/login - Attempting authentication for user: " + username);
        
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username is required");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password is required");
        }
        
        Optional<User> userOptional = userDAO.findByUsername(username);
        if (userOptional.isEmpty()) {
            logger.warning("Authentication failed: User not found - " + username);
            throw new AuthenticationException("Invalid username or password");
        }
        
        User user = userOptional.get();
        
        // Simple password validation (in real application, use hashed passwords)
        if (!password.equals(user.getPassword())) {
            logger.warning("Authentication failed: Invalid password for user - " + username);
            throw new AuthenticationException("Invalid username or password");
        }
        
        if (user.getStatus() != User.UserStatus.ACTIVO) {
            logger.warning("Authentication failed: User inactive - " + username);
            throw new AuthenticationException("User account is inactive");
        }
        
        logger.info("Authentication successful for user: " + username + " with role: " + user.getRole());
        return user;
    }
    
    @Override
    public User createUser(String username, String password, String email, User.UserRole role) throws Exception {
        logger.info("POST /users - Creating new user: " + username);
        
        // Validate input parameters
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }
        
        // Check if username already exists
        if (userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        
        // Check if email already exists
        if (userDAO.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        // Create user with default properties (decorator pattern)
        User user = new User(username, password, email, role, User.UserStatus.ACTIVO);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User createdUser = userDAO.create(user);
        logger.info("User created successfully: " + createdUser.getUsername() + " with role: " + createdUser.getRole());
        
        return createdUser;
    }
    
    @Override
    public User getUserById(int id) throws Exception {
        logger.info("GET /users/" + id + " - Retrieving user by ID");
        
        Optional<User> userOptional = userDAO.findById(id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        return userOptional.get();
    }
    
    @Override
    public User getUserByUsername(String username) throws Exception {
        logger.info("GET /users/username/" + username + " - Retrieving user by username");
        
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        Optional<User> userOptional = userDAO.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
        
        return userOptional.get();
    }
    
    @Override
    public List<User> getAllUsers() throws Exception {
        logger.info("GET /users - Retrieving all users");
        
        return userDAO.findAll();
    }
    
    @Override
    public User updateUser(User user) throws Exception {
        
        
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        logger.info("PATCH /users/" + user.getId() + " - Updating user");
        
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
        
        // Check if user exists
        Optional<User> existingUserOptional = userDAO.findById(user.getId());
        if (existingUserOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }
        
        // Check username uniqueness if changed
        User existingUser = existingUserOptional.get();
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userDAO.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        // Check email uniqueness if changed
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userDAO.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userDAO.update(user);
        logger.info("User updated successfully: " + updatedUser.getUsername());
        
        return updatedUser;
    }
    
    @Override
    public boolean deleteUser(int id) throws Exception {
        logger.info("DELETE /users/" + id + " - Deleting user");
        
        if (id <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
        
        // Check if user exists
        Optional<User> userOptional = userDAO.findById(id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        boolean deleted = userDAO.delete(id);
        if (deleted) {
            logger.info("User deleted successfully: ID " + id);
        } else {
            logger.warning("Failed to delete user: ID " + id);
        }
        
        return deleted;
    }
    
    @Override
    public boolean isUsernameAvailable(String username) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return !userDAO.existsByUsername(username);
    }
    
    @Override
    public boolean isEmailAvailable(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return !userDAO.existsByEmail(email);
    }
}
