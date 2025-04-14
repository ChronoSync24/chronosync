package com.sinergy.chronosync.service;

import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for retrieving security context information.
 */
@Service
@RequiredArgsConstructor
public class SecurityContextService {

	private final UserRepository userRepository;

	/**
	 * Retrieves the authenticated user.
	 *
	 * <p>This method extracts the currently authenticated username from the security context
	 * and constructs a filter to query the user repository. If the user is found, it is returned.
	 *
	 * @throws UserNotFoundException if no user is found with the authenticated username
	 */
	public User getAuthUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findOne(UserFilterBuilder.builder()
				.username(username)
				.build()
				.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	/**
	 * Retrieves the authenticated user's associated firm.
	 *
	 * <p>This method extracts the currently authenticated username from the security context
	 * and constructs a filter to query the user repository. If the user is found, their associated
	 * firm is returned.
	 *
	 * @return the {@link Firm} associated with the authenticated user
	 * @throws UserNotFoundException if no user is found with the authenticated username
	 * @throws InvalidStateException if the user is not associated with any firm
	 */
	public Firm getAuthUserFirm() {
		User user = getAuthUser();
		Firm firm = user.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}
		return firm;
	}
}
