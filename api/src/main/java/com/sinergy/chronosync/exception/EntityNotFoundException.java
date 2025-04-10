package com.sinergy.chronosync.exception;

/**
 * Exception indicating an entity could not be found within the database.
 */
public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String message) {
		super(message);
	}
}
