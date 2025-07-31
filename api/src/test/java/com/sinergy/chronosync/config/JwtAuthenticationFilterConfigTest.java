package com.sinergy.chronosync.config;

import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the JwtAuthenticationFilterConfig class, ensuring correct functionality
 * of the JWT authentication filtering process.
 */
class JwtAuthenticationFilterConfigTest {

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@InjectMocks
	private JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		SecurityContextHolder.clearContext();
	}

	/**
	 * Verifies that the filter continues without setting an authentication object
	 * when there is no Authorization header in the request.
	 *
	 * @throws ServletException if a servlet-related error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Test
	void filterInternalNoAuthHeaderTest() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(null);

		jwtAuthenticationFilterConfig.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	/**
	 * Ensures that the filter continues without setting an authentication object
	 * when the Authorization header is invalid (not prefixed with "Bearer").
	 *
	 * @throws ServletException if a servlet-related error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Test
	void filterInternalInvalidAuthHeaderTest() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

		jwtAuthenticationFilterConfig.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	/**
	 * Ensures that an expired or invalid JWT token prevents authentication from
	 * being set in the security context.
	 *
	 * @throws ServletException if a servlet-related error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Test
	void filterInternalExpiredOrInvalidTokenTest() throws ServletException, IOException {

		String jwt = "expiredOrInvalid.jwt.token";
		String username = "testUser";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
		when(jwtUtils.extractUsername(jwt)).thenReturn(username);

		UserDetails userDetails = new User(username, "password", Collections.emptyList());
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
		when(jwtUtils.isTokenValid(jwt, userDetails)).thenReturn(false);

		jwtAuthenticationFilterConfig.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);

		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	/**
	 * Validates that when a token is valid but cannot be found in the repository,
	 * no authentication is set in the security context.
	 *
	 * @throws ServletException if a servlet-related error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Test
	void filterInternalValidTokenUserNotFoundTest() throws ServletException, IOException {
		String jwt = "valid.jwt.token";
		String username = "testUser";
		when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
		when(jwtUtils.extractUsername(jwt)).thenReturn(username);

		UserDetails userDetails = new User(username, "password", Collections.emptyList());
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
		when(jwtUtils.isTokenValid(jwt, userDetails)).thenReturn(true);

		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.empty());

		jwtAuthenticationFilterConfig.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}
}
