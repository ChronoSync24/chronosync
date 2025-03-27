package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Appointment;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

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
	private Path<String> startTimePath;
	@Mock
	private Path<String> endTimePath;
	@Mock
	private Path<String> datePath;
	@Mock
	private Path<Long> clientIdPath;
	@Mock
	private Path<Long> taskedEmployeeIdPath;
	@Mock
	private Path<Long> appointmentTypeIdPath;
	@Mock
	private Path<Long> creatorIdPath;
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
		String startTime = "10:00";
		String endTime = "11:00";
		String date = "2025-07-07";
		Long clientId = 2L;
		Long taskedEmployeeId = 3L;
		Long appointmentTypeId = 4L;
		Long creatorId = 5L;
		Long firmId = 1L;

		AppointmentFilterBuilder filterBuilder = AppointmentFilterBuilder.builder()
			.note(note)
			.startTime(startTime)
			.endTime(endTime)
			.date(date)
			.clientId(clientId)
			.taskedEmployeeId(taskedEmployeeId)
			.appointmentTypeId(appointmentTypeId)
			.creatorId(creatorId)
			.firmId(firmId)
			.build();

		when(root.<String>get("note")).thenReturn(notePath);
		when(root.<String>get("startTime")).thenReturn(startTimePath);
		when(root.<String>get("endTime")).thenReturn(endTimePath);
		when(root.<String>get("date")).thenReturn(datePath);
		when(root.<Long>get("client")).thenReturn(clientIdPath);
		when(root.<Long>get("taskedEmployee")).thenReturn(taskedEmployeeIdPath);
		when(root.<Long>get("appointmentType")).thenReturn(appointmentTypeIdPath);
		when(root.<Long>get("creator")).thenReturn(creatorIdPath);
		when(root.<Long>get("firm")).thenReturn(firmIdPath);

		when(criteriaBuilder.like(notePath, "%" + note + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(startTimePath, "%" + startTime + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(endTimePath, "%" + endTime + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(datePath, "%" + date + "%")).thenReturn(predicate);

		when(firmIdPath.<Long>get("id")).thenReturn(firmIdPath);
		when(criteriaBuilder.equal(firmIdPath, firmId)).thenReturn(predicate);

		when(clientIdPath.<Long>get("id")).thenReturn(clientIdPath);
		when(criteriaBuilder.equal(clientIdPath, clientId)).thenReturn(predicate);

		when(taskedEmployeeIdPath.<Long>get("id")).thenReturn(taskedEmployeeIdPath);
		when(criteriaBuilder.equal(taskedEmployeeIdPath, taskedEmployeeId)).thenReturn(predicate);

		when(appointmentTypeIdPath.<Long>get("id")).thenReturn(appointmentTypeIdPath);
		when(criteriaBuilder.equal(appointmentTypeIdPath, appointmentTypeId)).thenReturn(predicate);

		when(creatorIdPath.<Long>get("id")).thenReturn(creatorIdPath);
		when(criteriaBuilder.equal(creatorIdPath, creatorId)).thenReturn(predicate);

		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<Appointment> spec = filterBuilder.toSpecification();
		assertNotNull(spec);

		Predicate result = spec.toPredicate(root, query, criteriaBuilder);
		assertNotNull(result);

		verify(criteriaBuilder).like(notePath, "%" + note + "%");
		verify(criteriaBuilder).like(startTimePath, "%" + startTime + "%");
		verify(criteriaBuilder).like(endTimePath, "%" + endTime + "%");
		verify(criteriaBuilder).like(datePath, "%" + date + "%");
		verify(criteriaBuilder).equal(clientIdPath, clientId);
		verify(criteriaBuilder).equal(taskedEmployeeIdPath, taskedEmployeeId);
		verify(criteriaBuilder).equal(appointmentTypeIdPath, appointmentTypeId);
		verify(criteriaBuilder).equal(creatorIdPath, creatorId);
		verify(criteriaBuilder).equal(firmIdPath, firmId);
		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}
