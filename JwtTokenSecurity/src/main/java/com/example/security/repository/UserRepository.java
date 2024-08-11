package com.example.security.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.example.security.models.User;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends {@link CrudRepository} to provide CRUD operations for {@link User}
 * entities.
 * </p>
 * 
 * @author Shivraj.Jadhav
 * 
 */
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * Finds a user by their email address.
	 * 
	 * @param email the email address of the user
	 * @return an {@link Optional} containing the user if found, otherwise
	 *         {@link Optional#empty()}
	 */
	Optional<User> findByEmail(String email);
}
