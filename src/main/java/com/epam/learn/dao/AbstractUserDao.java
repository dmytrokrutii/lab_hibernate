package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.User;
import com.epam.learn.util.validate.DaoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Abstract implementation of UserDao using JPA.
 * This class provides the base implementation for all user-related DAOs that use JPA.
 *
 * @param <T> the user entity type
 */
@Slf4j
public abstract class AbstractUserDao<T extends User> extends AbstractDao<T> implements UserDao<T> {

    protected AbstractUserDao(EntityManager entityManager, Class<T> entityClass) {
        super(entityManager, entityClass);
    }

    @Override
    public Optional<T> findByUsername(String username) {
        LOGGER.debug("Finding user by username: {}", username);
        DaoValidator.validateUsername(username);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());

        cq.select(root)
          .where(cb.equal(root.get("username"), username));

        TypedQuery<T> query = entityManager.createQuery(cq);

        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            LOGGER.debug("No user found with username: {}", username);
            return Optional.empty();
        }
    }

    @Override
    public PagedResponse<T> findByFirstNameAndLastName(String firstName, String lastName, int page, int size) {
        LOGGER.debug("Finding users by firstName: {} and lastName: {}", firstName, lastName);
        DaoValidator.validateFirstName(firstName);
        DaoValidator.validateLastName(lastName);
        DaoValidator.validatePagination(page, size);

        return createPagedResponse(page, size, (cb, root) -> 
            cb.and(
                cb.equal(root.get("firstName"), firstName),
                cb.equal(root.get("lastName"), lastName)
            )
        );
    }

}
