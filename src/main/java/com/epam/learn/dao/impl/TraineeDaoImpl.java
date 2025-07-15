package com.epam.learn.dao.impl;

import com.epam.learn.dao.AbstractUserDao;
import com.epam.learn.dao.TraineeDao;
import com.epam.learn.dao.annotation.Dao;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.util.validate.DaoValidator;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * JPA implementation of TraineeDao.
 * This class provides database access for Trainee entities using JPA.
 */
@Dao
@Slf4j
public class TraineeDaoImpl extends AbstractUserDao<Trainee> implements TraineeDao {

    public TraineeDaoImpl(EntityManager entityManager) {
        super(entityManager, Trainee.class);
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.TRAINEE;
    }

    @Override
    public PagedResponse<Trainee> findByFirstName(String firstName, int page, int size) {
        LOGGER.debug("Finding trainees by firstName: {}", firstName);
        DaoValidator.validateFirstName(firstName);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("firstName"), firstName)
        );
    }

    @Override
    public PagedResponse<Trainee> findByLastName(String lastName, int page, int size) {
        LOGGER.debug("Finding trainees by lastName: {}", lastName);
        DaoValidator.validateLastName(lastName);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("lastName"), lastName)
        );
    }

    @Override
    public PagedResponse<Trainee> findActive(int page, int size) {
        LOGGER.debug("Finding active trainees");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("active"), true)
        );
    }

    @Override
    @Transactional
    public void updateTrainers(UUID traineeId, Set<Trainer> trainers) {
        LOGGER.debug("Updating trainers for trainee with id: {}", traineeId);
        DaoValidator.validateId(traineeId, "Trainee");

        Trainee trainee = entityManager.find(Trainee.class, traineeId);
        if (trainee == null) {
            throw new EntityNotFoundException(getEntityType(), traineeId);
        }

        // Clear existing trainers and add new ones
        trainee.getTrainers().clear();
        if (trainers != null) {
            trainee.getTrainers().addAll(trainers);
        }

        entityManager.merge(trainee);
        LOGGER.debug("Trainers updated successfully for trainee with id: {}", traineeId);
    }
}
