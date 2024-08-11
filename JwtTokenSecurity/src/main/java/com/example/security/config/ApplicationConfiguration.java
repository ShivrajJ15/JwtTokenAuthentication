package com.example.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.security.repository.UserRepository;

/**
 * Configuration class for application-specific security settings.
 * 
 * <p>
 * Sets up user details service, password encoder, and authentication provider.
 * </p>
 * 
 * 
 * 
 * @author Shivraj.Jadhav
 */
@Configuration
public class ApplicationConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

	private final UserRepository userRepository;

	/**
	 * Constructs an {@code ApplicationConfiguration} with the specified
	 * {@code UserRepository}.
	 *
	 * @param userRepository the user repository
	 */
	public ApplicationConfiguration(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Provides a {@code UserDetailsService} bean that loads user-specific data.
	 * 
	 * <p>
	 * Uses the {@code UserRepository} to find users by email.
	 * </p>
	 *
	 * @return the {@code UserDetailsService} bean
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		logger.info("Creating UserDetailsService bean");

		UserDetailsService userDetailsService = username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		logger.info("UserDetailsService bean created");

		return userDetailsService;
	}

	/**
	 * Provides a {@code BCryptPasswordEncoder} bean for password encoding.
	 *
	 * @return the {@code BCryptPasswordEncoder} bean
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		logger.info("Creating BCryptPasswordEncoder bean");

		return new BCryptPasswordEncoder();
	}

	/**
	 * Provides an {@code AuthenticationManager} bean.
	 *
	 * <p>
	 * Obtains the authentication manager from the provided
	 * {@code AuthenticationConfiguration}.
	 * </p>
	 *
	 * @param config the {@code AuthenticationConfiguration} to obtain the manager
	 *               from
	 * @return the {@code AuthenticationManager} bean
	 * @throws Exception if an error occurs while obtaining the manager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		logger.info("Creating AuthenticationManager bean");

		return config.getAuthenticationManager();
	}

	/**
	 * Provides an {@code AuthenticationProvider} bean for handling authentication.
	 *
	 * <p>
	 * Configures the provider with the user details service and password encoder.
	 * </p>
	 *
	 * @return the {@code AuthenticationProvider} bean
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		logger.info("Creating AuthenticationProvider bean");

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		logger.info("AuthenticationProvider bean created");

		return authProvider;
	}
}
