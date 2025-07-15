package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;

import java.util.Set;
import java.util.UUID;

public interface TraineeDao extends UserDao<Trainee> {

    /**
     * Retrieves a paginated list of trainees by their first name.
     *
     * @param firstName the first name to search for must not be null or empty
     * @param page      the zero-based page index
     * @param size      the number of records to include in each page
     * @return a {@code PagedResponse<Trainee>} containing the list of trainees with the matching first name
     * along with pagination metadata
     */
    PagedResponse<Trainee> findByFirstName(String firstName, int page, int size);

    /**
     * Retrieves a paginated list of trainees whose last name matches the provided value.
     *
     * @param lastName the last name to search for; must not be null or empty
     * @param page     the zero-based index of the page to retrieve; must be non-negative
     * @param size     the number of items per page; must be a positive integer
     * @return a {@code PagedResponse<Trainee>} containing a list of trainees with matching last names
     * and pagination information
     */
    PagedResponse<Trainee> findByLastName(String lastName, int page, int size);

    /**
     * Retrieves a paginated list of active trainees.
     * <p>
     * An active trainee is determined by evaluating the trainee's status (e.g., {@code isActive()}).
     * The list is divided into pages based on the specified page index and size.
     *
     * @param page the zero-based index of the desired page; must be non-negative
     * @param size the number of items to include in each page; must be non-negative
     * @return a {@code PagedResponse<Trainee>} containing the active trainees for the specified page, along with pagination metadata
     * @throws IllegalArgumentException if the page or size is negative
     */
    PagedResponse<Trainee> findActive(int page, int size);

    /**
     * Updates the trainers list for a trainee.
     *
     * @param traineeId the ID of the trainee whose trainers list is to be updated
     * @param trainers  the new set of trainers for the trainee
     * @throws IllegalArgumentException if traineeId is null
     */
    void updateTrainers(UUID traineeId, Set<Trainer> trainers);
}
