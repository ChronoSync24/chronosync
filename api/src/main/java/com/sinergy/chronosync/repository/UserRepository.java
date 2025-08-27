package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.user.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * User repository class for managing users.
 */
@Repository
public interface UserRepository extends BaseRepository<User>, JpaSpecificationExecutor<User> {
}
