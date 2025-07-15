package com.epam.learn.service.impl;

import com.epam.learn.dao.TrainerDao;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.service.TraineeService;
import com.epam.learn.service.TrainerService;
import com.epam.learn.service.TrainingTypeService;
import com.epam.learn.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDao;
    private TraineeService traineeService;
    private final AuthService authService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao, AuthService authService, TrainingTypeService trainingTypeService) {
        this.trainerDao = trainerDao;
        this.authService = authService;
        this.trainingTypeService = trainingTypeService;
    }

    @Autowired
    @Lazy
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Override
    @Transactional
    public void create(Trainer trainer) {
        LOGGER.info("create:: creating trainer");

        // Check if trainer has a specialization
        if (trainer.getSpecialization() == null || trainer.getSpecialization().getName() == null) {
            throw new IllegalArgumentException("Trainer specialization is missing or has no name");
        }

        // Find or create the training type
        String specializationName = trainer.getSpecialization().getName();
        trainer.setSpecialization(trainingTypeService.findOrCreate(specializationName));

        // Initialize user and save
        authService.initializeUser(trainer);
        trainerDao.save(trainer);
    }

    @Override
    public Trainer getById(UUID id) {
        return trainerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINER, id));
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        LOGGER.info("getTrainerByUsername:: getting trainer by username: '{}'", username);
        return trainerDao.findByUsername(username)
                .orElse(null);
    }

    @Override
    public PagedResponse<Trainer> getAll(int page, int size) {
        return trainerDao.findAll(page, size);
    }

    @Override
    public void update(UUID id, Trainer updatedTrainer) {
        LOGGER.info("update:: updating trainer with id: '{}'", id);
        Trainer existingTrainer = trainerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINER, id));

        // Handle name change and username regeneration
        authService.handleNameChange(existingTrainer, updatedTrainer);

        updatedTrainer.setId(existingTrainer.getId());
        trainerDao.update(id, updatedTrainer);
    }


    @Override
    public void updateTrainerStatus(String username, boolean status) {
        LOGGER.info("updateTrainerStatus:: updating trainer status for username: '{}' to: {}", username, status);
        Trainer trainer = getTrainerByUsername(username);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with username " + username + " not found");
        }
        trainer.setActive(status);
        update(trainer.getId(), trainer);
    }


    @Override
    public PagedResponse<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername, int page, int size) {
        LOGGER.info("getTrainersNotAssignedToTrainee:: getting trainers not assigned to trainee: '{}'", traineeUsername);

        // Get trainee by username using the TraineeService
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + traineeUsername + " not found");
        }

        // Get trainers not assigned to the trainee
        return trainerDao.findNotAssignedToTrainee(trainee.getId(), page, size);
    }

    @Override
    public PagedResponse<Trainee> getTraineesByTrainerUsername(String trainerUsername, int page, int size) {
        LOGGER.info("getTraineesByTrainerUsername:: getting trainees assigned to trainer: '{}'", trainerUsername);

        // Get trainer by username
        Trainer trainer = getTrainerByUsername(trainerUsername);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with username " + trainerUsername + " not found");
        }

        // Convert the Set<Trainee> to a List<Trainee> for pagination
        List<Trainee> traineesList = new ArrayList<>(trainer.getTrainees());

        // Apply pagination and return the result
        return createPagedResponse(traineesList, page, size);
    }

    /**
     * Creates a paged response from a list of items.
     * This is a helper method to handle pagination logic.
     *
     * @param items the list of items to paginate
     * @param page the zero-based page index
     * @param size the number of items per page
     * @param <T> the type of items in the list
     * @return a paged response containing the requested page of items
     */
    private <T> PagedResponse<T> createPagedResponse(List<T> items, int page, int size) {
        // Calculate total elements and pages
        int totalElements = items.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;

        // Apply pagination
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        // Handle case where fromIndex is out of bounds
        if (fromIndex >= totalElements) {
            return new PagedResponse<>(new ArrayList<>(), page, size, totalElements, totalPages);
        }

        List<T> pagedItems = items.subList(fromIndex, toIndex);

        return new PagedResponse<>(pagedItems, page, size, totalElements, totalPages);
    }
}
