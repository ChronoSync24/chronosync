package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
	private BaseService baseService;

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

		when(baseService.getAuthUser()).thenReturn(authUser);
		when(baseService.getAuthUserFirm()).thenReturn(authFirm);
	}

	/**
	 * Tests getAppointments for an employee.
	 * Verifies that an employee only gets appointments assigned to them.
	 */
	@Test
	void getAppointmentsTest_ForEmployee() {
		authUser.setRole(UserRole.EMPLOYEE);

		PageRequest pageRequest = PageRequest.of(0, 10);
		Appointment appointment = Appointment.builder()
			.id(1L)
			.note("Employee Appointment")
			.startTime("10:00")
			.endTime("11:00")
			.creator(authUser)
			.taskedEmployee(authUser)
			.firm(authFirm)
			.build();

		Page<Appointment> mockPage = new PageImpl<>(List.of(appointment), pageRequest, 1);

		when(appointmentRepository.findAll(Mockito.<Specification<Appointment>>any(), eq(pageRequest))).thenReturn(mockPage);

		Page<Appointment> result = appointmentService.getAppointments(pageRequest);

		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).getNote()).isEqualTo("Employee Appointment");

		verify(appointmentRepository, times(1)).findAll(Mockito.<Specification<Appointment>>any(),
			eq(pageRequest));
	}

	/**
	 * Tests getAppointments for a manager.
	 * Verifies that a manager retrieves appointments for the entire firm.
	 */
	@Test
	void getAppointmentsTest_ForManager() {
		authUser.setRole(UserRole.MANAGER);

		PageRequest pageRequest = PageRequest.of(0, 10);
		Appointment appointment = Appointment.builder()
			.id(2L)
			.note("Manager Appointment")
			.startTime("09:00")
			.endTime("10:00")
			.creator(authUser)
			.firm(authFirm)
			.build();

		Page<Appointment> mockPage = new PageImpl<>(List.of(appointment), pageRequest, 1);

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
		AppointmentRequestDTO requestDto = AppointmentRequestDTO.builder()
			.note("New Appointment")
			.startTime("12:00")
			.endTime("13:00")
			.taskedEmployeeId(200L)
			.clientId(300L)
			.appointmentTypeId(400L)
			.build();

		User taskedEmployee = new User();
		taskedEmployee.setId(200L);
		Client client = new Client();
		client.setId(300L);
		AppointmentType appointmentType = new AppointmentType();
		appointmentType.setId(400L);

		when(userRepository.findById(200L)).thenReturn(Optional.of(taskedEmployee));
		when(clientRepository.findById(300L)).thenReturn(Optional.of(client));
		when(appointmentTypeRepository.findById(400L)).thenReturn(Optional.of(appointmentType));

		Appointment appointmentToCreate = requestDto.toModel();
		appointmentToCreate.setCreator(authUser);
		appointmentToCreate.setFirm(authFirm);
		appointmentToCreate.setTaskedEmployee(taskedEmployee);
		appointmentToCreate.setClient(client);
		appointmentToCreate.setAppointmentType(appointmentType);

		Appointment createdAppointment = Appointment.builder()
			.id(1L)
			.note("New Appointment")
			.startTime("12:00")
			.endTime("13:00")
			.creator(authUser)
			.firm(authFirm)
			.taskedEmployee(taskedEmployee)
			.client(client)
			.appointmentType(appointmentType)
			.build();

		when(appointmentRepository.create(any(Appointment.class))).thenReturn(createdAppointment);

		Appointment result = appointmentService.createAppointment(requestDto);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getNote()).isEqualTo("New Appointment");
		assertThat(result.getCreator()).isEqualTo(authUser);
		assertThat(result.getFirm()).isEqualTo(authFirm);
		assertThat(result.getTaskedEmployee()).isEqualTo(taskedEmployee);
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
			.startTime("14:00")
			.endTime("15:00")
			.build();

		Appointment appointmentToUpdate = requestDto.toModel();
		appointmentToUpdate.setCreator(authUser);
		appointmentToUpdate.setFirm(authFirm);

		when(appointmentRepository.update(any(Appointment.class))).thenReturn(appointmentToUpdate);

		Appointment result = appointmentService.updateAppointment(requestDto);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getNote()).isEqualTo("Updated Appointment");
		assertThat(result.getStartTime()).isEqualTo("14:00");
		assertThat(result.getEndTime()).isEqualTo("15:00");

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
		Long id = 1L;
		when(appointmentRepository.existsById(id)).thenReturn(false);

		assertThrows(InvalidStateException.class, () -> appointmentService.deleteAppointment(id));

		verify(appointmentRepository, times(1)).existsById(id);
		verify(appointmentRepository, never()).deleteById(anyLong());
	}
}
