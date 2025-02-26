package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.exception.RepositoryException;
import com.sinergy.chronosync.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface providing common CRUD operations for all entities.
 *
 * @param <T> The entity type that this repository manages, extending {@link BaseEntity}.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

	/**
	 * Saves a new entity in the database.
	 *
	 * @param entity {@link T} The entity to be saved.
	 * @return {@link T} The saved entity, including any automatically generated fields (e.g., ID).
	 */
	default T create(T entity) {
		return save(entity);
	}

	/**
	 * Updates the entity in the database.
	 *
	 * @param entity {@link T} The entity with updated data to be saved.
	 * @return {@link T} The updated entity after being persisted.
	 * @throws RepositoryException in case the entity does not exist
	 */
	default T update(T entity) throws RepositoryException {
		findById(entity.getId())
			.orElseThrow(() -> new RepositoryException("Entity with ID " + entity.getId() + " not found."));

		return save(entity);
	}
}
