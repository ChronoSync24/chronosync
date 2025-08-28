package com.sinergy.chronosync.config.policy.user;

import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.service.SecurityContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserReadPolicy}.
 */
class UserReadPolicyTest {

	@Mock
	private SecurityContextService securityContextService;

	private UserReadPolicy userReadPolicy;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userReadPolicy = new UserReadPolicy(securityContextService);
	}

	/**
	 * Tests that administrator has no restrictions on user read access.
	 */
	@Test
	void administratorRoleNoRestrictionsTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();

		assertDoesNotThrow(() -> userReadPolicy.validate(UserRole.ADMINISTRATOR, request));
	}

	/**
	 * Tests that manager role sets firm ID and allowed roles.
	 */
	@Test
	void managerRoleSetsFirmIdAndRolesTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();

		Firm firm = new Firm();
		firm.setId(1L);
		firm.setName("Test Firm");

		when(securityContextService.getAuthUserFirm()).thenReturn(firm);

		assertDoesNotThrow(() -> userReadPolicy.validate(UserRole.MANAGER, request));

		assertEquals(1L, request.getFirmId());
		assertEquals(2, request.getRoles().size());
	}

	/**
	 * Tests that employee role cannot read users.
	 */
	@Test
	void employeeRoleCannotReadUsersTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();

		assertThrows(AccessDeniedException.class,
			() -> userReadPolicy.validate(UserRole.EMPLOYEE, request));
	}
}
