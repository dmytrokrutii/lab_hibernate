package com.epam.learn.console;

/**
 * Interface used to indicate that a bean should run when it is contained within a Spring application.
 * Multiple CommandLineRunner beans can be defined within the same application context and can be ordered
 * using the Ordered interface or @Order annotation.
 */
public interface CommandLineRunner {

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    void run(String... args) throws Exception;
}