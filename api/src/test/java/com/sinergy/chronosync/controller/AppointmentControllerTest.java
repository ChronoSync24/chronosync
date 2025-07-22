package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.dto.request.BasePaginationRequest;
import com.sinergy.chronosync.dto.request.PaginatedAppointmentRequestDTO;
import com.sinergy.chronosync.dto.request.PaginatedClientRequestDTO;
import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppointmentController}.
 */
class AppointmentControllerTest {

	@Mock
	private AppointmentService appointmentService;

	@InjectMocks
	private AppointmentController appointmentController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link AppointmentController#getAppointments(PaginatedAppointmentRequestDTO)} method.
	 * Verifies that the service is called with the correct parameters and the response is valid.
	 */
	@Test
	void getAppointmentsTest() {
		int page = 0;
		int size = 10;

		Appointment appointment = Appointment.builder()
			.id(1L)
			.note("Test Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.build();
		Page<Appointment> mockPage = new PageImpl<>(List.of(appointment), PageRequest.of(page, size), 1);

		PaginatedAppointmentRequestDTO pageRequest = new PaginatedAppointmentRequestDTO();

		when(appointmentService.getAppointments(pageRequest)).thenReturn(mockPage);

		ResponseEntity<Page<Appointment>> response = appointmentController.getAppointments(pageRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).contains(appointment);

		verify(appointmentService, times(1)).getAppointments(pageRequest);
	}

	/**
	 * Tests the {@link AppointmentController#createAppointment(AppointmentRequestDTO)} method.
	 * Verifies that the service is called with the correct DTO and the response contains the created entity.
	 */
	@Test
	void createAppointmentTest() {
		AppointmentRequestDTO requestDto = AppointmentRequestDTO.builder()
			.note("New Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.build();

		Appointment createdAppointment = Appointment.builder()
			.id(1L)
			.note("New Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.build();

		when(appointmentService.createAppointment(requestDto)).thenReturn(createdAppointment);

		ResponseEntity<Appointment> response = appointmentController.createAppointment(requestDto);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(createdAppointment);

		verify(appointmentService, times(1)).createAppointment(requestDto);
	}

	/**
	 * Tests the {@link AppointmentController#updateAppointment(AppointmentRequestDTO)} method.
	 * Verifies that the service is called with the correct DTO and the response contains the updated entity.
	 */
	@Test
	void updateAppointmentTest() {
		AppointmentRequestDTO requestDto = AppointmentRequestDTO.builder()
			.id(1L)
			.note("Updated Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.build();

		Appointment updatedAppointment = Appointment.builder()
			.id(1L)
			.note("Updated Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.build();

		when(appointmentService.updateAppointment(requestDto)).thenReturn(updatedAppointment);

		ResponseEntity<Appointment> response = appointmentController.updateAppointment(requestDto);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(updatedAppointment);

		verify(appointmentService, times(1)).updateAppointment(requestDto);
	}

	/**
	 * Tests the {@link AppointmentController#deleteAppointment(Long)} method.
	 * Verifies that the service is called with the correct ID and the response status is 204 (No Content).
	 */
	@Test
	void deleteAppointmentTest() {
		Long id = 1L;
		doNothing().when(appointmentService).deleteAppointment(id);

		ResponseEntity<Void> response = appointmentController.deleteAppointment(id);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(appointmentService, times(1)).deleteAppointment(id);
	}
}
