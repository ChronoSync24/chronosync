package com.sinergy.chronosync.service;

import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.service.impl.LogoutServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LogoutServiceImpl}.
 */
class LogoutServiceTest {

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private LogoutServiceImpl logoutService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the logout functionality with a valid JWT token.
	 * Verifies that the token is deleted and the security context is cleared.
	 */
	@Test
	void successfulLogoutTest() {
		String validJwt = "Bearer jwt123";
		Token token = new Token();

		when(request.getHeader("Authorization")).thenReturn(validJwt);
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any()))
			.thenReturn(Optional.of(token));

		logoutService.logout(request, response, authentication);

		verify(tokenRepository, times(1)).delete(token);
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}
}

