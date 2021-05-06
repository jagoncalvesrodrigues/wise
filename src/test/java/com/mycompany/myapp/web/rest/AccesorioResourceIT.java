package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Accesorio;
import com.mycompany.myapp.repository.AccesorioRepository;
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
 * Integration tests for the {@link AccesorioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccesorioResourceIT {

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

    private static final String ENTITY_API_URL = "/api/accesorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccesorioRepository accesorioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccesorioMockMvc;

    private Accesorio accesorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accesorio createEntity(EntityManager em) {
        Accesorio accesorio = new Accesorio()
            .stock(DEFAULT_STOCK)
            .imagen(DEFAULT_IMAGEN)
            .talla(DEFAULT_TALLA)
            .color(DEFAULT_COLOR)
            .coleccion(DEFAULT_COLECCION);
        return accesorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accesorio createUpdatedEntity(EntityManager em) {
        Accesorio accesorio = new Accesorio()
            .stock(UPDATED_STOCK)
            .imagen(UPDATED_IMAGEN)
            .talla(UPDATED_TALLA)
            .color(UPDATED_COLOR)
            .coleccion(UPDATED_COLECCION);
        return accesorio;
    }

    @BeforeEach
    public void initTest() {
        accesorio = createEntity(em);
    }

    @Test
    @Transactional
    void createAccesorio() throws Exception {
        int databaseSizeBeforeCreate = accesorioRepository.findAll().size();
        // Create the Accesorio
        restAccesorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accesorio)))
            .andExpect(status().isCreated());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeCreate + 1);
        Accesorio testAccesorio = accesorioList.get(accesorioList.size() - 1);
        assertThat(testAccesorio.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testAccesorio.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testAccesorio.getTalla()).isEqualTo(DEFAULT_TALLA);
        assertThat(testAccesorio.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testAccesorio.getColeccion()).isEqualTo(DEFAULT_COLECCION);
    }

    @Test
    @Transactional
    void createAccesorioWithExistingId() throws Exception {
        // Create the Accesorio with an existing ID
        accesorio.setId(1L);

        int databaseSizeBeforeCreate = accesorioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccesorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accesorio)))
            .andExpect(status().isBadRequest());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccesorios() throws Exception {
        // Initialize the database
        accesorioRepository.saveAndFlush(accesorio);

        // Get all the accesorioList
        restAccesorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accesorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].talla").value(hasItem(DEFAULT_TALLA)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].coleccion").value(hasItem(DEFAULT_COLECCION)));
    }

    @Test
    @Transactional
    void getAccesorio() throws Exception {
        // Initialize the database
        accesorioRepository.saveAndFlush(accesorio);

        // Get the accesorio
        restAccesorioMockMvc
            .perform(get(ENTITY_API_URL_ID, accesorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accesorio.getId().intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.talla").value(DEFAULT_TALLA))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.coleccion").value(DEFAULT_COLECCION));
    }

    @Test
    @Transactional
    void getNonExistingAccesorio() throws Exception {
        // Get the accesorio
        restAccesorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccesorio() throws Exception {
        // Initialize the database
        accesorioRepository.saveAndFlush(accesorio);

        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();

        // Update the accesorio
        Accesorio updatedAccesorio = accesorioRepository.findById(accesorio.getId()).get();
        // Disconnect from session so that the updates on updatedAccesorio are not directly saved in db
        em.detach(updatedAccesorio);
        updatedAccesorio.stock(UPDATED_STOCK).imagen(UPDATED_IMAGEN).talla(UPDATED_TALLA).color(UPDATED_COLOR).coleccion(UPDATED_COLECCION);

        restAccesorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccesorio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccesorio))
            )
            .andExpect(status().isOk());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
        Accesorio testAccesorio = accesorioList.get(accesorioList.size() - 1);
        assertThat(testAccesorio.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testAccesorio.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testAccesorio.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testAccesorio.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testAccesorio.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void putNonExistingAccesorio() throws Exception {
        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();
        accesorio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccesorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accesorio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accesorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccesorio() throws Exception {
        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();
        accesorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accesorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccesorio() throws Exception {
        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();
        accesorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesorioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accesorio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccesorioWithPatch() throws Exception {
        // Initialize the database
        accesorioRepository.saveAndFlush(accesorio);

        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();

        // Update the accesorio using partial update
        Accesorio partialUpdatedAccesorio = new Accesorio();
        partialUpdatedAccesorio.setId(accesorio.getId());

        partialUpdatedAccesorio.imagen(UPDATED_IMAGEN);

        restAccesorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccesorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccesorio))
            )
            .andExpect(status().isOk());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
        Accesorio testAccesorio = accesorioList.get(accesorioList.size() - 1);
        assertThat(testAccesorio.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testAccesorio.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testAccesorio.getTalla()).isEqualTo(DEFAULT_TALLA);
        assertThat(testAccesorio.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testAccesorio.getColeccion()).isEqualTo(DEFAULT_COLECCION);
    }

    @Test
    @Transactional
    void fullUpdateAccesorioWithPatch() throws Exception {
        // Initialize the database
        accesorioRepository.saveAndFlush(accesorio);

        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();

        // Update the accesorio using partial update
        Accesorio partialUpdatedAccesorio = new Accesorio();
        partialUpdatedAccesorio.setId(accesorio.getId());

        partialUpdatedAccesorio
            .stock(UPDATED_STOCK)
            .imagen(UPDATED_IMAGEN)
            .talla(UPDATED_TALLA)
            .color(UPDATED_COLOR)
            .coleccion(UPDATED_COLECCION);

        restAccesorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccesorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccesorio))
            )
            .andExpect(status().isOk());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
        Accesorio testAccesorio = accesorioList.get(accesorioList.size() - 1);
        assertThat(testAccesorio.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testAccesorio.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testAccesorio.getTalla()).isEqualTo(UPDATED_TALLA);
        assertThat(testAccesorio.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testAccesorio.getColeccion()).isEqualTo(UPDATED_COLECCION);
    }

    @Test
    @Transactional
    void patchNonExistingAccesorio() throws Exception {
        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();
        accesorio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccesorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accesorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accesorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccesorio() throws Exception {
        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();
        accesorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accesorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccesorio() throws Exception {
        int databaseSizeBeforeUpdate = accesorioRepository.findAll().size();
        accesorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesorioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accesorio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accesorio in the database
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccesorio() throws Exception {
        // Initialize the database
        accesorioRepository.saveAndFlush(accesorio);

        int databaseSizeBeforeDelete = accesorioRepository.findAll().size();

        // Delete the accesorio
        restAccesorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, accesorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Accesorio> accesorioList = accesorioRepository.findAll();
        assertThat(accesorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
