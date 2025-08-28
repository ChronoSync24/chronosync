package com.sinergy.chronosync.config;

import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up security-related beans.
 *
 * <p>This class is responsible for defining beans related to user authentication,
 * including user details service, password encoding, and authentication management.
 * It integrates with the user repository to provide user details and handle
 * authentication processes.</p>
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository userRepository;

	/**
	 * Provides a {@link UserDetailsService} implementation for loading user-specific data.
	 *
	 * <p> Retrieves a user by their username from the user repository. If the user
	 * is not found, a {@link UsernameNotFoundException} is thrown.</p>
	 *
	 * @return a {@link UserDetailsService} instance for user authentication
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository
			.findOne(
				UserFilterBuilder.builder()
					.username(username)
					.exactUsernameMatch(true)
					.build()
					.toSpecification()
			)
			.orElseThrow(() -> new UsernameNotFoundException("User not found."));
	}

	/**
	 * Provides a {@link PasswordEncoder} bean for encoding passwords.
	 *
	 * <p> Uses the {@link BCryptPasswordEncoder} for securely encoding passwords
	 * before storing them in the database.</p>
	 *
	 * @return a {@link PasswordEncoder} instance for password encryption
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures an {@link AuthenticationProvider} bean.
	 *
	 * <p> Sets up a {@link DaoAuthenticationProvider} that uses the
	 * {@link UserDetailsService} and {@link PasswordEncoder} to authenticate users.</p>
	 *
	 * @return an {@link AuthenticationProvider} instance for managing authentication
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	/**
	 * Provides an {@link AuthenticationManager} bean.
	 *
	 * <p> Retrieves the {@link AuthenticationManager} from the provided
	 * {@link AuthenticationConfiguration}.</p>
	 *
	 * @param config the {@link AuthenticationConfiguration} containing configuration for
	 *               authentication management
	 * @return an {@link AuthenticationManager} instance for processing authentication requests
	 * @throws Exception if an error occurs while retrieving the authentication manager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
