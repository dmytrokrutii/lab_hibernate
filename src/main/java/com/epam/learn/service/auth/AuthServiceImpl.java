package com.epam.learn.service.auth;

import com.epam.learn.dao.TraineeDao;
import com.epam.learn.dao.TrainerDao;
import com.epam.learn.exception.UserInitializationException;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.model.user.User;
import com.epam.learn.util.validate.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the AuthService interface.
 * This service provides functionality for user authentication, password management,
 * and user initialization.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    @Override
    public String generateUsername(String firstName, String lastName) {
        UserValidator.validateFirstName(firstName);
        UserValidator.validateLastName(lastName);

        LOGGER.debug("generateUsername:: generating username for new user");
        String baseUsername = (firstName + "." + lastName).toLowerCase();

        // Check if username exists
        int counter = 0;
        String username = baseUsername;
        boolean usernameExists = true;

        while (usernameExists) {
            // Check if username exists in trainee or trainer
            Optional<Trainee> trainee = traineeDao.findByUsername(username);
            Optional<Trainer> trainer = trainerDao.findByUsername(username);

            if (trainee.isEmpty() && trainer.isEmpty()) {
                usernameExists = false;
            } else {
                counter++;
                username = baseUsername + counter;
            }
        }

        return username;
    }

    @Override
    public String generateRandomPassword() {
        LOGGER.debug("generateRandomPassword:: generating random password");
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    @Override
    public void initializeUser(User user) {
        if (user == null) {
            throw new UserInitializationException("User cannot be null");
        }

        LOGGER.debug("initializeUser:: initializing user");

        String generatedUsername = generateUsername(user.getFirstName(), user.getLastName());
        String generatedPassword = generateRandomPassword();

        user.setUsername(generatedUsername);
        user.setPassword(generatedPassword);
    }

    @Override
    public boolean authenticate(String username, String password) {
        UserValidator.validateUsername(username);
        UserValidator.validatePassword(password);

        LOGGER.debug("authenticate:: authenticating user with username: {}", username);

        // Check trainee users with exact username
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if (trainee.isPresent() && password.equals(trainee.get().getPassword())) {
            LOGGER.debug("authenticate:: trainee authentication successful");
            return true;
        }

        // Check trainer users with exact username
        Optional<Trainer> trainer = trainerDao.findByUsername(username);
        if (trainer.isPresent() && password.equals(trainer.get().getPassword())) {
            LOGGER.debug("authenticate:: trainer authentication successful");
            return true;
        }

        LOGGER.debug("authenticate:: authentication failed for username: {}", username);
        return false;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        UserValidator.validateUsername(username);
        UserValidator.validatePassword(oldPassword);
        UserValidator.validatePassword(newPassword);

        LOGGER.debug("changePassword:: changing password for user with username: {}", username);

        // Authenticate user first
        if (!authenticate(username, oldPassword)) {
            LOGGER.debug("changePassword:: authentication failed for username: {}", username);
            return false;
        }

        // Validate new password strength
        if (!isPasswordStrong(newPassword)) {
            LOGGER.debug("changePassword:: new password is not strong enough");
            return false;
        }

        // Update trainee password
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if (trainee.isPresent()) {
            Trainee traineeToUpdate = trainee.get();
            traineeToUpdate.setPassword(newPassword);
            traineeDao.update(traineeToUpdate.getId(), traineeToUpdate);
            LOGGER.debug("changePassword:: trainee password updated successfully");
            return true;
        }

        // Update trainer password
        Optional<Trainer> trainer = trainerDao.findByUsername(username);
        if (trainer.isPresent()) {
            Trainer trainerToUpdate = trainer.get();
            trainerToUpdate.setPassword(newPassword);
            trainerDao.update(trainerToUpdate.getId(), trainerToUpdate);
            LOGGER.debug("changePassword:: trainer password updated successfully");
            return true;
        }

        LOGGER.debug("changePassword:: user not found with username: {}", username);
        return false;
    }

    @Override
    public boolean isPasswordStrong(String password) {
        return UserValidator.isPasswordStrong(password);
    }

    /**
     * Checks if the user's name has changed and updates the username if necessary.
     *
     * @param existingUser the existing user from the database
     * @param updatedUser  the updated user with potentially new name
     */
    public void handleNameChange(User existingUser, User updatedUser) {
        // Check if first name or last name has changed
        boolean nameChanged = !existingUser.getFirstName().equals(updatedUser.getFirstName()) || 
                             !existingUser.getLastName().equals(updatedUser.getLastName());

        // If name changed, regenerate username
        if (nameChanged) {
            String newUsername = generateUsername(updatedUser.getFirstName(), updatedUser.getLastName());
            LOGGER.info("handleNameChange:: regenerating username from '{}' to '{}' due to name change", 
                       existingUser.getUsername(), newUsername);
            updatedUser.setUsername(newUsername);
        } else {
            // Preserve existing username if name hasn't changed
            updatedUser.setUsername(existingUser.getUsername());
        }
    }

    /**
     * Finds a similar username in the database when an exact match fails.
     * This method checks for common typos like transposed letters.
     *
     * @param username the username to find similar matches for
     * @return a corrected username if found, or null if no similar username exists
     */
    private String findSimilarUsername(String username) {
        if (username == null || username.length() < 3) {
            return null;
        }

        // Check for transposed letters (e.g., "jonh.doe" instead of "john.doe")
        for (int i = 0; i < username.length() - 1; i++) {
            // Create a new username with adjacent characters swapped
            char[] chars = username.toCharArray();
            char temp = chars[i];
            chars[i] = chars[i + 1];
            chars[i + 1] = temp;
            String transposedUsername = new String(chars);

            // Check if this transposed username exists
            Optional<Trainee> trainee = traineeDao.findByUsername(transposedUsername);
            if (trainee.isPresent()) {
                LOGGER.debug("findSimilarUsername:: found similar trainee username: {}", transposedUsername);
                return transposedUsername;
            }

            Optional<Trainer> trainer = trainerDao.findByUsername(transposedUsername);
            if (trainer.isPresent()) {
                LOGGER.debug("findSimilarUsername:: found similar trainer username: {}", transposedUsername);
                return transposedUsername;
            }
        }

        return null;
    }
}
