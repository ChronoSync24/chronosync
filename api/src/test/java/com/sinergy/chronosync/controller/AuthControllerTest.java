package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthController}.
 */
class AuthControllerTest {

	@Mock
	private AuthenticationService authenticationService;

	@InjectMocks
	private AuthController authController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link AuthController#authenticate(LoginRequestDTO)} method.
	 * Verifies that the authentication service is called with the correct request
	 * and that the response contains a valid JWT.
	 */
	@Test
	void authenticateUserTest() {
		LoginRequestDTO request = new LoginRequestDTO("testUser", "password123");
		AuthenticationResponse response = new AuthenticationResponse("jwtToken123");

		when(authenticationService.authenticate(any(LoginRequestDTO.class))).thenReturn(response);

		ResponseEntity<AuthenticationResponse> result = authController.authenticate(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getJwtString()).isEqualTo("jwtToken123");

		verify(authenticationService, times(1)).authenticate(any(LoginRequestDTO.class));
	}
}
