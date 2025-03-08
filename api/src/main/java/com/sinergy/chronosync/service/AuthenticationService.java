package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;

/**
 * Authentication service interface.
 */
public interface AuthenticationService {

	/**
	 * Authenticates provided user with username and password.
	 *
	 * @param request {@link LoginRequestDTO} authentication request
	 * @return {@link AuthenticationResponse} JSON web token
	 */
	AuthenticationResponse authenticate(LoginRequestDTO request);

	/**
	 * Validates provided JWT.
	 *
	 * @param token {@link String} json web token
	 * @return {@link Boolean} token validity
	 */
	Boolean validateToken(String token);
}
