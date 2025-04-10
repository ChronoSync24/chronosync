package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.SecurityContextService;
import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * User service implementation.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
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
		User user = request.toModel(false);
		user.setCreatedBy(securityContextService.getAuthUser());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		User createdUser = userRepository.save(user);
		createdUser.setPassword(null);

		return createdUser;
	}
}
