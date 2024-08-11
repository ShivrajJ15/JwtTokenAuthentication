package com.example.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.dtos.LoginResponse;
import com.example.security.dtos.LoginUserDto;
import com.example.security.dtos.RegisterUserDto;
import com.example.security.jwt.AuthenticationService;
import com.example.security.jwt.JwtService;
import com.example.security.models.User;

import jakarta.validation.Valid;

/**
 * Controller for handling authentication and user registration.
 * 
 * <p>
 * Provides end points for user sign up and login, using JWT for authentication.
 * </p>
 * 
 * @author Shivraj.Jadhav
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	private final JwtService jwtService;
	private final AuthenticationService authenticationService;

	/**
	 * Constructs an {@code AuthenticationController} with the specified
	 * {@code JwtService} and {@code AuthenticationService}.
	 *
	 * @param jwtService            the JWT service
	 * @param authenticationService the authentication service
	 */
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	/**
	 * Registers a new user with the provided registration details.
	 *
	 * @param registerUserDto the user registration details. Must include email,
	 *                        password, and full name.
	 * @param bindingResult   the result of the validation process. Contains
	 *                        validation errors, if any.
	 * @return a {@code ResponseEntity} containing the result of the registration
	 *         process.
	 */
	@PostMapping("/signup")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDto registerUserDto,
			BindingResult bindingResult) {
		// Handle validation errors
		if (bindingResult.hasErrors()) {
			StringBuilder errorMessages = new StringBuilder("Validation failed for registration: ");
			bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("; "));

			logger.error("Validation failed for registration: {}", errorMessages.toString());

			// Return bad request with validation errors
			return ResponseEntity.badRequest().body(errorMessages.toString());
		}

		try {
			logger.info("Attempting to register user with email: {}", registerUserDto.getEmail());

			// Perform user registration
			User registeredUser = authenticationService.signup(registerUserDto);

			logger.info("User registered successfully with email: {}", registeredUser.getEmail());

			// Return a success message
			return ResponseEntity.ok("Registration successful for user: " + registeredUser.getEmail());
		} catch (Exception ex) {
			logger.error("Error during user registration for email: {}. Exception: {}", registerUserDto.getEmail(),
					ex.getMessage());

			// Return internal server error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Registration failed due to a server error. Please try again later.");
		}
	}

	/**
	 * Authenticates a user and generates a JWT token.
	 *
	 * @param loginUserDto the user login details.
	 * @return a {@code ResponseEntity} containing the login response with JWT token
	 *         and expiration time.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
		logger.info("Authenticating user (Login) with email: {}", loginUserDto.getEmail());

		try {
			User authenticatedUser = authenticationService.authenticate(loginUserDto);

			if (authenticatedUser == null) {
				logger.warn("Login failed for user: {}", loginUserDto.getEmail());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
			}

			// Generate JWT token and get expiration time
			String jwtToken = jwtService.generateToken(authenticatedUser);
			long expiresIn = jwtService.getExpirationTime();

			logger.info("User authenticated successfully (Login) with email: {}", authenticatedUser.getEmail());

			// Return response with token and expiration time
			LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(expiresIn);

			return ResponseEntity.ok(loginResponse);
		} catch (Exception ex) {
			logger.error("Error during authentication for email: {}. Exception: {}", loginUserDto.getEmail(),
					ex.getMessage());

			// Return internal server error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Authentication failed due to a server error. Please try again later.");
		}
	}

}
