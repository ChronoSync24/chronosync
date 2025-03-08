package com.sinergy.chronosync.config;

import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Class that configures web security settings.
 * <p>This class is responsible for defining security filters, authentication providers,
 * and session management settings. It allows configuring which URLs are accessible
 * without authentication and which require it.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;
	private final CorsConfig corsConfig;

	private static final List<String> WHITE_LIST_URL = List.of("/api/v1/auth/login");

	private static final List<String> EMPLOYEE_LIST_URL = List.of(
			"/api/v1/auth/logout",
			"/api/v1/auth/validate-token",
			"/api/v1/client/**"
	);

	private static final List<String> MANAGER_LIST_URL = CollectionUtils.concat(
		EMPLOYEE_LIST_URL,
		List.of(
			"/api/v1/user/create",
			"/api/v1/test/test-manager",
			"/api/v1/appointment-type/**"
		)
	);

	private static final List<String> ADMIN_LIST_URL = CollectionUtils.concat(
		MANAGER_LIST_URL,
		List.of(
			"/api/v1/user/enable",
			"/api/v1/test/test-manager"
		)
	);

	/**
	 * Configures the default security filter chain.
	 *
	 * <p> Sets up HTTP security configurations such as disabling CSRF protection,
	 * allowing unauthenticated access to specific URL patterns,\ managing session creation policy,
	 * adding filters for JWT authentication and logout handling.</p>
	 *
	 * @param http {@link HttpSecurity} http object to configure security settings
	 * @return {@link SecurityFilterChain} object that contains the security filter configuration
	 * @throws Exception if an error occurs while configuring security settings
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(req -> req
				.requestMatchers(WHITE_LIST_URL.toArray(String[]::new)).permitAll()
				.requestMatchers(ADMIN_LIST_URL.toArray(String[]::new)).hasRole(UserRole.ADMINISTRATOR.name())
				.requestMatchers(MANAGER_LIST_URL.toArray(String[]::new)).hasRole(UserRole.MANAGER.name())
				.requestMatchers(EMPLOYEE_LIST_URL.toArray(String[]::new)).hasRole(UserRole.EMPLOYEE.name())
			)
			.cors(c -> c.configurationSource(corsConfig))
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthenticationFilterConfig, UsernamePasswordAuthenticationFilter.class)
			.logout(logout ->
				logout.logoutUrl("/api/v1/auth/logout")
					.addLogoutHandler(logoutHandler)
					.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
			).build();
	}
}
