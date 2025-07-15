package com.epam.learn.service.impl;

import com.epam.learn.dao.TraineeDao;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.service.TraineeService;
import com.epam.learn.service.TrainerService;
import com.epam.learn.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDao;
    private final @Lazy TrainerService trainerService;
    private final AuthService authService;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void create(Trainee trainee) {
        LOGGER.info("create:: creating trainee");
        authService.initializeUser(trainee);
        traineeDao.save(trainee);
    }

    @Override
    public Trainee getById(UUID id) {
        return traineeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINEE, id));
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        LOGGER.info("getTraineeByUsername:: getting trainee by username: '{}'", username);
        return traineeDao.findByUsername(username)
                .orElse(null);
    }

    @Override
    public PagedResponse<Trainee> getAll(int page, int size) {
        return traineeDao.findAll(page, size);
    }

    @Override
    public void update(UUID id, Trainee updatedTrainee) {
        LOGGER.info("update:: updating trainee with id: '{}'", id);
        Trainee existingTrainee = traineeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINEE, id));

        // Handle name change and username regeneration
        authService.handleNameChange(existingTrainee, updatedTrainee);

        updatedTrainee.setId(existingTrainee.getId());
        traineeDao.update(id, updatedTrainee);
    }

    @Override
    public void updateTraineeStatus(String username, boolean status) {
        LOGGER.info("updateTraineeStatus:: updating trainee status for username: '{}' to: {}", username, status);
        Trainee trainee = getTraineeByUsername(username);
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + username + " not found");
        }
        trainee.setActive(status);
        update(trainee.getId(), trainee);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional
    public void updateTrainersList(String username, List<UUID> trainerIds) {
        LOGGER.info("updateTrainersList:: updating trainers list for trainee with username: '{}'", username);
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (trainerIds == null) {
            throw new IllegalArgumentException("Trainer IDs list cannot be null");
        }

        // Get trainee by username
        Trainee trainee = getTraineeByUsername(username);
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + username + " not found");
        }

        // Get trainers by their IDs and create a set
        Set<Trainer> trainers = new HashSet<>();
        for (UUID trainerId : trainerIds) {
            Trainer trainer = trainerService.getById(trainerId);
            trainers.add(trainer);
        }

        // Update trainee's trainers list
        traineeDao.updateTrainers(trainee.getId(), trainers);
        LOGGER.info("updateTrainersList:: trainers list updated successfully for trainee with username: '{}'", username);
    }
}
