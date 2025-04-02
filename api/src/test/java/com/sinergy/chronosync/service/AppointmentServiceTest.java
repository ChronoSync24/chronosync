package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.AppointmentRepository;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppointmentServiceImpl}
 */
class AppointmentServiceImplTest {

	@Mock
	private AppointmentRepository appointmentRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ClientRepository clientRepository;
	@Mock
	private AppointmentTypeRepository appointmentTypeRepository;
	@Mock
	private SecurityContextService securityContextService;

	@InjectMocks
	private AppointmentServiceImpl appointmentService;

	private User authUser;
	private Firm authFirm;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		authFirm = new Firm();
		authFirm.setId(1L);
		authFirm.setName("Test Firm");

		authUser = new User();
		authUser.setId(100L);
		authUser.setUsername("testUser");
		authUser.setRole(UserRole.EMPLOYEE);
		authUser.setFirm(authFirm);

		when(securityContextService.getAuthUser()).thenReturn(authUser);
		when(securityContextService.getAuthUserFirm()).thenReturn(authFirm);
	}

	/**
	 * Tests getAppointments for an employee.
	 * Verifies that an employee only gets appointments assigned to them.
	 */
	@Test
	void getEmployeeAppointmentsTest() {
		authUser.setRole(UserRole.EMPLOYEE);

		PageRequest pageRequest = PageRequest.of(0, 10);

		Appointment assignedAppointment = Appointment.builder()
			.id(1L)
			.note("Employee Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.createdBy(authUser)
			.employee(authUser)
			.firm(authFirm)
			.build();

		User otherEmployee = mock(User.class);
		when(otherEmployee.getId()).thenReturn(999L);

		Appointment notAssignedAppointment = Appointment.builder()
			.id(2L)
			.note("Other Employee's Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-03T10:00"))
			.endDateTime(LocalDateTime.parse("2025-02-03T11:00"))
			.createdBy(otherEmployee)
			.employee(otherEmployee)
			.firm(authFirm)
			.build();

		Page<Appointment> mockPage = new PageImpl<>(List.of(assignedAppointment), pageRequest, 1);
		when(appointmentRepository.findAll(Mockito.<Specification<Appointment>>any(), eq(pageRequest))).thenReturn(mockPage);

		Page<Appointment> result = appointmentService.getAppointments(pageRequest);

		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).containsExactly(assignedAppointment);
		assertThat(result.getContent()).doesNotContain(notAssignedAppointment);

		verify(appointmentRepository, times(1)).findAll(Mockito.<Specification<Appointment>>any(), eq(pageRequest));
	}


	/**
	 * Tests getAppointments for a manager.
	 * Verifies that a manager retrieves appointments for the entire firm.
	 */
	@Test
	void getManagerAppointmentsTest() {
		authUser.setRole(UserRole.MANAGER);
		Firm mockFirm = mock(Firm.class);
		mockFirm.setId(1L);
		PageRequest pageRequest = PageRequest.of(0, 10);
		Appointment appointment = Appointment.builder()
			.id(2L)
			.note("Manager Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.createdBy(authUser)
			.firm(authFirm)
			.build();

		Appointment mockAppointment = mock(Appointment.class);
		mockAppointment.setFirm(mockFirm);
		mockAppointment.setNote("Manager Appointment");

		Page<Appointment> mockPage = new PageImpl<>(List.of(appointment), pageRequest, 2);

		when(appointmentRepository.findAll(Mockito.<Specification<Appointment>>any(),
			eq(pageRequest))).thenReturn(mockPage);

		Page<Appointment> result = appointmentService.getAppointments(pageRequest);

		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).getNote()).isEqualTo("Manager Appointment");

		verify(appointmentRepository, times(1)).findAll(Mockito.<Specification<Appointment>>any(),
			eq(pageRequest));
	}

	/**
	 * Tests createAppointment method.
	 * Verifies that the appointment is created with the correct creator, firm, and associations.
	 */
	@Test
	void createAppointmentTest() {
		User mockUser = new User();
		Client mockClient = new Client();

		User taskedEmployee = new User();
		taskedEmployee.setId(200L);
		Client client = new Client();
		client.setId(300L);
		AppointmentType appointmentType = new AppointmentType();
		appointmentType.setId(400L);

		AppointmentType mockAppointmentType = new AppointmentType();
		AppointmentRequestDTO requestDto = AppointmentRequestDTO.builder()
			.note("New Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.employee(mockUser)
			.client(mockClient)
			.appointmentType(mockAppointmentType)
			.createdBy(authUser)
			.firm(authFirm)
			.employee(taskedEmployee)
			.client(client)
			.appointmentType(appointmentType)
			.build();

		when(userRepository.findById(200L)).thenReturn(Optional.of(taskedEmployee));
		when(clientRepository.findById(300L)).thenReturn(Optional.of(client));
		when(appointmentTypeRepository.findById(400L)).thenReturn(Optional.of(appointmentType));

		Appointment createdAppointment = Appointment.builder()
			.id(1L)
			.note("New Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.createdBy(authUser)
			.firm(authFirm)
			.employee(taskedEmployee)
			.client(client)
			.appointmentType(appointmentType)
			.build();

		when(appointmentRepository.create(any(Appointment.class))).thenReturn(createdAppointment);

		Appointment result = appointmentService.createAppointment(requestDto);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(createdAppointment.getId());
		assertThat(result.getNote()).isEqualTo(createdAppointment.getNote());
		assertThat(result.getCreatedBy()).isEqualTo(authUser);
		assertThat(result.getFirm()).isEqualTo(authFirm);
		assertThat(result.getEmployee()).isEqualTo(taskedEmployee);
		assertThat(result.getClient()).isEqualTo(client);
		assertThat(result.getAppointmentType()).isEqualTo(appointmentType);

		verify(appointmentRepository, times(1)).create(any(Appointment.class));
	}

	/**
	 * Tests updateAppointment method.
	 * Verifies that an existing appointment is updated with the new details.
	 */
	@Test
	void updateAppointmentTest() {
		AppointmentRequestDTO requestDto = AppointmentRequestDTO.builder()
			.id(1L)
			.note("Updated Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.createdBy(authUser)
			.firm(authFirm)
			.build();

		when(appointmentRepository.update(any(Appointment.class))).thenReturn(requestDto.toModel());

		Appointment result = appointmentService.updateAppointment(requestDto);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getNote()).isEqualTo("Updated Appointment");
		assertThat(result.getStartDateTime()).isEqualTo("2025-02-02T12:45");
		assertThat(result.getEndDateTime()).isEqualTo("2025-02-02T13:45");

		verify(appointmentRepository, times(1)).update(any(Appointment.class));
	}

	/**
	 * Tests deleteAppointment method when the appointment exists.
	 */
	@Test
	void deleteAppointmentTest() {
		Long id = 1L;
		when(appointmentRepository.existsById(id)).thenReturn(true);
		doNothing().when(appointmentRepository).deleteById(id);

		appointmentService.deleteAppointment(id);

		verify(appointmentRepository, times(1)).existsById(id);
		verify(appointmentRepository, times(1)).deleteById(id);
	}

	/**
	 * Tests deleteAppointment method when the appointment does not exist.
	 * Expects an InvalidStateException to be thrown.
	 */
	@Test
	void deleteAppointmentNotFoundTest() {
		Appointment mockAppointment = new Appointment();
		mockAppointment.setId(1L);
		when(appointmentRepository.existsById(mockAppointment.getId())).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> appointmentService.deleteAppointment(mockAppointment.getId()));

		verify(appointmentRepository, times(1)).existsById(mockAppointment.getId());
		verify(appointmentRepository, never()).deleteById(anyLong());
	}
}
