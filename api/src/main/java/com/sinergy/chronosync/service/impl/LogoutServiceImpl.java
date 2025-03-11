package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.service.LogoutService;
import com.sinergy.chronosync.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Logout service implementation with logout handler.
 */
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService, LogoutHandler {

	private final TokenRepository tokenRepository;

	/**
	 * <p> Extracts the JWT token from the {@code Authorization} header of the
	 * incoming {@link HttpServletRequest}. If the token is found and it deletes the token.
	 * Additionally, it clears the security context to log the user out of the current session.</p>
	 *
	 * @param request        {@link HttpServletRequest} http request containing the authorization header
	 * @param response       {@link  HttpServletResponse} http response
	 * @param authentication {@link Authentication} object containing authentication
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String jwt = JwtUtils.extractToken(request);

		tokenRepository.findOne(
			TokenFilterBuilder.builder()
				.jwtString(jwt)
				.build()
				.toSpecification()
		).ifPresent(tokenRepository::delete);

		SecurityContextHolder.clearContext();
	}
}
