package com.epam.learn.config;

import com.epam.learn.config.database.PostgresFlywayConfig;
import com.epam.learn.config.database.H2Config;
import com.epam.learn.config.database.PostgresConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.epam.learn.console", "com.epam.learn.service", "com.epam.learn.dao", "com.epam.learn.config"})
@Import({PropertyConfig.class, PostgresConfig.class, PostgresFlywayConfig.class, H2Config.class, HibernateConfig.class, H2HibernateConfig.class, EntityManagerConfig.class})
public class AppConfig {
    // Aggregates all configs
}
