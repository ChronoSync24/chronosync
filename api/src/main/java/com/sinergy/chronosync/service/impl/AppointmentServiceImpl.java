package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.AppointmentFilterBuilder;
import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.RepositoryException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentRepository;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.AppointmentService;
import com.sinergy.chronosync.service.BaseService;
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
	private final UserRepository userRepository;
	private final ClientRepository clientRepository;
	private final AppointmentTypeRepository appointmentTypeRepository;
	private final BaseService baseService;

	/**
	 * Retrieves all appointments associated with the current user.
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link Appointment} objects linked to that user's ID.
	 *
	 * @return {@link Page} of {@link Appointment} objects associated with the current user.
	 */
	@Override
	public Page<Appointment> getAppointments(PageRequest pageRequest) {
		User authUser = baseService.getAuthUser();
		AppointmentFilterBuilder filterBuilder;

		int role = authUser.getRole().ordinal();
		if (role == 0) {
			filterBuilder = AppointmentFilterBuilder.builder()
				.firmId(baseService.getAuthUserFirm().getId())
				.build();
		} else {
			filterBuilder = AppointmentFilterBuilder.builder()
				.taskedEmployeeId(baseService.getAuthUser().getId())
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
		User authUser = baseService.getAuthUser();
		requestDto.setCreator(authUser);

		if (requestDto.getTaskedEmployee().getId() != null) {
			User taskedEmployee = userRepository.findById(requestDto.getTaskedEmployee().getId())
				.orElseThrow(() -> new UserNotFoundException("Tasked employee not found"));
			requestDto.setTaskedEmployee(taskedEmployee);
		}
		if (requestDto.getClient().getId() != null) {
			Client client = clientRepository.findById(requestDto.getClient().getId())
				.orElseThrow(() -> new RepositoryException("Client not found"));
			requestDto.setClient(client);
		}
		if (requestDto.getAppointmentType().getId() != null) {
			AppointmentType appointmentType = appointmentTypeRepository.findById(requestDto.getAppointmentType().getId())
				.orElseThrow(() -> new RepositoryException("Appointment type not found"));
			requestDto.setAppointmentType(appointmentType);
		}

		return appointmentRepository.save(requestDto.toModel());
	}

	/**
	 * Updates an existing appointment.
	 *
	 * @param requestDto {@link AppointmentRequestDTO} containing appointment details
	 * @return {@link Appointment} representing the updated or newly created appointment
	 */
	@Override
	public Appointment updateAppointment(AppointmentRequestDTO requestDto) {
		return appointmentRepository.save(requestDto.toModel());
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
			throw new InvalidStateException("Appointment does not exist.");
		}
		appointmentRepository.deleteById(id);
	}
}
