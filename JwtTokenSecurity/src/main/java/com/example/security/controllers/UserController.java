package com.example.security.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.security.models.User;
import com.example.security.service.UserService;

/**
 * Controller for managing user-related operations.
 * 
 * <p>
 * This class provides endpoints for retrieving information about the currently
 * authenticated user and for listing all users.
 * </p>
 */
@RequestMapping("/users")
@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	/**
	 * Constructs a new {@link UserController} with the specified
	 * {@link UserService}.
	 *
	 * @param userService the service used for user-related operations
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Retrieves the details of the currently authenticated user.
	 * 
	 * <p>
	 * This endpoint returns the {@link User} object of the currently authenticated
	 * user based on the security context.
	 * </p>
	 *
	 * @return a {@code ResponseEntity} containing the authenticated user's details
	 */
	@GetMapping("/me")
	public ResponseEntity<User> authenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();

		logger.info("Fetching details for authenticated user: {}", currentUser.getEmail());

		return ResponseEntity.ok(currentUser);
	}

	/**
	 * Retrieves a list of all users.
	 * 
	 * <p>
	 * This endpoint returns a list of all users in the system.
	 * </p>
	 *
	 * @return a {@code ResponseEntity} containing a list of all users
	 */
	@GetMapping("/")
	public ResponseEntity<List<User>> allUsers() {
		List<User> users = userService.allUsers();

		logger.info("Fetching details for all users. Total users found: {}", users.size());

		return ResponseEntity.ok(users);
	}
}
