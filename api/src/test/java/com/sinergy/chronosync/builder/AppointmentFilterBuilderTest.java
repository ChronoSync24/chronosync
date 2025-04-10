package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.*;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
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
	private Path<Client> clientPath;
	@Mock
	private Path<User> employeePath;
	@Mock
	private Path<AppointmentType> appointmentTypePath;
	@Mock
	private Path<Firm> firmPath;

	private AutoCloseable mocks;

	private Client client;
	private User employee;
	private AppointmentType appointmentType;
	private Firm firm;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);

		client = new Client();
		client.setId(2L);

		employee = new User();
		employee.setId(3L);

		appointmentType = new AppointmentType();
		appointmentType.setId(4L);

		firm = new Firm();
		firm.setId(1L);
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

		AppointmentFilterBuilder filterBuilder = AppointmentFilterBuilder.builder()
			.note(note)
			.startDateTime(startDateTime)
			.endDateTime(endDateTime)
			.client(client)
			.employee(employee)
			.appointmentType(appointmentType)
			.firm(firm)
			.build();

		when(root.<String>get("note")).thenReturn(notePath);
		when(root.<LocalDateTime>get("startDateTime")).thenReturn(startTimePath);
		when(root.<LocalDateTime>get("endDateTime")).thenReturn(endTimePath);
		when(root.<Client>get("client")).thenReturn(clientPath);
		when(root.<User>get("taskedEmployee")).thenReturn(employeePath);
		when(root.<AppointmentType>get("appointmentType")).thenReturn(appointmentTypePath);
		when(root.<Firm>get("firm")).thenReturn(firmPath);

		when(criteriaBuilder.equal(startTimePath, startDateTime)).thenReturn(predicate);
		when(criteriaBuilder.equal(endTimePath, endDateTime)).thenReturn(predicate);
		when(criteriaBuilder.equal(clientPath, client)).thenReturn(predicate);
		when(criteriaBuilder.equal(employeePath, employee)).thenReturn(predicate);
		when(criteriaBuilder.equal(appointmentTypePath, appointmentType)).thenReturn(predicate);
		when(criteriaBuilder.equal(firmPath, firm)).thenReturn(predicate);
		when(criteriaBuilder.like(notePath, "%" + note + "%")).thenReturn(predicate);
		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<Appointment> spec = filterBuilder.toSpecification();
		assertNotNull(spec);
		spec.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(startTimePath, startDateTime);
		verify(criteriaBuilder).equal(endTimePath, endDateTime);
		verify(criteriaBuilder).equal(clientPath, client);
		verify(criteriaBuilder).equal(employeePath, employee);
		verify(criteriaBuilder).equal(appointmentTypePath, appointmentType);
		verify(criteriaBuilder).equal(firmPath, firm);
		verify(criteriaBuilder).like(notePath, "%" + note + "%");
		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}