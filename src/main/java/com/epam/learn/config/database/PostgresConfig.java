package com.epam.learn.config.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile({"default", "local"})
public class PostgresConfig implements DatabaseConfig {

    @Value("${postgres.database}")
    private String database;

    @Value("${postgres.username}")
    private String username;

    @Value("${postgres.password}")
    private String password;

    @Value("${postgres.host:localhost}")
    private String postgresHost;

    @Value("${postgres.port:5432}")
    private int postgresPort;

    private static final String POSTGRES_URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s";

    /**
     * Основне джерело даних для використання в EntityManagerFactory.
     *
     * @return основний DataSource
     */
    @Bean(name = "dataSource")
    @Primary
    @Override
    public DataSource dataSource() {
        Properties connectionProperties = createConnectionProperties("training-application", 10, 30, 10);
        return createDataSource(connectionProperties);
    }

    /**
     * Окреме джерело даних, призначене для Flyway.
     *
     * @return DataSource для Flyway
     */
    @Bean(name = "flywayDataSource")
    public DataSource flywayDataSource() {
        Properties connectionProperties = createConnectionProperties("training-application-flyway", 30, 60, 30);
        return createDataSource(connectionProperties);
    }

    /**
     * Метод для створення властивостей з’єднання.
     *
     * @param appName         назва застосунку
     * @param connectTimeout  таймаут підключення
     * @param socketTimeout   таймаут сокету
     * @param loginTimeout    таймаут логіну
     * @return властивості з’єднання
     */
    private Properties createConnectionProperties(String appName, int connectTimeout, int socketTimeout, int loginTimeout) {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("ApplicationName", appName);
        connectionProperties.setProperty("connectTimeout", String.valueOf(connectTimeout));
        connectionProperties.setProperty("socketTimeout", String.valueOf(socketTimeout));
        connectionProperties.setProperty("loginTimeout", String.valueOf(loginTimeout));
        connectionProperties.setProperty("tcpKeepAlive", "true");
        connectionProperties.setProperty("ssl", "false");
        connectionProperties.setProperty("autoReconnect", "true");
        return connectionProperties;
    }

    /**
     * Метод для створення DriverManagerDataSource.
     *
     * @param connectionProperties властивості з’єднання
     * @return готовий DataSource
     */
    private DataSource createDataSource(Properties connectionProperties) {
        String url = String.format(POSTGRES_URL_TEMPLATE, postgresHost, postgresPort, database);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setConnectionProperties(connectionProperties);

        return dataSource;
    }
}