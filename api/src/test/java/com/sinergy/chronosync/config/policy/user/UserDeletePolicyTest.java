package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserDeletePolicy}.
 */
class UserDeletePolicyTest {

	@Mock
	private UserRepository userRepository;

	private UserDeletePolicy userDeletePolicy;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userDeletePolicy = new UserDeletePolicy(userRepository);
	}

	/**
	 * Tests that administrator can delete employee users.
	 */
	@Test
	void administratorRoleDeleteEmployeeTest() {
		User targetUser = createUser(UserRole.EMPLOYEE);
		when(userRepository.findById(1L)).thenReturn(Optional.of(targetUser));

		assertDoesNotThrow(() -> userDeletePolicy.validate(UserRole.ADMINISTRATOR, 1L));
	}

	/**
	 * Tests that administrator can delete administrator users.
	 */
	@Test
	void administratorRoleDeleteAdministratorTest() {
		User targetUser = createUser(UserRole.ADMINISTRATOR);
		when(userRepository.findById(1L)).thenReturn(Optional.of(targetUser));

		assertDoesNotThrow(() -> userDeletePolicy.validate(UserRole.ADMINISTRATOR, 1L));
	}

	/**
	 * Tests that manager can delete employee users.
	 */
	@Test
	void managerRoleDeleteEmployeeTest() {
		User targetUser = createUser(UserRole.EMPLOYEE);
		when(userRepository.findById(1L)).thenReturn(Optional.of(targetUser));

		assertDoesNotThrow(() -> userDeletePolicy.validate(UserRole.MANAGER, 1L));
	}

	/**
	 * Tests that manager cannot delete administrator users.
	 */
	@Test
	void managerRoleDeleteAdministratorTest() {
		User targetUser = createUser(UserRole.ADMINISTRATOR);
		when(userRepository.findById(1L)).thenReturn(Optional.of(targetUser));

		assertThrows(AccessDeniedException.class,
			() -> userDeletePolicy.validate(UserRole.MANAGER, 1L));
	}

	/**
	 * Tests that employee cannot delete any users.
	 */
	@Test
	void employeeRoleDeleteAnyUserTest() {
		User targetUser = createUser(UserRole.EMPLOYEE);
		when(userRepository.findById(1L)).thenReturn(Optional.of(targetUser));

		assertThrows(AccessDeniedException.class,
			() -> userDeletePolicy.validate(UserRole.EMPLOYEE, 1L));
	}

	/**
	 * Tests that non-existent user throws entity not found exception.
	 */
	@Test
	void userNotFoundTest() {
		when(userRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class,
			() -> userDeletePolicy.validate(UserRole.ADMINISTRATOR, 999L));
	}

	/**
	 * Helper method that creates mock user with provided user role.
	 *
	 * @param role {@link UserRole} user role
	 * @return {@link User} created user
	 */
	private User createUser(UserRole role) {
		User user = new User();
		user.setId(1L);
		user.setRole(role);
		return user;
	}
}