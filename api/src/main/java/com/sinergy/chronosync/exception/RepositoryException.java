package com.sinergy.chronosync.exception;

/**
 * Exception indicating an error related to repository operations.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) { super(message); }
}
