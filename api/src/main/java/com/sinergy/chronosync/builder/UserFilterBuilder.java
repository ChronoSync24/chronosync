package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import lombok.Setter;
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
@Setter
@Builder
public class UserFilterBuilder extends BaseFilterBuilder<User> {

	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private List<UserRole> roles;
	private Long firmId;
	private String uniqueIdentifier;

	@Builder.Default
	private boolean exactUsernameMatch = false;

	/**
	 * Builds a list of predicates based on the provided filter criteria for querying {@link User} entities.
	 * The predicates are constructed using the {@link CriteriaBuilder} and applied to the {@link Root} entity.
	 *
	 * @param criteriaBuilder {@link CriteriaBuilder} used for constructing predicates.
	 * @param root            {@link Root} representing the {@link User} entity in the query.
	 * @return {@link List} of {@link Predicate} objects representing the filtering conditions.
	 */
	public List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<User> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), id));
		}
		if (firstName != null && !firstName.isEmpty()) {
			predicates.add(criteriaBuilder.like(
				criteriaBuilder.lower(root.get("firstName")),
				"%" + firstName.toLowerCase() + "%"
			));
		}
		if (lastName != null && !lastName.isEmpty()) {
			predicates.add(criteriaBuilder.like(
				criteriaBuilder.lower(root.get("lastName")),
				"%" + lastName.toLowerCase() + "%"
			));
		}
		if (username != null && !username.isEmpty()) {
			if (exactUsernameMatch) {
				predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(root.get("username")),
					username.toLowerCase()
				));
			} else {
				predicates.add(criteriaBuilder.like(
					criteriaBuilder.lower(root.get("username")),
					"%" + username.toLowerCase() + "%"
				));
			}
		}
		if (roles != null && !roles.isEmpty()) {
			predicates.add(root.get("role").in(roles));
		}
		if (firmId != null) {
			predicates.add(criteriaBuilder.equal(root.get("firm").get("id"), firmId));
		}
		if (uniqueIdentifier != null && !uniqueIdentifier.isEmpty()) {
			predicates.add(criteriaBuilder.like(
				criteriaBuilder.lower(root.get("uniqueIdentifier")),
				"%" + uniqueIdentifier.toLowerCase() + "%"
			));
		}

		return predicates;
	}

	/**
	 * Converts the filter criteria defined in this builder into a
	 * {@link Specification} for querying {@link User} entities.
	 *
	 * <p>The method constructs a conjunction of predicates based on the
	 * filter values set in this builder.</p>
	 *
	 * @return a {@link Specification} that can be used to filter {@link User} entities
	 */
	@Override
	public Specification<User> toSpecification() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.and(
			buildPredicates(criteriaBuilder, root).toArray(new Predicate[0])
		);
	}
}
