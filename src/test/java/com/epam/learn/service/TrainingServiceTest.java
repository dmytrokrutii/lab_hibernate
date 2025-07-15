/*
package com.epam.learn.service;

import com.epam.learn.dao.impl.TrainingDaoImpl;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;
import com.epam.learn.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDaoImpl trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;
    private UUID trainingId;

    @BeforeEach
    void setUp() {
        trainingId = UUID.randomUUID();
        training = new Training();
        training.setId(trainingId);
        training.setTrainingName("Cardio Training");
    }

    @Test
    void create_shouldSaveNewTraining() {
        trainingService.create(training);

        verify(trainingDao).save(training);
    }

    @Test
    void createShouldGenerateIdWhenTrainingIdIsNull() {
        Training trainingWithNullId = new Training();
        trainingWithNullId.setTrainingName("New Training");
        trainingWithNullId.setId(null);

        trainingService.create(trainingWithNullId);

        assertThat(trainingWithNullId.getId()).isNotNull();
        verify(trainingDao).save(trainingWithNullId);
    }

    @Test
    void getByIdShouldReturnTrainingWhenExists() {
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        Training result = trainingService.getById(trainingId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(trainingId);
        verify(trainingDao).findById(trainingId);
    }

    @Test
    void getByIdShouldThrowExceptionWhenTrainingNotFound() {
        when(trainingDao.findById(trainingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.getById(trainingId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINING.name());

        verify(trainingDao).findById(trainingId);
    }

    @Test
    void getAllShouldReturnPagedResponseOfTrainings() {
        PagedResponse<Training> pagedResponse = new PagedResponse<>(
                List.of(training), 0, 1, 1L, 1
        );
        when(trainingDao.findAll(0, 1)).thenReturn(pagedResponse);

        PagedResponse<Training> result = trainingService.getAll(0, 1);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(training);

        verify(trainingDao).findAll(0, 1);
    }

    @Test
    void updateShouldUpdateTrainingWhenValid() {
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        trainingService.update(trainingId, training);

        verify(trainingDao).update(trainingId, training);
    }

    @Test
    void updateShouldThrowExceptionWhenTrainingNotFound() {
        when(trainingDao.findById(trainingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.update(trainingId, training))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINING.name());

        verify(trainingDao, never()).update(any(), any());
    }

    @Test
    void deleteShouldDeleteTrainingWhenExists() {
        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        trainingService.delete(trainingId);

        verify(trainingDao).delete(trainingId);
    }

    @Test
    void deleteShouldThrowExceptionWhenTrainingNotFound() {
        when(trainingDao.findById(trainingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.delete(trainingId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINING.name());

        verify(trainingDao, never()).delete(any());
    }
}
*/
