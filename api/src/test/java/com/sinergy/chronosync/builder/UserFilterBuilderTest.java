package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserFilterBuilder}.
 *
 * <p>Verifies the behavior of the {@link UserFilterBuilder} class,
 * specifically its ability to convert various filter criteria into a {@link Specification}
 * for querying {@link User} entities based on ID, first name, last name, and other attributes.</p>
 *
 * <p>Mock objects are used to simulate the database environment, including
 * {@link Root}, {@link CriteriaBuilder}, and {@link Predicate} instances.</p>
 */
public class UserFilterBuilderTest {

	@Mock
	private Root<User> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder criteriaBuilder;

	@Mock
	private Predicate predicate;

	@Mock
	private Path<Long> idPath;

	@Mock
	private Path<String> firstNamePath;

	@Mock
	private Path<String> lastNamePath;

	@Mock
	private Path<String> addressPath;

	@Mock
	private Path<String> phonePath;

	@Mock
	private Path<String> emailPath;

	@Mock
	private Path<String> usernamePath;

	@Mock
	private Path<String> rolePath;

	@InjectMocks
	private UserFilterBuilder.UserFilterBuilderBuilder userFilterBuilderBuilder;

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
	 * Verifies that the {@link UserFilterBuilder#toSpecification()} method correctly builds
	 * a {@link Specification} based on provided filter values such as ID, first name,
	 * last name, and role.
	 *
	 * <p>Checks if the appropriate predicates are added to the
	 * {@link CriteriaBuilder} based on the filter criteria in {@link UserFilterBuilder}.
	 * Mock paths and criteria builder methods are used to simulate each attribute's behavior.</p>
	 */
	@Test
	void toSpecificationTest() {
		Long id = 1L;
		String firstName = "John";
		String lastName = "Doe";
		String address = "123 Main St";
		String phone = "555-1234";
		String email = "john.doe@example.com";
		String username = "test";
		UserRole role = UserRole.ADMINISTRATOR;

		UserFilterBuilder userFilterBuilder = userFilterBuilderBuilder
			.id(id)
			.firstName(firstName)
			.lastName(lastName)
			.address(address)
			.phone(phone)
			.email(email)
			.username(username)
			.role(role)
			.build();

		when(root.<Long>get("id")).thenReturn(idPath);
		when(root.<String>get("first_name")).thenReturn(firstNamePath);
		when(root.<String>get("last_name")).thenReturn(lastNamePath);
		when(root.<String>get("address")).thenReturn(addressPath);
		when(root.<String>get("phone")).thenReturn(phonePath);
		when(root.<String>get("email")).thenReturn(emailPath);
		when(root.<String>get("username")).thenReturn(usernamePath);
		when(root.<String>get("role")).thenReturn(rolePath);

		when(criteriaBuilder.equal(idPath, id)).thenReturn(predicate);
		when(criteriaBuilder.like(firstNamePath, "%" + firstName + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(lastNamePath, "%" + lastName + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(addressPath, "%" + address + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(phonePath, "%" + phone + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(emailPath, "%" + email + "%")).thenReturn(predicate);
		when(criteriaBuilder.like(usernamePath, "%" + username + "%")).thenReturn(predicate);
		when(criteriaBuilder.equal(rolePath, role.name())).thenReturn(predicate);

		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<User> specification = userFilterBuilder.toSpecification();
		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(idPath, id);
		verify(criteriaBuilder).like(firstNamePath, "%" + firstName + "%");
		verify(criteriaBuilder).like(lastNamePath, "%" + lastName + "%");
		verify(criteriaBuilder).like(addressPath, "%" + address + "%");
		verify(criteriaBuilder).like(phonePath, "%" + phone + "%");
		verify(criteriaBuilder).like(emailPath, "%" + email + "%");
		verify(criteriaBuilder).like(usernamePath, "%" + username + "%");
		verify(criteriaBuilder).equal(rolePath, role.name());
		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}
