package com.example.security.service;

import org.springframework.stereotype.Service;
import com.example.security.models.User;
import com.example.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing user-related operations.
 * 
 * <p>
 * This service provides methods for interacting with user data, including
 * retrieving all users from the repository.
 * </p>
 * 
 * @author Your Name
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserService} with the specified {@code UserRepository}.
     * 
     * @param userRepository the {@link UserRepository} to be used for accessing user data
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the repository.
     * 
     * <p>
     * This method fetches all users and returns them as a list.
     * </p>
     * 
     * @return a {@link List} of {@link User} entities
     */
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        
        // Fetch all users from the repository and add them to the list
        userRepository.findAll().forEach(users::add);
        
        return users;
    }
}
