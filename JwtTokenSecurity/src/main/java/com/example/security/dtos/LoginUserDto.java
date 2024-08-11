package com.example.security.dtos;

/**
 * DTO for transferring login credentials.
 * 
 * <p>
 * This class encapsulates the email and password required for user
 * authentication.
 * </p>
 * 
 * 
 * 
 * @author Shivraj.Jadhav
 */
public class LoginUserDto {

	private String email;
	private String password;

	/**
	 * Gets the email address of the user.
	 *
	 * @return the email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address of the user.
	 *
	 * @param email the email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the password of the user.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the user.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
