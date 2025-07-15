package com.epam.learn.config.database;

import javax.sql.DataSource;

/**
 * Interface for database configuration.
 * Implementations should provide a configured data source.
 */
public interface DatabaseConfig {

    /**
     * Returns a configured data source for the database.
     * This can be any implementation of the DataSource interface,
     * such as a simple connection provider or a connection pool.
     * 
     * @return the data source
     */
    DataSource dataSource();
}
