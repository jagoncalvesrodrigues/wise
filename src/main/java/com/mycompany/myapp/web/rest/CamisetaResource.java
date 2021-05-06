package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Camiseta;
import com.mycompany.myapp.repository.CamisetaRepository;
import com.mycompany.myapp.service.CamisetaService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Camiseta}.
 */
@RestController
@RequestMapping("/api")
public class CamisetaResource {

    private final Logger log = LoggerFactory.getLogger(CamisetaResource.class);

    private static final String ENTITY_NAME = "camiseta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CamisetaService camisetaService;

    private final CamisetaRepository camisetaRepository;

    public CamisetaResource(CamisetaService camisetaService, CamisetaRepository camisetaRepository) {
        this.camisetaService = camisetaService;
        this.camisetaRepository = camisetaRepository;
    }

    /**
     * {@code POST  /camisetas} : Create a new camiseta.
     *
     * @param camiseta the camiseta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new camiseta, or with status {@code 400 (Bad Request)} if the camiseta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/camisetas")
    public ResponseEntity<Camiseta> createCamiseta(@RequestBody Camiseta camiseta) throws URISyntaxException {
        log.debug("REST request to save Camiseta : {}", camiseta);
        if (camiseta.getId() != null) {
            throw new BadRequestAlertException("A new camiseta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Camiseta result = camisetaService.save(camiseta);
        return ResponseEntity
            .created(new URI("/api/camisetas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /camisetas/:id} : Updates an existing camiseta.
     *
     * @param id the id of the camiseta to save.
     * @param camiseta the camiseta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated camiseta,
     * or with status {@code 400 (Bad Request)} if the camiseta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the camiseta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/camisetas/{id}")
    public ResponseEntity<Camiseta> updateCamiseta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Camiseta camiseta
    ) throws URISyntaxException {
        log.debug("REST request to update Camiseta : {}, {}", id, camiseta);
        if (camiseta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, camiseta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!camisetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Camiseta result = camisetaService.save(camiseta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, camiseta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /camisetas/:id} : Partial updates given fields of an existing camiseta, field will ignore if it is null
     *
     * @param id the id of the camiseta to save.
     * @param camiseta the camiseta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated camiseta,
     * or with status {@code 400 (Bad Request)} if the camiseta is not valid,
     * or with status {@code 404 (Not Found)} if the camiseta is not found,
     * or with status {@code 500 (Internal Server Error)} if the camiseta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/camisetas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Camiseta> partialUpdateCamiseta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Camiseta camiseta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Camiseta partially : {}, {}", id, camiseta);
        if (camiseta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, camiseta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!camisetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Camiseta> result = camisetaService.partialUpdate(camiseta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, camiseta.getId().toString())
        );
    }

    /**
     * {@code GET  /camisetas} : get all the camisetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of camisetas in body.
     */
    @GetMapping("/camisetas")
    public ResponseEntity<List<Camiseta>> getAllCamisetas(Pageable pageable) {
        log.debug("REST request to get a page of Camisetas");
        Page<Camiseta> page = camisetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /camisetas/:id} : get the "id" camiseta.
     *
     * @param id the id of the camiseta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the camiseta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/camisetas/{id}")
    public ResponseEntity<Camiseta> getCamiseta(@PathVariable Long id) {
        log.debug("REST request to get Camiseta : {}", id);
        Optional<Camiseta> camiseta = camisetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(camiseta);
    }

    /**
     * {@code DELETE  /camisetas/:id} : delete the "id" camiseta.
     *
     * @param id the id of the camiseta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/camisetas/{id}")
    public ResponseEntity<Void> deleteCamiseta(@PathVariable Long id) {
        log.debug("REST request to delete Camiseta : {}", id);
        camisetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
