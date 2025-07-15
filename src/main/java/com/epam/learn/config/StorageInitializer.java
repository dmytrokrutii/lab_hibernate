package com.epam.learn.config;

import com.epam.learn.dao.TraineeDao;
import com.epam.learn.dao.TrainerDao;
import com.epam.learn.dao.TrainingDao;
import com.epam.learn.dao.TrainingTypeDao;
import com.epam.learn.exception.FailedStorageInitializationException;
import com.epam.learn.model.training.Training;
import com.epam.learn.model.training.TrainingType;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Component responsible for initializing the database with data from a JSON file.
 * This is used primarily for the H2 in-memory database to provide initial data.
 */
@Slf4j
@Component
@Profile("h2")
public class StorageInitializer {

    @Value("${storage.data-file}")
    private String dataFile;

    private final TrainingTypeDao trainingTypeDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final TrainingDao trainingDao;
    private final ObjectMapper objectMapper;

    /**
     * Initialize the ObjectMapper with proper modules for date/time handling.
     */
    public StorageInitializer(TrainingTypeDao trainingTypeDao, TraineeDao traineeDao, 
                             TrainerDao trainerDao, TrainingDao trainingDao) {
        this.trainingTypeDao = trainingTypeDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.trainingDao = trainingDao;

        // Configure ObjectMapper for proper date/time handling
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // This will register JavaTimeModule
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Initializes the database with data from the JSON file.
     * This method is called automatically after the bean is constructed.
     */
    @PostConstruct
    public void initializeStorage() {
        LOGGER.info("initializeStorage:: Initializing storage from {}", dataFile);
        try {
            // Load the JSON file from the classpath using InputStream
            // This approach works both when running from IDE and when packaged as a JAR
            ClassLoader classLoader = getClass().getClassLoader();
            JsonNode rootNode;

            try (java.io.InputStream inputStream = classLoader.getResourceAsStream(dataFile)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found: " + dataFile);
                }
                rootNode = objectMapper.readTree(inputStream);
            }

            // Maps to store references between entities
            Map<String, TrainingType> trainingTypeMap = new HashMap<>();
            Map<String, Trainee> traineeMap = new HashMap<>();
            Map<String, Trainer> trainerMap = new HashMap<>();

            // Load training types
            JsonNode trainingTypesNode = rootNode.get("trainingTypes");
            if (trainingTypesNode != null) {
                for (JsonNode node : trainingTypesNode) {
                    TrainingType trainingType = objectMapper.treeToValue(node, TrainingType.class);

                    // Check if the training type already exists
                    Optional<TrainingType> existingType = trainingTypeDao.findById(trainingType.getId());
                    if (existingType.isPresent()) {
                        // Update the existing training type
                        trainingTypeDao.update(trainingType.getId(), trainingType);
                        LOGGER.debug("initializeStorage:: Updated training type: {}", trainingType.getName());
                    } else {
                        // Save the new training type
                        trainingTypeDao.save(trainingType);
                        LOGGER.debug("initializeStorage:: Saved training type: {}", trainingType.getName());
                    }

                    trainingTypeMap.put(trainingType.getId().toString(), trainingType);
                }
            }

            // Load trainees
            JsonNode traineesNode = rootNode.get("trainees");
            if (traineesNode != null) {
                for (JsonNode node : traineesNode) {
                    Trainee trainee = objectMapper.treeToValue(node, Trainee.class);

                    // Check if the trainee already exists
                    Optional<Trainee> existingTrainee = traineeDao.findById(trainee.getId());
                    if (existingTrainee.isPresent()) {
                        // Update the existing trainee
                        traineeDao.update(trainee.getId(), trainee);
                        LOGGER.debug("initializeStorage:: Updated trainee: {}", trainee.getUsername());
                    } else {
                        // Save the new trainee
                        traineeDao.save(trainee);
                        LOGGER.debug("initializeStorage:: Saved trainee: {}", trainee.getUsername());
                    }

                    traineeMap.put(trainee.getId().toString(), trainee);
                }
            }

            // Load trainers
            JsonNode trainersNode = rootNode.get("trainers");
            if (trainersNode != null) {
                for (JsonNode node : trainersNode) {
                    Trainer trainer = objectMapper.treeToValue(node, Trainer.class);

                    // Set the specialization from the map
                    if (trainer.getSpecialization() != null && trainer.getSpecialization().getId() != null) {
                        String specId = trainer.getSpecialization().getId().toString();
                        TrainingType specialization = trainingTypeMap.get(specId);
                        if (specialization == null) {
                            // If the specialization is not in the map, try to find it in the database
                            Optional<TrainingType> existingType = trainingTypeDao.findById(trainer.getSpecialization().getId());
                            if (existingType.isPresent()) {
                                specialization = existingType.get();
                                trainingTypeMap.put(specId, specialization);
                                LOGGER.debug("initializeStorage:: Found training type in database: {}", specialization.getName());
                            } else {
                                LOGGER.error("initializeStorage:: Training type with ID {} not found", specId);
                                throw new IllegalStateException("Training type with ID " + specId + " not found");
                            }
                        }
                        trainer.setSpecialization(specialization);
                    }

                    // Check if the trainer already exists
                    Optional<Trainer> existingTrainer = trainerDao.findById(trainer.getId());
                    if (existingTrainer.isPresent()) {
                        // Update the existing trainer
                        trainerDao.update(trainer.getId(), trainer);
                        LOGGER.debug("initializeStorage:: Updated trainer: {}", trainer.getUsername());
                    } else {
                        // Save the new trainer
                        trainerDao.save(trainer);
                        LOGGER.debug("initializeStorage:: Saved trainer: {}", trainer.getUsername());
                    }

                    trainerMap.put(trainer.getId().toString(), trainer);
                }
            }

            // Load trainings
            JsonNode trainingsNode = rootNode.get("trainings");
            if (trainingsNode != null) {
                for (JsonNode node : trainingsNode) {
                    Training training = objectMapper.treeToValue(node, Training.class);

                    // Set the trainee from the map
                    if (training.getTrainee() != null && training.getTrainee().getId() != null) {
                        String traineeId = training.getTrainee().getId().toString();
                        Trainee trainee = traineeMap.get(traineeId);
                        training.setTrainee(trainee);
                    }

                    // Set the trainer from the map
                    if (training.getTrainer() != null && training.getTrainer().getId() != null) {
                        String trainerId = training.getTrainer().getId().toString();
                        Trainer trainer = trainerMap.get(trainerId);
                        training.setTrainer(trainer);
                    }

                    // Set the training type from the map
                    if (training.getTrainingType() != null && training.getTrainingType().getId() != null) {
                        String typeId = training.getTrainingType().getId().toString();
                        TrainingType trainingType = trainingTypeMap.get(typeId);
                        training.setTrainingType(trainingType);
                    }

                    // Check if the training already exists
                    Optional<Training> existingTraining = trainingDao.findById(training.getId());
                    if (existingTraining.isPresent()) {
                        // Update the existing training
                        trainingDao.update(training.getId(), training);
                        LOGGER.debug("initializeStorage:: Updated training: {}", training.getTrainingName());
                    } else {
                        // Save the new training
                        trainingDao.save(training);
                        LOGGER.debug("initializeStorage:: Saved training: {}", training.getTrainingName());
                    }
                }
            }

            // Process trainee-trainer relationships
            JsonNode relationshipsNode = rootNode.get("traineeTrainerRelationships");
            if (relationshipsNode != null) {
                // Create a map to collect trainers for each trainee
                Map<UUID, Set<Trainer>> traineeTrainersMap = new HashMap<>();

                for (JsonNode node : relationshipsNode) {
                    String traineeId = node.get("traineeId").asText();
                    String trainerId = node.get("trainerId").asText();

                    Trainee trainee = traineeMap.get(traineeId);
                    Trainer trainer = trainerMap.get(trainerId);

                    if (trainee != null && trainer != null) {
                        // Add the trainer to the trainee's set of trainers
                        traineeTrainersMap.computeIfAbsent(trainee.getId(), k -> new HashSet<>()).add(trainer);
                        LOGGER.debug("initializeStorage:: Collected trainer {} for trainee {}", 
                                    trainer.getUsername(), trainee.getUsername());
                    }
                }

                // Update each trainee's trainers list
                for (Map.Entry<UUID, Set<Trainer>> entry : traineeTrainersMap.entrySet()) {
                    UUID traineeId = entry.getKey();
                    Set<Trainer> trainers = entry.getValue();

                    traineeDao.updateTrainers(traineeId, trainers);
                    LOGGER.debug("initializeStorage:: Updated trainers list for trainee ID: {}", traineeId);
                }
            }

            LOGGER.info("initializeStorage:: Storage initialized successfully");
        } catch (IOException e) {
            LOGGER.error("initializeStorage:: Failed to initialize storage from {}", dataFile, e);
            throw new FailedStorageInitializationException("Failed to initialize storage from " + dataFile, e);
        }
    }
}
