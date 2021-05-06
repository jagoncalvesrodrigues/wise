package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Camiseta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Camiseta}.
 */
public interface CamisetaService {
    /**
     * Save a camiseta.
     *
     * @param camiseta the entity to save.
     * @return the persisted entity.
     */
    Camiseta save(Camiseta camiseta);

    /**
     * Partially updates a camiseta.
     *
     * @param camiseta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Camiseta> partialUpdate(Camiseta camiseta);

    /**
     * Get all the camisetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Camiseta> findAll(Pageable pageable);

    /**
     * Get the "id" camiseta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Camiseta> findOne(Long id);

    /**
     * Delete the "id" camiseta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
