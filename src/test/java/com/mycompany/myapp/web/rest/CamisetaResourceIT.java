package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Camiseta;
import com.mycompany.myapp.repository.CamisetaRepository;
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
 * Integration tests for the {@link CamisetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CamisetaResourceIT {

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

    private static final String ENTITY_API_URL = "/api/camisetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CamisetaRepository camisetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCamisetaMockMvc;

    private Camiseta camiseta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Camiseta createEntity(EntityManager em) {
        Camiseta camiseta = new Camiseta()
            .stock(DEFAULT_STOCK)
            .imagen(DEFAULT_IMAGEN)
            .talla(DEFAULT_TALLA)
            .color(DEFAULT_COLOR)
            .coleccion(DEFAULT_COLECCION);
        return camiseta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Camiseta createUpdatedEntity(EntityManager em) {
        Camiseta camiseta = new Camiseta()
            .stock(UPDATED_STOCK)
            .imagen(UPDATED_IMAGEN)
            .talla(UPDATED_TALLA)
            .color(UPDATED_COLOR)
            .coleccion(UPDATED_COLECCION);
        return camiseta;
    }

    @BeforeEach
    public void initTest() {
        camiseta = createEntity(em);
    }

    @Test
    @Transactional
    void createCamiseta() throws Exception {
        int databaseSizeBeforeCreate = camisetaRepository.findAll().size();
        // Create the Camiseta
        restCamisetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(camiseta)))
            .andExpect(status().isCreated());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeCreate + 1);
        Camiseta testCamiseta = camisetaList.get(camisetaList.size() - 1);
        assertThat(testCamiseta.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testCamiseta.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testCamiseta.getTalla()).isEqualTo(DEFAULT_TALLA);
        assertThat(testCamiseta.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCamiseta.getColeccion()).isEqualTo(DEFAULT_COLECCION);
    }

    @Test
    @Transactional
    void createCamisetaWithExistingId() throws Exception {
        // Create the Camiseta with an existing ID
        camiseta.setId(1L);

        int databaseSizeBeforeCreate = camisetaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCamisetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(camiseta)))
            .andExpect(status().isBadRequest());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCamisetas() throws Exception {
        // Initialize the database
        camisetaRepository.saveAndFlush(camiseta);

        // Get all the camisetaList
        restCamisetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(camiseta.getId().intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].talla").value(hasItem(DEFAULT_TALLA)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].coleccion").value(hasItem(DEFAULT_COLECCION)));
    }

    @Test
    @Transactional
    void getCamiseta() throws Exception {
        // Initialize the database
        camisetaRepository.saveAndFlush(camiseta);

        // Get the camiseta
        restCamisetaMockMvc
            .perform(get(ENTITY_API_URL_ID, camiseta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(camiseta.getId().intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.talla").value(DEFAULT_TALLA))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.coleccion").value(DEFAULT_COLECCION));
    }

    @Test
    @Transactional
    void getNonExistingCamiseta() throws Exception {
        // Get the camiseta
        restCamisetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCamiseta() throws Exception {
        // Initialize the database
        camisetaRepository.saveAndFlush(camiseta);

        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();

        // Update the camiseta
        Camiseta updatedCamiseta = camisetaRepository.findById(camiseta.getId()).get();
        // Disconnect from session so that the updates on updatedCamiseta are not directly saved in db
        em.detach(updatedCamiseta);
        updatedCamiseta.stock(UPDATED_STOCK).imagen(UPDATED_IMAGEN).talla(UPDATED_TALLA).color(UPDATED_COLOR).coleccion(UPDATED_COLECCION);

        restCamisetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCamiseta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCamiseta))
            )
            .andExpect(status().isOk());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
        Camiseta testCamiseta = camisetaList.get(camisetaList.size() - 1);
        assertThat(testCamiseta.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testCamiseta.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testCamiseta.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testCamiseta.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCamiseta.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void putNonExistingCamiseta() throws Exception {
        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();
        camiseta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCamisetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, camiseta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(camiseta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCamiseta() throws Exception {
        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();
        camiseta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamisetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(camiseta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCamiseta() throws Exception {
        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();
        camiseta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamisetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(camiseta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCamisetaWithPatch() throws Exception {
        // Initialize the database
        camisetaRepository.saveAndFlush(camiseta);

        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();

        // Update the camiseta using partial update
        Camiseta partialUpdatedCamiseta = new Camiseta();
        partialUpdatedCamiseta.setId(camiseta.getId());

        partialUpdatedCamiseta.imagen(UPDATED_IMAGEN);

        restCamisetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCamiseta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCamiseta))
            )
            .andExpect(status().isOk());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
        Camiseta testCamiseta = camisetaList.get(camisetaList.size() - 1);
        assertThat(testCamiseta.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testCamiseta.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testCamiseta.getTalla()).isEqualTo(DEFAULT_TALLA);
        assertThat(testCamiseta.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCamiseta.getColeccion()).isEqualTo(DEFAULT_COLECCION);
    }

    @Test
    @Transactional
    void fullUpdateCamisetaWithPatch() throws Exception {
        // Initialize the database
        camisetaRepository.saveAndFlush(camiseta);

        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();

        // Update the camiseta using partial update
        Camiseta partialUpdatedCamiseta = new Camiseta();
        partialUpdatedCamiseta.setId(camiseta.getId());

        partialUpdatedCamiseta
            .stock(UPDATED_STOCK)
            .imagen(UPDATED_IMAGEN)
            .talla(UPDATED_TALLA)
            .color(UPDATED_COLOR)
            .coleccion(UPDATED_COLECCION);

        restCamisetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCamiseta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCamiseta))
            )
            .andExpect(status().isOk());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
        Camiseta testCamiseta = camisetaList.get(camisetaList.size() - 1);
        assertThat(testCamiseta.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testCamiseta.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testCamiseta.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testCamiseta.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCamiseta.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void patchNonExistingCamiseta() throws Exception {
        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();
        camiseta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCamisetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, camiseta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(camiseta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCamiseta() throws Exception {
        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();
        camiseta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamisetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(camiseta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCamiseta() throws Exception {
        int databaseSizeBeforeUpdate = camisetaRepository.findAll().size();
        camiseta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCamisetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(camiseta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Camiseta in the database
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCamiseta() throws Exception {
        // Initialize the database
        camisetaRepository.saveAndFlush(camiseta);

        int databaseSizeBeforeDelete = camisetaRepository.findAll().size();

        // Delete the camiseta
        restCamisetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, camiseta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Camiseta> camisetaList = camisetaRepository.findAll();
        assertThat(camisetaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
