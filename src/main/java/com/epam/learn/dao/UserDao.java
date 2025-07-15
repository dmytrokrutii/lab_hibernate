package com.epam.learn.dao;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.user.User;

import java.util.Optional;

public interface UserDao<T extends User> extends GenericDao<T> {

    /**
     * Finds a user by their unique username.
     *
     * @param username the unique username of the user to search for; must not be null or empty
     * @return an {@code Optional<T>} containing the user if found, otherwise an empty {@code Optional}
     */
    Optional<T> findByUsername(String username);

    /**
     * Retrieves a paginated list of users whose first name and last name match the provided values.
     *
     * @param firstName the first name to search for; must not be null or empty
     * @param lastName  the last name to search for; must not be null or empty
     * @param page      the zero-based index of the page to retrieve; must be non-negative
     * @param size      the number of items to include in each page; must be a positive integer
     * @return a {@code PagedResponse<T>} containing the list of users with the given first name and last name,
     * along with pagination metadata
     * @throws IllegalArgumentException if the page is negative or the size is not a positive integer
     */
    PagedResponse<T> findByFirstNameAndLastName(String firstName, String lastName, int page, int size);
}
