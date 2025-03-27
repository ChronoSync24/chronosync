package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.AppointmentFilterBuilder;
import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.AppointmentRepository;
import com.sinergy.chronosync.service.AppointmentService;
import com.sinergy.chronosync.service.SecurityContextService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing appointments.
 */
@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final SecurityContextService securityContextService;

	/**
	 * Retrieves all appointments.
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link Appointment} objects linked to that user's ID.
	 *
	 * @return {@link Page} of {@link Appointment} objects associated with the current user.
	 */
	@Override
	public Page<Appointment> getAppointments(PageRequest pageRequest) {
		User authUser = securityContextService.getAuthUser();
		AppointmentFilterBuilder filterBuilder;

		if (authUser.getRole() == UserRole.MANAGER || authUser.getRole() == UserRole.ADMINISTRATOR) {
			filterBuilder = AppointmentFilterBuilder.builder()
				.firmId(securityContextService.getAuthUserFirm().getId())
				.build();
		} else {
			filterBuilder = AppointmentFilterBuilder.builder()
				.taskedEmployeeId(authUser.getId())
				.build();
		}

		return appointmentRepository.findAll(filterBuilder.toSpecification(), filterBuilder.getPageable());
	}

	/**
	 * Creates a new appointment and stores it in the database.
	 *
	 * @param requestDto {@link AppointmentRequestDTO} containing appointment details.
	 * @return {@link Appointment} representing the saved appointment.
	 */
	@Override
	public Appointment createAppointment(AppointmentRequestDTO requestDto) {
		Appointment appointment = requestDto.toModel();
		appointment.setFirm(securityContextService.getAuthUserFirm());
		return appointmentRepository.create(appointment);
	}

	/**
	 * Updates an existing appointment.
	 *
	 * @param requestDto {@link AppointmentRequestDTO} containing appointment details
	 * @return {@link Appointment} representing the updated or newly created appointment
	 */
	@Override
	public Appointment updateAppointment(AppointmentRequestDTO requestDto) {
		return appointmentRepository.update(requestDto.toModel());
	}

	/**
	 * Deletes an appointment identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws InvalidStateException if deletion fails or the appointment does not exist
	 */
	@Override
	public void deleteAppointment(Long id) {
		if (!appointmentRepository.existsById(id)) {
			throw new EntityNotFoundException("Appointment does not exist.");
		}
		appointmentRepository.deleteById(id);
	}
}
