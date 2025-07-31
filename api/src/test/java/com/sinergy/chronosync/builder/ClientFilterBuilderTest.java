package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Client;
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
 * Unit tests for {@link ClientFilterBuilder}.
 *
 * <p>Verifies the behavior of the {@link ClientFilterBuilder} class,
 * specifically its ability to convert filter criteria into a {@link Specification}
 * for querying {@link Client} entities based on attributes</p>
 *
 * <p>Mock objects are utilized to simulate the database environment, including
 * {@link Root}, {@link CriteriaBuilder}, {@link Predicate}, and {@link Path} instances.</p>
 *
 * <p>The tests ensure that the {@link ClientFilterBuilder#toSpecification()} method
 * constructs the appropriate predicates and combines them as expected using the criteria builder.</p>
 */
public class ClientFilterBuilderTest {

	@Mock
	private Root<Client> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder criteriaBuilder;

	@Mock
	private Predicate predicate;

	@Mock
	private Path<String> firstNamePath;

	@Mock
	private Path<String> lastNamePath;

	@Mock
	private Path<String> emailPath;

	@Mock
	private Path<String> phoneNumberPath;

	@Mock
	private Path<Object> firmPath;

	@Mock
	private Path<Long> firmIdPath;

	@Mock
	private Expression<String> lowerFirstNamePath;

	@Mock
	private Expression<String> lowerLastNamePath;

	@Mock
	private Expression<String> lowerEmailPath;

	@Mock
	private Expression<String> lowerPhoneNumberPath;

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
	 * Verifies that the {@link ClientFilterBuilder#toSpecification()} method correctly builds
	 * a {@link Specification} based on provided filter values.
	 */
	@Test
	void toSpecificationTest() {
		Long firmId = 1L;
		String firstName = "Test";
		String lastName = "Client";
		String email = "test@test.com";
		String phone = "+555-555-555";

		ClientFilterBuilder filterBuilder = ClientFilterBuilder.builder()
			.firmId(firmId)
			.firstName(firstName)
			.lastName(lastName)
			.email(email)
			.phone(phone)
			.build();

		when(root.get("firm")).thenReturn(firmPath);
		when(firmPath.<Long>get("id")).thenReturn(firmIdPath);
		when(criteriaBuilder.equal(firmIdPath, firmId)).thenReturn(predicate);

		when(root.<String>get("firstName")).thenReturn(firstNamePath);
		when(criteriaBuilder.lower(firstNamePath)).thenReturn(lowerFirstNamePath);
		when(criteriaBuilder.like(lowerFirstNamePath, "%" + firstName.toLowerCase() + "%")).thenReturn(predicate);

		when(root.<String>get("lastName")).thenReturn(lastNamePath);
		when(criteriaBuilder.lower(lastNamePath)).thenReturn(lowerLastNamePath);
		when(criteriaBuilder.like(lowerLastNamePath, "%" + lastName.toLowerCase() + "%")).thenReturn(predicate);

		when(root.<String>get("email")).thenReturn(emailPath);
		when(criteriaBuilder.lower(emailPath)).thenReturn(lowerEmailPath);
		when(criteriaBuilder.like(lowerEmailPath, "%" + email.toLowerCase() + "%")).thenReturn(predicate);

		when(root.<String>get("phone")).thenReturn(phoneNumberPath);
		when(criteriaBuilder.lower(phoneNumberPath)).thenReturn(lowerPhoneNumberPath);
		when(criteriaBuilder.like(lowerPhoneNumberPath, "%" + phone.toLowerCase() + "%")).thenReturn(predicate);

		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<Client> specification = filterBuilder.toSpecification();

		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(firmIdPath, firmId);

		verify(criteriaBuilder).lower(firstNamePath);
		verify(criteriaBuilder).like(lowerFirstNamePath, "%" + firstName.toLowerCase() + "%");

		verify(criteriaBuilder).lower(lastNamePath);
		verify(criteriaBuilder).like(lowerLastNamePath, "%" + lastName.toLowerCase() + "%");

		verify(criteriaBuilder).lower(emailPath);
		verify(criteriaBuilder).like(lowerEmailPath, "%" + email.toLowerCase() + "%");

		verify(criteriaBuilder).lower(phoneNumberPath);
		verify(criteriaBuilder).like(lowerPhoneNumberPath, "%" + phone.toLowerCase() + "%");

		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}
