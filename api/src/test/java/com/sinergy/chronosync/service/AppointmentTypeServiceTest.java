package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.dto.request.PaginatedAppointmentTypeRequestDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppointmentTypeServiceImpl}.
 */
class AppointmentTypeServiceTest {

	@Mock
	private AppointmentTypeRepository appointmentTypeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private SecurityContextService securityContextService;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private AppointmentTypeServiceImpl appointmentTypeService;

	/**
	 * Sets up the mock dependencies before each test.
	 */
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("testUser");
		SecurityContextHolder.setContext(securityContext);

		Firm firm = new Firm();
		firm.setId(1L);
		when(securityContextService.getAuthUserFirm()).thenReturn(firm);
	}

	/**
	 * Tests the getUserAppointmentTypes method when the user is valid.
	 */
	@Test
	void getUserAppointmentTypesTest() {
		when(userRepository.findOne(Mockito.<Specification<User>>any()))
			.thenReturn(Optional.of(getUser()));

		AppointmentType mockAppointmentType = AppointmentType.builder().name("Test Appointment Type").build();

		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<AppointmentType> appointmentTypes = new PageImpl<>(List.of(mockAppointmentType));

		when(appointmentTypeRepository.findAll(Mockito.<Specification<AppointmentType>>any(), eq(pageRequest)))
			.thenReturn(appointmentTypes);

        PaginatedAppointmentTypeRequestDTO paginatedRequest = new PaginatedAppointmentTypeRequestDTO();

		Page<AppointmentType> result = appointmentTypeService.getAppointmentTypes(paginatedRequest);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("Test Appointment Type", result.getContent().getFirst().getName());

		verify(appointmentTypeRepository, times(1)).findAll(
			Mockito.<Specification<AppointmentType>>any(),
			eq(pageRequest)
		);
	}

	/**
	 * Tests the createAppointmentType method when the request is valid.
	 */
	@Test
	void createAppointmentTypeTest() {
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(getUser()));

		AppointmentTypeRequestDTO requestDto = AppointmentTypeRequestDTO.builder()
			.name("New Appointment")
			.durationMinutes(30)
			.price(200.0)
			.build();

		AppointmentType mockAppointmentType = getAppointmentType();

		when(appointmentTypeRepository.create(Mockito.any(AppointmentType.class))).thenReturn(mockAppointmentType);

		AppointmentType createdAppointmentType = appointmentTypeService.createAppointmentType(requestDto);

		assertNotNull(createdAppointmentType);
		assertEquals("New Appointment", createdAppointmentType.getName());
		assertEquals(30, createdAppointmentType.getDurationMinutes());
		assertEquals(200.0, createdAppointmentType.getPrice());

		verify(appointmentTypeRepository, times(1)).create(Mockito.any(AppointmentType.class));
	}

	/**
	 * Tests the deleteAppointmentType method when the appointment type exists.
	 */
	@Test
	void deleteAppointmentTypeTest() {
		Long appointmentTypeId = 1L;

		when(appointmentTypeRepository.existsById(appointmentTypeId)).thenReturn(true);

		appointmentTypeService.deleteAppointmentType(appointmentTypeId);

		verify(appointmentTypeRepository, times(1)).deleteById(appointmentTypeId);
	}

	/**
	 * Tests the deleteAppointmentType method when the appointment type does not exist.
	 */
	@Test
	void deleteAppointmentTypeInvalidIdStateExceptionTest() {
		Long appointmentTypeId = 1L;

		when(appointmentTypeRepository.existsById(appointmentTypeId)).thenReturn(false);

		EntityNotFoundException thrownException = assertThrows(
			EntityNotFoundException.class,
			() -> appointmentTypeService.deleteAppointmentType(appointmentTypeId)
		);

		assertEquals("Appointment type does not exist.", thrownException.getMessage());

		verify(appointmentTypeRepository, never()).deleteById(appointmentTypeId);
	}

	/**
	 * Tests the updateAppointmentType method with valid data.
	 */
	@Test
	void updateAppointmentTypeTest() {
		AppointmentTypeRequestDTO requestDto = AppointmentTypeRequestDTO.builder()
			.id(1L)
			.name("updatedName")
			.durationMinutes(60)
			.price(100.0)
			.build();

		when(appointmentTypeRepository.update(Mockito.any(AppointmentType.class))).thenReturn(requestDto.toModel());

		AppointmentType updatedAppointmentType = appointmentTypeService.updateAppointmentType(requestDto);

		assertNotNull(updatedAppointmentType);
		assertEquals(requestDto.getName(), updatedAppointmentType.getName());
		assertEquals(requestDto.getDurationMinutes(), updatedAppointmentType.getDurationMinutes());
		assertEquals(requestDto.getPrice(), updatedAppointmentType.getPrice());

		verify(appointmentTypeRepository, times(1)).update(Mockito.any(AppointmentType.class));
	}

	/**
	 * Gets mock user.
	 * @return {@link User} mocked user class
	 */
	private User getUser() {
		User mockUser = new User();
		mockUser.setUsername("test user");

		Firm mockFirm = new Firm();
		mockFirm.setId(1L);
		mockUser.setFirm(mockFirm);

		return mockUser;
	}

	/**
	 * Gets mock appointment type.
	 * @return {@link AppointmentType}
	 */
	private AppointmentType getAppointmentType() {
		AppointmentType appointmentType = AppointmentType.builder()
			.name("New Appointment")
			.durationMinutes(30)
			.price(200.0)
			.firm(getUser().getFirm())
			.build();

		appointmentType.setId(1L);

		return appointmentType;
	}
}
