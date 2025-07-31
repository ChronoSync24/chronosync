package com.sinergy.chronosync.model.user;

import com.sinergy.chronosync.model.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Abstract Person class.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class Person extends BaseEntity {

	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
	private String uniqueIdentifier;
}
