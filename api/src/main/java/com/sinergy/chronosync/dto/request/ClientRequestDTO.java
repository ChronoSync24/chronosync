package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
			client.setIdentificationNumber(identificationNumber);
			return client;
	}

}
