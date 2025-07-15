package com.epam.learn.config.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Slf4j
@Configuration
@AllArgsConstructor
@Profile("default")
public class PostgresFlywayConfig {

    private Environment env;

    /**
     * Creates and configures a Flyway instance for database migrations.
     * This bean is explicitly configured to use the dedicated flywayDataSource.
     * This bean is only created when the default profile is active (PostgreSQL).
     *
     * @param flywayDataSource the dedicated data source for Flyway migrations
     * @return the configured Flyway instance
     */
    @Bean(name = "flyway", initMethod = "migrate")
    public Flyway flyway(@Qualifier("flywayDataSource") DataSource flywayDataSource) {
        LOGGER.info("Configuring Flyway with properties from application.properties");

        String locations = env.getProperty("spring.flyway.locations", "classpath:db/migration");
        boolean baselineOnMigrate = Boolean.parseBoolean(env.getProperty("spring.flyway.baseline-on-migrate", "true"));
        boolean validateOnMigrate = Boolean.parseBoolean(env.getProperty("spring.flyway.validate-on-migrate", "false"));
        boolean cleanDisabled = Boolean.parseBoolean(env.getProperty("spring.flyway.clean-disabled", "false"));
        boolean skipDefaultResolvers = Boolean.parseBoolean(env.getProperty("spring.flyway.skip-default-resolvers", "false"));
        boolean skipDefaultCallbacks = Boolean.parseBoolean(env.getProperty("spring.flyway.skip-default-callbacks", "false"));
        boolean group = Boolean.parseBoolean(env.getProperty("spring.flyway.group", "true"));
        int connectRetries = Integer.parseInt(env.getProperty("spring.flyway.connect-retries", "3"));

        LOGGER.info("Flyway configuration: locations={}, baselineOnMigrate={}, validateOnMigrate={}, cleanDisabled={}, skipDefaultResolvers={}, skipDefaultCallbacks={}, group={}, connectRetries={}",
                locations, baselineOnMigrate, validateOnMigrate, cleanDisabled, skipDefaultResolvers, skipDefaultCallbacks, group, connectRetries);

        return Flyway.configure()
                .dataSource(flywayDataSource)
                .locations(locations)
                .baselineOnMigrate(baselineOnMigrate)
                .validateOnMigrate(validateOnMigrate)
                .cleanDisabled(cleanDisabled)
                .skipDefaultResolvers(skipDefaultResolvers)
                .skipDefaultCallbacks(skipDefaultCallbacks)
                .outOfOrder(false)
                .placeholderReplacement(true)
                .group(group)
                .connectRetries(connectRetries)
                .ignoreMigrationPatterns("*:missing")
                .load();
    }
}
