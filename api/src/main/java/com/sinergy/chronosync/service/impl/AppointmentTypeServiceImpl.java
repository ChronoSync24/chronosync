package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.AppointmentTypeFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.AppointmentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing appointment types.
 */
@Service
@AllArgsConstructor
public class AppointmentTypeServiceImpl implements AppointmentTypeService {

	private final AppointmentTypeRepository appointmentTypeRepository;
	private final UserRepository userRepository;

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
			.firmId(getAuthUserFirm().getId())
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
		appointmentType.setFirm(getAuthUserFirm());

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
			throw new InvalidStateException("Appointment type does not exist.");
		}
		appointmentTypeRepository.deleteById(id);
	}

	/**
	 * Retrieves the authenticated user's associated firm.
	 *
	 * <p>This method extracts the currently authenticated username from the security context
	 * and constructs a filter to query the user repository. If the user is found, their associated
	 * firm is returned.
	 *
	 * @return the {@link Firm} associated with the authenticated user
	 * @throws UserNotFoundException if no user is found with the authenticated username
	 * @throws InvalidStateException if the user is not associated with any firm
	 */
	private Firm getAuthUserFirm() {
		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(SecurityContextHolder.getContext().getAuthentication().getName())
			.build();

		User user = userRepository.findOne(filterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Firm firm = user.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		return firm;
	}
}
