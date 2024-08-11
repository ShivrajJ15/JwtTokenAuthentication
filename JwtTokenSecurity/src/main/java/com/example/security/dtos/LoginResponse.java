package com.example.security.dtos;

/**
 * DTO for representing the response of a login operation.
 * 
 * <p>
 * This class contains the authentication token and its expiration time.
 * </p>
 * 
 * 
 * @author Shivraj.Jadhav
 */
public class LoginResponse {

	private String token;
	private long expiresIn;
	//private String message;

	/**
	 * Gets the authentication token.
	 *
	 * @return the authentication token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the authentication token.
	 *
	 * @param token the authentication token
	 * @return the {@code LoginResponse} instance
	 */
	public LoginResponse setToken(String token) {
		this.token = token;
		return this;
	}

	/**
	 * Gets the expiration time of the token in seconds.
	 *
	 * @return the expiration time
	 */
	public long getExpiresIn() {
		return expiresIn;
	}

	/**
	 * Sets the expiration time of the token in seconds.
	 *
	 * @param expiresIn the expiration time
	 * @return the {@code LoginResponse} instance
	 */
	public LoginResponse setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
		return this;
	}

//	public String getMessage() {
//		return message;
//	}

//	public LoginResponse setMessage(String message) {
//		this.message = message;
//		return this;
//	}
	
}
