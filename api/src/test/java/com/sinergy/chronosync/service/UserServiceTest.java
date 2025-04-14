package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl}.
 */
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private SecurityContextService securityContextService;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		User user = new User();
		when(securityContextService.getAuthUser()).thenReturn(user);
	}

	/**
	 * Tests the {@link UserServiceImpl#create(UserRequestDTO)} method.
	 * Verifies that a user is created successfully and that the correct methods are called
	 * for encoding the password and saving the user to the repository.
	 */
	@Test
	void createUserTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setFirstName("Test");
		request.setLastName("Test");

		User user = request.toModel(false);
		user.setPassword("encodedPassword");

		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);

		User response = userService.create(request);

		assertThat(response.getUsername()).isEqualTo(request.getUsername());

		verify(userRepository, times(1)).save(any(User.class));
		verify(passwordEncoder, times(1)).encode(request.getPassword());
	}
}
