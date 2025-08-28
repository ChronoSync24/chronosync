package com.sinergy.chronosync.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CorsConfig implements CorsConfigurationSource {

	@Value("#{'${security.cors.allowed-origins}'.split(',')}")
	private List<String> allowedOrigins;

	@Value("#{'${security.cors.allowed-methods}'.split(',')}")
	private List<String> allowedMethods;

	@Value("#{'${security.cors.allowed-headers}'.split(',')}")
	private List<String> allowedHeaders;

	@SuppressWarnings("NullableProblems")
	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(allowedOrigins);
		config.setAllowedMethods(allowedMethods);
		config.setAllowedHeaders(allowedHeaders);

		return config;
	}
}
