package com.codeup.novabook.controller;

import com.codeup.novabook.exception.AuthenticationException;
import com.codeup.novabook.model.User;
import com.codeup.novabook.service.UserService;
import com.codeup.novabook.service.impl.UserServiceImpl;
import com.codeup.novabook.util.TableFormatter;

import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for User operations with JOptionPane interface
 */
public class UserController {
    
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;
    private User currentUser;
    
    public UserController() {
        this.userService = new UserServiceImpl();
    }
    
    /**
     * Show login dialog
     */
    public User showLoginDialog() {
        while (true) {
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            
            JLabel titleLabel = new JLabel("LibroNova Login");
            titleLabel.setFont(titleLabel.getFont().deriveFont(16.0f));
            loginPanel.add(titleLabel);
            loginPanel.add(Box.createVerticalStrut(10));
            
            JTextField usernameField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);
            
            loginPanel.add(new JLabel("Username:"));
            loginPanel.add(usernameField);
            loginPanel.add(Box.createVerticalStrut(5));
            loginPanel.add(new JLabel("Password:"));
            loginPanel.add(passwordField);
            
            int option = JOptionPane.showConfirmDialog(
                null,
                loginPanel,
                "Login - LibroNova",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return null;
            }
            
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter both username and password.", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            try {
                User user = userService.authenticate(username, password);
                this.currentUser = user;
                
                JOptionPane.showMessageDialog(null, 
                    "Welcome, " + user.getUsername() + "!\n" +
                    "Role: " + user.getRole() + "\n" +
                    "Status: " + user.getStatus(), 
                    "Login Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                return user;
                
            } catch (AuthenticationException e) {
                JOptionPane.showMessageDialog(null, 
                    "Authentication failed: " + e.getMessage(), 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error during login: " + e.getMessage(), 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Error during login: " + e.getMessage());
            }
        }
    }
    
    /**
     * Show user management menu (admin only)
     */
    public void showUserMenu() {
        if (currentUser == null || currentUser.getRole() != User.UserRole.ADMIN) {
            JOptionPane.showMessageDialog(null, 
                "Access denied. Admin privileges required.", 
                "Access Denied", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] options = {
            "1. List all users",
            "2. Add new user",
            "3. Update user",
            "4. Delete user",
            "5. Back to main menu"
        };
        
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "User Management (Admin)\n\nSelect an option:",
                "LibroNova - User Management",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice == null || choice.contains("5")) {
                break;
            }
            
            try {
                if (choice.contains("1")) {
                    listAllUsers();
                } else if (choice.contains("2")) {
                    addNewUser();
                } else if (choice.contains("3")) {
                    updateUser();
                } else if (choice.contains("4")) {
                    deleteUser();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                logger.severe("Error in user management: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        this.currentUser = null;
        JOptionPane.showMessageDialog(null, 
            "Logged out successfully.", 
            "Logout", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * List all users
     */
    private void listAllUsers() throws Exception {
        List<User> users = userService.getAllUsers();
        String table = TableFormatter.formatUsersTable(users);
        
        JOptionPane.showMessageDialog(null, 
            "All Users:\n\n" + table, 
            "All Users", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Add new user
     */
    private void addNewUser() throws Exception {
        String username = JOptionPane.showInputDialog(null, "Enter username:", "Add New User", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) return;
        
        String password = JOptionPane.showInputDialog(null, "Enter password:", "Add New User", JOptionPane.QUESTION_MESSAGE);
        if (password == null || password.trim().isEmpty()) return;
        
        String email = JOptionPane.showInputDialog(null, "Enter email:", "Add New User", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;
        
        String[] roleOptions = {"ADMIN", "ASISTENTE"};
        String roleChoice = (String) JOptionPane.showInputDialog(null, "Select role:", "Add New User", JOptionPane.QUESTION_MESSAGE, null, roleOptions, "ASISTENTE");
        if (roleChoice == null) return;
        
        User.UserRole role = User.UserRole.valueOf(roleChoice);
        
        User user = userService.createUser(username, password, email, role);
        
        JOptionPane.showMessageDialog(null, 
            "User created successfully!\n\n" +
            "ID: " + user.getId() + "\n" +
            "Username: " + user.getUsername() + "\n" +
            "Email: " + user.getEmail() + "\n" +
            "Role: " + user.getRole() + "\n" +
            "Status: " + user.getStatus(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Update user
     */
    private void updateUser() throws Exception {
        String idStr = JOptionPane.showInputDialog(null, "Enter user ID to update:", "Update User", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userService.getUserById(id);
        
        String username = (String) JOptionPane.showInputDialog(null, "Enter username:", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, user.getUsername());
        if (username == null || username.trim().isEmpty()) return;
        
        String password = (String) JOptionPane.showInputDialog(null, "Enter password:", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, "");
        if (password == null) return;
        
        String email = (String) JOptionPane.showInputDialog(null, "Enter email:", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, user.getEmail());
        if (email == null || email.trim().isEmpty()) return;
        
        String[] roleOptions = {"ADMIN", "ASISTENTE"};
        String roleChoice = (String) JOptionPane.showInputDialog(null, "Select role:", "Update User", JOptionPane.QUESTION_MESSAGE, null, roleOptions, user.getRole().name());
        if (roleChoice == null) return;
        
        String[] statusOptions = {"ACTIVO", "INACTIVO"};
        String statusChoice = (String) JOptionPane.showInputDialog(null, "Select status:", "Update User", JOptionPane.QUESTION_MESSAGE, null, statusOptions, user.getStatus().name());
        if (statusChoice == null) return;
        
        user.setUsername(username);
        if (!password.trim().isEmpty()) {
            user.setPassword(password);
        }
        user.setEmail(email);
        user.setRole(User.UserRole.valueOf(roleChoice));
        user.setStatus(User.UserStatus.valueOf(statusChoice));
        
        User updatedUser = userService.updateUser(user);
        
        JOptionPane.showMessageDialog(null, 
            "User updated successfully!\n\n" +
            "ID: " + updatedUser.getId() + "\n" +
            "Username: " + updatedUser.getUsername() + "\n" +
            "Email: " + updatedUser.getEmail() + "\n" +
            "Role: " + updatedUser.getRole() + "\n" +
            "Status: " + updatedUser.getStatus(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Delete user
     */
    private void deleteUser() throws Exception {
        String idStr = JOptionPane.showInputDialog(null, "Enter user ID to delete:", "Delete User", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userService.getUserById(id);
        
        // Prevent deleting current user
        if (currentUser != null && currentUser.getId() == id) {
            JOptionPane.showMessageDialog(null, 
                "Cannot delete your own account.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete this user?\n\n" +
            "Username: " + user.getUsername() + "\n" +
            "Email: " + user.getEmail() + "\n" +
            "Role: " + user.getRole(), 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
