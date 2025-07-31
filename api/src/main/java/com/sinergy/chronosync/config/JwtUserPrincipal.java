package com.sinergy.chronosync.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * JwtUserPrincipal class that wraps user id, firm id, username and user roles to JWT.
 */
@Getter
@AllArgsConstructor
public class JwtUserPrincipal implements UserDetails {

	private Long id;
	private Long firmId;
	private String username;
	private List<GrantedAuthority> authorities;

	@Override
	public String getPassword() {
		return null; // Not needed for JWT authentication
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
