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

import java.util.Date;

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
	private String startTime;
	private String endTime;
	private String date;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "taskedEmployee_id")
	private User taskedEmployee;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "appointmentType_id")
	private AppointmentType appointmentType;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "creator_id")
	private User creator;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;
}
