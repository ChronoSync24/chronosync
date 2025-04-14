package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.AppointmentTypeFilterBuilder;
import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.service.AppointmentTypeService;
import com.sinergy.chronosync.service.SecurityContextService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service implementation for managing appointment types.
 */
@Service
@AllArgsConstructor
public class AppointmentTypeServiceImpl implements AppointmentTypeService {

	private final AppointmentTypeRepository appointmentTypeRepository;
	private final SecurityContextService securityContextService;

	/**
	 * Retrieves all appointment types associated with the current user's firm.
	 * <p>
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link AppointmentType} objects linked to that firm's ID.
	 *
	 * @return {@link Page} of {@link AppointmentType} objects associated with the current user's firm.
	 */
	public Page<AppointmentType> getAppointmentTypes(PageRequest pageRequest) {
		AppointmentTypeFilterBuilder filterBuilder = AppointmentTypeFilterBuilder.builder()
			.firmId(securityContextService.getAuthUserFirm().getId())
			.build();

		filterBuilder.setPageable(pageRequest);

		return appointmentTypeRepository.findAll(filterBuilder.toSpecification(), filterBuilder.getPageable());
	}

	/**
	 * Creates a new appointment type and stores it in the database.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing appointment type details.
	 * @return {@link AppointmentType} representing the saved appointment type.
	 */
	@Override
	public AppointmentType createAppointmentType(AppointmentTypeRequestDTO requestDto) {
		AppointmentType appointmentType = requestDto.toModel();
		appointmentType.setFirm(securityContextService.getAuthUserFirm());
		return appointmentTypeRepository.create(appointmentType);
	}

	/**
	 * Updates an existing appointment type.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link AppointmentType} representing the updated or newly created appointment type
	 * @throws UserNotFoundException if the user cannot be found.
	 * @throws InvalidStateException if the appointment type cannot be found for update.
	 */
	@Override
	public AppointmentType updateAppointmentType(AppointmentTypeRequestDTO requestDto) {
		return appointmentTypeRepository.update(requestDto.toModel());
	}

	/**
	 * Deletes an appointment type identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws InvalidStateException if deletion fails or the appointment type does not exist
	 */
	@Override
	public void deleteAppointmentType(Long id) {
		if (!appointmentTypeRepository.existsById(id)) {
			throw new EntityNotFoundException("Appointment type does not exist.");
		}
		appointmentTypeRepository.deleteById(id);
	}
}
