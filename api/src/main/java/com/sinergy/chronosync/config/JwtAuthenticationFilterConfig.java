package com.sinergy.chronosync.config;

import com.sinergy.chronosync.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that represents JSON Web Token Authentication Filter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilterConfig extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;

	/**
	 * Filters incoming requests to authenticate users based on JWT tokens.
	 * </br> </br>
	 * <i>Extracts the JWT from the Authorization header, validates it,
	 * and sets the authentication in the security context if valid.</i>
	 *
	 * @param request     {@link HttpServletRequest} HTTP request
	 * @param response    {@link HttpServletResponse} HTTP response
	 * @param filterChain {@link FilterChain} filter chain to continue processing
	 * @throws ServletException {@link ServletException} if a servlet-related error occurs
	 * @throws IOException      {@link IOException} if an I/O error occurs
	 */
	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = authHeader.substring(7);

		if (jwtUtils.isTokenExpired(jwt)) {
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtils.extractUsername(jwt);
		Long userId = jwtUtils.extractUserId(jwt);
		Long firmId = jwtUtils.extractFirmId(jwt);
		List<String> roles = jwtUtils.extractRoles(jwt);

		List<GrantedAuthority> authorities = roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		if (
			userId != 0 &&
			firmId != 0 &&
			!roles.isEmpty() &&
			username != null &&
			SecurityContextHolder.getContext().getAuthentication() == null
	) {
			JwtUserPrincipal userPrincipal = new JwtUserPrincipal(
				userId,
				firmId,
				username,
				authorities
			);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userPrincipal,
				null,
				userPrincipal.getAuthorities()
			);

			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(request, response);
	}

}
