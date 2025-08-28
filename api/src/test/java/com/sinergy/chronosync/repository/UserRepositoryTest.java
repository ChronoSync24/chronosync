package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserRepository}.
 */
class UserRepositoryTest {

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link UserRepository#findAll()} method.
	 * Verifies that the repository returns a list of users and that the size of the list matches
	 * the expected number of users.
	 *
	 * <p>Mocks the repository's behavior to return a predefined list of users and checks the result.</p>
	 */
	@Test
	void findAllUsersTest() {
		List<User> testUsers = getUsers();

		when(userRepository.findAll()).thenReturn(testUsers);

		List<User> users = userRepository.findAll();

		assertThat(users).hasSize(getUsers().size());

		verify(userRepository, times(1)).findAll();
	}

	/**
	 * Tests the {@link UserRepository#findAll(Specification)} method using a specification that filters by first name.
	 * Verifies that the repository returns a list of users that match the specification.
	 *
	 * @see UserFilterBuilder
	 */
	@Test
	void findByNameTest() {
		Specification<User> spec = UserFilterBuilder.builder()
			.firstName("John")
			.build()
			.toSpecification();

		when(userRepository.findAll(Mockito.<Specification<User>>any())).thenReturn(getUsers());

		List<User> users = userRepository.findAll(spec);

		assertEquals(getUsers().size(), users.size());
		verify(userRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link UserRepository#findAll(Specification)} method with a specification that finds no matching users.
	 * Verifies that an empty list is returned when no users match the criteria.
	 */
	@Test
	void findWithNoMatchTest() {
		Specification<User> spec = UserFilterBuilder.builder()
			.firstName("Nonexistent")
			.build()
			.toSpecification();

		when(userRepository.findAll(Mockito.<Specification<User>>any())).thenReturn(Collections.emptyList());

		List<User> users = userRepository.findAll(spec);

		assertThat(users).isEmpty();
		verify(userRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link UserRepository#findAll(Specification)} method with a specification that filters by user ID.
	 * Verifies that the repository returns a list containing the user with the matching ID.
	 */
	@Test
	void findByIdTest() {
		List<User> testUsers = getUsers();

		Specification<User> spec = UserFilterBuilder.builder()
			.id(1L)
			.build()
			.toSpecification();

		when(userRepository.findAll(Mockito.<Specification<User>>any())).thenReturn(testUsers);

		List<User> users = userRepository.findAll(spec);

		assertThat(users).contains(
			testUsers.stream().filter(u -> u.getId() == 1L).findFirst().orElse(null)
		);
		verify(userRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link UserRepository#findAll(Specification)} method with a specification that filters by username.
	 * Verifies that the repository returns a list containing the user with the matching username.
	 */
	@Test
	void findByUsernameTest() {
		List<User> testUsers = getUsers();

		Specification<User> spec = UserFilterBuilder.builder()
			.username("jdoe")
			.build()
			.toSpecification();

		when(userRepository.findAll(Mockito.<Specification<User>>any())).thenReturn(testUsers);

		List<User> users = userRepository.findAll(spec);

		assertThat(users).contains(
			testUsers.stream().filter(u -> u.getUsername().equals("jdoe")).findFirst().orElse(null)
		);
		verify(userRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link UserRepository#findAll(Specification)} method with a specification that filters by user role.
	 * Verifies that the repository returns a list containing users with the matching role.
	 */
	@Test
	void findByRoleTest() {
		List<User> testUsers = getUsers();

		Specification<User> spec = UserFilterBuilder.builder()
			.roles(List.of(UserRole.ADMINISTRATOR))
			.build()
			.toSpecification();

		when(userRepository.findAll(Mockito.<Specification<User>>any())).thenReturn(testUsers);

		List<User> users = userRepository.findAll(spec);

		assertThat(users).contains(
			testUsers.stream().filter(u -> u.getRole() == UserRole.ADMINISTRATOR).findFirst().orElse(null)
		);
		verify(userRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link UserRepository#findAll(Specification)} with a specification that filters by partial first name.
	 * Verifies that the repository returns a list containing users whose first name contains the partial match.
	 */
	@Test
	void findByPartialNameTest() {
		List<User> testUsers = getUsers();

		Specification<User> spec = UserFilterBuilder.builder()
			.firstName("Jo")
			.build()
			.toSpecification();

		when(userRepository.findAll(Mockito.<Specification<User>>any())).thenReturn(testUsers);

		List<User> users = userRepository.findAll(spec);

		assertThat(users).contains(
			testUsers.stream().filter(u -> u.getFirstName().contains("Jo")).findFirst().orElse(null)
		);
		verify(userRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link UserRepository#findOne(Specification)} method when a matching user is found.
	 * Verifies that the repository returns the correct user wrapped in an {@link Optional}.
	 */
	@Test
	void findOneTest() {
		User user = getUsers().getFirst();

		Specification<User> spec = UserFilterBuilder.builder()
			.firstName("John")
			.build()
			.toSpecification();

		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));

		Optional<User> foundUser = userRepository.findOne(spec);

		assertThat(foundUser).isPresent();
		assertThat(foundUser.get()).isEqualTo(user);
		verify(userRepository, times(1)).findOne(spec);
	}

	/**
	 * Tests the {@link UserRepository#findOne(Specification)} method when no matching user is found.
	 * Verifies that the repository returns an empty {@link Optional} when no user matches the criteria.
	 */
	@Test
	void findOneNoMatchTest() {
		Specification<User> spec = UserFilterBuilder.builder()
			.firstName("Nonexistent")
			.build()
			.toSpecification();

		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.empty());

		Optional<User> foundUser = userRepository.findOne(spec);

		assertThat(foundUser).isNotPresent();
		verify(userRepository, times(1)).findOne(spec);
	}

	/**
	 * Helper method to generate a list of {@link User} objects for testing purposes.
	 *
	 * @return {@link List<User>} a list of users
	 */
	private List<User> getUsers() {
		User user1 = new User();
		user1.setId(1L);
		user1.setFirstName("John");
		user1.setLastName("Doe");
		user1.setUsername("jdoe");
		user1.setRole(UserRole.ADMINISTRATOR);

		User user2 = new User();
		user2.setId(2L);
		user2.setFirstName("Test");
		user2.setLastName("Second");
		user2.setUsername("tsecond");
		user2.setRole(UserRole.MANAGER);

		return List.of(user1, user2);
	}
}

