package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.TrainingType;

import java.util.Optional;

/**
 * Data Access Object for TrainingType entities.
 * This interface defines operations for accessing and manipulating TrainingType data.
 */
public interface TrainingTypeDao extends GenericDao<TrainingType> {

    /**
     * Finds a TrainingType by its name, ignoring case.
     *
     * @param name the name of the training type to find
     * @return an Optional containing the TrainingType if found, or an empty Optional if not found
     * @throws IllegalArgumentException if name is null or empty
     */
    Optional<TrainingType> findByNameIgnoreCase(String name);

    /**
     * Retrieves a paginated list of training types by their name.
     *
     * @param name the name to search for; must not be null or empty
     * @param page the zero-based page index
     * @param size the number of records to include in each page
     * @return a {@code PagedResponse<TrainingType>} containing the list of training types with the matching name
     * along with pagination metadata
     */
    PagedResponse<TrainingType> findByName(String name, int page, int size);
}