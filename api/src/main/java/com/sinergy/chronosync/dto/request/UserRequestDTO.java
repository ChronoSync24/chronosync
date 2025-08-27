package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User registration request data transfer object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
	private String password;
	private String uniqueIdentifier;
	private Boolean isEnabled;
	private UserRole role;

	/**
	 * Returns username combining first letter of the first name and full last name.
	 *
	 * @return {@link String} username
	 */
	public String getUsername() {
		return (firstName.charAt(0) + lastName).toLowerCase();
	}

	/**
	 * Creates and returns User model from Data Transfer Object.
	 *
	 * @return {@link User} user model
	 */
	public User toModel() {
		User user = new User();
		user.setId(id);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setPhone(phone);
		user.setEmail(email);
		user.setUsername(getUsername());
		user.setPassword(password);
		user.setRole(role);
		user.setIsEnabled(isEnabled);
		user.setIsLocked(false);
		user.setUniqueIdentifier(uniqueIdentifier);

		return user;
	}
}
