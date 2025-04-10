package com.sinergy.chronosync.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sinergy.chronosync.model.user.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Client model class representing a person linked to a firm.
 * Enforces uniqueness based on full identity within a firm.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(
	name = "clients",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_client_identifiers_per_firm",
		columnNames = {"first_name", "last_name", "email", "phone", "firm_id"}
	)
)
public class Client extends Person {

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "firm_id", nullable = false, foreignKey = @ForeignKey(name = "fk_client_firm"))
	private Firm firm;
}
