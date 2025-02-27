package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating an appointment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

	private Long id;
	private String note;
	private String startTime;
	private String endTime;
	private AppointmentType appointmentType;
	private User user;
	private Client client;

	/**
	 * Creates and returns Appointment model from Data Transfer Object.
	 *
	 * @return {@link Appointment} user model
	 */
	public Appointment toModel() {
		return Appointment.builder()
			.id(id)
			.note(note)
			.startTime(startTime)
			.endTime(endTime)
			.appointmentType(appointmentType)
			.user(user)
			.client(client)
			.build();
	}
}
