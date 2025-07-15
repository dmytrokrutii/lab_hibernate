package com.epam.learn.service.impl;

import com.epam.learn.dao.TrainingDao;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.service.TraineeService;
import com.epam.learn.service.TrainerService;
import com.epam.learn.service.TrainingService;
import com.epam.learn.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Transactional
    public void create(Training training) {
        LOGGER.info("create:: creating training");

        if (training.getTrainee() == null || training.getTrainee().getUsername() == null) {
            throw new IllegalArgumentException("Trainee is missing or has no username");
        }

        if (training.getTrainer() == null || training.getTrainer().getUsername() == null) {
            throw new IllegalArgumentException("Trainer is missing or has no username");
        }

        if (training.getTrainingType() == null || training.getTrainingType().getName() == null) {
            throw new IllegalArgumentException("TrainingType is missing or has no name");
        }

        Trainee trainee = traineeService.getTraineeByUsername(training.getTrainee().getUsername());
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + training.getTrainee().getUsername() + " not found");
        }

        Trainer trainer = trainerService.getTrainerByUsername(training.getTrainer().getUsername());
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with username " + training.getTrainer().getUsername() + " not found");
        }

        String trainingTypeName = training.getTrainingType().getName();
        training.setTrainingType(trainingTypeService.findOrCreate(trainingTypeName));

        training.setTrainee(trainee);
        training.setTrainer(trainer);

        trainingDao.save(training);
    }

    @Override
    public Training getById(UUID id) {
        return trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINING, id));
    }

    @Override
    public PagedResponse<Training> getAll(int page, int size) {
        return trainingDao.findAll(page, size);
    }

    @Override
    public void update(UUID id, Training updatedTraining) {
        LOGGER.info("update:: updating training with id: '{}'", id);
        Training existingTraining = trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINING, id));

        updatedTraining.setId(existingTraining.getId());
        trainingDao.update(id, updatedTraining);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("delete:: deleting training with id: '{}'", id);
        trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.TRAINING, id));
        trainingDao.delete(id);
    }

    @Override
    public PagedResponse<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                       String trainerName, String trainingType, int page, int size) {
        LOGGER.info("getTraineeTrainings:: getting trainings for trainee: '{}' with criteria", traineeUsername);

        // Get trainee by username
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee with username " + traineeUsername + " not found");
        }

        // Use the DAO to get trainings by criteria
        return trainingDao.findByTraineeCriteria(trainee.getId(), fromDate, toDate, trainerName, trainingType, page, size);
    }

    @Override
    public PagedResponse<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                       String traineeName, int page, int size) {
        LOGGER.info("getTrainerTrainings:: getting trainings for trainer: '{}' with criteria", trainerUsername);

        // Get trainer by username
        Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with username " + trainerUsername + " not found");
        }

        // Use the DAO to get trainings by criteria
        return trainingDao.findByTrainerCriteria(trainer.getId(), fromDate, toDate, traineeName, page, size);
    }
}
