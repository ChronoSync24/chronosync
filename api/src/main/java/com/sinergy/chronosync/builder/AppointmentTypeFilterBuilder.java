package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter builder for creating specifications to query {@link AppointmentType} entities.
 *
 * <p>This class extends the {@link BaseFilterBuilder} and implements the
 * filter criteria for user attributes</p>
 *
 * <p>The builder uses the {@link Specification} interface to dynamically
 * create predicates based on the provided filter values. If a filter value is
 * not provided (i.e., null or empty), it will be ignored in the specification.</p>
 */
@Builder
public class AppointmentTypeFilterBuilder extends BaseFilterBuilder<AppointmentType> {

	private String name;
	private Long firmId;

	/**
	 * Builds a list of predicates based on the provided filter criteria for querying {@link AppointmentType} entities.
	 * The predicates are constructed using the {@link CriteriaBuilder} and applied to the {@link Root} entity.
	 *
	 * @param criteriaBuilder {@link CriteriaBuilder} used for constructing predicates
	 * @param root            {@link Root} representing the {@link AppointmentType} entity in the query
	 * @return {@link List} of {@link Predicate} objects representing the filtering conditions
	 */
	public List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<AppointmentType> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (firmId != null) {
			predicates.add(criteriaBuilder.equal(root.get("firm").get("id"), firmId));
		}
		if (name != null && !name.isEmpty()) {
			predicates.add(
				criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%" + name.toLowerCase() + "%")
			);

		}
		return predicates;
	}

	/**
	 * Converts the filter criteria defined in this builder into a
	 * {@link Specification} for querying {@link AppointmentType} entities.
	 *
	 * <p>The method constructs a conjunction of predicates based on the
	 * filter values set in this builder.</p>
	 *
	 * @return a {@link Specification} that can be used to filter {@link AppointmentType} entities
	 */
	@Override
	public Specification<AppointmentType> toSpecification() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.and(
			buildPredicates(criteriaBuilder, root).toArray(new Predicate[0])
		);
	}
}