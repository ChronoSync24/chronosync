package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.dto.request.PaginatedAppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppointmentTypeController}.
 */
class AppointmentTypeControllerTest {

	@Mock
	private AppointmentTypeServiceImpl appointmentTypeService;

	@InjectMocks
	private AppointmentTypeController appointmentTypeController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link AppointmentTypeController#getAppointmentTypes(PaginatedAppointmentTypeRequestDTO)} method.
	 * Verifies that the service is called with the correct parameters and the response is valid.
	 */
	@Test
	void getAppointmentTypesTest() {
		int page = 0;
		int size = 10;

		AppointmentType appointmentType = AppointmentType.builder().name("Test").colorCode("#FFFFFF").build();
		Page<AppointmentType> mockPage = new PageImpl<>(List.of(appointmentType), PageRequest.of(page, size), 1);

        PaginatedAppointmentTypeRequestDTO pageRequest = new PaginatedAppointmentTypeRequestDTO();

		when(appointmentTypeService.getAppointmentTypes(pageRequest)).thenReturn(mockPage);

		ResponseEntity<Page<AppointmentType>> response = appointmentTypeController.getAppointmentTypes(pageRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).contains(appointmentType);

		verify(appointmentTypeService, times(1)).getAppointmentTypes(pageRequest);
	}

	/**
	 * Tests the {@link AppointmentTypeController#createAppointmentType(AppointmentTypeRequestDTO)} method.
	 * Verifies that the service is called with the correct DTO and the response contains the created entity.
	 */
	@Test
	void createAppointmentTypeTest() {
		AppointmentTypeRequestDTO requestDTO = AppointmentTypeRequestDTO.builder()
			.name("test")
			.colorCode("#FFFFFF")
			.build();
		AppointmentType createdAppointmentType = AppointmentType.builder().name("Test").colorCode("#FFFFFF").build();

		when(appointmentTypeService.createAppointmentType(requestDTO)).thenReturn(createdAppointmentType);

		ResponseEntity<AppointmentType> response = appointmentTypeController.createAppointmentType(requestDTO);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(createdAppointmentType);

		verify(appointmentTypeService, times(1)).createAppointmentType(requestDTO);
	}

	/**
	 * Tests the {@link AppointmentTypeController#updateAppointmentType(AppointmentTypeRequestDTO)} method.
	 * Verifies that the service is called with the correct DTO and the response contains the updated entity.
	 */
	@Test
	void updateAppointmentTypeTest() {
		AppointmentTypeRequestDTO requestDTO = AppointmentTypeRequestDTO.builder()
			.name("test")
			.colorCode("#FFFFFF")
			.build();
		AppointmentType updatedAppointmentType = AppointmentType.builder().name("Test").colorCode("#FFFFFF").build();

		when(appointmentTypeService.updateAppointmentType(requestDTO)).thenReturn(updatedAppointmentType);

		ResponseEntity<AppointmentType> response = appointmentTypeController.updateAppointmentType(requestDTO);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(updatedAppointmentType);

		verify(appointmentTypeService, times(1)).updateAppointmentType(requestDTO);
	}

	/**
	 * Tests the {@link AppointmentTypeController#deleteAppointmentType(Long)} method.
	 * Verifies that the service is called with the correct ID and the response status is 204 (No Content).
	 */
	@Test
	void deleteAppointmentTypeTest() {
		Long id = 1L;

		doNothing().when(appointmentTypeService).deleteAppointmentType(id);

		ResponseEntity<Void> response = appointmentTypeController.deleteAppointmentType(id);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		verify(appointmentTypeService, times(1)).deleteAppointmentType(id);
	}
}
