package com.sinergy.chronosync.dto.response;

import com.sinergy.chronosync.model.user.Person;
import com.sinergy.chronosync.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * User response DTO.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserResponseDTO extends Person {

	private String username;
	private Boolean isEnabled;
	private UserRole role;
}
