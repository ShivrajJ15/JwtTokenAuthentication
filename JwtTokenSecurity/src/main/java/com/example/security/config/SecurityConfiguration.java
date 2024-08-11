package com.example.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.security.jwt.JwtAuthenticationFilter;

import java.util.List;

/**
 * Configuration class for Spring Security.
 * 
 * <p>
 * Configures HTTP security settings, including CORS, session management, and
 * authentication.
 * </p>
 * 
 * 
 * 
 * @author Shivraj.Jadhav
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * Constructs a {@code SecurityConfiguration} with the specified
	 * {@code JwtAuthenticationFilter} and {@code AuthenticationProvider}.
	 *
	 * @param jwtAuthenticationFilter the JWT authentication filter
	 * @param authenticationProvider  the authentication provider
	 */
	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
			AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	/**
	 * Configures the security filter chain for HTTP requests.
	 *
	 * <p>
	 * Disables CSRF protection, configures URL-based authorization, sets session
	 * management policy, and adds custom filters.
	 * </p>
	 *
	 * @param http the {@code HttpSecurity} to configure
	 * @return the configured {@code SecurityFilterChain}
	 * @throws Exception if an error occurs while configuring security
	 */
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("Configuring security filter chain");

		http.csrf().disable().authorizeHttpRequests().requestMatchers("/auth/**").permitAll()
				.requestMatchers("/h2-console/**").permitAll().anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		// Allow H2 console frames
		http.headers().frameOptions().disable();

		logger.info("Security filter chain configured");

		return http.build();
	}

	/**
	 * Configures CORS settings.
	 *
	 * <p>
	 * Sets allowed origins, methods, and headers for CORS requests.
	 * </p>
	 *
	 * @return the configured {@code CorsConfigurationSource}
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		logger.info("Configuring CORS settings");

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:8005"));
		configuration.setAllowedMethods(List.of("GET", "POST"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		logger.info("CORS settings configured");

		return source;
	}
}
