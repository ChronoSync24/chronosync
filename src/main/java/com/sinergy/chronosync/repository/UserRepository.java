package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User repository class for managing users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	/**
	 * Finds a user by their username.
	 *
	 * @param username {@link String} username of the user
	 * @return {@link Optional} containing the {@link User} if found, empty otherwise
	 */
	Optional<User> findByUsername(String username);

}
