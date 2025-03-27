package com.sinergy.chronosync.exception;

/**
 * Exception indicating an entity could not be found.
 */
public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String message) {
		super(message);
	}
}
