package com.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * This class contains the main method which is used to run the Spring Boot application.
 * </p>
 * 
 * @author Shivraj.Jadhav
 */
@SpringBootApplication
public class JwtTokenSecurityApplication {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenSecurityApplication.class);

    /**
     * The main method which serves as the entry point for the application.
     * <p>
     * This method initializes and starts the Spring Boot application.
     * </p>
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        logger.info("Starting JwtTokenSecurityApplication...");
        SpringApplication.run(JwtTokenSecurityApplication.class, args);
        logger.info("JwtTokenSecurityApplication started successfully.");
    }
}
