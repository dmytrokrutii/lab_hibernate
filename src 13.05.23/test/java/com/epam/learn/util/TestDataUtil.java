/*
package com.epam.learn.util;

import com.epam.learn.model.training.Training;
import com.epam.learn.model.training.TrainingType;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public final class TestDataUtil {

    public static final UUID TRAINEE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID TRAINEE_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID TRAINER_ID_1 = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID TRAINER_ID_2 = UUID.fromString("44444444-4444-4444-4444-444444444444");
    public static final UUID TRAINING_ID_1 = UUID.fromString("55555555-5555-5555-5555-555555555555");

    public static final String TRAINING_TYPE_CARDIO = "Cardio";
    public static final String TRAINING_TYPE_STRENGTH = "Strength";

    */
/**
     * Creates a Trainee object with the specified ID.
     *
     * @param id the ID to set for the Trainee
     * @return a new Trainee object with the specified ID
     *//*

    public static Trainee createTraineeWithId(UUID id) {
        Trainee trainee = new Trainee();
        trainee.setId(id);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john.doe");
        trainee.setDateOfBirth(LocalDate.of(1990, 5, 15));
        trainee.setAddress("123 Main St");
        trainee.setActive(true);
        return trainee;
    }

    */
/**
     * Creates a second Trainee object with the specified ID.
     *
     * @param id the ID to set for the Trainee
     * @return a new Trainee object with the specified ID
     *//*

    public static Trainee createSecondTraineeWithId(UUID id) {
        Trainee trainee = new Trainee();
        trainee.setId(id);
        trainee.setFirstName("Jane");
        trainee.setLastName("Smith");
        trainee.setUsername("jane.smith");
        trainee.setDateOfBirth(LocalDate.of(1992, 8, 21));
        trainee.setAddress("456 Oak Ave");
        trainee.setActive(false);
        return trainee;
    }

    */
/**
     * Creates a Trainer object with the specified ID.
     *
     * @param id the ID to set for the Trainer
     * @return a new Trainer object with the specified ID
     *//*

    public static Trainer createTrainerWithId(UUID id) {
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setFirstName("Alice");
        trainer.setLastName("Brown");
        trainer.setUsername("alice.brown");
        TrainingType trainingType = new TrainingType();
        trainingType.setName(TRAINING_TYPE_CARDIO);
        trainer.setSpecialization(trainingType);
        trainer.setActive(true);
        return trainer;
    }

    */
/**
     * Creates a second Trainer object with the specified ID.
     *
     * @param id the ID to set for the Trainer
     * @return a new Trainer object with the specified ID
     *//*

    public static Trainer createSecondTrainerWithId(UUID id) {
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setFirstName("Bob");
        trainer.setLastName("Smith");
        trainer.setUsername("bob.smith");
        TrainingType trainingType = new TrainingType();
        trainingType.setName(TRAINING_TYPE_STRENGTH);
        trainer.setSpecialization(trainingType);
        trainer.setActive(false);
        return trainer;
    }

    */
/**
     * Creates a basic Training object with a random ID.
     *
     * @return a new Training object
     *//*

    public static Training createTraining() {
        UUID id = UUID.randomUUID();
        return createTrainingWithId(id);
    }

    */
/**
     * Creates a Training object with the specified ID.
     *
     * @param id the ID to set for the Training
     * @return a new Training object with the specified ID
     *//*

    public static Training createTrainingWithId(UUID id) {
        Training training = new Training();
        training.setId(id);
        training.setTrainingName("Test Training");
        return training;
    }

    */
/**
     * Creates a Training object with the specified trainee ID.
     *
     * @param traineeId the trainee ID to set for the Training
     * @return a new Training object with the specified trainee ID
     *//*

    public static Training createTrainingWithTraineeId(UUID traineeId) {
        Training training = createTraining();
        Trainee trainee = createTraineeWithId(traineeId);
        training.setTrainee(trainee);
        return training;
    }

    */
/**
     * Creates a Training object with the specified trainer ID.
     *
     * @param trainerId the trainer ID to set for the Training
     * @return a new Training object with the specified trainer ID
     *//*

    public static Training createTrainingWithTrainerId(UUID trainerId) {
        Training training = createTraining();
        Trainer trainer = createTrainerWithId(trainerId);
        training.setTrainer(trainer);
        return training;
    }

    */
/**
     * Creates a Training object with the specified training date.
     *
     * @param trainingDate the training date to set for the Training
     * @return a new Training object with the specified training date
     *//*

    public static Training createTrainingWithDate(LocalDateTime trainingDate) {
        Training training = createTraining();
        training.setTrainingDate(trainingDate);
        return training;
    }

    */
/**
     * Creates a Training object with the specified training type.
     *
     * @param typeName the name of the training type
     * @return a new Training object with the specified training type
     *//*

    public static Training createTrainingWithType(String typeName) {
        Training training = createTraining();
        TrainingType type = new TrainingType();
        type.setName(typeName);
        training.setTrainingType(type);
        return training;
    }

    */
/**
     * Creates a Training object with null ID.
     *
     * @return a new Training object with null ID
     *//*

    public static Training createTrainingWithNullId() {
        Training training = createTraining();
        training.setId(null);
        return training;
    }
}
*/
