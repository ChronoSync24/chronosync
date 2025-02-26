package com.sinergy.chronosync.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

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
public class Client extends BaseEntity {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;

	@JsonBackReference
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "client_firm",
		joinColumns = @JoinColumn(name = "client_id"),
		inverseJoinColumns = @JoinColumn(name = "firm_id")
	)
	private Set<Firm> firms = new HashSet<>();
}
