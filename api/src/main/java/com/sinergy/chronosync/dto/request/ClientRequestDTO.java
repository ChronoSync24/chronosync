package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
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

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private Long firmId;

	/**
	 * Creates and returns Client model from Data Transfer Object.
	 *
	 * @return {@link Client} user model
	 */
	public Client toModel() {
		Firm firm = new Firm();
		firm.setId(firmId);

		return Client.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.phone(phone)
				.address(address)

				.firm(firm)
				.build();
	}

}
