package com.example.security.jwt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.security.dtos.LoginUserDto;
import com.example.security.dtos.RegisterUserDto;
import com.example.security.models.User;
import com.example.security.repository.UserRepository;

/**
 * Service class responsible for user authentication and registration.
 * 
 * <p>
 * This service handles user registration by encoding passwords and saving user
 * details to the repository. It also manages user authentication by validating
 * credentials and retrieving user details.
 * </p>
 * 
 * <p>
 * It interacts with {@link UserRepository} to perform CRUD operations on user
 * data, and with {@link AuthenticationManager} for authentication purposes.
 * </p>
 * 
 * @author Shivraj.Jadhav
 */
@Service
public class AuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	/**
	 * Constructs an instance of {@code AuthenticationService}.
	 *
	 * @param userRepository        the user repository to interact with user data
	 * @param authenticationManager the authentication manager to handle
	 *                              authentication
	 * @param passwordEncoder       the password encoder to encode passwords
	 */
	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Registers a new user with the given details.
	 *
	 * <p>
	 * This method creates a new user, encodes the password, and saves the user to
	 * the repository.
	 * </p>
	 *
	 * @param input the user registration details
	 * @return the saved {@link User} entity
	 */
	public User signup(RegisterUserDto input) {
		logger.info("Registering new user with email: {}", input.getEmail());

		User user = new User().setFullName(input.getFullName()).setEmail(input.getEmail())
				.setPassword(passwordEncoder.encode(input.getPassword()));

		User savedUser = userRepository.save(user);

		logger.info("User registered successfully with email: {}", savedUser.getEmail());
		return savedUser;
	}

	/**
	 * Authenticates a user with the given login details.
	 *
	 * <p>
	 * This method verifies the user's credentials and returns the authenticated
	 * user.
	 * </p>
	 *
	 * @param input the login credentials
	 * @return the authenticated {@link User} entity
	 * @throws RuntimeException if authentication fails or user is not found
	 */
	public User authenticate(LoginUserDto input) {
		try {
			logger.info("Attempting to authenticate user with email: {}", input.getEmail());

			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

			User user = userRepository.findByEmail(input.getEmail())
					.orElseThrow(() -> new RuntimeException("User not found"));

			logger.info("User authenticated successfully with email: {}", user.getEmail());
			return user;
		} catch (Exception ex) {
			logger.error("Authentication failed for email: {}. Exception: {}", input.getEmail(), ex.getMessage());
			throw new RuntimeException("Authentication failed", ex);
		}
	}

}
