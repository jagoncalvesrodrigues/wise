package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Sudadera;
import com.mycompany.myapp.repository.SudaderaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SudaderaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SudaderaResourceIT {

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String DEFAULT_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEN = "BBBBBBBBBB";

    private static final String DEFAULT_TALLA = "AAAAAAAAAA";
    private static final String UPDATED_TALLA = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_COLECCION = 1;
    private static final Integer UPDATED_COLECCION = 2;

    private static final String ENTITY_API_URL = "/api/sudaderas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SudaderaRepository sudaderaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSudaderaMockMvc;

    private Sudadera sudadera;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sudadera createEntity(EntityManager em) {
        Sudadera sudadera = new Sudadera()
            .stock(DEFAULT_STOCK)
            .imagen(DEFAULT_IMAGEN)
            .talla(DEFAULT_TALLA)
            .color(DEFAULT_COLOR)
            .coleccion(DEFAULT_COLECCION);
        return sudadera;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sudadera createUpdatedEntity(EntityManager em) {
        Sudadera sudadera = new Sudadera()
            .stock(UPDATED_STOCK)
            .imagen(UPDATED_IMAGEN)
            .talla(UPDATED_TALLA)
            .color(UPDATED_COLOR)
            .coleccion(UPDATED_COLECCION);
        return sudadera;
    }

    @BeforeEach
    public void initTest() {
        sudadera = createEntity(em);
    }

    @Test
    @Transactional
    void createSudadera() throws Exception {
        int databaseSizeBeforeCreate = sudaderaRepository.findAll().size();
        // Create the Sudadera
        restSudaderaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sudadera)))
            .andExpect(status().isCreated());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeCreate + 1);
        Sudadera testSudadera = sudaderaList.get(sudaderaList.size() - 1);
        assertThat(testSudadera.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testSudadera.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testSudadera.getTalla()).isEqualTo(DEFAULT_TALLA);
        assertThat(testSudadera.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testSudadera.getColeccion()).isEqualTo(DEFAULT_COLECCION);
    }

    @Test
    @Transactional
    void createSudaderaWithExistingId() throws Exception {
        // Create the Sudadera with an existing ID
        sudadera.setId(1L);

        int databaseSizeBeforeCreate = sudaderaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSudaderaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sudadera)))
            .andExpect(status().isBadRequest());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSudaderas() throws Exception {
        // Initialize the database
        sudaderaRepository.saveAndFlush(sudadera);

        // Get all the sudaderaList
        restSudaderaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sudadera.getId().intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].talla").value(hasItem(DEFAULT_TALLA)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].coleccion").value(hasItem(DEFAULT_COLECCION)));
    }

    @Test
    @Transactional
    void getSudadera() throws Exception {
        // Initialize the database
        sudaderaRepository.saveAndFlush(sudadera);

        // Get the sudadera
        restSudaderaMockMvc
            .perform(get(ENTITY_API_URL_ID, sudadera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sudadera.getId().intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.talla").value(DEFAULT_TALLA))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.coleccion").value(DEFAULT_COLECCION));
    }

    @Test
    @Transactional
    void getNonExistingSudadera() throws Exception {
        // Get the sudadera
        restSudaderaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSudadera() throws Exception {
        // Initialize the database
        sudaderaRepository.saveAndFlush(sudadera);

        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();

        // Update the sudadera
        Sudadera updatedSudadera = sudaderaRepository.findById(sudadera.getId()).get();
        // Disconnect from session so that the updates on updatedSudadera are not directly saved in db
        em.detach(updatedSudadera);
        updatedSudadera.stock(UPDATED_STOCK).imagen(UPDATED_IMAGEN).talla(UPDATED_TALLA).color(UPDATED_COLOR).coleccion(UPDATED_COLECCION);

        restSudaderaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSudadera.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSudadera))
            )
            .andExpect(status().isOk());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
        Sudadera testSudadera = sudaderaList.get(sudaderaList.size() - 1);
        assertThat(testSudadera.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testSudadera.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testSudadera.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testSudadera.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testSudadera.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void putNonExistingSudadera() throws Exception {
        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();
        sudadera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSudaderaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sudadera.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sudadera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSudadera() throws Exception {
        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();
        sudadera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSudaderaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sudadera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSudadera() throws Exception {
        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();
        sudadera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSudaderaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sudadera)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSudaderaWithPatch() throws Exception {
        // Initialize the database
        sudaderaRepository.saveAndFlush(sudadera);

        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();

        // Update the sudadera using partial update
        Sudadera partialUpdatedSudadera = new Sudadera();
        partialUpdatedSudadera.setId(sudadera.getId());

        partialUpdatedSudadera.stock(UPDATED_STOCK).talla(UPDATED_TALLA).color(UPDATED_COLOR).coleccion(UPDATED_COLECCION);

        restSudaderaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSudadera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSudadera))
            )
            .andExpect(status().isOk());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
        Sudadera testSudadera = sudaderaList.get(sudaderaList.size() - 1);
        assertThat(testSudadera.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testSudadera.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testSudadera.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testSudadera.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testSudadera.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void fullUpdateSudaderaWithPatch() throws Exception {
        // Initialize the database
        sudaderaRepository.saveAndFlush(sudadera);

        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();

        // Update the sudadera using partial update
        Sudadera partialUpdatedSudadera = new Sudadera();
        partialUpdatedSudadera.setId(sudadera.getId());

        partialUpdatedSudadera
            .stock(UPDATED_STOCK)
            .imagen(UPDATED_IMAGEN)
            .talla(UPDATED_TALLA)
            .color(UPDATED_COLOR)
            .coleccion(UPDATED_COLECCION);

        restSudaderaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSudadera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSudadera))
            )
            .andExpect(status().isOk());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
        Sudadera testSudadera = sudaderaList.get(sudaderaList.size() - 1);
        assertThat(testSudadera.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testSudadera.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testSudadera.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testSudadera.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testSudadera.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void patchNonExistingSudadera() throws Exception {
        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();
        sudadera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSudaderaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sudadera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sudadera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSudadera() throws Exception {
        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();
        sudadera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSudaderaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sudadera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSudadera() throws Exception {
        int databaseSizeBeforeUpdate = sudaderaRepository.findAll().size();
        sudadera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSudaderaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sudadera)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sudadera in the database
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSudadera() throws Exception {
        // Initialize the database
        sudaderaRepository.saveAndFlush(sudadera);

        int databaseSizeBeforeDelete = sudaderaRepository.findAll().size();

        // Delete the sudadera
        restSudaderaMockMvc
            .perform(delete(ENTITY_API_URL_ID, sudadera.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sudadera> sudaderaList = sudaderaRepository.findAll();
        assertThat(sudaderaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
