package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Service interface for managing appointments.
 */
public interface AppointmentService {

	/**
	 * Retrieves a paginated list of appointments associated with the authenticated user.
	 *
	 * @return {@link Page} of {@link Appointment} containing all appointments for the authenticated user.
	 */
	Page<Appointment> getAppointments(PageRequest pageRequest);

	/**
	 * Creates a new appointment and stores it in the database.
	 *
	 * @param requestDto {@link AppointmentRequestDTO} containing appointment details.
	 * @return {@link Appointment} representing the saved appointment
	 */
	Appointment createAppointment(AppointmentRequestDTO requestDto);

	/**
	 * Updates an existing appointment identified by its ID.
	 *
	 * @param requestDto {@link AppointmentRequestDTO} containing updated details
	 * @return {@link Appointment} representing the updated appointment
	 */
	Appointment updateAppointment(AppointmentRequestDTO requestDto);

	/**
	 * Deletes an appointment by its ID.
	 *
	 * @param id {@link Long} ID of the appointment to delete
	 */
	void deleteAppointment(Long id);
}
