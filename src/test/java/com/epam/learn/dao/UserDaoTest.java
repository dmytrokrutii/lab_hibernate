/*
package com.epam.learn.dao;

import com.epam.learn.exception.EntityNotFoundException;
import com.epam.learn.model.EntityType;
import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.storage.GenericStorage;
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
class UserDaoTest {

    @Mock
    private GenericStorage<Trainee> storage;

    private AbstractUserDao<Trainee> userDao;

    private UUID traineeId;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        userDao = new AbstractUserDao<>(storage) {
            @Override
            protected EntityType getEntityType() {
                return EntityType.TRAINEE;
            }

            @Override
            public Optional<Trainee> findByUsername(String username) {
                return Optional.empty(); // Not used in these tests
            }
        };

        traineeId = UUID.randomUUID();
        trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setActive(true);
    }

    @Test
    void saveShouldStoreEntityInStorage() {
        userDao.save(trainee);

        verify(storage).save(traineeId, trainee);
    }

    @Test
    void findByIdShouldReturnEntityWhenFound() {
        when(storage.findById(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = userDao.findById(traineeId);

        assertThat(result).contains(trainee);
        verify(storage).findById(traineeId);
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenEntityNotFound() {
        when(storage.findById(traineeId)).thenReturn(Optional.empty());

        Optional<Trainee> result = userDao.findById(traineeId);

        assertThat(result).isEmpty();
        verify(storage).findById(traineeId);
    }

    @Test
    void deleteShouldRemoveEntityById() {
        userDao.delete(traineeId);

        verify(storage).delete(traineeId);
    }

    @Test
    void findAllShouldReturnPagedResults() {
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        when(storage.findAll()).thenReturn(List.of(trainee1, trainee2));

        PagedResponse<Trainee> result = userDao.findAll(0, 2);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(2);
        verify(storage).findAll();
    }

    @Test
    void updateShouldModifyEntityWhenExists() {
        when(storage.update(traineeId, trainee)).thenReturn(true);

        userDao.update(traineeId, trainee);

        verify(storage).update(traineeId, trainee);
    }

    @Test
    void updateShouldThrowExceptionWhenEntityDoesNotExist() {
        when(storage.update(traineeId, trainee)).thenReturn(false);

        assertThatThrownBy(() -> userDao.update(traineeId, trainee))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(EntityType.TRAINEE.name());

        verify(storage).update(traineeId, trainee);
    }

    @Test
    void findAllShouldReturnEmptyResponseForOutOfRangePage() {
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();

        when(storage.findAll()).thenReturn(List.of(trainee1, trainee2));

        PagedResponse<Trainee> result = userDao.findAll(2, 2);

        assertThat(result).isNotNull();
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(1);

        verify(storage, times(1)).findAll();
    }

    @Test
    void saveShouldGenerateNewIdWhenEntityIdIsNull() {
        Trainee traineeWithoutId = new Trainee();
        traineeWithoutId.setId(null);
        traineeWithoutId.setFirstName("Jane");
        traineeWithoutId.setLastName("Smith");

        userDao.save(traineeWithoutId);

        assertThat(traineeWithoutId.getId()).isNotNull();
        verify(storage).save(traineeWithoutId.getId(), traineeWithoutId);
    }
}
*/
