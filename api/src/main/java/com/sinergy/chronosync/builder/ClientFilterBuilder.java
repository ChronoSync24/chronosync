package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter builder for creating specifications to query {@link Client} entities.
 *
 * <p>This class extends the {@link BaseFilterBuilder} and implements the
 * filter criteria for user attributes</p>
 *
 * <p>The builder uses the {@link Specification} interface to dynamically
 * create predicates based on the provided filter values. If a filter value is
 * not provided (i.e., null or empty), it will be ignored in the specification.</p>
 */
@Builder
public class ClientFilterBuilder extends BaseFilterBuilder<Client> {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Long firmId;

	public List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Client> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (firmId != null) {
			predicates.add(criteriaBuilder.equal(root.get("firm").get("id"), firmId));
		}
		if (firstName != null && !firstName.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
		}
		if (lastName != null && !lastName.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
		}
		if (email != null && !email.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
		}
		if (phone != null && !phone.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
		}
		return predicates;
	}

	/**
	 * Converts the filter criteria defined in this builder into a
	 * {@link Specification} for querying {@link Client} entities.
	 *
	 * <p>The method constructs a conjunction of predicates based on the
	 * filter values set in this builder.</p>
	 *
	 * @return a {@link Specification} that can be used to filter {@link Client} entities
	 */
	@Override
	public Specification<Client> toSpecification() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.and(
			buildPredicates(criteriaBuilder, root).toArray(new Predicate[0])
		);
	}
}
