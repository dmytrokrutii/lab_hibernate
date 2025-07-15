package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;

import java.time.LocalDate;
import java.util.UUID;

public interface TrainingDao extends GenericDao<Training> {

    /**
     * Retrieves a paginated list of trainings associated with the specified trainee ID.
     *
     * @param traineeId the unique identifier of the trainee; must not be null
     * @param page      the zero-based page index; must be non-negative
     * @param size      the number of records to include in each page; must be positive
     * @return a {@code PagedResponse<Training>} containing the trainings for the specified trainee
     * along with pagination metadata
     * @throws IllegalArgumentException if traineeId is null, or if page or size are invalid
     */
    PagedResponse<Training> findByTraineeId(UUID traineeId, int page, int size);

    /**
     * Retrieves a paginated list of trainings associated with the specified trainer ID.
     *
     * @param trainerId the unique identifier of the trainer whose trainings are to be retrieved; must not be null
     * @param page      the zero-based page index; must be non-negative
     * @param size      the number of trainings to retrieve per page; must be a positive integer
     * @return a {@code PagedResponse<Training>} containing the list of trainings conducted by the specified trainer,
     * along with pagination metadata
     * @throws IllegalArgumentException if the page index is negative or the size is not positive
     */
    PagedResponse<Training> findByTrainerId(UUID trainerId, int page, int size);

    /**
     * Retrieves a paginated list of training sessions occurring within the specified date range.
     *
     * @param startDate the start date of the range to query for; must not be null
     * @param endDate   the end date of the range to query for; must not be null
     * @param page      the zero-based index of the page to retrieve; must be non-negative
     * @param size      the number of records to include in each page; must be a positive integer
     * @return a {@code PagedResponse<Training>} containing the training sessions within the specified date range
     * along with pagination metadata
     * @throws IllegalArgumentException if startDate or endDate is null, if the page is negative, or if size is not positive
     */
    PagedResponse<Training> findByDateRange(LocalDate startDate, LocalDate endDate, int page, int size);

    /**
     * Retrieves a paginated list of trainings based on the specified training type.
     *
     * @param type the type of training to search for must not be null or empty
     * @param page the zero-based index of the desired page; must be non-negative
     * @param size the number of items to include in each page; must be a positive integer
     * @return a {@code PagedResponse<Training>} containing the trainings of the specified type
     * along with pagination metadata
     * @throws IllegalArgumentException if the type is null or empty, or if the page is negative, or size is not positive
     */
    PagedResponse<Training> findByType(String type, int page, int size);

    /**
     * Retrieves a paginated list of trainings for a trainee based on multiple criteria.
     *
     * @param traineeId    the ID of the trainee whose trainings to retrieve; must not be null
     * @param fromDate     the start date for filtering trainings; can be null
     * @param toDate       the end date for filtering trainings; can be null
     * @param trainerName  the name of the trainer for filtering trainings; can be null or empty
     * @param trainingType the type of training for filtering trainings; can be null or empty
     * @param page         the zero-based index of the page to retrieve; must be non-negative
     * @param size         the number of records to include in each page; must be a positive integer
     * @return a {@code PagedResponse<Training>} containing the trainings that match the criteria
     * along with pagination metadata
     * @throws IllegalArgumentException if traineeId is null, or if page is negative, or if size is not positive
     */
    PagedResponse<Training> findByTraineeCriteria(UUID traineeId, LocalDate fromDate, LocalDate toDate,
                                                 String trainerName, String trainingType, int page, int size);

    /**
     * Retrieves a paginated list of trainings for a trainer based on multiple criteria.
     *
     * @param trainerId the ID of the trainer whose trainings to retrieve; must not be null
     * @param fromDate  the start date for filtering trainings; can be null
     * @param toDate    the end date for filtering trainings; can be null
     * @param traineeName the name of the trainee for filtering trainings; can be null or empty
     * @param page      the zero-based index of the page to retrieve; must be non-negative
     * @param size      the number of records to include in each page; must be a positive integer
     * @return a {@code PagedResponse<Training>} containing the trainings that match the criteria
     * along with pagination metadata
     * @throws IllegalArgumentException if trainerId is null, or if page is negative, or if size is not positive
     */
    PagedResponse<Training> findByTrainerCriteria(UUID trainerId, LocalDate fromDate, LocalDate toDate,
                                                 String traineeName, int page, int size);
}
