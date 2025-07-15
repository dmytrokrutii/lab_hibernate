package com.epam.learn;

import com.epam.learn.config.AppConfig;
import com.epam.learn.console.CommandLineRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    public static void main(String[] args) {
        try {
            LOGGER.info("Initializing application context...");
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(AppConfig.class);

            LOGGER.info("Application context initialized successfully");

            // Get all beans that implement CommandLineRunner
            String[] beanNames = context.getBeanNamesForType(CommandLineRunner.class);
            if (beanNames.length > 0) {
                LOGGER.info("Running CommandLineRunner beans...");
                for (String beanName : beanNames) {
                    CommandLineRunner runner = context.getBean(beanName, CommandLineRunner.class);
                    runner.run(args);
                }
            } else {
                LOGGER.warn("No CommandLineRunner beans found");
            }

            context.registerShutdownHook();
        } catch (Exception e) {
            LOGGER.error("Error starting application", e);
            System.exit(1);
        }
    }
}
