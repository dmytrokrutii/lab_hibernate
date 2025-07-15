package com.epam.learn.util.validate;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Utility class for validating DAO input parameters.
 * This class provides methods for validating input parameters for DAO operations.
 */
@Slf4j
public class DaoValidator {

    /**
     * Validates that the first name is not null or empty.
     *
     * @param firstName the first name to validate
     * @throws IllegalArgumentException if the first name is null or empty
     */
    public static void validateFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
    }

    /**
     * Validates that the last name is not null or empty.
     *
     * @param lastName the last name to validate
     * @throws IllegalArgumentException if the last name is null or empty
     */
    public static void validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
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
     * Validates that the specialization is not null or empty.
     *
     * @param specialization the specialization to validate
     * @throws IllegalArgumentException if the specialization is null or empty
     */
    public static void validateSpecialization(String specialization) {
        if (specialization == null || specialization.isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be null or empty");
        }
    }

    /**
     * Validates that the ID is not null.
     *
     * @param id the ID to validate
     * @param entityName the name of the entity (for error message)
     * @throws IllegalArgumentException if the ID is null
     */
    public static void validateId(UUID id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " ID cannot be null");
        }
    }

    /**
     * Validates pagination parameters.
     *
     * @param page the page number
     * @param size the page size
     * @throws IllegalArgumentException if page or size is negative
     */
    public static void validatePagination(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be non-negative");
        }
        if (size < 0) {
            throw new IllegalArgumentException("Size must be non-negative");
        }
    }

    /**
     * Validates that the training type is not null or empty.
     *
     * @param type the training type to validate
     * @throws IllegalArgumentException if the type is null or empty
     */
    public static void validateTrainingType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Training type cannot be null or empty");
        }
    }

    /**
     * Validates that the date range is valid.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @throws IllegalArgumentException if either date is null or if the start date is after the end date
     */
    public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    /**
     * Validates that the name is not null or empty.
     *
     * @param name the name to validate
     * @param fieldName the name of the field (for error message)
     * @throws IllegalArgumentException if the name is null or empty
     */
    public static void validateName(String name, String fieldName) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
}
