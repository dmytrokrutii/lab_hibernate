package com.epam.learn.dao.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a Data Access Object (DAO).
 * This annotation is a specialization of {@link Component} and is used to indicate
 * that the annotated class is a "Data Access Object", which provides
 * an abstraction layer for some type of data storage.
 *
 * <p>Classes annotated with @Dao will be automatically detected by Spring's
 * component scanning and registered as beans in the application context.</p>
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any (or empty String otherwise)
     */
    String value() default "";
}