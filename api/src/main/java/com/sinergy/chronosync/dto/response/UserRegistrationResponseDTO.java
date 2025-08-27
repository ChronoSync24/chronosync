package com.sinergy.chronosync.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User registration response data transfer object.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationResponseDTO {

	private Long id;
	private String username;
}
