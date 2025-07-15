package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;

import java.util.Optional;
import java.util.UUID;

public interface GenericDao<T> {

    /**
     * Persists the provided entity into the underlying data storage.
     * If the entity does not already have an identifier, a new UUID is generated and assigned.
     *
     * @param entity the entity to be saved must not be null
     */
    void save(T entity);

    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return an {@link Optional} containing the entity if found, otherwise an empty {@link Optional}
     */
    Optional<T> findById(UUID id);

    /**
     * Retrieves all entities in a paginated manner.
     *
     * @param page the zero-based page index.
     * @param size the number of elements to include in each page.
     * @return a {@code PagedResponse<T>} containing the content of the requested page, along with pagination metadata.
     */
    PagedResponse<T> findAll(int page, int size);

    /**
     * Deletes an entity identified by the provided UUID.
     *
     * @param id the unique identifier of the entity to be deleted
     */
    void delete(UUID id);

    /**
     * Updates an entity in the data store with the specified ID.
     *
     * @param id     the unique identifier of the entity to be updated
     * @param entity the updated entity to replace the existing entity
     */
    void update(UUID id, T entity);
}
