package com.sinergy.chronosync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for enabling JPA Auditing functionality.
 *
 * <p>This class activates Spring Data JPA's auditing features and specifies
 * the {@link AuditorProvider} to be used for tracking created/modified by information.</p>
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
}