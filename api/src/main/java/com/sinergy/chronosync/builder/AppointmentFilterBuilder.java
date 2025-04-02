package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Appointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
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
	private Long clientId;
	private Long taskedEmployeeId;
	private Long appointmentTypeId;
	private Long createdById;
	private Long modifiedById;
	private Long firmId;

	/**
	 * Builds a list of predicates based on the provided filter criteria for querying {@link Appointment} entities.
	 * The predicates are constructed using the {@link CriteriaBuilder} and applied to the {@link Root} entity.
	 *
	 * @param criteriaBuilder {@link CriteriaBuilder} used for constructing predicates.
	 * @param root {@link Root} representing the {@link Appointment} entity in the query.
	 * @return {@link List} of {@link Predicate} objects representing the filtering conditions.
	 */
	public List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Appointment> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (startDateTime != null) {
			predicates.add(criteriaBuilder.equal(root.get("startDateTime"), startDateTime));
		}
		if (endDateTime != null) {
			predicates.add(criteriaBuilder.equal(root.get("endDateTime"), endDateTime));
		}
		if (note != null && !note.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("note"), "%" + note + "%"));
		}
		if (clientId != null) {
			Path<Long> clientIdPath = root.get("client").get("id");
			predicates.add(criteriaBuilder.equal(clientIdPath, clientId));
		}
		if (appointmentTypeId != null) {
			Path<Long> appointmentTypeIdPath = root.get("appointmentType").get("id");
			predicates.add(criteriaBuilder.equal(appointmentTypeIdPath, appointmentTypeId));
		}
		if (taskedEmployeeId != null) {
			Path<Long> taskedEmployeeIdPath = root.get("taskedEmployee").get("id");
			predicates.add(criteriaBuilder.equal(taskedEmployeeIdPath, taskedEmployeeId));
		}
		if (createdById != null) {
			Path<Long> createdByIdPath = root.get("createdBy").get("id");
			predicates.add(criteriaBuilder.equal(createdByIdPath, createdById));
		}
		if (firmId != null) {
			Path<Long> firmIdPath = root.get("firm").get("id");
			predicates.add(criteriaBuilder.equal(firmIdPath, firmId));
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
