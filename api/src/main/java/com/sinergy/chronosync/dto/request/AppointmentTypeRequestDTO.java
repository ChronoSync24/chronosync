package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.appointmentType.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating an appointment type.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTypeRequestDTO {

	private Long id;
	private String name;
	private Integer durationMinutes;
	private Double price;
	private Currency currency;
	private String colorCode;
	private Firm firm;

	/**
	 * Creates and returns AppointmentType model from Data Transfer Object.
	 *
	 * @return {@link AppointmentType} user model
	 */
	public AppointmentType toModel() {
		return AppointmentType.builder()
			.id(id)
			.name(name)
			.durationMinutes(durationMinutes)
			.price(price)
			.currency(currency)
			.colorCode(colorCode)
			.firm(firm)
			.build();
	}
}