package com.epam.learn.service;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.TrainingType;

import java.util.UUID;

/**
 * Service for managing TrainingType entities.
 * This service provides operations for creating, retrieving, and managing training types.
 */
public interface TrainingTypeService {

    /**
     * Creates a new TrainingType and persists it in the system.
     *
     * @param trainingType the TrainingType object to be created and saved
     * @return the created TrainingType with its ID assigned
     */
    TrainingType create(TrainingType trainingType);

    /**
     * Retrieves a TrainingType by its unique identifier.
     *
     * @param id the unique identifier of the training type to retrieve
     * @return the TrainingType corresponding to the given identifier
     */
    TrainingType getById(UUID id);

    /**
     * Retrieves a paginated list of all training types.
     *
     * @param page the zero-based page index to retrieve
     * @param size the number of training types to retrieve per page
     * @return a paginated response containing a list of training types, along with pagination details
     */
    PagedResponse<TrainingType> getAll(int page, int size);

    /**
     * Updates the information of an existing training type based on the provided ID.
     *
     * @param id the unique identifier of the training type to be updated
     * @param trainingType the updated training type object containing the new details
     */
    void update(UUID id, TrainingType trainingType);

    /**
     * Finds a TrainingType by its name, ignoring case.
     * If no TrainingType with the given name exists, returns null.
     *
     * @param name the name of the training type to find
     * @return the TrainingType with the given name, or null if not found
     */
    TrainingType getByName(String name);

    /**
     * Finds or creates a TrainingType with the given name.
     * If a TrainingType with the given name already exists (case-insensitive), it is returned.
     * Otherwise, a new TrainingType is created with the given name and returned.
     *
     * @param name the name of the training type to find or create
     * @return the existing or newly created TrainingType
     */
    TrainingType findOrCreate(String name);
}