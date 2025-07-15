/*
package com.epam.learn.dao;

import com.epam.learn.dao.impl.JpaTraineeDaoImpl;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.storage.GenericStorage;
import com.epam.learn.util.TestDataUtil;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDaoTest {

    @Mock
    private GenericStorage<Trainee> traineeStorage;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    private Trainee trainee1;
    private Trainee trainee2;

    @BeforeEach
    void setUp() {
        UUID traineeId1 = TestDataUtil.TRAINEE_ID_1;
        trainee1 = TestDataUtil.createTraineeWithId(traineeId1);

        UUID traineeId2 = TestDataUtil.TRAINEE_ID_2;
        trainee2 = TestDataUtil.createSecondTraineeWithId(traineeId2);
    }

    @Test
    void findByUsernameShouldReturnTraineeWhenFound() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        Optional<Trainee> result = traineeDao.findByUsername("john.doe");

        assertThat(result).contains(trainee1);
    }

    @Test
    void findByUsernameShouldReturnEmptyWhenNotFound() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        Optional<Trainee> result = traineeDao.findByUsername("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findByUsernameShouldThrowExceptionWhenUsernameIsNull() {
        assertThatThrownBy(() -> traineeDao.findByUsername(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null or empty");
    }

    @Test
    void findByUsernameShouldThrowExceptionWhenUsernameIsEmpty() {
        assertThatThrownBy(() -> traineeDao.findByUsername(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null or empty");
    }

    @Test
    void findByFirstNameShouldReturnTraineesWithMatchingFirstName() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        PagedResponse<Trainee> result = traineeDao.findByFirstName("John", 0, 10);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainee1);
    }

    @Test
    void findByFirstNameShouldReturnEmptyWhenNoMatch() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        PagedResponse<Trainee> result = traineeDao.findByFirstName("Alice", 0, 10);

        assertThat(result.content()).isEmpty();
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenFirstNameIsNull() {
        assertThatThrownBy(() -> traineeDao.findByFirstName(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be null or empty");
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenFirstNameIsEmpty() {
        assertThatThrownBy(() -> traineeDao.findByFirstName("", 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be null or empty");
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> traineeDao.findByFirstName("John", -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByFirstNameShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> traineeDao.findByFirstName("John", 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByLastNameShouldReturnTraineesWithMatchingLastName() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        PagedResponse<Trainee> result = traineeDao.findByLastName("Doe", 0, 10);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainee1);
    }

    @Test
    void findByLastNameShouldReturnEmptyWhenNoMatch() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        PagedResponse<Trainee> result = traineeDao.findByLastName("Johnson", 0, 10);

        assertThat(result.content()).isEmpty();
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenLastNameIsNull() {
        assertThatThrownBy(() -> traineeDao.findByLastName(null, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be null or empty");
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenLastNameIsEmpty() {
        assertThatThrownBy(() -> traineeDao.findByLastName("", 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be null or empty");
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> traineeDao.findByLastName("Doe", -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findByLastNameShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> traineeDao.findByLastName("Doe", 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findActiveShouldReturnActiveTrainees() {
        List<Trainee> trainees = List.of(trainee1, trainee2);

        when(traineeStorage.findAll()).thenReturn(trainees);

        PagedResponse<Trainee> result = traineeDao.findActive(0, 10);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0)).isEqualTo(trainee1);
    }

    @Test
    void findActiveShouldThrowExceptionWhenPageIsNegative() {
        assertThatThrownBy(() -> traineeDao.findActive(-1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }

    @Test
    void findActiveShouldThrowExceptionWhenSizeIsNegative() {
        assertThatThrownBy(() -> traineeDao.findActive(0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page and size must be non-negative");
    }
}
*/
