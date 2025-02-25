package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.dto.request.BasePaginationRequest;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing appointment types.
 * <p>
 * This controller provides endpoints for retrieving, creating, updating, and deleting appointment types.
 * </p>
 */
@RestController
@RequestMapping(path = "api/v1/appointment-type")
@RequiredArgsConstructor
public class AppointmentTypeController {

	private final AppointmentTypeServiceImpl appointmentTypeService;

	/**
	 * Retrieves a paginated list of appointment types associated with the current user's firm.
	 *
	 * <p>This endpoint allows users associated with a firm to view appointment types
	 * specific to their firm. It ensures data isolation by returning only the appointment
	 * types linked to the logged-in user's firm.</p>
	 *
	 * <p>The pagination details, such as the page number and page size, are provided
	 * in the request body using the {@link BasePaginationRequest} class. Default values
	 * are used if not specified.</p>
	 *
	 */
	@PostMapping("/search")
	public ResponseEntity<Page<AppointmentType>> getAppointmentTypes(
		@RequestBody BasePaginationRequest paginationRequest
	) {
		int page = paginationRequest.getPage();
		int size = paginationRequest.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<AppointmentType> appointmentTypes = appointmentTypeService.getAppointmentTypes(pageRequest);
		return ResponseEntity.ok(appointmentTypes);
	}

	/**
	 * Creates new appointment type for the current user's firm.
	 *
	 * @param request {@link AppointmentTypeRequestDTO} containing the details of the new appointment type
	 * @return created {@link AppointmentType} along with an HTTP status of 201 (Created)
	 */
	@PostMapping("/create")
	public ResponseEntity<AppointmentType> createAppointmentType(
		@RequestBody AppointmentTypeRequestDTO request
	) {
		AppointmentType appointmentType = appointmentTypeService.createAppointmentType(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(appointmentType);
	}

	/**
	 * Updates an existing appointment type identified by its ID, or throws an exception.
	 *
	 * @param request {@link AppointmentTypeRequestDTO} containing details of the appointment type to update or create
	 * @return {@link ResponseEntity} containing the updated or created {@link AppointmentType} and HTTP status
	 */
	@PutMapping
	public ResponseEntity<AppointmentType> updateAppointmentType(
		@RequestBody AppointmentTypeRequestDTO request
	) {
		AppointmentType updatedAppointmentType = appointmentTypeService.updateAppointmentType(request);
		return ResponseEntity.ok(updatedAppointmentType);
	}

	/**
	 * Deletes an appointment type by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteAppointmentType(
		@RequestParam Long id
	) {
		appointmentTypeService.deleteAppointmentType(id);
		return ResponseEntity.noContent().build();
	}
}
