package com.sinergy.chronosync.service;

import com.sinergy.chronosync.config.JwtUserPrincipal;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
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
	 * @return {@link User} authenticated user
	 */
	public Long getAuthUserId() {
		return getUserPrincipal().getId();
	}

	/**
	 * Retrieves the authenticated user's associated firm.
	 *
	 * <p>This method extracts the currently authenticated username from the security context
	 * and constructs a filter to query the user repository. If the user is found, their associated
	 * firm is returned.
	 *
	 * @return the {@link Firm} associated with the authenticated user
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

	/**
	 * Extracts user role from granted authorities.
	 *
	 * @return {@link UserRole} authenticated user role
	 * @throws InvalidStateException in case user is not associated with any role
	 */
	public UserRole getAuthUserRole() {
		GrantedAuthority authority = getUserPrincipal().getAuthorities().getFirst();

		if (authority == null || authority.getAuthority().isEmpty()) {
			throw new InvalidStateException("User is not associated with any role.");
		}

		return UserRole.valueOf(authority.getAuthority().replaceFirst("^ROLE_", ""));
	}
}
