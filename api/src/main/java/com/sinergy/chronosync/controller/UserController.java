package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.UserRequestDTO;
import com.sinergy.chronosync.dto.response.UserResponseDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	 * @return {@link ResponseEntity<UserResponseDTO>} user creation response
	 */
	@PostMapping("/create")
	public ResponseEntity<User> create(
		@RequestBody UserRequestDTO request
	) {
		return ResponseEntity.ok(userService.create(request));
	}
}
