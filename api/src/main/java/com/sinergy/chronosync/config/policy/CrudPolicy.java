package com.sinergy.chronosync.config.policy;

import com.sinergy.chronosync.model.user.UserRole;
import jakarta.annotation.Nullable;

/**
 * CRUD Policy interface that defines policy for CRUD operations.
 *
 * @param <R> request with the attributes
 */
public interface CrudPolicy<R> {

	/**
	 * Method that validates sent request.
	 *
	 * @param role    {@link UserRole} user role
	 * @param request {@link R} request
	 */
	void validate(UserRole role, @Nullable R request);
}

