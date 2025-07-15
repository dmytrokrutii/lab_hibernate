package com.epam.learn.util.validate;

import com.epam.learn.exception.InvalidUserDataException;
import com.epam.learn.model.user.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for validating user data.
 * This class provides methods for validating user input, such as names and passwords.
 */
@Slf4j
public class UserValidator {

    // Regular expression for password validation
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    /**
     * Validates that the first name is not null or empty.
     *
     * @param firstName the first name to validate
     * @throws InvalidUserDataException if the first name is null or empty
     */
    public static void validateFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new InvalidUserDataException("First name cannot be empty");
        }
    }

    /**
     * Validates that the last name is not null or empty.
     *
     * @param lastName the last name to validate
     * @throws InvalidUserDataException if the last name is null or empty
     */
    public static void validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new InvalidUserDataException("Last name cannot be empty");
        }
    }

    /**
     * Validates that the username is not null or empty.
     *
     * @param username the username to validate
     * @throws IllegalArgumentException if the username is null or empty
     */
    public static void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
    }

    /**
     * Validates that the password is not null or empty.
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if the password is null or empty
     */
    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    /**
     * Validates that the user object is not null.
     *
     * @param user the user object to validate
     * @throws IllegalArgumentException if the user is null
     */
    public static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    /**
     * Checks if a password meets the strength requirements.
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
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        return password.matches(PASSWORD_PATTERN);
    }
}