package com.epam.learn.exception;

/**
 * Exception thrown when storage initialization fails.
 */
public class FailedStorageInitializationException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public FailedStorageInitializationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public FailedStorageInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}