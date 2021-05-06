package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Sudadera;
import com.mycompany.myapp.repository.SudaderaRepository;
import com.mycompany.myapp.service.SudaderaService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Sudadera}.
 */
@RestController
@RequestMapping("/api")
public class SudaderaResource {

    private final Logger log = LoggerFactory.getLogger(SudaderaResource.class);

    private static final String ENTITY_NAME = "sudadera";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SudaderaService sudaderaService;

    private final SudaderaRepository sudaderaRepository;

    public SudaderaResource(SudaderaService sudaderaService, SudaderaRepository sudaderaRepository) {
        this.sudaderaService = sudaderaService;
        this.sudaderaRepository = sudaderaRepository;
    }

    /**
     * {@code POST  /sudaderas} : Create a new sudadera.
     *
     * @param sudadera the sudadera to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sudadera, or with status {@code 400 (Bad Request)} if the sudadera has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sudaderas")
    public ResponseEntity<Sudadera> createSudadera(@RequestBody Sudadera sudadera) throws URISyntaxException {
        log.debug("REST request to save Sudadera : {}", sudadera);
        if (sudadera.getId() != null) {
            throw new BadRequestAlertException("A new sudadera cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sudadera result = sudaderaService.save(sudadera);
        return ResponseEntity
            .created(new URI("/api/sudaderas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sudaderas/:id} : Updates an existing sudadera.
     *
     * @param id the id of the sudadera to save.
     * @param sudadera the sudadera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sudadera,
     * or with status {@code 400 (Bad Request)} if the sudadera is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sudadera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sudaderas/{id}")
    public ResponseEntity<Sudadera> updateSudadera(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sudadera sudadera
    ) throws URISyntaxException {
        log.debug("REST request to update Sudadera : {}, {}", id, sudadera);
        if (sudadera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sudadera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sudaderaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sudadera result = sudaderaService.save(sudadera);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sudadera.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sudaderas/:id} : Partial updates given fields of an existing sudadera, field will ignore if it is null
     *
     * @param id the id of the sudadera to save.
     * @param sudadera the sudadera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sudadera,
     * or with status {@code 400 (Bad Request)} if the sudadera is not valid,
     * or with status {@code 404 (Not Found)} if the sudadera is not found,
     * or with status {@code 500 (Internal Server Error)} if the sudadera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sudaderas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Sudadera> partialUpdateSudadera(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sudadera sudadera
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sudadera partially : {}, {}", id, sudadera);
        if (sudadera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sudadera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sudaderaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sudadera> result = sudaderaService.partialUpdate(sudadera);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sudadera.getId().toString())
        );
    }

    /**
     * {@code GET  /sudaderas} : get all the sudaderas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sudaderas in body.
     */
    @GetMapping("/sudaderas")
    public ResponseEntity<List<Sudadera>> getAllSudaderas(Pageable pageable) {
        log.debug("REST request to get a page of Sudaderas");
        Page<Sudadera> page = sudaderaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sudaderas/:id} : get the "id" sudadera.
     *
     * @param id the id of the sudadera to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sudadera, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sudaderas/{id}")
    public ResponseEntity<Sudadera> getSudadera(@PathVariable Long id) {
        log.debug("REST request to get Sudadera : {}", id);
        Optional<Sudadera> sudadera = sudaderaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sudadera);
    }

    /**
     * {@code DELETE  /sudaderas/:id} : delete the "id" sudadera.
     *
     * @param id the id of the sudadera to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sudaderas/{id}")
    public ResponseEntity<Void> deleteSudadera(@PathVariable Long id) {
        log.debug("REST request to delete Sudadera : {}", id);
        sudaderaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
