package com.epam.learn.config;

import com.epam.learn.config.database.DatabaseConfig;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@Profile("default")
@AllArgsConstructor
@EnableTransactionManagement
public class HibernateConfig {

    private final Environment env;
    private final DatabaseConfig databaseConfig;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        LOGGER.debug("Creating DataSource");
        try {
            DataSource ds = databaseConfig.dataSource();
            LOGGER.debug("DataSource created successfully");
            return ds;
        } catch (Exception e) {
            LOGGER.error("Error creating DataSource", e);
            throw e;
        }
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) { // Додаємо @Qualifier
        LOGGER.debug("Configuring EntityManagerFactory");
        try {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource); // Використовуємо основний DataSource
            em.setPackagesToScan("com.epam.learn.model");

            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);

            Properties props = hibernateProperties();
            LOGGER.debug("Hibernate properties: {}", props);
            em.setJpaProperties(props);

            LOGGER.info("EntityManagerFactory configured successfully");
            return em;
        } catch (Exception e) {
            LOGGER.error("Error configuring EntityManagerFactory", e);
            throw e;
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }


    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql", "true"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql", "true"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "validate"));
        properties.put("hibernate.connection.pool_size", "10");
        properties.put("hibernate.connection.autocommit", "true");
        return properties;
    }
}
