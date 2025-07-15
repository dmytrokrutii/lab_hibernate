package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainer;

import java.util.UUID;

public interface TrainerDao extends UserDao<Trainer> {

    /**
     * Retrieves a paginated list of trainers based on their specialization.
     *
     * @param specialization the specialization to search for must not be null or empty
     * @param page           the zero-based page index; must be non-negative
     * @param size           the number of items to include in each page; must be positive
     * @return a {@code PagedResponse<Trainer>} containing the trainers with the specified specialization
     * and pagination metadata
     * @throws IllegalArgumentException if the page is negative or size is not positive
     */
    PagedResponse<Trainer> findBySpecialization(String specialization, int page, int size);

    /**
     * Retrieves a paginated list of trainers with the matching first name.
     *
     * @param firstName the first name to search for must not be null or empty
     * @param page      the zero-based index of the page to retrieve must be non-negative
     * @param size      the number of records to include in each page must be a positive integer
     * @return a {@code PagedResponse<Trainer>} containing the list of trainers with the given first name
     * along with pagination metadata
     */
    PagedResponse<Trainer> findByFirstName(String firstName, int page, int size);

    /**
     * Retrieves a paginated list of trainers whose last name matches the provided value.
     *
     * @param lastName the last name to search for; must not be null or empty
     * @param page     the zero-based index of the page to retrieve; must be non-negative
     * @param size     the number of items per page; must be a positive integer
     * @return a {@code PagedResponse<Trainer>} containing a list of trainers with matching last names
     * and pagination information
     */
    PagedResponse<Trainer> findByLastName(String lastName, int page, int size);

    /**
     * Retrieves a paginated list of active trainers.
     * The active trainers are determined based on specific business logic,
     * such as evaluating their status or other conditions.
     *
     * @param page the zero-based index of the desired page; must be non-negative
     * @param size the number of trainers to retrieve per page; must be a positive integer
     * @return a {@code PagedResponse<Trainer>} containing the list of active trainers for the specified page,
     * along with pagination metadata
     * @throws IllegalArgumentException if the page or size is negative
     */
    PagedResponse<Trainer> findActive(int page, int size);

    /**
     * Retrieves a paginated list of trainers that are not assigned to the specified trainee.
     *
     * @param traineeId the ID of the trainee; must not be null
     * @param page      the zero-based index of the page to retrieve; must be non-negative
     * @param size      the number of trainers to retrieve per page; must be a positive integer
     * @return a {@code PagedResponse<Trainer>} containing the list of trainers not assigned to the trainee,
     * along with pagination metadata
     * @throws IllegalArgumentException if traineeId is null, or if page is negative, or if size is not positive
     */
    PagedResponse<Trainer> findNotAssignedToTrainee(UUID traineeId, int page, int size);
}
