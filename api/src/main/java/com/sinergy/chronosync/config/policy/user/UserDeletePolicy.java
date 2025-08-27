package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.config.policy.CrudPolicy;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Enforces role-based rules when deleting a {@link User}.
 * <p>
 * Validates access according to {@link UserRole}:
 * <ul>
 *     <li>{@link UserRole#ADMINISTRATOR} can delete any user.</li>
 *     <li>{@link UserRole#MANAGER} can only delete users with the {@link UserRole#EMPLOYEE} role.</li>
 *     <li>All other roles are not allowed to delete users.</li>
 * </ul>
 */
@Component
@AllArgsConstructor
public class UserDeletePolicy implements CrudPolicy<Long> {

	private final UserRepository userRepository;
	//TODO: Remove UserRepository from this class. Pass the whole User object for deletion.

	/**
	 * Validates that a {@link User} identified by the given ID can be deleted by a user with the specified {@link UserRole}.
	 * <p>
	 * Throws {@link AccessDeniedException} if role-specific rules are violated, or {@link EntityNotFoundException} if the user does not exist.
	 *
	 * @param role   {@link UserRole} of the user attempting the deletion
	 * @param userId {@link Long} ID of the {@link User} to delete; must not be null
	 * @throws AccessDeniedException   if validation fails or role is not permitted
	 * @throws EntityNotFoundException if {@link User} with the given ID does not exist
	 */
	@Override
	public void validate(UserRole role, @Nullable Long userId) throws AccessDeniedException, EntityNotFoundException {
		assert userId != null;

		User targetUser = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		switch (role) {
			case ADMINISTRATOR -> {
				// Admin has no restrictions
			}
			case MANAGER -> {
				if (!targetUser.getRole().equals(UserRole.EMPLOYEE)) {
					throw new AccessDeniedException("Manager can only delete employee users.");
				}
			}
			default -> throw new AccessDeniedException("Unsupported role: " + role);
		}
	}
}
