package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.config.policy.CrudOperation;
import com.sinergy.chronosync.config.policy.EnforcePolicy;
import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.dto.response.UserRegistrationResponseDTO;
import com.sinergy.chronosync.dto.response.UserResponseDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User controller class.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * Creates new user.
	 *
	 * @param request {@link UserRequestDTO} user create request
	 * @return {@link ResponseEntity< UserRegistrationResponseDTO >} user creation response
	 */
	@PostMapping("/create")
	@EnforcePolicy(entity = User.class, operation = CrudOperation.CREATE)
	public ResponseEntity<User> create(
		@RequestBody UserRequestDTO request
	) {
		return ResponseEntity.ok(userService.create(request));
	}

	/**
	 * Retrieves a paginated list of users.
	 *
	 * @param paginationRequest {@link PaginatedUserRequestDTO} paginated request
	 * @return {@link Page<UserResponseDTO>} page of users
	 */
	@PostMapping("/search")
	@EnforcePolicy(entity = User.class, operation = CrudOperation.READ)
	public ResponseEntity<Page<UserResponseDTO>> getUsers(
		@RequestBody PaginatedUserRequestDTO paginationRequest
	) {
		return ResponseEntity.ok(userService.getUsers(paginationRequest));
	}

	/**
	 * Updates an existing user identified by its ID.
	 *
	 * @param request {@link UserRequestDTO} containing details of the user
	 * @return {@link ResponseEntity} containing the updated {@link User}
	 */
	@PutMapping
	@EnforcePolicy(entity = User.class, operation = CrudOperation.UPDATE)
	public ResponseEntity<UserResponseDTO> updateUser(
		@RequestBody UserRequestDTO request
	) {
		return ResponseEntity.ok(userService.updateUser(request));
	}

	/**
	 * Deletes a user by its ID.
	 *
	 * @param id {@link Long} ID of the user to delete
	 */
	@DeleteMapping
	@EnforcePolicy(entity = User.class, operation = CrudOperation.DELETE)
	public ResponseEntity<User> deleteUser(
		@RequestParam Long id
	) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
