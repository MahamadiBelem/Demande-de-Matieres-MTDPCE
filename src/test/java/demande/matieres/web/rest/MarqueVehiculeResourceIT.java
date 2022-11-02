package demande.matieres.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.MarqueVehicule;
import demande.matieres.repository.MarqueVehiculeRepository;
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
 * Integration tests for the {@link MarqueVehiculeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarqueVehiculeResourceIT {

    private static final String DEFAULT_LIBELLE_MARQUE_VEHICULE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_MARQUE_VEHICULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marque-vehicules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarqueVehiculeRepository marqueVehiculeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarqueVehiculeMockMvc;

    private MarqueVehicule marqueVehicule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarqueVehicule createEntity(EntityManager em) {
        MarqueVehicule marqueVehicule = new MarqueVehicule().libelleMarqueVehicule(DEFAULT_LIBELLE_MARQUE_VEHICULE);
        return marqueVehicule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarqueVehicule createUpdatedEntity(EntityManager em) {
        MarqueVehicule marqueVehicule = new MarqueVehicule().libelleMarqueVehicule(UPDATED_LIBELLE_MARQUE_VEHICULE);
        return marqueVehicule;
    }

    @BeforeEach
    public void initTest() {
        marqueVehicule = createEntity(em);
    }

    @Test
    @Transactional
    void createMarqueVehicule() throws Exception {
        int databaseSizeBeforeCreate = marqueVehiculeRepository.findAll().size();
        // Create the MarqueVehicule
        restMarqueVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isCreated());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        MarqueVehicule testMarqueVehicule = marqueVehiculeList.get(marqueVehiculeList.size() - 1);
        assertThat(testMarqueVehicule.getLibelleMarqueVehicule()).isEqualTo(DEFAULT_LIBELLE_MARQUE_VEHICULE);
    }

    @Test
    @Transactional
    void createMarqueVehiculeWithExistingId() throws Exception {
        // Create the MarqueVehicule with an existing ID
        marqueVehicule.setId(1L);

        int databaseSizeBeforeCreate = marqueVehiculeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarqueVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleMarqueVehiculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = marqueVehiculeRepository.findAll().size();
        // set the field null
        marqueVehicule.setLibelleMarqueVehicule(null);

        // Create the MarqueVehicule, which fails.

        restMarqueVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isBadRequest());

        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarqueVehicules() throws Exception {
        // Initialize the database
        marqueVehiculeRepository.saveAndFlush(marqueVehicule);

        // Get all the marqueVehiculeList
        restMarqueVehiculeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marqueVehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleMarqueVehicule").value(hasItem(DEFAULT_LIBELLE_MARQUE_VEHICULE)));
    }

    @Test
    @Transactional
    void getMarqueVehicule() throws Exception {
        // Initialize the database
        marqueVehiculeRepository.saveAndFlush(marqueVehicule);

        // Get the marqueVehicule
        restMarqueVehiculeMockMvc
            .perform(get(ENTITY_API_URL_ID, marqueVehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marqueVehicule.getId().intValue()))
            .andExpect(jsonPath("$.libelleMarqueVehicule").value(DEFAULT_LIBELLE_MARQUE_VEHICULE));
    }

    @Test
    @Transactional
    void getNonExistingMarqueVehicule() throws Exception {
        // Get the marqueVehicule
        restMarqueVehiculeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMarqueVehicule() throws Exception {
        // Initialize the database
        marqueVehiculeRepository.saveAndFlush(marqueVehicule);

        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();

        // Update the marqueVehicule
        MarqueVehicule updatedMarqueVehicule = marqueVehiculeRepository.findById(marqueVehicule.getId()).get();
        // Disconnect from session so that the updates on updatedMarqueVehicule are not directly saved in db
        em.detach(updatedMarqueVehicule);
        updatedMarqueVehicule.libelleMarqueVehicule(UPDATED_LIBELLE_MARQUE_VEHICULE);

        restMarqueVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarqueVehicule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMarqueVehicule))
            )
            .andExpect(status().isOk());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
        MarqueVehicule testMarqueVehicule = marqueVehiculeList.get(marqueVehiculeList.size() - 1);
        assertThat(testMarqueVehicule.getLibelleMarqueVehicule()).isEqualTo(UPDATED_LIBELLE_MARQUE_VEHICULE);
    }

    @Test
    @Transactional
    void putNonExistingMarqueVehicule() throws Exception {
        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();
        marqueVehicule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarqueVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marqueVehicule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarqueVehicule() throws Exception {
        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();
        marqueVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarqueVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarqueVehicule() throws Exception {
        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();
        marqueVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarqueVehiculeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marqueVehicule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarqueVehiculeWithPatch() throws Exception {
        // Initialize the database
        marqueVehiculeRepository.saveAndFlush(marqueVehicule);

        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();

        // Update the marqueVehicule using partial update
        MarqueVehicule partialUpdatedMarqueVehicule = new MarqueVehicule();
        partialUpdatedMarqueVehicule.setId(marqueVehicule.getId());

        partialUpdatedMarqueVehicule.libelleMarqueVehicule(UPDATED_LIBELLE_MARQUE_VEHICULE);

        restMarqueVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarqueVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarqueVehicule))
            )
            .andExpect(status().isOk());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
        MarqueVehicule testMarqueVehicule = marqueVehiculeList.get(marqueVehiculeList.size() - 1);
        assertThat(testMarqueVehicule.getLibelleMarqueVehicule()).isEqualTo(UPDATED_LIBELLE_MARQUE_VEHICULE);
    }

    @Test
    @Transactional
    void fullUpdateMarqueVehiculeWithPatch() throws Exception {
        // Initialize the database
        marqueVehiculeRepository.saveAndFlush(marqueVehicule);

        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();

        // Update the marqueVehicule using partial update
        MarqueVehicule partialUpdatedMarqueVehicule = new MarqueVehicule();
        partialUpdatedMarqueVehicule.setId(marqueVehicule.getId());

        partialUpdatedMarqueVehicule.libelleMarqueVehicule(UPDATED_LIBELLE_MARQUE_VEHICULE);

        restMarqueVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarqueVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarqueVehicule))
            )
            .andExpect(status().isOk());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
        MarqueVehicule testMarqueVehicule = marqueVehiculeList.get(marqueVehiculeList.size() - 1);
        assertThat(testMarqueVehicule.getLibelleMarqueVehicule()).isEqualTo(UPDATED_LIBELLE_MARQUE_VEHICULE);
    }

    @Test
    @Transactional
    void patchNonExistingMarqueVehicule() throws Exception {
        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();
        marqueVehicule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarqueVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marqueVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarqueVehicule() throws Exception {
        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();
        marqueVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarqueVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarqueVehicule() throws Exception {
        int databaseSizeBeforeUpdate = marqueVehiculeRepository.findAll().size();
        marqueVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarqueVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(marqueVehicule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarqueVehicule in the database
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarqueVehicule() throws Exception {
        // Initialize the database
        marqueVehiculeRepository.saveAndFlush(marqueVehicule);

        int databaseSizeBeforeDelete = marqueVehiculeRepository.findAll().size();

        // Delete the marqueVehicule
        restMarqueVehiculeMockMvc
            .perform(delete(ENTITY_API_URL_ID, marqueVehicule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MarqueVehicule> marqueVehiculeList = marqueVehiculeRepository.findAll();
        assertThat(marqueVehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
