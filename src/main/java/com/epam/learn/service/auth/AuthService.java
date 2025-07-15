package com.epam.learn.service.auth;

import com.epam.learn.model.user.User;
import com.epam.learn.service.UserService;

/**
 * Service for authentication and user management operations.
 * This service provides functionality for user authentication, password management,
 * and user initialization.
 */
public interface AuthService extends UserService {
    /**
     * Checks if the user's name has changed and updates the username if necessary.
     *
     * @param existingUser the existing user from the database
     * @param updatedUser  the updated user with potentially new name
     */
    void handleNameChange(User existingUser, User updatedUser);
}
