package com.epam.learn.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for EntityManager.
 * This class provides a bean for EntityManager that can be injected into DAOs.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class EntityManagerConfig {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Creates a bean for EntityManager.
     * This bean can be injected into DAOs.
     *
     * @return the EntityManager
     */
    @Bean
    public EntityManager entityManager() {
        LOGGER.debug("Creating EntityManager from EntityManagerFactory");
        return entityManagerFactory.createEntityManager();
    }
}
