package com.example.security.models;

import java.util.Collection;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a user in the system, implementing Spring Security's
 * {@link UserDetails} interface.
 * <p>
 * This class is mapped to the "users" table in the database and contains
 * user-related information such as full name, email, and password. It also
 * includes timestamps for creation and updates.
 * </p>
 * 
 * @author Shivraj.Jadhav
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Integer id;

	@Column(nullable = false)
	private String fullName;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private Date createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Date updatedAt;

	/**
	 * Gets the unique identifier for the user.
	 * 
	 * @return the user ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the user.
	 * 
	 * @param id the user ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the full name of the user.
	 * 
	 * @return the full name of the user
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name of the user.
	 * 
	 * @param fullName the full name of the user
	 * @return the updated User object
	 */
	public User setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	/**
	 * Gets the email of the user.
	 * 
	 * @return the email of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the user.
	 * 
	 * @param email the email of the user
	 * @return the updated User object
	 */
	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Gets the password of the user.
	 * 
	 * @return the password of the user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the user.
	 * 
	 * @param password the password of the user
	 * @return the updated User object
	 */
	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Gets the creation timestamp of the user record.
	 * 
	 * @return the creation timestamp
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the creation timestamp of the user record.
	 * 
	 * @param createdAt the creation timestamp
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the last update timestamp of the user record.
	 * 
	 * @return the last update timestamp
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Sets the last update timestamp of the user record.
	 * 
	 * @param updatedAt the last update timestamp
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Returns the authorities granted to the user. This implementation does not
	 * grant any authorities.
	 * 
	 * @return a collection of granted authorities (empty in this case)
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	/**
	 * Returns the username of the user, which is represented by the user's email.
	 * 
	 * @return the email of the user
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/**
	 * Returns whether the user's account is expired. This implementation does not
	 * check for expiration.
	 * 
	 * @return true if the account is expired, false otherwise
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Returns whether the user is locked. This implementation does not check for
	 * locked status.
	 * 
	 * @return true if the user is not locked, false otherwise
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Returns whether the user's credentials (password) are expired. This
	 * implementation does not check for expiration.
	 * 
	 * @return true if the credentials are not expired, false otherwise
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Returns whether the user is enabled. This implementation always returns true.
	 * 
	 * @return true if the user is enabled, false otherwise
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
