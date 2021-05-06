package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Camiseta;
import com.mycompany.myapp.repository.CamisetaRepository;
import com.mycompany.myapp.service.CamisetaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Camiseta}.
 */
@Service
@Transactional
public class CamisetaServiceImpl implements CamisetaService {

    private final Logger log = LoggerFactory.getLogger(CamisetaServiceImpl.class);

    private final CamisetaRepository camisetaRepository;

    public CamisetaServiceImpl(CamisetaRepository camisetaRepository) {
        this.camisetaRepository = camisetaRepository;
    }

    @Override
    public Camiseta save(Camiseta camiseta) {
        log.debug("Request to save Camiseta : {}", camiseta);
        return camisetaRepository.save(camiseta);
    }

    @Override
    public Optional<Camiseta> partialUpdate(Camiseta camiseta) {
        log.debug("Request to partially update Camiseta : {}", camiseta);

        return camisetaRepository
            .findById(camiseta.getId())
            .map(
                existingCamiseta -> {
                    if (camiseta.getStock() != null) {
                        existingCamiseta.setStock(camiseta.getStock());
                    }
                    if (camiseta.getImagen() != null) {
                        existingCamiseta.setImagen(camiseta.getImagen());
                    }
                    if (camiseta.getTalla() != null) {
                        existingCamiseta.setTalla(camiseta.getTalla());
                    }
                    if (camiseta.getColor() != null) {
                        existingCamiseta.setColor(camiseta.getColor());
                    }
                    if (camiseta.getColeccion() != null) {
                        existingCamiseta.setColeccion(camiseta.getColeccion());
                    }

                    return existingCamiseta;
                }
            )
            .map(camisetaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Camiseta> findAll(Pageable pageable) {
        log.debug("Request to get all Camisetas");
        return camisetaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Camiseta> findOne(Long id) {
        log.debug("Request to get Camiseta : {}", id);
        return camisetaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Camiseta : {}", id);
        camisetaRepository.deleteById(id);
    }
}
