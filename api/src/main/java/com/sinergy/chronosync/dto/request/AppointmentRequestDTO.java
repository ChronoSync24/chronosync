package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for creating or updating an appointment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AppointmentRequestDTO {

	private Long id;
	private String note;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private AppointmentType appointmentType;
	private User employee;
	private Client client;
	private Firm firm;

	/**
	 * Creates and returns Appointment model from Data Transfer Object.
	 *
	 * @return {@link Appointment} user model
	 */
	public Appointment toModel() {
		return Appointment.builder()
			.id(id)
			.note(note)
			.startDateTime(startDateTime)
			.endDateTime(endDateTime)
			.appointmentType(appointmentType)
			.employee(employee)
			.client(client)
			.firm(firm)
			.build();
	}
}
