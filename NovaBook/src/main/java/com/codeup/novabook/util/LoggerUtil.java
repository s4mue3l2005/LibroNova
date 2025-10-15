package com.codeup.novabook.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Utility class for logging configuration
 */
public class LoggerUtil {
    
    private static final String LOG_FILE = "app.log";
    
    /**
     * Setup application logging
     */
    public static void setupLogging() {
        try {
            // Get root logger
            Logger rootLogger = Logger.getLogger("");
            
            // Remove default console handlers
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }
            
            // Create file handler
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            
            // Create console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.INFO);
            
            // Add handlers to root logger
            rootLogger.addHandler(fileHandler);
            rootLogger.addHandler(consoleHandler);
            rootLogger.setLevel(Level.INFO);
            
            // Set specific logger levels
            Logger.getLogger("com.codeup.novabook").setLevel(Level.INFO);
            
            Logger logger = Logger.getLogger(LoggerUtil.class.getName());
            logger.info("Logging system initialized successfully");
            
        } catch (IOException e) {
            System.err.println("Error setting up logging: " + e.getMessage());
        }
    }
    
    /**
     * Create a logger for a specific class
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
