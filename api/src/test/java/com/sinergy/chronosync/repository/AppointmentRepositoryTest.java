package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppointmentRepository}.
 */
public class AppointmentRepositoryTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the creation of an Appointment entity.
	 * Ensures that a new Appointment can be saved and has a generated ID.
	 */
	@Test
	void testCreateAppointment() {
		Appointment mockAppointment = new Appointment();
		mockAppointment.setId(1L);

		when(appointmentRepository.create(any(Appointment.class))).thenReturn(mockAppointment);

		Appointment saved = appointmentRepository.create(mockAppointment);

		assertThat(saved).isNotNull();
		assertThat(saved.getId()).isNotNull();
	}

	/**
	 * Tests the retrieval of all Appointment entities.
	 * Ensures that findAll() returns a non-empty list when appointments exist.
	 */
	@Test
	void findAllAppointmentTypesTest() {
		List<Appointment> testAppointments = getAppointments();
		when(appointmentRepository.findAll()).thenReturn(testAppointments);
		List<Appointment> appointments = appointmentRepository.findAll();
		assertThat(appointments).hasSize(getAppointments().size());
	    verify(appointmentRepository, times(1)).findAll();
	}


	/**
	 * Tests the findOne(Specification) method using a specification that filters by note.
	 * It creates an Appointment, then uses a Specification to retrieve it.
	 */
	@Test
	void testFindOneBySpecification() {
		Appointment mockAppointment = Appointment.builder()
			.note("SpecTest")
			.startDateTime(LocalDateTime.parse("2025-02-02T12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02T13:45"))
			.build();

		Specification<Appointment> spec = (root, query, cb) ->
			cb.equal(root.get("note"), "SpecTest");

		when(appointmentRepository.findOne(spec)).thenReturn(Optional.of(mockAppointment));
		Optional<Appointment> found = appointmentRepository.findOne(spec);
		assertThat(found).isPresent();
		assertThat(found.get().getNote()).isEqualTo("SpecTest");

		verify(appointmentRepository).findOne(spec);
	}

	/**
	 * Helper method to generate a list of {@link Appointment} objects for testing purposes.
	 *
	 * @return {@link List<Appointment>} a list of appointments
	 */
	private List<Appointment> getAppointments() {
		Appointment appointment1 = new Appointment();
		appointment1.setId(1L);
		appointment1.setNote("Test Appointment");
		appointment1.setStartDateTime(LocalDateTime.of(2025, 2, 2, 12, 45));
		appointment1.setEndDateTime(LocalDateTime.of(2025, 2, 2, 13, 45));

		Appointment appointment2 = new Appointment();
		appointment2.setId(2L);
		appointment2.setNote("Test Appointment");
		appointment2.setStartDateTime(LocalDateTime.of(2025, 2, 2, 13, 45));
		appointment2.setEndDateTime(LocalDateTime.of(2025, 2, 2, 14, 45));

		return List.of(appointment1, appointment2);
	}
}
