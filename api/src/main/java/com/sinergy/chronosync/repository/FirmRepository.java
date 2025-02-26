package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Firm repository class for managing firms.
 */
@Repository
public interface FirmRepository extends JpaRepository<Firm, Long>, JpaSpecificationExecutor<Firm> {
}