package com.example.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for transferring registration details of a user.
 * 
 * <p>
 * This class encapsulates the email, password, and full name required for user
 * registration.
 * </p>
 * 
 * @author Shivraj.Jadhav
 */
public class RegisterUserDto {

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Full name is required")
	private String fullName;

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

	/**
	 * Gets the full name of the user.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name of the user.
	 *
	 * @param fullName the full name
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
