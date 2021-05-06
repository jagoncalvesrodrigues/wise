package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Sudadera;
import com.mycompany.myapp.repository.SudaderaRepository;
import com.mycompany.myapp.service.SudaderaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sudadera}.
 */
@Service
@Transactional
public class SudaderaServiceImpl implements SudaderaService {

    private final Logger log = LoggerFactory.getLogger(SudaderaServiceImpl.class);

    private final SudaderaRepository sudaderaRepository;

    public SudaderaServiceImpl(SudaderaRepository sudaderaRepository) {
        this.sudaderaRepository = sudaderaRepository;
    }

    @Override
    public Sudadera save(Sudadera sudadera) {
        log.debug("Request to save Sudadera : {}", sudadera);
        return sudaderaRepository.save(sudadera);
    }

    @Override
    public Optional<Sudadera> partialUpdate(Sudadera sudadera) {
        log.debug("Request to partially update Sudadera : {}", sudadera);

        return sudaderaRepository
            .findById(sudadera.getId())
            .map(
                existingSudadera -> {
                    if (sudadera.getStock() != null) {
                        existingSudadera.setStock(sudadera.getStock());
                    }
                    if (sudadera.getImagen() != null) {
                        existingSudadera.setImagen(sudadera.getImagen());
                    }
                    if (sudadera.getTalla() != null) {
                        existingSudadera.setTalla(sudadera.getTalla());
                    }
                    if (sudadera.getColor() != null) {
                        existingSudadera.setColor(sudadera.getColor());
                    }
                    if (sudadera.getColeccion() != null) {
                        existingSudadera.setColeccion(sudadera.getColeccion());
                    }

                    return existingSudadera;
                }
            )
            .map(sudaderaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sudadera> findAll(Pageable pageable) {
        log.debug("Request to get all Sudaderas");
        return sudaderaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sudadera> findOne(Long id) {
        log.debug("Request to get Sudadera : {}", id);
        return sudaderaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sudadera : {}", id);
        sudaderaRepository.deleteById(id);
    }
}
