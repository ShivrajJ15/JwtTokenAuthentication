package com.example.security.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * Global exception handler for handling various types of exceptions.
 * 
 * <p>
 * This class captures and handles exceptions thrown during request processing,
 * including validation errors, authentication errors, and method not supported errors.
 * It provides a structured error response and logs details for debugging.
 * </p>
 * 
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Define the logger for this class
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles various types of exceptions and returns a structured error response.
     * 
     * <p>
     * This method provides detailed error messages based on the type of exception
     * and logs the exception stack trace for debugging purposes.
     * </p>
     * 
     * @param exception the {@link Exception} that was thrown
     * @return a {@link ResponseEntity} containing a {@link ProblemDetail} with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        ProblemDetail errorDetail;
        HttpStatus status;
        String description;

        // Determine the appropriate error message and status based on the exception type
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
            description = "The request method is not supported for this endpoint.";
        } else if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            description = "The username or password is incorrect.";
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            description = "The account is locked or disabled.";
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            description = "You do not have permission to access this resource.";
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            description = "The JWT signature is invalid.";
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            description = "The JWT token has expired.";
        } else {
            // For other exceptions, use a generic error message
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            description = "An unexpected error occurred.";
        }

        // Log the exception details for debugging
        logger.error("Exception occurred: {}", exception.getMessage(), exception);

        // Create and return the error detail
        errorDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        errorDetail.setProperty("description", description);

        return ResponseEntity.status(status).body(errorDetail);
    }
}
