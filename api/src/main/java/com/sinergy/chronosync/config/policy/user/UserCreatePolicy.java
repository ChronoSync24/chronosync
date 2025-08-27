package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.config.policy.CrudPolicy;
import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.util.StringUtils;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Policy that enforces role-based rules when creating a {@link UserRequestDTO}.
 * <p>
 * Validates that required fields are present and applies role-specific constraints:
 * <ul>
 *     <li>Administrators can create any user.</li>
 *     <li>Managers can only create users with the {@link UserRole#EMPLOYEE} role.</li>
 *     <li>Other roles are not allowed to create users.</li>
 * </ul>
 */
@Component
@AllArgsConstructor
public class UserCreatePolicy implements CrudPolicy<UserRequestDTO> {

	/**
	 * Validates that the given {@link UserRequestDTO} can be created by a user with the specified {@link UserRole}.
	 * <p>
	 * Checks for non-empty required fields and enforces role-specific creation rules:
	 * <ul>
	 *     <li>{@link UserRole#ADMINISTRATOR} has no restrictions.</li>
	 *     <li>{@link UserRole#MANAGER} can only create {@link UserRole#EMPLOYEE} users.</li>
	 *     <li>All other roles are denied.</li>
	 * </ul>
	 *
	 * @param role    {@link UserRole} of the user attempting to create the entity
	 * @param request {@link UserRequestDTO} to validate; must not be null
	 * @throws AccessDeniedException if validation fails or the role is not permitted
	 */
	@Override
	public void validate(UserRole role, @Nullable UserRequestDTO request) throws AccessDeniedException {
		assert request != null;

		if (StringUtils.isEmpty(request.getPassword()) ||
			StringUtils.isEmpty(request.getFirstName()) ||
			StringUtils.isEmpty(request.getLastName()) ||
			StringUtils.isEmpty(request.getEmail()) ||
			StringUtils.isEmpty(request.getPhone()) ||
			StringUtils.isEmpty(request.getUniqueIdentifier()) ||
			StringUtils.isEmpty(request.getAddress()) ||
			request.getRole() == null ||
			request.getIsEnabled() == null
		) {
			throw new AccessDeniedException("Entity cannot be empty.");
		}

		switch (role) {
			case ADMINISTRATOR -> {
				// Admin has no restrictions
			}
			case MANAGER -> {
				if (!request.getRole().equals(UserRole.EMPLOYEE)) {
					throw new AccessDeniedException("Manager can only create employee users.");
				}
			}
			default -> throw new AccessDeniedException("Unsupported role: " + role);
		}
	}
}
