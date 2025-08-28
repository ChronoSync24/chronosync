package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.AuthenticationServiceImpl;
import com.sinergy.chronosync.util.JwtUtils;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthenticationServiceImpl}.
 */
class AuthenticationServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private Authentication authentication;

	@Mock
	private UserDetailsService userDetailsService;

	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	private User testUser;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);

		testUser = new User();
		testUser.setUsername("test");
		testUser.setId(1L);

		when(authentication.getPrincipal()).thenReturn(testUser);
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * with valid user credentials. Verifies that a JWT token is generated and returned in the response.
	 */
	@Test
	void authenticateValidUserTest() {
		LoginRequestDTO request = new LoginRequestDTO("test", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenReturn(authentication);
		when(jwtUtils.generateJWTString(testUser)).thenReturn("jwt123");

		Token existingToken = new Token();
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(existingToken));

		AuthenticationResponse response = authenticationService.authenticate(request);

		assertThat(response.getJwtString()).isEqualTo("jwt123");

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(jwtUtils, times(1)).generateJWTString(testUser);
		verify(tokenRepository, times(1)).findOne(Mockito.<Specification<Token>>any());
		verify(tokenRepository, times(1)).save(existingToken);
		verify(userRepository, never()).findOne(Mockito.<Specification<User>>any());
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * when a valid user is authenticated but lacks an existing token. Verifies that a new token is created
	 * and saved in the repository.
	 */
	@Test
	void authenticateValidUserWithNewTokenTest() {
		LoginRequestDTO request = new LoginRequestDTO("test", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenReturn(authentication);
		when(jwtUtils.generateJWTString(testUser)).thenReturn("jwt123");

		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.empty());

		AuthenticationResponse response = authenticationService.authenticate(request);

		assertThat(response.getJwtString()).isEqualTo("jwt123");

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(jwtUtils, times(1)).generateJWTString(testUser);
		verify(tokenRepository, times(1)).findOne(Mockito.<Specification<Token>>any());
		verify(tokenRepository, times(1)).save(any(Token.class));
		verify(userRepository, never()).findOne(Mockito.<Specification<User>>any());
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * with invalid user credentials. Verifies that a {@link ServiceException} is thrown for invalid credentials.
	 */
	@Test
	void invalidCredentialsTest() {
		LoginRequestDTO request = new LoginRequestDTO("invalidUser", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenThrow(new BadCredentialsException("Invalid credentials."));

		assertThrows(ServiceException.class, () -> authenticationService.authenticate(request));

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verifyNoInteractions(jwtUtils, tokenRepository, userRepository);
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * when authentication fails due to a disabled user. Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void disabledUserTest() {
		LoginRequestDTO request = new LoginRequestDTO("disabledUser", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenThrow(new DisabledException("User is disabled"));

		assertThrows(ServiceException.class, () -> authenticationService.authenticate(request));

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verifyNoInteractions(jwtUtils, tokenRepository, userRepository);
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#validateToken(String)} method
	 * with a valid token that exists in the database.
	 */
	@Test
	void validateTokenValidTest() {
		String tokenString = "valid.jwt.token";
		String username = "testuser";

		Token dbToken = new Token();
		dbToken.setJwtString(tokenString);

		UserDetails userDetails = mock(UserDetails.class);

		when(jwtUtils.extractUsername(tokenString)).thenReturn(username);
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(dbToken));
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
		when(jwtUtils.isTokenValid(tokenString, userDetails)).thenReturn(true);

		Boolean isValid = authenticationService.validateToken(tokenString);

		assertThat(isValid).isTrue();

		verify(jwtUtils, times(1)).extractUsername(tokenString);
		verify(tokenRepository, times(1)).findOne(Mockito.<Specification<Token>>any());
		verify(userDetailsService, times(1)).loadUserByUsername(username);
		verify(jwtUtils, times(1)).isTokenValid(tokenString, userDetails);
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#validateToken(String)} method
	 * with an invalid token (JWT validation fails).
	 */
	@Test
	void validateTokenInvalidJwtTest() {
		String tokenString = "invalid.jwt.token";
		String username = "testuser";

		Token dbToken = new Token();
		dbToken.setJwtString(tokenString);

		UserDetails userDetails = mock(UserDetails.class);

		when(jwtUtils.extractUsername(tokenString)).thenReturn(username);
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(dbToken));
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
		when(jwtUtils.isTokenValid(tokenString, userDetails)).thenReturn(false);

		Boolean isValid = authenticationService.validateToken(tokenString);

		assertThat(isValid).isFalse();

		verify(jwtUtils, times(1)).extractUsername(tokenString);
		verify(tokenRepository, times(1)).findOne(Mockito.<Specification<Token>>any());
		verify(userDetailsService, times(1)).loadUserByUsername(username);
		verify(jwtUtils, times(1)).isTokenValid(tokenString, userDetails);
	}
}