package com.sinergy.chronosync.builder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Abstract base class for building filter criteria for entities in a repository.
 *
 * <p>Class implements the {@link FilterBuilder} interface and provides
 * pagination capabilities for derived filter builders. It allows the specification
 * of a pageable object that defines the page number and size of the results to be
 * retrieved from the database.</p>
 *
 * <p>By default, the pageable is set to page 0 with 10 items per page. Subclasses
 * should implement the filtering logic specific to the entity type they are handling.</p>
 *
 * @param <T> the type of the entity that this filter builder will handle
 */
public abstract class BaseFilterBuilder<T> implements FilterBuilder<T> {

	// Default page size: 10
	private Pageable pageable = PageRequest.of(0, 10);

	/**
	 * Returns the current pageable object which defines the pagination information.
	 *
	 * @return the {@link Pageable} object containing the page number and size
	 */
	@Override
	public Pageable getPageable() {
		return pageable;
	}

	/**
	 * Sets the pageable object for pagination.
	 *
	 * <p>This method allows users to customize the pagination settings by providing
	 * a {@link Pageable} object. The default pagination settings can be overridden
	 * using this method.</p>
	 *
	 * @param pageable the {@link Pageable} object to set
	 * @return the current instance of {@link FilterBuilder} for method chaining
	 */
	@Override
	public FilterBuilder<T> setPageable(Pageable pageable) {
		this.pageable = pageable;
		return this;
	}

	/**
	 * Adds a predicate for equality based on the given attribute name and value.
	 * <p>
	 * Checks if the provided value is not null. If it is not null,
	 * it creates a predicate using the equality operator for the specified
	 * attribute name in the entity's root. The created predicate is then
	 * added to the provided list of predicates.
	 * </p>
	 *
	 * @param predicates      {@link List<Predicate>} the list of predicates to which the new predicate will be added
	 * @param root            {@link Root<T>} the root of the query, representing the entity type
	 * @param criteriaBuilder {@link CriteriaBuilder} the criteria builder used to construct the predicate
	 * @param attributeName   {@link String} the name of the attribute in the entity to filter by
	 * @param value           {@link Object} the value to compare against the specified attribute
	 */
	void addEqualPredicate(
		List<Predicate> predicates,
		Root<T> root,
		CriteriaBuilder criteriaBuilder,
		String attributeName,
		Object value
	) {
		if (value != null) {
			predicates.add(criteriaBuilder.equal(root.get(attributeName), value));
		}
	}

	/**
	 * Adds a predicate for string matching using the LIKE operator based on the given attribute name and value.
	 * <p>
	 * Checks if the provided string value is not null and not empty.
	 * If the value meets these conditions, it creates a predicate using the LIKE
	 * operator for the specified attribute name in the entity's root. The created
	 * predicate is then added to the provided list of predicates. The matching is
	 * done with wildcard characters before and after the value, allowing for
	 * substring matches.
	 * </p>
	 *
	 * @param predicates      {@link List<Predicate>} the list of predicates to which the new predicate will be added
	 * @param root            {@link Root<T>} the root of the query, representing the entity type
	 * @param criteriaBuilder {@link CriteriaBuilder} the criteria builder used to construct the predicate
	 * @param attributeName   {@link String} the name of the attribute in the entity to filter by
	 * @param value           {@link String} the string value to match against the specified attribute
	 */
	void addLikePredicate(
		List<Predicate> predicates,
		Root<T> root,
		CriteriaBuilder criteriaBuilder,
		String attributeName,
		String value
	) {
		if (value != null && !value.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get(attributeName), "%" + value + "%"));
		}
	}
}
