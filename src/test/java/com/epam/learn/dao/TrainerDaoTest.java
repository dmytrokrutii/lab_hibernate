/*
package com.epam.learn.dao;

import com.epam.learn.dao.impl.JpaTrainerDaoImpl;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.util.TestDataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDaoTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private JpaTrainerDaoImpl trainerDao;

    private Trainer trainer1;
    private Trainer trainer2;

    @BeforeEach
    void setUp() {
        UUID trainerId1 = TestDataUtil.TRAINER_ID_1;
        trainer1 = TestDataUtil.createTrainerWithId(trainerId1);

        UUID trainerId2 = TestDataUtil.TRAINER_ID_2;
        trainer2 = TestDataUtil.createSecondTrainerWithId(trainerId2);
    }

    @Test
    void findByUsernameShouldReturnTrainerWhenFound() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        Optional<Trainer> result = trainerDao.findByUsername("alice.brown");

        assertThat(result).contains(trainer1);
    }

    @Test
    void findByUsernameShouldReturnEmptyWhenNotFound() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        Optional<Trainer> result = trainerDao.findByUsername("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findByUsernameShouldThrowExceptionWhenUsernameIsNull() {
        assertThatThrownBy(() -> trainerDao.findByUsername(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null or empty");
    }

    @Test
    void findByUsernameShouldThrowExceptionWhenUsernameIsEmpty() {
        assertThatThrownBy(() -> trainerDao.findByUsername(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null or empty");
    }

    @Test
    void findBySpecializationShouldReturnEmptyWhenNoMatch() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        PagedResponse<Trainer> result = trainerDao.findBySpecialization("Yoga", 0, 10);

        assertThat(result.content()).isEmpty();
    }

    @Test
    void findBySpecializationShouldThrowExceptionWhenSpecializationIsNull() {
        assertThatThrownBy(() -> trainerDao.findBySpecialization(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Specialization cannot be null or empty");
    }

    @Test
    void findBySpecializationShouldThrowExceptionWhenSpecializationIsEmpty() {
        assertThatThrownBy(() -> trainerDao.findBySpecialization("", 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Specialization cannot be null or empty");
    }

    @Test
    void findBySpecializationShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> trainerDao.findBySpecialization("Cardio", -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findBySpecializationShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> trainerDao.findBySpecialization("Cardio", 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByFirstNameShouldReturnTrainersWithMatchingFirstName() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        PagedResponse<Trainer> result = trainerDao.findByFirstName("Alice", 0, 10);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainer1);
    }

    @Test
    void findByFirstNameShouldReturnEmptyWhenNoMatch() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        PagedResponse<Trainer> result = trainerDao.findByFirstName("Charlie", 0, 10);

        assertThat(result.content()).isEmpty();
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenFirstNameIsNull() {
        assertThatThrownBy(() -> trainerDao.findByFirstName(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be null or empty");
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenFirstNameIsEmpty() {
        assertThatThrownBy(() -> trainerDao.findByFirstName("", 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be null or empty");
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> trainerDao.findByFirstName("Alice", -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> trainerDao.findByFirstName("Alice", 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByLastNameShouldReturnTrainersWithMatchingLastName() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        PagedResponse<Trainer> result = trainerDao.findByLastName("Brown", 0, 10);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainer1);
    }

    @Test
    void findByLastNameShouldReturnEmptyWhenNoMatch() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        PagedResponse<Trainer> result = trainerDao.findByLastName("Johnson", 0, 10);

        assertThat(result.content()).isEmpty();
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenLastNameIsNull() {
        assertThatThrownBy(() -> trainerDao.findByLastName(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be null or empty");
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenLastNameIsEmpty() {
        assertThatThrownBy(() -> trainerDao.findByLastName("", 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be null or empty");
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> trainerDao.findByLastName("Brown", -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> trainerDao.findByLastName("Brown", 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findActiveShouldReturnActiveTrainers() {
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(trainerStorage.findAll()).thenReturn(trainers);

        PagedResponse<Trainer> result = trainerDao.findActive(0, 10);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainer1);
    }

    @Test
    void findActiveShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> trainerDao.findActive(-1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findActiveShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> trainerDao.findActive(0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }
}
*/
