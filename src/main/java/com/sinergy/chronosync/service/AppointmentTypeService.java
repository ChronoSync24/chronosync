package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import java.util.List;

/**
 * Service interface for managing appointment types.
 */
public interface AppointmentTypeService {

	/**
	 * Retrieves all appointment types associated with the user's firm
	 *
	 * @return {@link List} of {@link AppointmentType} containing all appointment types for the user's firm
	 */
	List<AppointmentType> getAppointmentTypesForCurrentUser();

	/**
	 * Creates a new appointment type and stores it in the database.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link AppointmentType} representing the saved appointment type
	 */
	AppointmentType createAppointmentType(AppointmentTypeRequestDTO requestDto);

	/**
	 * Updates an existing appointment type identified by its ID.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing updated details
	 * @return {@link AppointmentType} representing the updated appointment type
	 */
	AppointmentType updateAppointmentType(AppointmentTypeRequestDTO requestDto);

	/**
	 * Deletes an appointment type by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 */
	void deleteAppointmentType(Long id);
}