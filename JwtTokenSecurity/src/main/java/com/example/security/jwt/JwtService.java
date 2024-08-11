package com.example.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service for handling JSON Web Tokens (JWTs).
 * <p>
 * Provides functionality to generate, validate, and extract claims from JWTs.
 * </p>
 * 
 * 
 * @author Shivraj.Jadhav
 * 
 */
@Service
public class JwtService {

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	/**
	 * Extracts the username from the given JWT token.
	 *
	 * @param token the JWT token from which to extract the username
	 * @return the username extracted from the token
	 */
	public String extractUsername(String token) {
		logger.debug("Extracting username from token");
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extracts a specific claim from the JWT token.
	 *
	 * @param <T>            the type of the claim
	 * @param token          the JWT token from which to extract the claim
	 * @param claimsResolver function to extract the claim from the Claims object
	 * @return the extracted claim
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		logger.debug("Extracting claim from token");
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Generates a new JWT token for the specified user details.
	 *
	 * @param userDetails the details of the user for whom the token is being
	 *                    generated
	 * @return the generated JWT token
	 */
	public String generateToken(UserDetails userDetails) {
		logger.debug("Generating token for user: {}", userDetails.getUsername());
		return generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * Generates a new JWT token with additional claims for the specified user
	 * details.
	 *
	 * @param extraClaims additional claims to include in the token
	 * @param userDetails the details of the user for whom the token is being
	 *                    generated
	 * @return the generated JWT token
	 */
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		logger.debug("Generating token with extra claims for user: {}", userDetails.getUsername());
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	/**
	 * Retrieves the expiration time for the JWT token.
	 *
	 * @return the expiration time in milliseconds
	 */
	public long getExpirationTime() {
		return jwtExpiration;
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		logger.debug("Building token with expiration time: {}", expiration);
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Checks if the given JWT token is valid.
	 *
	 * @param token       the JWT token to validate
	 * @param userDetails the details of the user to match against the token
	 * @return true if the token is valid, false otherwise
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		logger.debug("Validating token for user: {}", userDetails.getUsername());
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		logger.debug("Checking if token is expired");
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		logger.debug("Extracting expiration date from token");
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		logger.debug("Extracting all claims from token");
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		logger.debug("Getting signing key");
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
