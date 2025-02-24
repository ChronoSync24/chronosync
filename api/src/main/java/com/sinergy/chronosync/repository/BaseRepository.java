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
     * @param entity The entity to be saved.
     * @return The saved entity, including any automatically generated fields (e.g., ID).
     */
    default T create(T entity) {
        return save(entity);
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity The entity with updated data to be saved.
     * @return The updated entity after being persisted.
     */
    default T update(T entity) {
        findByIdOrThrow(entity.getId());
        return save(entity);
    }

    /**
     *
     * @param id The id of the entity to search for
     * @throws RepositoryException if the entity with the given ID does not exist
     */
    default T findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new RepositoryException("Entity with ID " + id + " not found."));
    }
}
