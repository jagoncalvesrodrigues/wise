package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Accesorio;
import com.mycompany.myapp.repository.AccesorioRepository;
import com.mycompany.myapp.service.AccesorioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Accesorio}.
 */
@Service
@Transactional
public class AccesorioServiceImpl implements AccesorioService {

    private final Logger log = LoggerFactory.getLogger(AccesorioServiceImpl.class);

    private final AccesorioRepository accesorioRepository;

    public AccesorioServiceImpl(AccesorioRepository accesorioRepository) {
        this.accesorioRepository = accesorioRepository;
    }

    @Override
    public Accesorio save(Accesorio accesorio) {
        log.debug("Request to save Accesorio : {}", accesorio);
        return accesorioRepository.save(accesorio);
    }

    @Override
    public Optional<Accesorio> partialUpdate(Accesorio accesorio) {
        log.debug("Request to partially update Accesorio : {}", accesorio);

        return accesorioRepository
            .findById(accesorio.getId())
            .map(
                existingAccesorio -> {
                    if (accesorio.getStock() != null) {
                        existingAccesorio.setStock(accesorio.getStock());
                    }
                    if (accesorio.getImagen() != null) {
                        existingAccesorio.setImagen(accesorio.getImagen());
                    }
                    if (accesorio.getTalla() != null) {
                        existingAccesorio.setTalla(accesorio.getTalla());
                    }
                    if (accesorio.getColor() != null) {
                        existingAccesorio.setColor(accesorio.getColor());
                    }
                    if (accesorio.getColeccion() != null) {
                        existingAccesorio.setColeccion(accesorio.getColeccion());
                    }

                    return existingAccesorio;
                }
            )
            .map(accesorioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Accesorio> findAll(Pageable pageable) {
        log.debug("Request to get all Accesorios");
        return accesorioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Accesorio> findOne(Long id) {
        log.debug("Request to get Accesorio : {}", id);
        return accesorioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Accesorio : {}", id);
        accesorioRepository.deleteById(id);
    }
}
