package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.dto.request.BasePaginationRequest;
import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing appointments.
 */
@RestController
@RequestMapping(path = "api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	/**
	 * Retrieves a paginated list of appointments.
	 *
	 * <p>The pagination details, such as the page number and page size, are provided
	 * in the request body using the {@link BasePaginationRequest} class. Default values
	 * are used if not specified.</p>
	 */
	@PostMapping("/search")
	public ResponseEntity<Page<Appointment>> getAppointments(
		@RequestBody BasePaginationRequest paginationRequest
	) {
		int page = paginationRequest.getPage();
		int size = paginationRequest.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Appointment> appointments = appointmentService.getAppointments(pageRequest);
		return ResponseEntity.ok(appointments);
	}

	/**
	 * Creates new appointment.
	 *
	 * @param request {@link AppointmentRequestDTO} containing the details of the new appointment
	 * @return created {@link Appointment} along with an HTTP status of 201 (Created)
	 */
	@PostMapping("/create")
	public ResponseEntity<Appointment> createAppointment(
		@RequestBody AppointmentRequestDTO request
	) {
		Appointment appointment = appointmentService.createAppointment(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
	}

	/**
	 * Updates an existing appointment.
	 *
	 * @param request {@link AppointmentRequestDTO} containing details of the appointment to update or create
	 * @return {@link ResponseEntity} containing the updated or created {@link Appointment} and HTTP status
	 */
	@PutMapping
	public ResponseEntity<Appointment> updateAppointment(
		@RequestBody AppointmentRequestDTO request
	) {
		Appointment appointment = appointmentService.updateAppointment(request);
		return ResponseEntity.ok(appointment);
	}

	/**
	 * Deletes an appointment by its ID.
	 *
	 * @param id {@link Long} ID of the appointment to delete
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteAppointment(
		@RequestParam Long id
	) {
		appointmentService.deleteAppointment(id);
		return ResponseEntity.noContent().build();
	}
}
