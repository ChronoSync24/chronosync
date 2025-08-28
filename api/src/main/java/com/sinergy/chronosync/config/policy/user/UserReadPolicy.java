package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.config.policy.CrudPolicy;
import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.service.SecurityContextService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Enforces role-based rules when reading {@link PaginatedUserRequestDTO}.
 * <p>
 * Adjusts request parameters based on {@link UserRole}:
 * <ul>
 *     <li>{@link UserRole#ADMINISTRATOR} has no restrictions.</li>
 *     <li>{@link UserRole#MANAGER} is limited to users within their firm and roles {@link UserRole#EMPLOYEE} and {@link UserRole#MANAGER}.</li>
 *     <li>All other roles are not allowed to read users.</li>
 * </ul>
 */
@Component
@AllArgsConstructor
public class UserReadPolicy implements CrudPolicy<PaginatedUserRequestDTO> {

	private final SecurityContextService securityContextService;

	/**
	 * Validates and adjusts {@link PaginatedUserRequestDTO} based on {@link UserRole}.
	 * <p>
	 * Sets firm ID and allowed roles for managers; administrators are unrestricted.
	 *
	 * @param role    {@link UserRole} of the user attempting to read
	 * @param request {@link PaginatedUserRequestDTO} to validate and adjust; must not be null
	 * @throws AccessDeniedException if role is not permitted
	 */
	@Override
	public void validate(UserRole role, PaginatedUserRequestDTO request) throws AccessDeniedException {
		switch (role) {
			case ADMINISTRATOR -> {
				// Admin has no restrictions
			}
			case MANAGER -> {
				request.setFirmId(securityContextService.getAuthUserFirm().getId());
				request.setRoles(List.of(UserRole.EMPLOYEE, UserRole.MANAGER));
			}
			default -> throw new AccessDeniedException("Unsupported role: " + role);
		}
	}
}
