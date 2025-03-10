package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
public class AppointmentRepositoryTest {

	@Autowired
	private AppointmentRepository appointmentRepository;

	/**
	 * Tests the creation and retrieval of an Appointment entity.
	 * It verifies that an Appointment can be created using the create() method and later retrieved via findAll().
	 */
	@Test
	void testCreateAndFindAllAppointments() {
		Appointment appointment = Appointment.builder()
			.note("Test Appointment")
			.startTime("10:00")
			.endTime("11:00")
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
		Appointment appointment = Appointment.builder()
			.note("SpecTest")
			.startTime("12:00")
			.endTime("13:00")
			.build();
		appointmentRepository.create(appointment);

		Specification<Appointment> spec = (root, query, cb) ->
			cb.equal(root.get("note"), "SpecTest");

		Optional<Appointment> found = appointmentRepository.findOne(spec);
		assertThat(found).isPresent();
		assertThat(found.get().getNote()).isEqualTo("SpecTest");
	}

	/**
	 * Tests the pagination functionality of the repository.
	 * It creates multiple appointments and verifies that the paginated query returns the expected results.
	 */
	@Test
	void testPagination() {
		for (int i = 1; i <= 15; i++) {
			Appointment appointment = Appointment.builder()
				.note("Appointment " + i)
				.startTime("08:00")
				.endTime("09:00")
				.build();
			appointmentRepository.create(appointment);
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
