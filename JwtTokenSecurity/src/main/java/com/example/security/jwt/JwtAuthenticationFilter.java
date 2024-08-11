package com.example.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Filter for JWT-based authentication.
 * <p>
 * This filter intercepts HTTP requests to check for a JWT token in the
 * "Authorization" header. If a valid token is found, it authenticates the user
 * and sets the security context accordingly.
 * </p>
 * 
 * @author Shivraj.Jadhav
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final HandlerExceptionResolver handlerExceptionResolver;

	/**
	 * Constructs an instance of {@code JwtAuthenticationFilter}.
	 *
	 * @param jwtService               the JWT service to handle token operations
	 * @param userDetailsService       the service to load user details
	 * @param handlerExceptionResolver the exception resolver for handling errors
	 */
	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	/**
	 * Processes the incoming HTTP request to perform JWT authentication.
	 *
	 * <p>
	 * This method extracts the JWT token from the "Authorization" header, validates
	 * it, and sets the authentication in the security context if the token is
	 * valid.
	 * </p>
	 *
	 * @param request     the HTTP request
	 * @param response    the HTTP response
	 * @param filterChain the filter chain to pass the request and response to the
	 *                    next filter
	 * @throws ServletException if an error occurs during processing
	 * @throws IOException      if an I/O error occurs during processing
	 */
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.debug("No JWT token found in the Authorization header or token does not start with Bearer.");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtService.extractUsername(jwt);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (userEmail != null && authentication == null) {
				logger.debug("JWT token found for user: {}", userEmail);

				UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

				if (jwtService.isTokenValid(jwt, userDetails)) {
					logger.debug("JWT token is valid for user: {}", userEmail);

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					logger.warn("Invalid JWT token for user: {}", userEmail);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			logger.error("Error occurred during JWT authentication", exception);
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}
	}
}
