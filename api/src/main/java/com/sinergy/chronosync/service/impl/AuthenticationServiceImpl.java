package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.service.AuthenticationService;
import com.sinergy.chronosync.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Authentication service interface implementation.
 */
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final TokenRepository tokenRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;

	/**
	 * Authenticates provided user with username and password.
	 *
	 * @param request {@link LoginRequestDTO} authentication request
	 * @return {@link AuthenticationResponse} JSON web token
	 */
	@Override
	public AuthenticationResponse authenticate(LoginRequestDTO request) {
		Authentication authentication;

		try {
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new ServiceException("Invalid credentials.");
		} catch (DisabledException e) {
			throw new ServiceException("User is not enabled. Please contact your manager.");
		}

		User user = (User) authentication.getPrincipal();

		String jwt = jwtUtils.generateJWTString(user);

		Token token = tokenRepository
			.findOne(TokenFilterBuilder.builder().user(user).build().toSpecification())
			.orElseGet(() -> Token.builder().user(user).build());

		token.setJwtString(jwt);
		tokenRepository.save(token);

		return AuthenticationResponse.builder().jwtString(jwt).build();
	}

	/**
	 * Validates provided JWT.
	 *
	 * @param token {@link String} json web token
	 * @return {@link Boolean} token validity
	 */
	@Override
	public Boolean validateToken(String token) {
		String username = jwtUtils.extractUsername(token);

		Optional<Token> dbToken = tokenRepository.findOne(
			TokenFilterBuilder.builder()
				.jwtString(token)
				.build()
				.toSpecification()
		);

		return dbToken.isPresent() && jwtUtils.isTokenValid(token, userDetailsService.loadUserByUsername(username));
	}
}
