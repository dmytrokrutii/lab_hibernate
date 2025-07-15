package com.epam.learn.dao.impl;

import com.epam.learn.dao.AbstractDao;
import com.epam.learn.dao.TrainingTypeDao;
import com.epam.learn.dao.annotation.Dao;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.TrainingType;
import com.epam.learn.util.validate.DaoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * JPA implementation of TrainingTypeDao.
 * This class provides database access for TrainingType entities using JPA.
 */
@Dao
@Slf4j
public class TrainingTypeDaoImpl extends AbstractDao<TrainingType> implements TrainingTypeDao {

    public TrainingTypeDaoImpl(EntityManager entityManager) {
        super(entityManager, TrainingType.class);
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.TRAINING_TYPE;
    }

    @Override
    public Optional<TrainingType> findByNameIgnoreCase(String name) {
        LOGGER.debug("Finding training type by name (ignore case): {}", name);
        DaoValidator.validateName(name, "Training type name");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingType> cq = cb.createQuery(TrainingType.class);
        Root<TrainingType> root = cq.from(TrainingType.class);

        // Use lower() function for case-insensitive comparison
        cq.select(root)
          .where(cb.equal(cb.lower(root.get("name")), name.toLowerCase()));

        TypedQuery<TrainingType> query = entityManager.createQuery(cq);

        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            LOGGER.debug("No training type found with name (ignore case): {}", name);
            return Optional.empty();
        }
    }

    @Override
    public PagedResponse<TrainingType> findByName(String name, int page, int size) {
        LOGGER.debug("Finding training types by name: {}", name);
        DaoValidator.validateName(name, "Training type name");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("name"), name)
        );
    }
}
