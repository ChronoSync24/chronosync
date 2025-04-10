package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Appointment;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter builder for creating specifications to query {@link Appointment} entities.
 *
 * <p>This class extends the {@link BaseFilterBuilder} and implements the
 * filter criteria for user attributes</p>
 *
 * <p>The builder uses the {@link Specification} interface to dynamically
 * create predicates based on the provided filter values. If a filter value is
 * not provided (i.e., null or empty), it will be ignored in the specification.</p>
 */
@Builder
public class AppointmentFilterBuilder extends BaseFilterBuilder<Appointment> {

	private String note;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Client client;
	private User employee;
	private AppointmentType appointmentType;
	private User createdBy;
	private User modifiedBy;
	private Firm firm;

	/**
	 * Builds a list of predicates based on the provided filter criteria for querying {@link Appointment} entities.
	 * The predicates are constructed using the {@link CriteriaBuilder} and applied to the {@link Root} entity.
	 *
	 * @param criteriaBuilder {@link CriteriaBuilder} used for constructing predicates
	 * @param root            {@link Root} representing the {@link Appointment} entity in the query
	 * @return {@link List} of {@link Predicate} objects representing the filtering conditions
	 */
	public List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Appointment> root) {
		List<Predicate> predicates = new ArrayList<>();

		addEqualPredicate(predicates, root, criteriaBuilder, "startDateTime", startDateTime);
		addEqualPredicate(predicates, root, criteriaBuilder, "endDateTime", endDateTime);
		addLikePredicate(predicates, root, criteriaBuilder, "note", note);

		if (client != null) {
			predicates.add(criteriaBuilder.equal(root.get("client"), client));
		}
		if (appointmentType != null) {
			predicates.add(criteriaBuilder.equal(root.get("appointmentType"), appointmentType));
		}
		if (employee != null) {
			predicates.add(criteriaBuilder.equal(root.get("taskedEmployee"), employee));
		}
		if (createdBy != null) {
			predicates.add(criteriaBuilder.equal(root.get("createdBy"), createdBy));
		}
		if (firm != null) {
			predicates.add(criteriaBuilder.equal(root.get("firm"), firm));
		}

		return predicates;
	}


	/**
	 * Converts the filter criteria defined in this builder into a
	 * {@link Specification} for querying {@link Appointment} entities.
	 *
	 * <p>The method constructs a conjunction of predicates based on the
	 * filter values set in this builder.</p>
	 *
	 * @return a {@link Specification} that can be used to filter {@link Appointment} entities
	 */
	@Override
	public Specification<Appointment> toSpecification() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.and(
			buildPredicates(criteriaBuilder, root).toArray(new Predicate[0])
		);
	}
}
