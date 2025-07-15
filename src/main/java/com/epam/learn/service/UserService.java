package com.epam.learn.service;

import com.epam.learn.exception.InvalidUserDataException;
import com.epam.learn.model.user.User;

public interface UserService {

    /**
     * Generates a unique username based on the given first name and last name. If other users with the same
     * first name and last name already exist, it appends a numerical value to ensure uniqueness.
     *
     * @param firstName the first name of the user; must not be null or empty
     * @param lastName  the last name of the user; must not be null or empty
     * @return a unique username generated using the provided first name and last name
     * @throws InvalidUserDataException if either the first name or last name is null or empty
     */
    String generateUsername(String firstName, String lastName);

    /**
     * Generates a random password string.
     * The generated password is a randomly created string, suitable for use as a
     * temporary or initial password for a user.
     *
     * @return a randomly generated password as a string
     */
    String generateRandomPassword();

    /**
     * Initializes a user by setting a unique username, a randomly generated password,
     * and a unique identifier if not already present.
     *
     * @param user the user to be initialized. Must contain the first name and last name
     *             to generate the username. If the ID is null, a new unique ID will
     *             be assigned to the user.
     */
    void initializeUser(User user);

    /**
     * Authenticates a user by verifying the provided username and password.
     *
     * @param username the username of the user to authenticate
     * @param password the password to verify
     * @return true if the authentication is successful, false otherwise
     * @throws IllegalArgumentException if username or password is null or empty
     */
    boolean authenticate(String username, String password);

    /**
     * Changes the password for a user.
     *
     * @param username    the username of the user whose password is to be changed
     * @param oldPassword the current password of the user
     * @param newPassword the new password to set
     * @return true if the password was successfully changed, false otherwise
     * @throws IllegalArgumentException if any of the parameters is null or empty
     */
    boolean changePassword(String username, String oldPassword, String newPassword);

    /**
     * Validates if a password meets the strength requirements.
     * A strong password should have:
     * - At least 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     *
     * @param password the password to validate
     * @return true if the password meets the strength requirements, false otherwise
     */
    boolean isPasswordStrong(String password);
}
