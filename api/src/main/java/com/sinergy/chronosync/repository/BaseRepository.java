package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.exception.RepositoryException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;

/**
 * Base repository interface providing common CRUD operations for all entities.
 *
 * <p>This repository extends {@link JpaRepository} and includes additional
 * methods for creating and updating entities. It is a generic repository
 * that can be used for any entity class.</p>
 *
 * <p>Marked with {@link NoRepositoryBean} to prevent Spring from instantiating
 * it directly as a bean.</p>
 *
 * @param <T>  The entity type that this repository manages.
 * @param <ID> The type of the entity's primary key.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    default T create(T entity) {
        return save(entity);
    }

    default T update(T entity, ID id) {
        if (!existsById(id)) {
            throw new RepositoryException("Entity with ID " + id + " does not exist.");
        }
        return save(entity);
    }
}
