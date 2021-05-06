package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Accesorio;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Accesorio}.
 */
public interface AccesorioService {
    /**
     * Save a accesorio.
     *
     * @param accesorio the entity to save.
     * @return the persisted entity.
     */
    Accesorio save(Accesorio accesorio);

    /**
     * Partially updates a accesorio.
     *
     * @param accesorio the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Accesorio> partialUpdate(Accesorio accesorio);

    /**
     * Get all the accesorios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Accesorio> findAll(Pageable pageable);

    /**
     * Get the "id" accesorio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Accesorio> findOne(Long id);

    /**
     * Delete the "id" accesorio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
