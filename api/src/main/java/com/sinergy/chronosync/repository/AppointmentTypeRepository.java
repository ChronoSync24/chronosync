package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link AppointmentType} entities.
 * This interface extends {@link BaseRepository} to provide CRUD operations for {@link AppointmentType} entities.
 */
@Repository
public interface AppointmentTypeRepository extends BaseRepository<AppointmentType, Long>,
		JpaSpecificationExecutor<AppointmentType> {
}