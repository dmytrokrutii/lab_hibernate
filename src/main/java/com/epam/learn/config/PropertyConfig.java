package com.epam.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
    @PropertySource("classpath:application.properties"),
    @PropertySource("classpath:application-${spring.profiles.active:default}.properties")
})
public class PropertyConfig {

    /**
     * Creates a PropertySourcesPlaceholderConfigurer bean to enable property placeholder resolution.
     * This bean is necessary for resolving ${...} Placeholders in @Value annotations.
     *
     * @return the PropertySourcesPlaceholderConfigurer bean
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}