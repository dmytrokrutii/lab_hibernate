package com.epam.learn.dao.impl;

import com.epam.learn.dao.AbstractUserDao;
import com.epam.learn.dao.TrainerDao;
import com.epam.learn.dao.annotation.Dao;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.util.validate.DaoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * JPA implementation of TrainerDao.
 * This class provides database access for Trainer entities using JPA.
 */
@Dao
@Slf4j
public class TrainerDaoImpl extends AbstractUserDao<Trainer> implements TrainerDao {

    public TrainerDaoImpl(EntityManager entityManager) {
        super(entityManager, Trainer.class);
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.TRAINER;
    }

    @Override
    @Transactional
    public void save(Trainer trainer) {
        LOGGER.debug("Saving trainer: {}", trainer.getUsername());

        try {
            // Check if the trainer already exists
            Trainer existingTrainer = entityManager.find(Trainer.class, trainer.getId());
            if (existingTrainer != null) {
                // Update the existing trainer
                entityManager.merge(trainer);
                LOGGER.debug("Updated existing trainer: {}", trainer.getUsername());
            } else {
                // For new trainers, we need to ensure the User part is saved first
                // and that the specialization exists

                // Check if the specialization exists
                if (trainer.getSpecialization() != null && trainer.getSpecialization().getId() != null) {
                    Object specialization = entityManager.find(
                        trainer.getSpecialization().getClass(), 
                        trainer.getSpecialization().getId()
                    );

                    if (specialization == null) {
                        LOGGER.error("Specialization with ID {} not found", trainer.getSpecialization().getId());
                        throw new IllegalStateException("Specialization with ID " + trainer.getSpecialization().getId() + " not found");
                    }
                }

                // Always use merge for trainers with IDs
                entityManager.merge(trainer);
                LOGGER.debug("Saved new trainer: {}", trainer.getUsername());
            }
        } catch (Exception e) {
            LOGGER.error("Error saving trainer: {}", e.getMessage(), e);
            throw new IllegalStateException("Error saving trainer: " + e.getMessage(), e);
        }
    }

    @Override
    public PagedResponse<Trainer> findBySpecialization(String specialization, int page, int size) {
        LOGGER.debug("Finding trainers by specialization: {}", specialization);
        DaoValidator.validateSpecialization(specialization);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("specialization").get("name"), specialization)
        );
    }

    @Override
    public PagedResponse<Trainer> findByFirstName(String firstName, int page, int size) {
        LOGGER.debug("Finding trainers by firstName: {}", firstName);
        DaoValidator.validateFirstName(firstName);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("firstName"), firstName)
        );
    }

    @Override
    public PagedResponse<Trainer> findByLastName(String lastName, int page, int size) {
        LOGGER.debug("Finding trainers by lastName: {}", lastName);
        DaoValidator.validateLastName(lastName);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("lastName"), lastName)
        );
    }

    @Override
    public PagedResponse<Trainer> findActive(int page, int size) {
        LOGGER.debug("Finding active trainers");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("active"), true)
        );
    }

    @Override
    public PagedResponse<Trainer> findNotAssignedToTrainee(UUID traineeId, int page, int size) {
        LOGGER.debug("Finding trainers not assigned to trainee with ID: {}", traineeId);
        DaoValidator.validateId(traineeId, "Trainee");
        DaoValidator.validatePagination(page, size);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> cq = cb.createQuery(Trainer.class);
        Root<Trainer> root = cq.from(Trainer.class);

        // Create a subquery to find trainers who have trainings with the specified trainee
        Subquery<UUID> subquery = cq.subquery(UUID.class);
        Root<Training> trainingRoot = subquery.from(Training.class);
        subquery.select(trainingRoot.get("trainer").get("id"))
               .where(cb.equal(trainingRoot.get("trainee").get("id"), traineeId));

        // Main query: find trainers whose ID is not in the subquery result
        cq.select(root).where(cb.not(root.get("id").in(subquery)));

        // Count total matching entities
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Trainer> countRoot = countQuery.from(Trainer.class);
        Subquery<UUID> countSubquery = countQuery.subquery(UUID.class);
        Root<Training> countTrainingRoot = countSubquery.from(Training.class);
        countSubquery.select(countTrainingRoot.get("trainer").get("id"))
                    .where(cb.equal(countTrainingRoot.get("trainee").get("id"), traineeId));
        countQuery.select(cb.count(countRoot))
                 .where(cb.not(countRoot.get("id").in(countSubquery)));
        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        // Get paginated results
        TypedQuery<Trainer> query = entityManager.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<Trainer> results = query.getResultList();

        // Calculate total pages
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;

        return new PagedResponse<>(results, page, size, totalElements.intValue(), totalPages);
    }
}
