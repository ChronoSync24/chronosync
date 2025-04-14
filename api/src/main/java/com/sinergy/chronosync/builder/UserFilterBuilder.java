package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import jakarta.persistence.criteria.Predicate;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter builder for creating specifications to query {@link User} entities.
 *
 * <p>This class extends the {@link BaseFilterBuilder} and implements the
 * filter criteria for user attributes such as ID, first name, last name,
 * address, phone, email, username, and user role.</p>
 *
 * <p>The builder uses the {@link Specification} interface to dynamically
 * create predicates based on the provided filter values. If a filter value is
 * not provided (i.e., null or empty), it will be ignored in the specification.</p>
 */
@Builder
public class UserFilterBuilder extends BaseFilterBuilder<User> {

	private static final String ID = "id";
	private static final String FIRST_NAME = "first_name";
	private static final String LAST_NAME = "last_name";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String EMAIL = "email";
	private static final String USERNAME = "username";
	private static final String ROLE = "role";

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
	private String username;
	private UserRole role;

	/**
	 * Converts the filter criteria defined in this builder into a
	 * {@link Specification} for querying {@link User} entities.
	 *
	 * <p>The method constructs a conjunction of predicates based on the
	 * filter values set in this builder. Each non-null and non-empty filter
	 * value will create a corresponding predicate using the LIKE operator for
	 * string fields and the EQUAL operator for the ID and role fields.</p>
	 *
	 * @return a {@link Specification} that can be used to filter {@link User} entities
	 */
	public Specification<User> toSpecification() {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			addEqualPredicate(predicates, root, criteriaBuilder, ID, id);
			addLikePredicate(predicates, root, criteriaBuilder, FIRST_NAME, firstName);
			addLikePredicate(predicates, root, criteriaBuilder, LAST_NAME, lastName);
			addLikePredicate(predicates, root, criteriaBuilder, ADDRESS, address);
			addLikePredicate(predicates, root, criteriaBuilder, PHONE, phone);
			addLikePredicate(predicates, root, criteriaBuilder, EMAIL, email);
			addLikePredicate(predicates, root, criteriaBuilder, USERNAME, username);
			addEqualPredicate(predicates, root, criteriaBuilder, ROLE, role != null ? role.name() : null);

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
