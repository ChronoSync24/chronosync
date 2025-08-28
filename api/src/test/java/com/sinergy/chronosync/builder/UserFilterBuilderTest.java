package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

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
 * {@link Root}, {@link CriteriaBuilder}, {@link Predicate}, and {@link Path} instances.</p>
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
	private Path<String> usernamePath;

	@Mock
	private Path<String> rolePath;

	@Mock
	private Path<Object> firmPath;

	@Mock
	private Path<Long> firmIdPath;

	@Mock
	private Path<String> uniqueIdentifierPath;

	@Mock
	private Expression<String> lowerFirstNamePath;

	@Mock
	private Expression<String> lowerLastNamePath;

	@Mock
	private Expression<String> lowerUsernamePath;

	@Mock
	private Expression<String> lowerUniqueIdentifierPath;

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
	 * last name, username, role, firm ID, and unique identifier.
	 */
	@Test
	void toSpecificationTest() {
		Long id = 1L;
		String firstName = "John";
		String lastName = "Doe";
		String username = "test";
		UserRole role = UserRole.ADMINISTRATOR;
		Long firmId = 2L;
		String uniqueIdentifier = "unique123";

		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.id(id)
			.firstName(firstName)
			.lastName(lastName)
			.username(username)
			.roles(List.of(role))
			.firmId(firmId)
			.uniqueIdentifier(uniqueIdentifier)
			.build();

		when(root.<Long>get("id")).thenReturn(idPath);
		when(criteriaBuilder.equal(idPath, id)).thenReturn(predicate);

		when(root.<String>get("firstName")).thenReturn(firstNamePath);
		when(criteriaBuilder.lower(firstNamePath)).thenReturn(lowerFirstNamePath);
		when(criteriaBuilder.like(lowerFirstNamePath, "%" + firstName.toLowerCase() + "%")).thenReturn(predicate);

		when(root.<String>get("lastName")).thenReturn(lastNamePath);
		when(criteriaBuilder.lower(lastNamePath)).thenReturn(lowerLastNamePath);
		when(criteriaBuilder.like(lowerLastNamePath, "%" + lastName.toLowerCase() + "%")).thenReturn(predicate);

		when(root.<String>get("username")).thenReturn(usernamePath);
		when(criteriaBuilder.lower(usernamePath)).thenReturn(lowerUsernamePath);
		when(criteriaBuilder.like(lowerUsernamePath, "%" + username.toLowerCase() + "%")).thenReturn(predicate);

		when(root.<String>get("role")).thenReturn(rolePath);
		when(rolePath.in(List.of(role))).thenReturn(predicate);

		when(root.get("firm")).thenReturn(firmPath);
		when(firmPath.<Long>get("id")).thenReturn(firmIdPath);
		when(criteriaBuilder.equal(firmIdPath, firmId)).thenReturn(predicate);

		when(root.<String>get("uniqueIdentifier")).thenReturn(uniqueIdentifierPath);
		when(criteriaBuilder.lower(uniqueIdentifierPath)).thenReturn(lowerUniqueIdentifierPath);
		when(criteriaBuilder.like(lowerUniqueIdentifierPath, "%" + uniqueIdentifier.toLowerCase() + "%")).thenReturn(predicate);

		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<User> specification = filterBuilder.toSpecification();
		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(idPath, id);

		verify(criteriaBuilder).lower(firstNamePath);
		verify(criteriaBuilder).like(lowerFirstNamePath, "%john%");

		verify(criteriaBuilder).lower(lastNamePath);
		verify(criteriaBuilder).like(lowerLastNamePath, "%doe%");

		verify(criteriaBuilder).lower(usernamePath);
		verify(criteriaBuilder).like(lowerUsernamePath, "%test%");

		verify(rolePath).in(List.of(role));

		verify(criteriaBuilder).equal(firmIdPath, firmId);

		verify(criteriaBuilder).lower(uniqueIdentifierPath);
		verify(criteriaBuilder).like(lowerUniqueIdentifierPath, "%unique123%");

		verify(criteriaBuilder).and(any(Predicate[].class));
	}

	/**
	 * Verifies that the {@link UserFilterBuilder#toSpecification()} method correctly handles
	 * exact username matching when the exactUsernameMatch flag is set to true.
	 */
	@Test
	void toSpecificationWithExactUsernameMatchTest() {
		String username = "exactUser";

		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(username)
			.exactUsernameMatch(true)
			.build();

		when(root.<String>get("username")).thenReturn(usernamePath);
		when(criteriaBuilder.lower(usernamePath)).thenReturn(lowerUsernamePath);
		when(criteriaBuilder.equal(lowerUsernamePath, username.toLowerCase())).thenReturn(predicate);

		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<User> specification = filterBuilder.toSpecification();
		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).lower(usernamePath);
		verify(criteriaBuilder).equal(lowerUsernamePath, "exactuser");
		verify(criteriaBuilder).and(any(Predicate[].class));
	}

	/**
	 * Verifies that the {@link UserFilterBuilder#toSpecification()} method correctly handles
	 * null or empty filter values by not creating predicates for them.
	 */
	@Test
	void toSpecificationWithNullValuesTest() {
		UserFilterBuilder filterBuilder = UserFilterBuilder.builder().build();

		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<User> specification = filterBuilder.toSpecification();
		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}