package com.sinergy.chronosync.model.appointmentType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sinergy.chronosync.model.BaseEntity;
import com.sinergy.chronosync.model.Firm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Appointment type model class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "appointmentTypes")
public class AppointmentType extends BaseEntity {

	private String name;
	private Integer durationMinutes;
	private Double price;
	private String colorCode;
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;

}
