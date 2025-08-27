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
 * Enforces role-based rules when updating a {@link UserRequestDTO}.
 * <p>
 * Applies constraints based on {@link UserRole}:
 * <ul>
 *     <li>{@link UserRole#ADMINISTRATOR} has no restrictions.</li>
 *     <li>{@link UserRole#MANAGER} cannot update password or role.</li>
 *     <li>All other roles are not allowed to update users.</li>
 * </ul>
 */
@Component
@AllArgsConstructor
public class UserUpdatePolicy implements CrudPolicy<UserRequestDTO> {

	/**
	 * Validates that the given {@link UserRequestDTO} can be updated by a user with the specified {@link UserRole}.
	 * <p>
	 * Throws {@link AccessDeniedException} if role-specific rules are violated.
	 *
	 * @param role    {@link UserRole} of the user attempting to update
	 * @param request {@link UserRequestDTO} to validate; may be null
	 * @throws AccessDeniedException if validation fails or role is not permitted
	 */
	@Override
	public void validate(UserRole role, @Nullable UserRequestDTO request) throws AccessDeniedException {
		switch (role) {
			case ADMINISTRATOR -> {
				// Admin has no restrictions
			}
			case MANAGER -> {
				if (request != null && (StringUtils.isEmpty(request.getPassword()) || request.getRole() != null)) {
					throw new AccessDeniedException("Insufficient privileges.");
				}
			}
			default -> throw new AccessDeniedException("Unsupported role: " + role);
		}
	}
}
