package com.epam.learn.service;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;

import java.util.UUID;

public interface TrainerService {

    /**
     * Creates a new Trainer and persists it in the system.
     *
     * @param trainer the Trainer object to be created and saved. It must contain all the necessary fields
     *                such as first name and last name, and will be initialized with a unique username,
     *                password, and ID.
     */
    void create(Trainer trainer);


    /**
     * Retrieves a Trainer by its unique identifier.
     *
     * @param id the unique identifier of the trainer to retrieve
     * @return the Trainer corresponding to the given identifier
     */
    Trainer getById(UUID id);

    /**
     * Retrieves a Trainer by its username.
     *
     * @param username the username of the trainer to retrieve
     * @return the Trainer corresponding to the given username, or null if not found
     */
    Trainer getTrainerByUsername(String username);

    /**
     * Retrieves a paginated list of all trainers.
     *
     * @param page the zero-based page index to retrieve
     * @param size the number of trainers to retrieve per page
     * @return a paginated response containing a list of trainers, along with pagination details
     */
    PagedResponse<Trainer> getAll(int page, int size);

    /**
     * Updates the information of an existing trainer based on the provided ID.
     *
     * @param id      the unique identifier of the trainer to be updated
     * @param trainer the updated trainer object containing new details
     */
    void update(UUID id, Trainer trainer);


    /**
     * Updates the active status of a trainer identified by username.
     *
     * @param username the username of the trainer to update
     * @param status   the new active status
     */
    void updateTrainerStatus(String username, boolean status);


    /**
     * Gets a list of trainers that are not assigned to the specified trainee.
     *
     * @param traineeUsername the username of the trainee
     * @param page            the zero-based page index
     * @param size            the number of trainers per page
     * @return a paginated response containing the trainers not assigned to the trainee
     */
    PagedResponse<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername, int page, int size);

    /**
     * Gets a list of trainees assigned to the specified trainer.
     *
     * @param trainerUsername the username of the trainer
     * @param page            the zero-based page index
     * @param size            the number of trainees per page
     * @return a paginated response containing the trainees assigned to the trainer
     */
    PagedResponse<Trainee> getTraineesByTrainerUsername(String trainerUsername, int page, int size);
}
