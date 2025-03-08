package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.service.AuthenticationService;
import com.sinergy.chronosync.service.LogoutService;
import com.sinergy.chronosync.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller class.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authenticationService;
	private final LogoutService logoutService;

	/**
	 * Authenticates user with provided credentials.
	 *
	 * @param request {@link LoginRequestDTO} authentication request with credentials
	 * @return {@link ResponseEntity<AuthenticationResponse>} authentication response with JWT
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> authenticate(
		@RequestBody LoginRequestDTO request
	) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}

	/**
	 * Log out user.
	 *
	 * @param request  {@link HttpServletRequest} http request
	 * @param response {@link HttpServletResponse} http response
	 * @return {@link ResponseEntity<String>} logout message
	 */
	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		logoutService.logout(request, response, null);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Validates JWT.
	 *
	 * @param request  {@link HttpServletRequest} http request
	 * @return {@link ResponseEntity<Boolean>} token validity
	 */
	@GetMapping("/validate-token")
	public ResponseEntity<Boolean> validateToken(HttpServletRequest request) {
		String jwt = JwtUtils.extractToken(request);

		return ResponseEntity.ok(authenticationService.validateToken(jwt));
	}
}
