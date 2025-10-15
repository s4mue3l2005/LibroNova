package com.codeup.novabook.service;

import com.codeup.novabook.exception.AuthenticationException;
import com.codeup.novabook.model.User;
import com.codeup.novabook.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserService
 */
public class UserServiceTest {
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }
    
    @Test
    @DisplayName("Should validate authentication parameters")
    void testAuthenticationValidation() {
        // Test null username
        assertThrows(AuthenticationException.class, () -> {
            userService.authenticate(null, "password");
        });
        
        // Test empty username
        assertThrows(AuthenticationException.class, () -> {
            userService.authenticate("", "password");
        });
        
        // Test null password
        assertThrows(AuthenticationException.class, () -> {
            userService.authenticate("username", null);
        });
        
        // Test empty password
        assertThrows(AuthenticationException.class, () -> {
            userService.authenticate("username", "");
        });
    }
    
    @Test
    @DisplayName("Should validate user creation parameters")
    void testUserCreationValidation() {
        // Test null username
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null, "password", "email@test.com", User.UserRole.ASISTENTE);
        });
        
        // Test empty username
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("", "password", "email@test.com", User.UserRole.ASISTENTE);
        });
        
        // Test null password
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("username", null, "email@test.com", User.UserRole.ASISTENTE);
        });
        
        // Test empty password
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("username", "", "email@test.com", User.UserRole.ASISTENTE);
        });
        
        // Test null email
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("username", "password", null, User.UserRole.ASISTENTE);
        });
        
        // Test empty email
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("username", "password", "", User.UserRole.ASISTENTE);
        });
        
        // Test null role
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("username", "password", "email@test.com", null);
        });
    }
    
    @Test
    @DisplayName("Should validate user retrieval parameters")
    void testUserRetrievalValidation() {
        // Test invalid user ID
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(-1);
        });
        
        // Test null username
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByUsername(null);
        });
        
        // Test empty username
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByUsername("");
        });
    }
    
    @Test
    @DisplayName("Should validate user update parameters")
    void testUserUpdateValidation() throws Exception {
        // Test null user
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null);
        });
        
        // Test user with invalid ID
        User userWithInvalidId = new User();
        userWithInvalidId.setId(0);
        userWithInvalidId.setUsername("test");
        userWithInvalidId.setPassword("password");
        userWithInvalidId.setEmail("test@test.com");
        userWithInvalidId.setRole(User.UserRole.ASISTENTE);
        userWithInvalidId.setStatus(User.UserStatus.ACTIVO);
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(userWithInvalidId);
        });
    }
    
    @Test
    @DisplayName("Should validate user deletion parameters")
    void testUserDeletionValidation() {
        // Test invalid user ID
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(-1);
        });
    }
    
    @Test
    @DisplayName("Should validate username and email availability checks")
    void testUsernameEmailAvailabilityValidation() throws Exception {
        // Test null values
        assertFalse(userService.isUsernameAvailable(null));
        assertFalse(userService.isUsernameAvailable(""));
        assertFalse(userService.isEmailAvailable(null));
        assertFalse(userService.isEmailAvailable(""));
        
        // Test with valid values (assuming no existing users in test DB)
        // These would require a test database setup
        assertDoesNotThrow(() -> {
            boolean usernameAvailable = userService.isUsernameAvailable("testuser");
            assertNotNull(Boolean.valueOf(usernameAvailable));
        });
        
        assertDoesNotThrow(() -> {
            boolean emailAvailable = userService.isEmailAvailable("test@test.com");
            assertNotNull(Boolean.valueOf(emailAvailable));
        });
    }
    
    @Test
    @DisplayName("Should validate user model properties")
    void testUserModelValidation() {
        User user = new User("testuser", "password", "test@test.com", User.UserRole.ASISTENTE, User.UserStatus.ACTIVO);
        
        // Test user creation
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@test.com", user.getEmail());
        assertEquals(User.UserRole.ASISTENTE, user.getRole());
        assertEquals(User.UserStatus.ACTIVO, user.getStatus());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        
        // Test toString method
        String userString = user.toString();
        assertNotNull(userString);
        assertTrue(userString.contains("testuser"));
        assertTrue(userString.contains("ASISTENTE"));
        assertTrue(userString.contains("ACTIVO"));
    }
    
    @Test
    @DisplayName("Should validate user role and status enums")
    void testUserEnumsValidation() {
        // Test UserRole enum
        assertEquals("ADMIN", User.UserRole.ADMIN.name());
        assertEquals("ASISTENTE", User.UserRole.ASISTENTE.name());
        
        // Test UserStatus enum
        assertEquals("ACTIVO", User.UserStatus.ACTIVO.name());
        assertEquals("INACTIVO", User.UserStatus.INACTIVO.name());
        
        // Test enum values
        assertEquals(2, User.UserRole.values().length);
        assertEquals(2, User.UserStatus.values().length);
    }
    
    @Test
    @DisplayName("Should validate user creation with decorator pattern")
    void testUserCreationWithDecorator() throws Exception {
        // Test that default properties are applied
        User user = new User("testuser", "password", "test@test.com", User.UserRole.ASISTENTE, User.UserStatus.ACTIVO);
        
        // Verify default status is set
        assertEquals(User.UserStatus.ACTIVO, user.getStatus());
        
        // Verify timestamps are set
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        
        // Verify timestamps are recent (within last minute)
        assertTrue(java.time.Duration.between(user.getCreatedAt(), java.time.LocalDateTime.now()).toSeconds() < 60);
        assertTrue(java.time.Duration.between(user.getUpdatedAt(), java.time.LocalDateTime.now()).toSeconds() < 60);
    }
    
    @Test
    @DisplayName("Should validate getAllUsers method")
    void testGetAllUsers() throws Exception {
        // This test would require a test database setup
        // For now, we'll test that the method doesn't throw exceptions
        assertDoesNotThrow(() -> {
            java.util.List<User> users = userService.getAllUsers();
            assertNotNull(users);
        });
    }
}
