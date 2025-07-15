/*
package com.epam.learn.dao;

import com.epam.learn.dao.impl.TrainingDaoImpl;
import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;
import com.epam.learn.storage.GenericStorage;
import com.epam.learn.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingDaoTest {

    @Mock
    private GenericStorage<Training> trainingStorage;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    private Training training;
    private UUID trainingId;

    @BeforeEach
    void setUp() {
        trainingId = TestDataUtil.TRAINING_ID_1;
        training = TestDataUtil.createTrainingWithId(trainingId);
        training.setTrainingName("Strong Lifting");
    }

    @Test
    void saveShouldSaveTrainingToStorage() {
        trainingDao.save(training);

        verify(trainingStorage).save(trainingId, training);
    }

    @Test
    void findByIdShouldReturnTrainingWhenItExists() {
        when(trainingStorage.findById(trainingId)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingDao.findById(trainingId);

        assertThat(result).contains(training);
        verify(trainingStorage).findById(trainingId);
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenTrainingNotFound() {
        when(trainingStorage.findById(trainingId)).thenReturn(Optional.empty());

        Optional<Training> result = trainingDao.findById(trainingId);

        assertThat(result).isEmpty();
        verify(trainingStorage).findById(trainingId);
    }

    @Test
    void deleteShouldRemoveTrainingById() {
        trainingDao.delete(trainingId);

        verify(trainingStorage).delete(trainingId);
    }

    @Test
    void updateShouldModifyTrainingWhenExists() {
        when(trainingStorage.update(trainingId, training)).thenReturn(true);

        trainingDao.update(trainingId, training);

        verify(trainingStorage).update(trainingId, training);
    }

    @Test
    void updateShouldThrowExceptionWhenTrainingDoesNotExist() {
        when(trainingStorage.update(trainingId, training)).thenReturn(false);

        assertThatThrownBy(() -> trainingDao.update(trainingId, training))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("TRAINING");

        verify(trainingStorage).update(trainingId, training);
    }

    @Test
    void findAllShouldReturnPagedResponseOfTrainings() {
        Training training1 = TestDataUtil.createTraining();
        Training training2 = TestDataUtil.createTraining();

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2));

        PagedResponse<Training> result = trainingDao.findAll(0, 2);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        verify(trainingStorage).findAll();
    }

    @Test
    void findAllShouldReturnEmptyResponseForOutOfRangePage() {
        Training training1 = TestDataUtil.createTraining();
        Training training2 = TestDataUtil.createTraining();

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2));

        PagedResponse<Training> result = trainingDao.findAll(2, 2);

        assertThat(result).isNotNull();
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(1);

        verify(trainingStorage).findAll();
    }

    @Test
    void saveShouldGenerateNewIdWhenTrainingIdIsNull() {
        Training trainingWithoutId = TestDataUtil.createTrainingWithNullId();
        trainingWithoutId.setTrainingName("Cardio Workout");

        trainingDao.save(trainingWithoutId);

        assertThat(trainingWithoutId.getId()).isNotNull();
        verify(trainingStorage).save(trainingWithoutId.getId(), trainingWithoutId);
    }

    @Test
    void findByTraineeIdShouldReturnTrainingsForTrainee() {
        UUID traineeId = TestDataUtil.TRAINEE_ID_1;
        Training training1 = TestDataUtil.createTrainingWithTraineeId(traineeId);
        Training training2 = TestDataUtil.createTrainingWithTraineeId(traineeId);
        Training training3 = TestDataUtil.createTrainingWithTraineeId(TestDataUtil.TRAINEE_ID_2);

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2, training3));

        PagedResponse<Training> result = trainingDao.findByTraineeId(traineeId, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).containsExactly(training1, training2);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByTraineeIdShouldThrowExceptionWhenTraineeIdIsNull() {
        assertThatThrownBy(() -> trainingDao.findByTraineeId(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Trainee ID cannot be null");
    }

    @Test
    void findByTraineeIdShouldThrowExceptionWhenPageIsNegative() {
        UUID traineeId = TestDataUtil.TRAINEE_ID_1;
        assertThatThrownBy(() -> trainingDao.findByTraineeId(traineeId, -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByTraineeIdShouldThrowExceptionWhenSizeIsNegative() {
        UUID traineeId = TestDataUtil.TRAINEE_ID_1;
        assertThatThrownBy(() -> trainingDao.findByTraineeId(traineeId, 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByTrainerIdShouldReturnTrainingsForTrainer() {
        UUID trainerId = TestDataUtil.TRAINER_ID_1;
        Training training1 = TestDataUtil.createTrainingWithTrainerId(trainerId);
        Training training2 = TestDataUtil.createTrainingWithTrainerId(trainerId);
        Training training3 = TestDataUtil.createTrainingWithTrainerId(TestDataUtil.TRAINER_ID_2);

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2, training3));

        PagedResponse<Training> result = trainingDao.findByTrainerId(trainerId, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).containsExactly(training1, training2);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByTrainerIdShouldThrowExceptionWhenTrainerIdIsNull() {
        assertThatThrownBy(() -> trainingDao.findByTrainerId(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Trainer ID cannot be null");
    }

    @Test
    void findByTrainerIdShouldThrowExceptionWhenPageIsNegative() {
        UUID trainerId = TestDataUtil.TRAINER_ID_1;
        assertThatThrownBy(() -> trainingDao.findByTrainerId(trainerId, -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByTrainerIdShouldThrowExceptionWhenSizeIsNegative() {
        UUID trainerId = TestDataUtil.TRAINER_ID_1;
        assertThatThrownBy(() -> trainingDao.findByTrainerId(trainerId, 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByDateRangeShouldReturnTrainingsInRange() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        Training training1 = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 15, 10, 0));
        Training training2 = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 20, 15, 30));
        Training training3 = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2022, 12, 15, 9, 0));
        Training training4 = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 2, 5, 11, 0));

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2, training3, training4));

        PagedResponse<Training> result = trainingDao.findByDateRange(startDate, endDate, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).containsExactly(training1, training2);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByDateRangeShouldThrowExceptionWhenDatesAreNull() {
        assertThatThrownBy(() -> trainingDao.findByDateRange(null, null, 0, 10)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start date and end date cannot be null");
    }

    @Test
    void findByDateRangeShouldThrowExceptionWhenStartDateIsNull() {
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        assertThatThrownBy(() -> trainingDao.findByDateRange(null, endDate, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start date and end date cannot be null");
    }

    @Test
    void findByDateRangeShouldThrowExceptionWhenEndDateIsNull() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        assertThatThrownBy(() -> trainingDao.findByDateRange(startDate, null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start date and end date cannot be null");
    }

    @Test
    void findByDateRangeShouldThrowExceptionWhenPageIsNegative() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        assertThatThrownBy(() -> trainingDao.findByDateRange(startDate, endDate, -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByDateRangeShouldThrowExceptionWhenSizeIsNegative() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        assertThatThrownBy(() -> trainingDao.findByDateRange(startDate, endDate, 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByDateRangeShouldHandleTrainingWithNullDate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        Training training1 = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 15, 10, 0));
        Training training2 = TestDataUtil.createTraining(); // No date set

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2));

        PagedResponse<Training> result = trainingDao.findByDateRange(startDate, endDate, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content()).containsExactly(training1);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByDateRangeShouldIncludeTrainingOnStartDate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        Training trainingOnStartDate = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 1, 10, 0));
        Training trainingAfterStartDate = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 15, 10, 0));
        Training trainingBeforeStartDate = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2022, 12, 31, 10, 0));

        when(trainingStorage.findAll()).thenReturn(List.of(trainingOnStartDate, trainingAfterStartDate, trainingBeforeStartDate));

        PagedResponse<Training> result = trainingDao.findByDateRange(startDate, endDate, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).containsExactly(trainingOnStartDate, trainingAfterStartDate);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByDateRangeShouldIncludeTrainingOnEndDate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        Training trainingOnEndDate = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 31, 10, 0));
        Training trainingBeforeEndDate = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 1, 15, 10, 0));
        Training trainingAfterEndDate = TestDataUtil.createTrainingWithDate(LocalDateTime.of(2023, 2, 1, 10, 0));

        when(trainingStorage.findAll()).thenReturn(List.of(trainingOnEndDate, trainingBeforeEndDate, trainingAfterEndDate));

        PagedResponse<Training> result = trainingDao.findByDateRange(startDate, endDate, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).containsExactly(trainingOnEndDate, trainingBeforeEndDate);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByTypeShouldReturnTrainingsOfSpecificType() {
        String strengthType = "Strength";
        String cardioType = "Cardio";

        Training training1 = TestDataUtil.createTrainingWithType(strengthType);
        Training training2 = TestDataUtil.createTrainingWithType(strengthType);
        Training training3 = TestDataUtil.createTrainingWithType(cardioType);

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2, training3));

        PagedResponse<Training> result = trainingDao.findByType(strengthType, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).containsExactly(training1, training2);
        verify(trainingStorage).findAll();
    }

    @Test
    void findByTypeShouldThrowExceptionWhenTypeIsNullOrEmpty() {
        assertThatThrownBy(() -> trainingDao.findByType(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Type cannot be null or empty");

        assertThatThrownBy(() -> trainingDao.findByType("", 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Type cannot be null or empty");
    }

    @Test
    void findByTypeShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> trainingDao.findByType("Strength", -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByTypeShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> trainingDao.findByType("Strength", 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByTypeShouldHandleTrainingWithNullType() {
        String strengthType = "Strength";

        Training training1 = TestDataUtil.createTrainingWithType(strengthType);
        Training training2 = TestDataUtil.createTraining(); // No type set

        when(trainingStorage.findAll()).thenReturn(List.of(training1, training2));

        PagedResponse<Training> result = trainingDao.findByType(strengthType, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content()).containsExactly(training1);
        verify(trainingStorage).findAll();
    }
}
*/
