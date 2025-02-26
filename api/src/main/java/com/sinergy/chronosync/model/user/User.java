package com.sinergy.chronosync.model.user;

import com.sinergy.chronosync.model.Firm;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User model class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Person implements UserDetails {

	//TODO: Add date created, modified (for every entity).
	//TODO: Add datetime of logging in, and datetime of logging out.
	//TODO: Implement global logger for user actions and system actions.

	private String username;
	private String password;
	private UserRole role;
	private Boolean isLocked;
	private Boolean isEnabled;

	/**
	 * Returns list of users roles.
	 *
	 * @return {@link List<SimpleGrantedAuthority>} list of authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
	}

	/**
	 * Returns true if account is non expired.
	 *
	 * @return {@link Boolean}
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Returns true if account is not locked.
	 *
	 * @return {@link Boolean}
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !this.isLocked;
	}

	/**
	 * Returns true if credentials are not expired.
	 *
	 * @return {@link Boolean}
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Returns true if user is enabled.
	 *
	 * @return {@link Boolean}
	 */
	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@ManyToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;
}
