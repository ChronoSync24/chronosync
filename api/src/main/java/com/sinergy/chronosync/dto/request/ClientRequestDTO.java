package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for creating or updating a client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String identificationNumber;
	private Long firmId;
	private User createdBy;
	private User modifiedBy;

	/**
	 * Creates and returns Client model from Data Transfer Object.
	 *
	 * @return {@link Client} user model
	 */
	public Client toModel() {
		Client client = new Client();
			client.setFirstName(firstName);
			client.setLastName(lastName);
			client.setEmail(email);
			client.setPhone(phone);
			client.setAddress(address);
			client.setCreated(LocalDateTime.now());
			client.setCreatedBy(createdBy);
			client.setModifiedBy(modifiedBy);
			client.setModified(LocalDateTime.now());
			return client;
	}

}
