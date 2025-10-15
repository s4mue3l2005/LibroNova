package com.codeup.novabook.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for database connections
 */
public class DatabaseConnection {
    
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final Properties config = new Properties();
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    
    static {
        loadConfiguration();
    }
    
    /**
     * Load database configuration from properties file
     */
    private static void loadConfiguration() {
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                logger.severe("Unable to find config.properties file");
                throw new RuntimeException("Configuration file not found");
            }
            
            config.load(input);
            dbUrl = config.getProperty("db.url");
            dbUser = config.getProperty("db.user");
            dbPassword = config.getProperty("db.password");
            
            logger.info("Database configuration loaded successfully");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    /**
     * Get a new database connection
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            logger.info("Database connection established");
            return connection;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error establishing database connection", e);
            throw e;
        }
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Get configuration property
     */
    public static String getProperty(String key) {
        return config.getProperty(key);
    }
    
    /**
     * Get configuration property with default value
     */
    public static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }
}
