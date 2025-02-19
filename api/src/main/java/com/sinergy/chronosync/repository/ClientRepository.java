package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Client;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Client} entities.
 * This interface extends {@link BaseRepository} to provide CRUD operations for {@link Client} entities.
 */
@Repository
public interface ClientRepository extends BaseRepository<Client, Long>, JpaSpecificationExecutor<Client> {
}
