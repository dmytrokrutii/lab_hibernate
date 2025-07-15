package com.epam.learn.config;

import com.epam.learn.config.database.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Hibernate configuration for H2 profile.
 * This configuration is active when the "h2" profile is active.
 * It overrides the entityManagerFactory bean to remove the dependency on Flyway.
 */
@Slf4j
@Profile("h2")
@Configuration
public class H2HibernateConfig extends HibernateConfig {

    public H2HibernateConfig(Environment env, DatabaseConfig databaseConfig) {
        super(env, databaseConfig);
    }


    /**
     * Creates and configures the EntityManagerFactory for H2 database.
     * This method overrides the parent method to remove the dependency on Flyway.
     * It does not have the @DependsOn("flyway") annotation because Flyway is disabled for H2.
     *
     * @param dataSource the data source to use
     * @return the configured EntityManagerFactory
     */
    @Bean
    @Primary
    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
        LOGGER.debug("Configuring EntityManagerFactory for H2");
        try {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource);
            em.setPackagesToScan("com.epam.learn.model");

            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);

            Properties props = hibernateProperties();
            LOGGER.debug("Hibernate properties for H2: {}", props);
            em.setJpaProperties(props);

            LOGGER.info("EntityManagerFactory for H2 configured successfully");
            return em;
        } catch (Exception e) {
            LOGGER.error("Error configuring EntityManagerFactory for H2", e);
            throw e;
        }
    }

    /**
     * Gets the Hibernate properties for H2 database.
     *
     * @return the Hibernate properties
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.connection.pool_size", "10");
        properties.put("hibernate.connection.autocommit", "true");

        // Load initial data from data.sql
        properties.put("hibernate.hbm2ddl.import_files", "data.sql");

        return properties;
    }
}
