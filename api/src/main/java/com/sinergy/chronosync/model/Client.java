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
 * Client model class.
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
		name = "uq_client_firm_identity",
		columnNames = {"firm_id", "first_name", "last_name", "email", "phone"}
	)
)
public class Client extends Person {

	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "firm_id", nullable = false)
	private Firm firm;
}
