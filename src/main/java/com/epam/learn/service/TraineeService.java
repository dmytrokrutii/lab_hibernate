package com.epam.learn.service;

import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;

import java.util.List;
import java.util.UUID;

public interface TraineeService {

    /**
     * Creates a new Trainee and persists it in the system.
     *
     * @param trainee the Trainee object to be created and saved. It must contain all the required fields, such as
     *                first name and last name, and will be initialized with a unique username, password, and ID.
     */
    void create(Trainee trainee);


    /**
     * Retrieves a {@link Trainee} by its unique identifier.
     *
     * @param id the unique identifier of the trainee to retrieve
     * @return the {@link Trainee} corresponding to the given identifier
     * @throws EntityNotFoundException if no trainee is found with the provided identifier
     */
    Trainee getById(UUID id);

    /**
     * Retrieves a {@link Trainee} by its username.
     *
     * @param username the username of the trainee to retrieve
     * @return the {@link Trainee} corresponding to the given username, or null if not found
     */
    Trainee getTraineeByUsername(String username);

    /**
     * Retrieves a paginated list of all trainees.
     *
     * @param page the zero-based page index to retrieve.
     * @param size the number of trainees to retrieve per page.
     * @return a paginated response containing a list of trainees, along with pagination details.
     */
    PagedResponse<Trainee> getAll(int page, int size);

    /**
     * Updates the information of an existing trainee based on the provided ID.
     *
     * @param id      the unique identifier of the trainee to be updated
     * @param trainee the updated trainee object containing the new details
     */
    void update(UUID id, Trainee trainee);


    /**
     * Updates the active status of a trainee identified by username.
     *
     * @param username the username of the trainee to update
     * @param status   the new active status
     */
    void updateTraineeStatus(String username, boolean status);


    /**
     * Updates the trainers list for a trainee.
     *
     * @param username the username of the trainee whose trainers list is to be updated
     * @param trainerIds the list of trainer IDs to assign to the trainee
     * @throws IllegalArgumentException if username is null or empty, or if trainerIds is null
     */
    void updateTrainersList(String username, List<UUID> trainerIds);
}
