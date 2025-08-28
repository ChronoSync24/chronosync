package com.sinergy.chronosync.config;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.service.SecurityContextService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditorProvider is a Spring-managed component that implements {@link AuditorAware}
 * to provide the current authenticated {@link User} for auditing purposes.
 *
 * <p>This class relies on a custom {@link SecurityContextService} to obtain the currently
 * authenticated user from the application's security context.</p>
 */
@Component
public class AuditorProvider implements AuditorAware<User> {

	private final SecurityContextService securityContextService;

	public AuditorProvider(SecurityContextService securityContextService) {
		this.securityContextService = securityContextService;
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Optional<User> getCurrentAuditor() {
		User user = new User();
		user.setId(securityContextService.getAuthUserId());

		return Optional.of(user);
	}
}
