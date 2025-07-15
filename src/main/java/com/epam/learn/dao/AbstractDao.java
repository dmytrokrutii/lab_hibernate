package com.epam.learn.dao;

import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.util.validate.DaoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Abstract implementation of GenericDao using JPA.
 * This class provides the base implementation for all DAOs that use JPA.
 *
 * @param <T> the entity type
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDao<T> implements GenericDao<T> {

    protected final EntityManager entityManager;
    private final Class<T> entityClass;

    @Override
    @Transactional
    public void save(T entity) {
        LOGGER.debug("Saving entity of type {}", entityClass.getSimpleName());

        try {
            // Special handling for User subclasses (Trainer, Trainee)
            if (entity.getClass().getSuperclass().getSimpleName().equals("User")) {
                // Get the ID
                Object id = entity.getClass().getMethod("getId").invoke(entity);

                if (id != null) {
                    // For entities with IDs, use merge
                    entityManager.merge(entity);
                    LOGGER.debug("User subclass entity merged successfully");
                } else {
                    // For new entities, use persist
                    entityManager.persist(entity);
                    LOGGER.debug("User subclass entity persisted successfully");
                }
            } else {
                // For non-User entities, always use merge
                entityManager.merge(entity);
                LOGGER.debug("Entity merged successfully");
            }
        } catch (Exception e) {
            LOGGER.error("Error saving entity", e);
            throw new IllegalStateException("Error saving entity", e);
        }

        LOGGER.debug("Entity saved successfully");
    }

    @Override
    public Optional<T> findById(UUID id) {
        LOGGER.debug("Finding entity of type {} with id {}", entityClass.getSimpleName(), id);
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    /**
     * Helper method to create a paged response with the given query conditions.
     *
     * @param page the page number
     * @param size the page size
     * @param predicateFunction a BiFunction that creates a Predicate based on the CriteriaBuilder and Root
     * @return a paged response with the query results
     */
    protected PagedResponse<T> createPagedResponse(int page, int size, 
                                                 BiFunction<CriteriaBuilder, Root<T>, Predicate> predicateFunction) {
        DaoValidator.validatePagination(page, size);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Set up main query
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.where(predicateFunction.apply(cb, root));

        // Set up count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicateFunction.apply(cb, countRoot));

        // Execute count query
        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        // Execute main query with pagination
        TypedQuery<T> query = entityManager.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<T> results = query.getResultList();

        // Calculate total pages
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;

        return new PagedResponse<>(results, page, size, totalElements.intValue(), totalPages);
    }

    @Override
    public PagedResponse<T> findAll(int page, int size) {
        LOGGER.debug("Finding all entities of type {} with pagination (page={}, size={})", 
                entityClass.getSimpleName(), page, size);

        return createPagedResponse(page, size, (cb, root) -> cb.conjunction());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        LOGGER.debug("Deleting entity of type {} with id {}", entityClass.getSimpleName(), id);
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
            LOGGER.debug("Entity deleted successfully");
        } else {
            LOGGER.warn("Entity of type {} with id {} not found for deletion", entityClass.getSimpleName(), id);
        }
    }

    @Override
    @Transactional
    public void update(UUID id, T entity) {
        LOGGER.debug("Updating entity of type {} with id {}", entityClass.getSimpleName(), id);
        if (entityManager.find(entityClass, id) == null) {
            throw new EntityNotFoundException(getEntityType(), id);
        }
        entityManager.merge(entity);
        LOGGER.debug("Entity updated successfully");
    }

    /**
     * Gets the entity type for this DAO.
     *
     * @return the entity type
     */
    protected abstract EntityType getEntityType();

    /**
     * Gets the entity class for this DAO.
     *
     * @return the entity class
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }
}
