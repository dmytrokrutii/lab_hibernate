/*
package com.epam.learn.service;

import com.epam.learn.dao.impl.JpaTraineeDaoImpl;
import com.epam.learn.dao.impl.JpaTrainerDaoImpl;
import com.epam.learn.exception.InvalidUserDataException;
import com.epam.learn.exception.UserInitializationException;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private JpaTraineeDaoImpl traineeDao;

    @Mock
    private JpaTrainerDaoImpl trainerDao;

    @InjectMocks
    private TestUserService userService;

    @BeforeEach
    void setUp() {
        // Mock findByFirstNameAndLastName to return an empty list by default
        lenient().when(traineeDao.findByFirstNameAndLastName(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));
        lenient().when(trainerDao.findByFirstNameAndLastName(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(new PagedResponse<>(List.of(), 0, 10, 0, 0));
    }

    @Test
    void initializeUserShouldSetGeneratedUsernameAndPassword() {
        User user = new Trainee();
        user.setFirstName("John");
        user.setLastName("Doe");

        userService.initializeUser(user);

        assertThat(user.getUsername()).as("Username should be generated and set.").isNotNull();
        assertThat(user.getPassword()).as("Password should be 10 characters long.").hasSize(10);
        assertThat(user.getId()).as("User ID should be initialized if not provided.").isNotNull();
    }

    @Test
    void initializeUserShouldUseProvidedId() {
        UUID existingId = UUID.randomUUID();
        User user = new Trainee();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setId(existingId);

        userService.initializeUser(user);

        assertThat(user.getId()).as("Provided ID should be retained.").isEqualTo(existingId);
    }

    @Test
    void generateUsernameShouldGenerateUniqueUsername() {
        Trainee existingTrainee = new Trainee();
        existingTrainee.setUsername("John.Doe");

        when(traineeDao.findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE)).thenReturn(
                new PagedResponse<>(List.of(existingTrainee), 0, 10, 1, 1)
        );

        when(trainerDao.findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE)).thenReturn(
                new PagedResponse<>(List.of(), 0, 10, 0, 0)
        );

        String generatedUsername = userService.generateUsername("John", "Doe");

        assertThat(generatedUsername)
                .as("Username should be unique and start with 'John.Doe'.")
                .isNotEqualTo("John.Doe")
                .startsWith("John.Doe");

        verify(traineeDao).findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE);
        verify(trainerDao).findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE);
    }

    @Test
    void generateUsernameShouldAppendNumberIfAlreadyExists() {
        Trainee trainee1 = new Trainee();
        trainee1.setUsername("John.Doe");
        Trainee trainee2 = new Trainee();
        trainee2.setUsername("John.Doe1");
        Trainee trainee3 = new Trainee();
        trainee3.setUsername("John.Doe2");

        PagedResponse<Trainee> traineeResponse = new PagedResponse<>(
                List.of(trainee1, trainee2, trainee3),
                0,
                3,
                3L,
                1
        );

        when(traineeDao.findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE))
                .thenReturn(traineeResponse);

        PagedResponse<Trainer> trainerResponse = new PagedResponse<>(
                List.of(),
                0,
                0,
                0L,
                0
        );

        when(trainerDao.findByFirstNameAndLastName("John", "Doe", 0, Integer.MAX_VALUE))
                .thenReturn(trainerResponse);

        String generatedUsername = userService.generateUsername("John", "Doe");
        assertThat(generatedUsername).isEqualTo("John.Doe3");
    }

    @Test
    void generateRandomPasswordShouldReturn10CharacterLengthPassword() {
        String password = userService.generateRandomPassword();

        assertThat(password).as("Password should be 10 characters long.").hasSize(10);
    }

    @Test
    void generateRandomPasswordShouldReturnDifferentPasswords() {
        String password1 = userService.generateRandomPassword();
        String password2 = userService.generateRandomPassword();

        assertThat(password1).as("Each generated password should be unique.").isNotEqualTo(password2);
    }

    @Test
    void generateUsernameShouldThrowExceptionWhenFirstNameIsNull() {
        assertThatThrownBy(() -> userService.generateUsername(null, "Doe"))
                .isInstanceOf(InvalidUserDataException.class)
                .hasMessageContaining("First name cannot be empty");
    }

    @Test
    void generateUsernameShouldThrowExceptionWhenFirstNameIsEmpty() {
        assertThatThrownBy(() -> userService.generateUsername("", "Doe"))
                .isInstanceOf(InvalidUserDataException.class)
                .hasMessageContaining("First name cannot be empty");
    }

    @Test
    void generateUsernameShouldThrowExceptionWhenLastNameIsNull() {
        assertThatThrownBy(() -> userService.generateUsername("John", null))
                .isInstanceOf(InvalidUserDataException.class)
                .hasMessageContaining("Last name cannot be empty");
    }

    @Test
    void generateUsernameShouldThrowExceptionWhenLastNameIsEmpty() {
        assertThatThrownBy(() -> userService.generateUsername("John", ""))
                .isInstanceOf(InvalidUserDataException.class)
                .hasMessageContaining("Last name cannot be empty");
    }

    @Test
    void initializeUserShouldThrowExceptionWhenUserIsNull() {
        assertThatThrownBy(() -> userService.initializeUser(null))
                .isInstanceOf(UserInitializationException.class)
                .hasMessageContaining("User cannot be null");
    }

    private static class TestUserService extends AbstractUserService {
        public TestUserService(JpaTraineeDaoImpl traineeDao, JpaTrainerDaoImpl trainerDao) {
            super(traineeDao, trainerDao);
        }
    }
}
*/
