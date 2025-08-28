package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.model.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link UserUpdatePolicy}.
 */
class UserUpdatePolicyTest {

	private UserUpdatePolicy userUpdatePolicy;

	@BeforeEach
	void setUp() {
		userUpdatePolicy = new UserUpdatePolicy();
	}

	/**
	 * Tests that administrator can update any user field.
	 */
	@Test
	void administratorRoleCanUpdateAnyFieldTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setPassword("newPassword");
		request.setRole(UserRole.ADMINISTRATOR);

		assertDoesNotThrow(() -> userUpdatePolicy.validate(UserRole.ADMINISTRATOR, request));
	}

	/**
	 * Tests that manager can update user without password or role changes.
	 */
	@Test
	void managerRoleCanUpdateWithoutPasswordOrRoleTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setPassword(null);
		request.setRole(null);

		assertDoesNotThrow(() -> userUpdatePolicy.validate(UserRole.MANAGER, request));
	}

	/**
	 * Tests that manager cannot update user password.
	 */
	@Test
	void managerRoleCannotUpdatePasswordTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setPassword("newPassword");
		request.setRole(null);

		assertThrows(AccessDeniedException.class,
			() -> userUpdatePolicy.validate(UserRole.MANAGER, request));
	}

	/**
	 * Tests that manager cannot update user role.
	 */
	@Test
	void managerRoleCannotUpdateRoleTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setPassword(null);
		request.setRole(UserRole.ADMINISTRATOR);

		assertThrows(AccessDeniedException.class,
			() -> userUpdatePolicy.validate(UserRole.MANAGER, request));
	}

	/**
	 * Tests that employee cannot update any users.
	 */
	@Test
	void employeeRoleCannotUpdateUsersTest() {
		UserRequestDTO request = createValidUserRequest();

		assertThrows(AccessDeniedException.class,
			() -> userUpdatePolicy.validate(UserRole.EMPLOYEE, request));
	}

	/**
	 * Creates mocked valid user request.
	 *
	 * @return {@link UserRequestDTO} user request DTO
	 */
	private UserRequestDTO createValidUserRequest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("john.doe@example.com");
		return request;
	}
}