/*
package com.epam.learn.service;

import com.epam.learn.dao.impl.JpaTraineeDaoImpl;
import com.epam.learn.dao.impl.JpaTrainerDaoImpl;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private JpaTraineeDaoImpl traineeDao;

    @Mock
    private JpaTrainerDaoImpl trainerDao;

    @Mock
    private TrainerService trainerService;

    private TraineeServiceImpl traineeService;

    private Trainee trainee;
    private UUID traineeId;

    @BeforeEach
    void setUp() {
        traineeId = UUID.randomUUID();
        trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setAddress("123 Main Street");
        trainee.setDateOfBirth(LocalDate.of(1990, 5, 20));

        traineeService = new TraineeServiceImpl(traineeDao, trainerDao, trainerService);
    }

    @Test
    void createShouldSaveNewTrainee() {
        when(traineeDao.findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));
        when(trainerDao.findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));

        traineeService.create(trainee);

        assertThat(trainee.getUsername()).isNotNull();
        assertThat(trainee.getPassword()).isNotNull();
        assertThat(trainee.getId()).isNotNull();
        verify(traineeDao).save(trainee);
    }

    @Test
    void getByIdShouldReturnTraineeWhenExists() {
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getById(traineeId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(traineeId);
        verify(traineeDao).findById(traineeId);
    }

    @Test
    void getByIdShouldThrowExceptionWhenTraineeNotFound() {
        when(traineeDao.findById(traineeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> traineeService.getById(traineeId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINEE.name());

        verify(traineeDao).findById(traineeId);
    }

    @Test
    void getAllShouldReturnPagedResponseOfTrainees() {
        PagedResponse<Trainee> pagedResponse = new PagedResponse<>(
                List.of(trainee), 0, 1, 1L, 1
        );
        when(traineeDao.findAll(0, 1)).thenReturn(pagedResponse);

        PagedResponse<Trainee> result = traineeService.getAll(0, 1);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainee);

        verify(traineeDao).findAll(0, 1);
    }

    @Test
    void updateShouldUpdateTraineeWhenValid() {
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        traineeService.update(traineeId, trainee);

        verify(traineeDao).update(traineeId, trainee);
    }

    @Test
    void updateShouldRegenerateUsernameWhenNameChanges() {
        // Setup
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(traineeId);
        existingTrainee.setFirstName("John");
        existingTrainee.setLastName("Doe");
        existingTrainee.setUsername("john.doe");

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(traineeId);
        updatedTrainee.setFirstName("John");
        updatedTrainee.setLastName("Smith"); // Last name changed

        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(existingTrainee));
        when(traineeDao.findByFirstNameAndLastName("John", "Smith", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));
        when(trainerDao.findByFirstNameAndLastName("John", "Smith", 0, Integer.MAX_VALUE))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));

        // Execute
        traineeService.update(traineeId, updatedTrainee);

        // Verify
        assertThat(updatedTrainee.getUsername()).isEqualTo("john.smith");
        verify(traineeDao).update(traineeId, updatedTrainee);
    }

    @Test
    void updateShouldThrowExceptionWhenTraineeNotFound() {
        when(traineeDao.findById(traineeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> traineeService.update(traineeId, trainee))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINEE.name());

        verify(traineeDao, never()).update(any(), any());
    }

}
*/
