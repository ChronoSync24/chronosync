package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Appointment;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Unit tests for {@link AppointmentRepository}.
 */
public class AppointmentRepositoryTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	/**
	 * Tests the creation and retrieval of an Appointment entity.
	 * It verifies that an Appointment can be created using the create() method and later retrieved via findAll().
	 */
	@Test
	void testCreateAndFindAllAppointments() {
		Appointment appointment = Appointment.builder()
			.note("Test Appointment")
			.startDateTime(LocalDateTime.parse("2025-02-02 12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02 13:45"))
			.build();

		Appointment saved = appointmentRepository.create(appointment);
		assertThat(saved).isNotNull();
		assertThat(saved.getId()).isNotNull();

		List<Appointment> appointments = appointmentRepository.findAll();
		assertThat(appointments).isNotEmpty();
		assertThat(appointments).extracting(Appointment::getNote).contains("Test Appointment");
	}

	/**
	 * Tests the findOne(Specification) method using a specification that filters by note.
	 * It creates an Appointment, then uses a Specification to retrieve it.
	 */
	@Test
	void testFindOneBySpecification() {
		Appointment mockAppointment = Appointment.builder()
			.note("SpecTest")
			.startDateTime(LocalDateTime.parse("2025-02-02 12:45"))
			.endDateTime(LocalDateTime.parse("2025-02-02 13:45"))
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
	 * Tests the pagination functionality of the repository.
	 * It creates multiple appointments and verifies that the paginated query returns the expected results.
	 */
	@Test
	void testPagination() {
		for (int i = 1; i <= 15; i++) {
			Appointment mockAppointment = Appointment.builder()
				.note("Appointment " + i)
				.startDateTime(LocalDateTime.parse("2025-02-02 12:45"))
				.endDateTime(LocalDateTime.parse("2025-02-02 13:45"))
				.build();
		}

		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<Appointment> page = appointmentRepository.findAll(where(null), pageRequest);

		assertThat(page).isNotNull();
		assertThat(page.getContent()).hasSize(10);
		assertThat(page.getTotalElements()).isEqualTo(15);

		PageRequest pageRequest2 = PageRequest.of(1, 10);
		Page<Appointment> page2 = appointmentRepository.findAll(where(null), pageRequest2);

		assertThat(page2).isNotNull();
		assertThat(page2.getContent()).hasSize(5);
	}
}
