package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.model.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link UserCreatePolicy}.
 */
class UserCreatePolicyTest {

	private UserCreatePolicy userCreatePolicy;

	@BeforeEach
	void setUp() {
		userCreatePolicy = new UserCreatePolicy();
	}

	/**
	 * Tests that administrator can create any user with valid request.
	 */
	@Test
	void administratorRoleValidRequestTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setRole(UserRole.ADMINISTRATOR);

		assertDoesNotThrow(() -> userCreatePolicy.validate(UserRole.ADMINISTRATOR, request));
	}

	/**
	 * Tests that manager can create employee users.
	 */
	@Test
	void managerRoleEmployeeRequestTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setRole(UserRole.EMPLOYEE);

		assertDoesNotThrow(() -> userCreatePolicy.validate(UserRole.MANAGER, request));
	}

	/**
	 * Tests that manager cannot create administrator users.
	 */
	@Test
	void managerRoleAdministratorRequestTest() {
		UserRequestDTO request = createValidUserRequest();
		request.setRole(UserRole.ADMINISTRATOR);

		assertThrows(AccessDeniedException.class,
			() -> userCreatePolicy.validate(UserRole.MANAGER, request));
	}

	/**
	 * Tests that employee cannot create any users.
	 */
	@Test
	void employeeRoleAnyRequestTest() {
		UserRequestDTO request = createValidUserRequest();

		assertThrows(AccessDeniedException.class,
			() -> userCreatePolicy.validate(UserRole.EMPLOYEE, request));
	}

	/**
	 * Tests that missing required fields throws access denied exception.
	 */
	@Test
	void missingRequiredFieldsTest() {
		UserRequestDTO request = new UserRequestDTO();

		assertThrows(AccessDeniedException.class,
			() -> userCreatePolicy.validate(UserRole.ADMINISTRATOR, request));
	}

	/**
	 * Helper function that creates {@link UserRequestDTO}
	 *
	 * @return {@link UserRequestDTO} mocked user request DTO
	 */
	private UserRequestDTO createValidUserRequest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setPassword("password123");
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("john.doe@example.com");
		request.setPhone("123-456-7890");
		request.setUniqueIdentifier("EMP123");
		request.setAddress("123 Main St");
		request.setRole(UserRole.EMPLOYEE);
		request.setIsEnabled(true);
		return request;
	}
}