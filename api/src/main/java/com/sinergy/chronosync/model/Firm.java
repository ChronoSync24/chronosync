package com.sinergy.chronosync.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Firm model class.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "firms")
public class Firm extends BaseEntity {
	private String name;

	@ManyToMany(mappedBy = "firms")
	private Set<Client> clients = new HashSet<>();
}