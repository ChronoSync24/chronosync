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

	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
	private String password;
	private UserRole role;

	/**
	 * Returns username combining first letter of the first name and full last name.
	 *
	 * @return {@link String} username
	 */
	public String getUsername() {
		return (firstName.charAt(0) + lastName).toLowerCase();
		//TODO: Append number at the end of username since there can be two persons with the same first and last name.
		//TODO: Trigger can be added when creating new users.
	}

	/**
	 * Creates and returns User model from Data Transfer Object.
	 *
	 * @param isEnabled {@link Boolean} is user account enabled
	 * @return {@link User} user model
	 */
	public User toModel(Boolean isEnabled) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setPhone(phone);
		user.setEmail(email);
		user.setUsername(this.getUsername());
		user.setPassword(password);
		user.setRole(role);
		user.setIsEnabled(isEnabled);
		user.setIsLocked(false);
		return user;
	}
}
