package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Appointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

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
	private String startTime;
	private String endTime;
	private String date;
	private Long clientId;
	private Long taskedEmployeeId;
	private Long appointmentTypeId;
	private Long creatorId;
	private Long firmId;

	public List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Appointment> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (startTime != null && !startTime.isEmpty()) {
			predicates.add(criteriaBuilder.equal(root.get("startTime"), "%" + startTime + "%"));
		}
		if (endTime != null && !endTime.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("endTime"), "%" + endTime + "%"));
		}
		if (note != null && !note.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("note"), "%" + note + "%"));
		}
		if (date != null && !date.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("date"), "%" + date + "%"));
		}
		if (clientId != null) {
			predicates.add(criteriaBuilder.equal(root.get("client").get("id"), clientId));
		}
		if (appointmentTypeId != null) {
			predicates.add(criteriaBuilder.equal(root.get("appointmentType").get("id"), appointmentTypeId));
		}
		if (taskedEmployeeId != null) {
			predicates.add(criteriaBuilder.equal(root.get("taskedEmployee").get("id"), taskedEmployeeId));
		}
		if (creatorId != null) {
			predicates.add(criteriaBuilder.equal(root.get("creator").get("id"), creatorId));
		}
		if (firmId != null) {
			predicates.add(criteriaBuilder.equal(root.get("firm").get("id"), firmId));
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
