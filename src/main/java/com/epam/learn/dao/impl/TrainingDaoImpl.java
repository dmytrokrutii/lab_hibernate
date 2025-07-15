package com.epam.learn.dao.impl;

import com.epam.learn.dao.AbstractDao;
import com.epam.learn.dao.TrainingDao;
import com.epam.learn.dao.annotation.Dao;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;
import com.epam.learn.util.validate.DaoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * JPA implementation of TrainingDao.
 * This class provides database access for Training entities using JPA.
 */
@Dao
@Slf4j
public class TrainingDaoImpl extends AbstractDao<Training> implements TrainingDao {

    public TrainingDaoImpl(EntityManager entityManager) {
        super(entityManager, Training.class);
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.TRAINING;
    }

    @Override
    public PagedResponse<Training> findByTraineeId(UUID traineeId, int page, int size) {
        LOGGER.debug("Finding trainings by traineeId: {}", traineeId);
        DaoValidator.validateId(traineeId, "Trainee");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("trainee").get("id"), traineeId)
        );
    }

    @Override
    public PagedResponse<Training> findByTrainerId(UUID trainerId, int page, int size) {
        LOGGER.debug("Finding trainings by trainerId: {}", trainerId);
        DaoValidator.validateId(trainerId, "Trainer");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("trainer").get("id"), trainerId)
        );
    }

    @Override
    public PagedResponse<Training> findByDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        LOGGER.debug("Finding trainings by date range: {} to {}", startDate, endDate);
        DaoValidator.validateDateRange(startDate, endDate);
        DaoValidator.validatePagination(page, size);

        // Convert LocalDate to LocalDateTime for comparison
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return createPagedResponse(page, size, (cb, root) -> {
            Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("trainingDate"), startDateTime);
            Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("trainingDate"), endDateTime);
            return cb.and(startDatePredicate, endDatePredicate);
        });
    }

    @Override
    public PagedResponse<Training> findByType(String type, int page, int size) {
        LOGGER.debug("Finding trainings by type: {}", type);
        DaoValidator.validateTrainingType(type);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.equal(root.get("trainingType").get("name"), type)
        );
    }

    @Override
    public PagedResponse<Training> findByTraineeCriteria(UUID traineeId, LocalDate fromDate, LocalDate toDate,
                                                       String trainerName, String trainingType, int page, int size) {
        LOGGER.debug("Finding trainings by trainee criteria: traineeId={}, fromDate={}, toDate={}, trainerName={}, trainingType={}",
                traineeId, fromDate, toDate, trainerName, trainingType);

        DaoValidator.validateId(traineeId, "Trainee");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> {
            // Start with the trainee ID predicate (required)
            Predicate predicate = cb.equal(root.get("trainee").get("id"), traineeId);

            // Add date range predicates if provided
            if (fromDate != null) {
                LocalDateTime fromDateTime = fromDate.atStartOfDay();
                predicate = cb.and(predicate, 
                    cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDateTime));
            }

            if (toDate != null) {
                LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
                predicate = cb.and(predicate, 
                    cb.lessThanOrEqualTo(root.get("trainingDate"), toDateTime));
            }

            // Add trainer name predicate if provided
            if (trainerName != null && !trainerName.isEmpty()) {
                predicate = cb.and(predicate, 
                    cb.or(
                        cb.equal(root.get("trainer").get("firstName"), trainerName),
                        cb.equal(root.get("trainer").get("lastName"), trainerName)
                    ));
            }

            // Add training type predicate if provided
            if (trainingType != null && !trainingType.isEmpty()) {
                predicate = cb.and(predicate, 
                    cb.equal(root.get("trainingType").get("name"), trainingType));
            }

            return predicate;
        });
    }

    @Override
    public PagedResponse<Training> findByTrainerCriteria(UUID trainerId, LocalDate fromDate, LocalDate toDate,
                                                       String traineeName, int page, int size) {
        LOGGER.debug("Finding trainings by trainer criteria: trainerId={}, fromDate={}, toDate={}, traineeName={}",
                trainerId, fromDate, toDate, traineeName);

        DaoValidator.validateId(trainerId, "Trainer");
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> {
            // Start with the trainer ID predicate (required)
            Predicate predicate = cb.equal(root.get("trainer").get("id"), trainerId);

            // Add date range predicates if provided
            if (fromDate != null) {
                LocalDateTime fromDateTime = fromDate.atStartOfDay();
                predicate = cb.and(predicate, 
                    cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDateTime));
            }

            if (toDate != null) {
                LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
                predicate = cb.and(predicate, 
                    cb.lessThanOrEqualTo(root.get("trainingDate"), toDateTime));
            }

            // Add trainee name predicate if provided
            if (traineeName != null && !traineeName.isEmpty()) {
                predicate = cb.and(predicate, 
                    cb.or(
                        cb.equal(root.get("trainee").get("firstName"), traineeName),
                        cb.equal(root.get("trainee").get("lastName"), traineeName)
                    ));
            }

            return predicate;
        });
    }
}
