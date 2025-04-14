package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Appointment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for managing {@link Appointment} entities.
 */
public interface AppointmentRepository extends BaseRepository<Appointment>, JpaSpecificationExecutor<Appointment> {
}
