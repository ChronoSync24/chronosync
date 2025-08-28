package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.dto.response.UserResponseDTO;
import com.sinergy.chronosync.model.user.User;
import org.springframework.data.domain.Page;

/**
 * User service interface class.
 */
public interface UserService {

	/**
	 * Creates new user.
	 *
	 * @param request {@link UserRequestDTO} user create request
	 * @return {@link User} user create response
	 */
	User create(UserRequestDTO request);

	/**
	 * Retrieves a paginated list of users.
	 *
	 * @return {@link Page} of {@link UserResponseDTO} containing users
	 */
	Page<UserResponseDTO> getUsers(PaginatedUserRequestDTO pageRequest);

	/**
	 * Updates an existing user.
	 *
	 * @param requestDTO {@link UserRequestDTO} containing updated details
	 * @return {@link UserResponseDTO} updated user
	 */
	UserResponseDTO updateUser(UserRequestDTO requestDTO);

	/**
	 * Deletes a user by its ID.
	 *
	 * @param id {@link Long} ID of the user to delete
	 */
	void deleteUser(Long id);
}
