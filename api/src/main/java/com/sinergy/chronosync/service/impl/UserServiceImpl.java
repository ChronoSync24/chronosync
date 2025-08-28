package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.PaginatedUserRequestDTO;
import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.dto.response.UserResponseDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.SecurityContextService;
import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * User service implementation.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final SecurityContextService securityContextService;

	/**
	 * Creates new user.
	 *
	 * @param request {@link UserRequestDTO} user create request
	 * @return {@link User} user create response
	 */
	@Override
	public User create(UserRequestDTO request) {
		String username = request.getUsername();

		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(username)
			.build();

		filterBuilder.setPageable(PageRequest.of(0, 50));

		Page<User> result = userRepository.findAll(filterBuilder.toSpecification(), filterBuilder.getPageable());

		if (result.hasContent()) {
			username = username + (result.getTotalElements() + 1);
		}

		User user = request.toModel();
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setUsername(username);
		user.setFirm(securityContextService.getAuthUserFirm());

		User createdUser = userRepository.save(user);
		createdUser.setPassword(null);

		return createdUser;
	}

	/**
	 * Retrieves users.
	 *
	 * @param pageRequest {@link PaginatedUserRequestDTO} The pagination and filtering information
	 * @return {@link Page<User>} paginated result
	 */
	@Override
	public Page<UserResponseDTO> getUsers(PaginatedUserRequestDTO pageRequest) {
		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.firstName(pageRequest.getFirstName())
			.lastName(pageRequest.getLastName())
			.uniqueIdentifier(pageRequest.getUniqueIdentifier())
			.username(pageRequest.getUsername())
			.firmId(pageRequest.getFirmId())
			.roles(pageRequest.getRoles())
			.build();

		filterBuilder.setPageable(PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize()));

		Page<User> result = userRepository.findAll(filterBuilder.toSpecification(), filterBuilder.getPageable());

		return result.map(user -> UserResponseDTO.builder()
			.id(user.getId())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.address(user.getAddress())
			.phone(user.getPhone())
			.email(user.getEmail())
			.username(user.getUsername())
			.role(user.getRole())
			.uniqueIdentifier(user.getUniqueIdentifier())
			.isEnabled(user.getIsEnabled())
			.build()
		);
	}

	/**
	 * Updates user with the provided information.
	 *
	 * @param requestDTO {@link UserRequestDTO} DTO containing updated details
	 * @return {@link User} updated user
	 */
	@Override
	public UserResponseDTO updateUser(UserRequestDTO requestDTO) {
		Optional<User> existingUser = userRepository.findById(requestDTO.getId());

		if (existingUser.isEmpty()) {
			throw new EntityNotFoundException("User with provided ID does not exist.");
		}

		User user = requestDTO.toModel();
		user.setPassword(existingUser.get().getPassword());
		user.setFirm(existingUser.get().getFirm());
		user.setRole(existingUser.get().getRole());

		if (securityContextService.getAuthUserRole().equals(UserRole.ADMINISTRATOR)) {
			user.setRole(requestDTO.getRole());

			if (!requestDTO.getPassword().isEmpty()) {
				user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
			}
		}

		userRepository.update(user);

		return UserResponseDTO.builder()
			.id(user.getId())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.address(user.getAddress())
			.phone(user.getPhone())
			.email(user.getEmail())
			.username(user.getUsername())
			.role(user.getRole())
			.uniqueIdentifier(user.getUniqueIdentifier())
			.isEnabled(user.getIsEnabled())
			.build();
	}

	/**
	 * Deletes a user identified by its ID.
	 *
	 * @param id {@link Long} ID of the user to delete
	 * @throws EntityNotFoundException if the user is not found with the provided id
	 */
	@Override
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new EntityNotFoundException("User not found");
		}

		User user = new User();
		user.setId(id);

		tokenRepository.findOne(
			TokenFilterBuilder.builder()
				.user(user)
				.build()
				.toSpecification()
		).ifPresent(tokenRepository::delete);

		userRepository.deleteById(id);
	}
}
