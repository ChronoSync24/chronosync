package com.sinergy.chronosync.service;

import com.sinergy.chronosync.config.JwtUserPrincipal;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for retrieving security context information.
 */
@Service
@RequiredArgsConstructor
public class SecurityContextService {

	/**
	 * Helper function that extracts UserPrincipal from Security Context Holder.
	 *
	 * @return {@link JwtUserPrincipal}
	 */
	private JwtUserPrincipal getUserPrincipal() {
		return (JwtUserPrincipal) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
	}

	/**
	 * Retrieves the authenticated user.
	 *
	 * <p>This method extracts the currently authenticated username from the security context
	 * and constructs a filter to query the user repository. If the user is found, it is returned.
	 *
	 * @throws UserNotFoundException if no user is found with the authenticated username
	 */
	public User getAuthUser() {
		User user = new User();
		user.setId(getUserPrincipal().getId());

		return user;
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
		Firm firm = new Firm();
		firm.setId(getUserPrincipal().getFirmId());

		if (firm.getId() == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		return firm;
	}
}
