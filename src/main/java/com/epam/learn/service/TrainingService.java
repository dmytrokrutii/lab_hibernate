package com.epam.learn.service;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;

import java.time.LocalDate;
import java.util.UUID;

public interface TrainingService {


    void create(Training training);

    /**
     * Retrieves a Training object by its unique identifier.
     *
     * @param id the unique identifier of the training to retrieve
     * @return the Training corresponding to the given identifier
     */
    Training getById(UUID id);

    /**
     * Retrieves a paginated list of all trainings.
     *
     * @param page the zero-based page index to retrieve
     * @param size the number of trainings to retrieve per page
     * @return a paginated response containing a list of trainings, along with pagination details
     */
    PagedResponse<Training> getAll(int page, int size);

    /**
     * Updates the information of an existing training based on its unique identifier.
     *
     * @param id              the unique identifier of the training to be updated
     * @param updatedTraining the updated training object containing the new details
     */
    void update(UUID id, Training updatedTraining);

    /**
     * Deletes an entity identified by the specified UUID.
     *
     * @param id the unique identifier of the entity to be deleted
     */
    void delete(UUID id);

    /**
     * Gets a list of trainings for a trainee based on specified criteria.
     *
     * @param traineeUsername the username of the trainee
     * @param fromDate        the start date for filtering trainings (optional)
     * @param toDate          the end date for filtering trainings (optional)
     * @param trainerName     the name of the trainer for filtering trainings (optional)
     * @param trainingType    the type of training for filtering trainings (optional)
     * @param page            the zero-based page index
     * @param size            the number of trainings per page
     * @return a paginated response containing the filtered trainings
     */
    PagedResponse<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                              String trainerName, String trainingType, int page, int size);

    /**
     * Gets a list of trainings for a trainer based on specified criteria.
     *
     * @param trainerUsername the username of the trainer
     * @param fromDate        the start date for filtering trainings (optional)
     * @param toDate          the end date for filtering trainings (optional)
     * @param traineeName     the name of the trainee for filtering trainings (optional)
     * @param page            the zero-based page index
     * @param size            the number of trainings per page
     * @return a paginated response containing the filtered trainings
     */
    PagedResponse<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                              String traineeName, int page, int size);
}
