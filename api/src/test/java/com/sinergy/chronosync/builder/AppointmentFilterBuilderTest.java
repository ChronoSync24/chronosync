package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Appointment;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AppointmentFilterBuilder}.
 *
 * <p>Verifies the behavior of the {@link AppointmentFilterBuilder} class,
 * specifically its ability to convert filter criteria into a {@link Specification}
 * for querying {@link Appointment} entities based on attributes.</p>
 *
 * <p>Mock objects are utilized to simulate the database environment, including
 * {@link Root}, {@link CriteriaBuilder}, {@link Predicate}, and {@link Path} instances.</p>
 *
 * <p>The tests ensure that the {@link AppointmentFilterBuilder#toSpecification()} method
 * constructs the appropriate predicates and combines them as expected using the criteria builder.</p>
 */
public class AppointmentFilterBuilderTest {

	@Mock
	private Root<Appointment> root;
	@Mock
	private CriteriaQuery<?> query;
	@Mock
	private CriteriaBuilder criteriaBuilder;
	@Mock
	private Predicate predicate;
	@Mock
	private Path<String> notePath;
	@Mock
	private Path<LocalDateTime> startTimePath;
	@Mock
	private Path<LocalDateTime> endTimePath;
	@Mock
	private Path<Long> clientIdPath;
	@Mock
	private Path<Long> taskedEmployeeIdPath;
	@Mock
	private Path<Long> appointmentTypeIdPath;
	@Mock
	private Path<Long> firmIdPath;

	private AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	/**
	 * Verifies that the {@link AppointmentFilterBuilder#toSpecification()} method correctly builds
	 * a {@link Specification} based on provided filter values.
	 */
	@Test
	void toSpecificationTest() {
		String note = "Test appointment";
		LocalDateTime startDateTime = LocalDateTime.parse("2025-02-02T10:00");
		LocalDateTime endDateTime = LocalDateTime.parse("2025-02-02T11:00");
		Long clientId = 2L;
		Long taskedEmployeeId = 3L;
		Long appointmentTypeId = 4L;
		Long firmId = 1L;

		AppointmentFilterBuilder filterBuilder = AppointmentFilterBuilder.builder()
			.note(note)
			.startDateTime(startDateTime)
			.endDateTime(endDateTime)
			.clientId(clientId)
			.taskedEmployeeId(taskedEmployeeId)
			.appointmentTypeId(appointmentTypeId)
			.firmId(firmId)
			.build();

		when(root.<String>get("note")).thenReturn(notePath);
		when(root.<LocalDateTime>get("startDateTime")).thenReturn(startTimePath);
		when(root.<LocalDateTime>get("endDateTime")).thenReturn(endTimePath);
		when(root.<Long>get("client")).thenReturn(clientIdPath);
		when(root.<Long>get("firm")).thenReturn(firmIdPath);
		when(root.<Long>get("appointmentType")).thenReturn(appointmentTypeIdPath);
		when(root.<Long>get("taskedEmployee")).thenReturn(taskedEmployeeIdPath);
		when(clientIdPath.<Long>get("id")).thenReturn(clientIdPath);
		when(taskedEmployeeIdPath.<Long>get("id")).thenReturn(taskedEmployeeIdPath);
		when(appointmentTypeIdPath.<Long>get("id")).thenReturn(appointmentTypeIdPath);
		when(firmIdPath.<Long>get("id")).thenReturn(firmIdPath);

		when(criteriaBuilder.equal(startTimePath, startDateTime)).thenReturn(predicate);
		when(criteriaBuilder.equal(endTimePath, endDateTime)).thenReturn(predicate);
		when(criteriaBuilder.equal(clientIdPath, clientId)).thenReturn(predicate);
		when(criteriaBuilder.equal(taskedEmployeeIdPath, taskedEmployeeId)).thenReturn(predicate);
		when(criteriaBuilder.equal(appointmentTypeIdPath, appointmentTypeId)).thenReturn(predicate);
		when(criteriaBuilder.equal(firmIdPath, firmId)).thenReturn(predicate);
		when(criteriaBuilder.like(notePath, "%" + note + "%")).thenReturn(predicate);
		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<Appointment> spec = filterBuilder.toSpecification();
		assertNotNull(spec);
		spec.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(startTimePath, startDateTime);
		verify(criteriaBuilder).equal(endTimePath, endDateTime);
		verify(criteriaBuilder).equal(clientIdPath, clientId);
		verify(criteriaBuilder).equal(taskedEmployeeIdPath, taskedEmployeeId);
		verify(criteriaBuilder).equal(appointmentTypeIdPath, appointmentTypeId);
		verify(criteriaBuilder).equal(firmIdPath, firmId);
		verify(criteriaBuilder).like(notePath, "%" + note + "%");
		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}