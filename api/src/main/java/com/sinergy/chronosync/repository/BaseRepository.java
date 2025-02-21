package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.exception.RepositoryException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;

/**
 * Base repository interface providing common CRUD operations for all entities.
 *
 * @param <T>  The entity type that this repository manages.
 * @param <ID> The type of the entity's primary key.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

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
     * @param id The ID of the entity to be updated.
     * @return The updated entity after being persisted.
     * @throws RepositoryException if the entity with the given ID does not exist.
     */
    default T update(T entity, ID id) {
        if (!existsById(id)) {
            throw new RepositoryException("Entity with ID " + id + " does not exist.");
        }
        return save(entity);
    }
}
