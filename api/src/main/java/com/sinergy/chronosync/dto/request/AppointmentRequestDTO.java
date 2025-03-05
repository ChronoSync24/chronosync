package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import lombok.*;

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
	private String startTime;
	private String endTime;
	private AppointmentType appointmentType;
	private User taskedEmployee;
	private Client client;
	private User creator;
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
			.startTime(startTime)
			.endTime(endTime)
			.appointmentType(appointmentType)
			.taskedEmployee(taskedEmployee)
			.client(client)
			.creator(creator)
			.firm(firm)
			.build();
	}
}
