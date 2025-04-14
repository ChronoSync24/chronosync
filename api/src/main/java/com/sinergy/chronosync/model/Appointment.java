package com.sinergy.chronosync.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Appointment model class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	private String note;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Boolean paidAppointment;
	private Boolean attendedAppointment;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private User employee;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "appointment_type_id")
	private AppointmentType appointmentType;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;
}
