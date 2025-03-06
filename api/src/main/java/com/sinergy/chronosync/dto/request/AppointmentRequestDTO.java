package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Firm;
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
	private String date;
	private Long appointmentTypeId;
	private Long taskedEmployeeId;
	private Long clientId;
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
			.date(date)
			.firm(firm)
			.build();
	}
}
