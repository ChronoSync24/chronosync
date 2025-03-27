package com.sinergy.chronosync.config;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.service.SecurityContextService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuditorProvider implements AuditorAware<User> {

	private final SecurityContextService securityContextService;

	public AuditorProvider(SecurityContextService securityContextService) {
		this.securityContextService = securityContextService;
	}

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(securityContextService.getAuthUser());
	}
}
