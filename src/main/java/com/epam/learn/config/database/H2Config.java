package com.epam.learn.config.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration for H2 database.
 * This configuration is active when the "h2" profile is active.
 */
@Slf4j
@Primary
@Profile("h2")
@Configuration
public class H2Config implements DatabaseConfig {

    @Value("${h2.url}")
    private String url;

    @Value("${h2.username}")
    private String username;

    @Value("${h2.password}")
    private String password;

    /**
     * Creates and configures a data source for H2 database.
     *
     * @return the configured data source
     */
    @Override
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
