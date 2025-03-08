package com.sinergy.chronosync.exception;

/**
 * Exception indicating an error related to JWT operations.
 */
public class TokenException extends RuntimeException {
	public TokenException(String message) {
		super(message);
	}
}
