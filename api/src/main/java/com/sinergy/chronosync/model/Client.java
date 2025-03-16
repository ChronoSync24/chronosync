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
@Table(name = "clients")
public class Client extends Person {

	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "firm_id")
	private Firm firm;
}
