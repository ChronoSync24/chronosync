package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.dto.response.UserResponseDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl}.
 */
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private SecurityContextService securityContextService;

	@InjectMocks
	private UserServiceImpl userService;

	private User testUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		testUser = new User();
		testUser.setId(1L);
		testUser.setFirstName("John");
		testUser.setLastName("Doe");
		testUser.setUsername("jdoe");
		testUser.setPassword("encodedPassword");
		testUser.setRole(UserRole.EMPLOYEE);
		testUser.setIsEnabled(true);
	}

	/**
	 * Tests the {@link UserServiceImpl#create(UserRequestDTO)} method.
	 * Verifies that a user is created successfully with a unique username.
	 */
	@Test
	void createUserTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setFirstName("Test");
		request.setLastName("User");
		request.setPassword("password123");

		Page<User> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 50), 0);

		when(userRepository.findAll(Mockito.<Specification<User>>any(), any(Pageable.class))).thenReturn(emptyPage);
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
		when(securityContextService.getAuthUserFirm()).thenReturn(null);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
			User user = invocation.getArgument(0);
			user.setId(1L);
			return user;
		});

		User response = userService.create(request);

		assertThat(response).isNotNull();
		assertThat(response.getUsername()).isEqualTo("tuser");
		assertThat(response.getPassword()).isNull();

		verify(userRepository, times(1)).findAll(Mockito.<Specification<User>>any(), any(Pageable.class));
		verify(passwordEncoder, times(1)).encode("password123");
		verify(userRepository, times(1)).save(any(User.class));
	}

	/**
	 * Tests the {@link UserServiceImpl#create(UserRequestDTO)} method
	 * when username already exists and needs to be made unique.
	 */
	@Test
	void createUserWithExistingUsernameTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setFirstName("Test");
		request.setLastName("User");
		request.setPassword("password123");

		User existingUser = new User();
		existingUser.setUsername("tuser");
		Page<User> existingPage = new PageImpl<>(List.of(existingUser), PageRequest.of(0, 50), 1);

		when(userRepository.findAll(Mockito.<Specification<User>>any(), any(Pageable.class))).thenReturn(existingPage);
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
		when(securityContextService.getAuthUserFirm()).thenReturn(null);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
			User user = invocation.getArgument(0);
			user.setId(1L);
			return user;
		});

		User response = userService.create(request);

		assertThat(response).isNotNull();
		assertThat(response.getUsername()).isEqualTo("tuser2"); // Should be appended with number

		verify(userRepository, times(1)).findAll(Mockito.<Specification<User>>any(), any(Pageable.class));
		verify(userRepository, times(1)).save(any(User.class));
	}

	/**
	 * Tests the {@link UserServiceImpl#getUsers(PaginatedUserRequestDTO)} method.
	 * Verifies that users are retrieved with proper filtering and pagination.
	 */
	@Test
	void getUsersTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();
		request.setPage(0);
		request.setPageSize(10);
		request.setFirstName("John");
		request.setLastName("Doe");

		Page<User> userPage = new PageImpl<>(List.of(testUser), PageRequest.of(0, 10), 1);

		when(userRepository.findAll(Mockito.<Specification<User>>any(), any(Pageable.class))).thenReturn(userPage);

		Page<UserResponseDTO> response = userService.getUsers(request);

		assertThat(response).isNotNull();
		assertThat(response.getContent()).hasSize(1);
		assertThat(response.getContent().getFirst().getFirstName()).isEqualTo("John");
		assertThat(response.getContent().getFirst().getLastName()).isEqualTo("Doe");

		verify(userRepository, times(1)).findAll(Mockito.<Specification<User>>any(), any(Pageable.class));
	}

	/**
	 * Tests the {@link UserServiceImpl#getUsers(PaginatedUserRequestDTO)} method
	 * when no users match the filter criteria.
	 */
	@Test
	void getUsersEmptyResultTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();
		request.setPage(0);
		request.setPageSize(10);
		request.setFirstName("NonExistent");

		Page<User> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

		when(userRepository.findAll(Mockito.<Specification<User>>any(), any(Pageable.class))).thenReturn(emptyPage);

		Page<UserResponseDTO> response = userService.getUsers(request);

		assertThat(response).isNotNull();
		assertThat(response.getContent()).isEmpty();

		verify(userRepository, times(1)).findAll(Mockito.<Specification<User>>any(), any(Pageable.class));
	}

	/**
	 * Tests the {@link UserServiceImpl#updateUser(UserRequestDTO)} method
	 * when user exists and is updated by a regular user.
	 */
	@Test
	void updateUserByRegularUserTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setId(1L);
		request.setFirstName("Updated");
		request.setLastName("Name");
		request.setPassword("");
		request.setRole(UserRole.MANAGER);

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(securityContextService.getAuthUserRole()).thenReturn(UserRole.MANAGER);
		when(userRepository.update(any(User.class))).thenReturn(testUser);

		UserResponseDTO response = userService.updateUser(request);

		assertThat(response).isNotNull();
		assertThat(response.getFirstName()).isEqualTo("Updated");
		assertThat(response.getLastName()).isEqualTo("Name");
		assertThat(response.getRole()).isEqualTo(UserRole.EMPLOYEE);

		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).update(any(User.class));
		verify(securityContextService, times(1)).getAuthUserRole();
	}

	/**
	 * Tests the {@link UserServiceImpl#updateUser(UserRequestDTO)} method
	 * when user exists and is updated by an administrator.
	 */
	@Test
	void updateUserByAdministratorTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setId(1L);
		request.setFirstName("Updated");
		request.setLastName("Name");
		request.setPassword("newPassword");
		request.setRole(UserRole.ADMINISTRATOR);

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(securityContextService.getAuthUserRole()).thenReturn(UserRole.ADMINISTRATOR);
		when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
		when(userRepository.update(any(User.class))).thenReturn(testUser);

		UserResponseDTO response = userService.updateUser(request);

		assertThat(response).isNotNull();
		assertThat(response.getFirstName()).isEqualTo("Updated");
		assertThat(response.getLastName()).isEqualTo("Name");
		assertThat(response.getRole()).isEqualTo(UserRole.ADMINISTRATOR);

		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).update(any(User.class));
		verify(securityContextService, times(1)).getAuthUserRole();
		verify(passwordEncoder, times(1)).encode("newPassword");
	}

	/**
	 * Tests the {@link UserServiceImpl#updateUser(UserRequestDTO)} method
	 * when user does not exist.
	 */
	@Test
	void updateUserNotFoundTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setId(999L);

		when(userRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> userService.updateUser(request));

		verify(userRepository, times(1)).findById(999L);
		verify(userRepository, never()).update(any(User.class));
	}

	/**
	 * Tests the {@link UserServiceImpl#deleteUser(Long)} method
	 * when user exists and has associated tokens.
	 */
	@Test
	void deleteUserTest() {
		when(userRepository.existsById(1L)).thenReturn(true);
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(new Token()));

		userService.deleteUser(1L);

		verify(userRepository, times(1)).existsById(1L);
		verify(tokenRepository, times(1)).findOne(Mockito.<Specification<Token>>any());
		verify(tokenRepository, times(1)).delete(any(Token.class));
		verify(userRepository, times(1)).deleteById(1L);
	}

	/**
	 * Tests the {@link UserServiceImpl#deleteUser(Long)} method
	 * when user does not exist.
	 */
	@Test
	void deleteUserNotFoundTest() {
		when(userRepository.existsById(999L)).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(999L));

		verify(userRepository, times(1)).existsById(999L);
		verify(tokenRepository, never()).findOne(Mockito.<Specification<Token>>any());
		verify(userRepository, never()).deleteById(anyLong());
	}

	/**
	 * Tests the {@link UserServiceImpl#updateUser(UserRequestDTO)} method
	 * when administrator provides empty password (should not update password).
	 */
	@Test
	void updateUserByAdministratorWithEmptyPasswordTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setId(1L);
		request.setFirstName("Updated");
		request.setLastName("Name");
		request.setPassword("");
		request.setRole(UserRole.ADMINISTRATOR);

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(securityContextService.getAuthUserRole()).thenReturn(UserRole.ADMINISTRATOR);
		when(userRepository.update(any(User.class))).thenReturn(testUser);

		UserResponseDTO response = userService.updateUser(request);

		assertThat(response).isNotNull();
		verify(passwordEncoder, never()).encode(anyString());
	}

	/**
	 * Tests the {@link UserServiceImpl#getUsers(PaginatedUserRequestDTO)} method
	 * with all possible filter criteria.
	 */
	@Test
	void getUsersWithAllFiltersTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();
		request.setPage(0);
		request.setPageSize(10);
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setUsername("johndoe");
		request.setUniqueIdentifier("12345");
		request.setFirmId(1L);
		request.setRoles(List.of(UserRole.EMPLOYEE));

		Page<User> userPage = new PageImpl<>(List.of(testUser), PageRequest.of(0, 10), 1);

		when(userRepository.findAll(Mockito.<Specification<User>>any(), any(Pageable.class))).thenReturn(userPage);

		Page<UserResponseDTO> response = userService.getUsers(request);

		assertThat(response).isNotNull();
		assertThat(response.getContent()).hasSize(1);

		verify(userRepository, times(1)).findAll(Mockito.<Specification<User>>any(), any(Pageable.class));
	}

	/**
	 * Tests the {@link UserServiceImpl#updateUser(UserRequestDTO)} method
	 * when administrator provides a new password.
	 */
	@Test
	void updateUserByAdministratorWithNewPasswordTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setId(1L);
		request.setFirstName("Updated");
		request.setLastName("Name");
		request.setPassword("newPassword123");
		request.setRole(UserRole.ADMINISTRATOR);

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(securityContextService.getAuthUserRole()).thenReturn(UserRole.ADMINISTRATOR);
		when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
		when(userRepository.update(any(User.class))).thenReturn(testUser);

		UserResponseDTO response = userService.updateUser(request);

		assertThat(response).isNotNull();
		verify(passwordEncoder, times(1)).encode("newPassword123");
	}
}