package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.dto.response.UserResponseDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserController}.
 */
class UserControllerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link UserController#create(UserRequestDTO)} method.
	 * Verifies that the creation service is called with the correct request
	 * and that the response is properly constructed.
	 */
	@Test
	void createUserTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setPassword("password123");
		request.setFirstName("John");
		request.setLastName("Doe");

		User response = new User();
		response.setId(1L);
		response.setUsername("testUser");

		when(userService.create(any(UserRequestDTO.class))).thenReturn(response);

		ResponseEntity<User> result = userController.create(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getUsername()).isEqualTo("testUser");

		verify(userService, times(1)).create(any(UserRequestDTO.class));
	}

	/**
	 * Tests the {@link UserController#getUsers(PaginatedUserRequestDTO)} method.
	 * Verifies that the search service is called and returns a paginated response.
	 */
	@Test
	void getUsersTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();
		request.setPage(0);
		request.setPageSize(10);
		request.setFirstName("John");

		UserResponseDTO userResponse = UserResponseDTO.builder()
			.id(1L)
			.firstName("John")
			.lastName("Doe")
			.username("johndoe")
			.build();

		Page<UserResponseDTO> responsePage = new PageImpl<>(
			List.of(userResponse),
			PageRequest.of(0, 10),
			1
		);

		when(userService.getUsers(any(PaginatedUserRequestDTO.class))).thenReturn(responsePage);

		ResponseEntity<Page<UserResponseDTO>> result = userController.getUsers(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getContent()).hasSize(1);
		assertThat(result.getBody().getContent().getFirst().getFirstName()).isEqualTo("John");

		verify(userService, times(1)).getUsers(any(PaginatedUserRequestDTO.class));
	}

	/**
	 * Tests the {@link UserController#getUsers(PaginatedUserRequestDTO)} method
	 * when no users are found.
	 */
	@Test
	void getUsersEmptyTest() {
		PaginatedUserRequestDTO request = new PaginatedUserRequestDTO();
		request.setPage(0);
		request.setPageSize(10);

		Page<UserResponseDTO> emptyPage = new PageImpl<>(
			List.of(),
			PageRequest.of(0, 10),
			0
		);

		when(userService.getUsers(any(PaginatedUserRequestDTO.class))).thenReturn(emptyPage);

		ResponseEntity<Page<UserResponseDTO>> result = userController.getUsers(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getContent()).isEmpty();

		verify(userService, times(1)).getUsers(any(PaginatedUserRequestDTO.class));
	}

	/**
	 * Tests the {@link UserController#updateUser(UserRequestDTO)} method.
	 * Verifies that the update service is called and returns the updated user.
	 */
	@Test
	void updateUserTest() {
		UserRequestDTO request = new UserRequestDTO();
		request.setId(1L);
		request.setFirstName("Updated");
		request.setLastName("Name");

		UserResponseDTO response = UserResponseDTO.builder()
			.id(1L)
			.firstName("Updated")
			.lastName("Name")
			.username("johndoe")
			.build();

		when(userService.updateUser(any(UserRequestDTO.class))).thenReturn(response);

		ResponseEntity<UserResponseDTO> result = userController.updateUser(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getFirstName()).isEqualTo("Updated");
		assertThat(result.getBody().getLastName()).isEqualTo("Name");

		verify(userService, times(1)).updateUser(any(UserRequestDTO.class));
	}

	/**
	 * Tests the {@link UserController#deleteUser(Long)} method.
	 * Verifies that the delete service is called and returns no content.
	 */
	@Test
	void deleteUserTest() {
		Long userId = 1L;

		doNothing().when(userService).deleteUser(userId);

		ResponseEntity<User> result = userController.deleteUser(userId);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(result.getBody()).isNull();

		verify(userService, times(1)).deleteUser(userId);
	}
}