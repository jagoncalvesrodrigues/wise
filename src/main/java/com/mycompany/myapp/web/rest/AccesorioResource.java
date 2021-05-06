package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Accesorio;
import com.mycompany.myapp.repository.AccesorioRepository;
import com.mycompany.myapp.service.AccesorioService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Accesorio}.
 */
@RestController
@RequestMapping("/api")
public class AccesorioResource {

    private final Logger log = LoggerFactory.getLogger(AccesorioResource.class);

    private static final String ENTITY_NAME = "accesorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccesorioService accesorioService;

    private final AccesorioRepository accesorioRepository;

    public AccesorioResource(AccesorioService accesorioService, AccesorioRepository accesorioRepository) {
        this.accesorioService = accesorioService;
        this.accesorioRepository = accesorioRepository;
    }

    /**
     * {@code POST  /accesorios} : Create a new accesorio.
     *
     * @param accesorio the accesorio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accesorio, or with status {@code 400 (Bad Request)} if the accesorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accesorios")
    public ResponseEntity<Accesorio> createAccesorio(@RequestBody Accesorio accesorio) throws URISyntaxException {
        log.debug("REST request to save Accesorio : {}", accesorio);
        if (accesorio.getId() != null) {
            throw new BadRequestAlertException("A new accesorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Accesorio result = accesorioService.save(accesorio);
        return ResponseEntity
            .created(new URI("/api/accesorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accesorios/:id} : Updates an existing accesorio.
     *
     * @param id the id of the accesorio to save.
     * @param accesorio the accesorio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accesorio,
     * or with status {@code 400 (Bad Request)} if the accesorio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accesorio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accesorios/{id}")
    public ResponseEntity<Accesorio> updateAccesorio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Accesorio accesorio
    ) throws URISyntaxException {
        log.debug("REST request to update Accesorio : {}, {}", id, accesorio);
        if (accesorio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accesorio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accesorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Accesorio result = accesorioService.save(accesorio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accesorio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /accesorios/:id} : Partial updates given fields of an existing accesorio, field will ignore if it is null
     *
     * @param id the id of the accesorio to save.
     * @param accesorio the accesorio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accesorio,
     * or with status {@code 400 (Bad Request)} if the accesorio is not valid,
     * or with status {@code 404 (Not Found)} if the accesorio is not found,
     * or with status {@code 500 (Internal Server Error)} if the accesorio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/accesorios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Accesorio> partialUpdateAccesorio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Accesorio accesorio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Accesorio partially : {}, {}", id, accesorio);
        if (accesorio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accesorio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accesorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Accesorio> result = accesorioService.partialUpdate(accesorio);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accesorio.getId().toString())
        );
    }

    /**
     * {@code GET  /accesorios} : get all the accesorios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accesorios in body.
     */
    @GetMapping("/accesorios")
    public ResponseEntity<List<Accesorio>> getAllAccesorios(Pageable pageable) {
        log.debug("REST request to get a page of Accesorios");
        Page<Accesorio> page = accesorioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accesorios/:id} : get the "id" accesorio.
     *
     * @param id the id of the accesorio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accesorio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accesorios/{id}")
    public ResponseEntity<Accesorio> getAccesorio(@PathVariable Long id) {
        log.debug("REST request to get Accesorio : {}", id);
        Optional<Accesorio> accesorio = accesorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accesorio);
    }

    /**
     * {@code DELETE  /accesorios/:id} : delete the "id" accesorio.
     *
     * @param id the id of the accesorio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accesorios/{id}")
    public ResponseEntity<Void> deleteAccesorio(@PathVariable Long id) {
        log.debug("REST request to delete Accesorio : {}", id);
        accesorioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
