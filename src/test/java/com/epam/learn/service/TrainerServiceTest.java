/*
package com.epam.learn.service;

import com.epam.learn.dao.impl.JpaTraineeDaoImpl;
import com.epam.learn.dao.impl.JpaTrainerDaoImpl;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.TrainingType;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private JpaTrainerDaoImpl trainerDao;

    @Mock
    private JpaTraineeDaoImpl traineeDao;

    @Mock
    private TraineeService traineeService;

    private TrainerServiceImpl trainerService;

    private Trainer trainer;
    private UUID trainerId;

    @BeforeEach
    void setUp() {
        trainerId = UUID.randomUUID();
        trainer = new Trainer();
        trainer.setId(trainerId);
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");
        TrainingType trainingType = new TrainingType();
        trainingType.setName("Fitness");
        trainer.setSpecialization(trainingType);

        trainerService = new TrainerServiceImpl(traineeDao, trainerDao, traineeService);
    }

    @Test
    void createShouldSaveNewTrainer() {
        when(traineeDao.findByFirstNameAndLastName("Jane", "Doe", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));
        when(trainerDao.findByFirstNameAndLastName("Jane", "Doe", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));

        trainerService.create(trainer);

        assertThat(trainer.getUsername()).isNotNull();
        assertThat(trainer.getPassword()).isNotNull();
        assertThat(trainer.getId()).isNotNull();
        verify(trainerDao).save(trainer);
    }

    @Test
    void getByIdShouldReturnTrainerWhenExists() {
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getById(trainerId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(trainerId);
        verify(trainerDao).findById(trainerId);
    }

    @Test
    void getByIdShouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.findById(trainerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainerService.getById(trainerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINER.name());

        verify(trainerDao).findById(trainerId);
    }

    @Test
    void getAllShouldReturnPagedResponseOfTrainers() {
        PagedResponse<Trainer> pagedResponse = new PagedResponse<>(
                List.of(trainer), 0, 1, 1L, 1
        );
        when(trainerDao.findAll(0, 1)).thenReturn(pagedResponse);

        PagedResponse<Trainer> result = trainerService.getAll(0, 1);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainer);

        verify(trainerDao).findAll(0, 1);
    }

    @Test
    void updateShouldUpdateTrainerWhenValid() {
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        trainerService.update(trainerId, trainer);

        verify(trainerDao).update(trainerId, trainer);
    }

    @Test
    void updateShouldRegenerateUsernameWhenNameChanges() {
        // Setup
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(trainerId);
        existingTrainer.setFirstName("Jane");
        existingTrainer.setLastName("Doe");
        existingTrainer.setUsername("jane.doe");
        TrainingType trainingType = new TrainingType();
        trainingType.setName("Fitness");
        existingTrainer.setSpecialization(trainingType);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(trainerId);
        updatedTrainer.setFirstName("Jane");
        updatedTrainer.setLastName("Smith"); // Last name changed
        updatedTrainer.setSpecialization(trainingType);

        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(existingTrainer));
        when(traineeDao.findByFirstNameAndLastName("Jane", "Smith", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));
        when(trainerDao.findByFirstNameAndLastName("Jane", "Smith", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));

        // Execute
        trainerService.update(trainerId, updatedTrainer);

        // Verify
        assertThat(updatedTrainer.getUsername()).isEqualTo("jane.smith");
        verify(trainerDao).update(trainerId, updatedTrainer);
    }

    @Test
    void updateShouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.findById(trainerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainerService.update(trainerId, trainer))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINER.name());

        verify(trainerDao, never()).update(any(), any());
    }

}
*/
