package com.sinergy.chronosync.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Base entity abstract class.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
}
