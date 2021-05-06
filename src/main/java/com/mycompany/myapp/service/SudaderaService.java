package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Sudadera;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sudadera}.
 */
public interface SudaderaService {
    /**
     * Save a sudadera.
     *
     * @param sudadera the entity to save.
     * @return the persisted entity.
     */
    Sudadera save(Sudadera sudadera);

    /**
     * Partially updates a sudadera.
     *
     * @param sudadera the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sudadera> partialUpdate(Sudadera sudadera);

    /**
     * Get all the sudaderas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sudadera> findAll(Pageable pageable);

    /**
     * Get the "id" sudadera.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sudadera> findOne(Long id);

    /**
     * Delete the "id" sudadera.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
