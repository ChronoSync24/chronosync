package com.sinergy.chronosync.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	private String note;
	private String startTime;
	private String endTime;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "appointmentType_id")
	private AppointmentType appointmentType;
}
