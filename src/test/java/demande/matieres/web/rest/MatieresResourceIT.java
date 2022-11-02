package demande.matieres.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.Matieres;
import demande.matieres.repository.MatieresRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MatieresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatieresResourceIT {

    private static final String DEFAULT_DESIGNATION_MATIERES = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_MATIERES = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE_MATIERES = 1;
    private static final Integer UPDATED_QUANTITE_MATIERES = 2;

    private static final String DEFAULT_CARACTERISTIQUES_MATIERES = "AAAAAAAAAA";
    private static final String UPDATED_CARACTERISTIQUES_MATIERES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT_SUP = false;
    private static final Boolean UPDATED_STATUT_SUP = true;

    private static final String ENTITY_API_URL = "/api/matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatieresRepository matieresRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatieresMockMvc;

    private Matieres matieres;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matieres createEntity(EntityManager em) {
        Matieres matieres = new Matieres()
            .designationMatieres(DEFAULT_DESIGNATION_MATIERES)
            .quantiteMatieres(DEFAULT_QUANTITE_MATIERES)
            .caracteristiquesMatieres(DEFAULT_CARACTERISTIQUES_MATIERES)
            .statutSup(DEFAULT_STATUT_SUP);
        return matieres;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matieres createUpdatedEntity(EntityManager em) {
        Matieres matieres = new Matieres()
            .designationMatieres(UPDATED_DESIGNATION_MATIERES)
            .quantiteMatieres(UPDATED_QUANTITE_MATIERES)
            .caracteristiquesMatieres(UPDATED_CARACTERISTIQUES_MATIERES)
            .statutSup(UPDATED_STATUT_SUP);
        return matieres;
    }

    @BeforeEach
    public void initTest() {
        matieres = createEntity(em);
    }

    @Test
    @Transactional
    void createMatieres() throws Exception {
        int databaseSizeBeforeCreate = matieresRepository.findAll().size();
        // Create the Matieres
        restMatieresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matieres)))
            .andExpect(status().isCreated());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeCreate + 1);
        Matieres testMatieres = matieresList.get(matieresList.size() - 1);
        assertThat(testMatieres.getDesignationMatieres()).isEqualTo(DEFAULT_DESIGNATION_MATIERES);
        assertThat(testMatieres.getQuantiteMatieres()).isEqualTo(DEFAULT_QUANTITE_MATIERES);
        assertThat(testMatieres.getCaracteristiquesMatieres()).isEqualTo(DEFAULT_CARACTERISTIQUES_MATIERES);
        assertThat(testMatieres.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void createMatieresWithExistingId() throws Exception {
        // Create the Matieres with an existing ID
        matieres.setId(1L);

        int databaseSizeBeforeCreate = matieresRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatieresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matieres)))
            .andExpect(status().isBadRequest());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationMatieresIsRequired() throws Exception {
        int databaseSizeBeforeTest = matieresRepository.findAll().size();
        // set the field null
        matieres.setDesignationMatieres(null);

        // Create the Matieres, which fails.

        restMatieresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matieres)))
            .andExpect(status().isBadRequest());

        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteMatieresIsRequired() throws Exception {
        int databaseSizeBeforeTest = matieresRepository.findAll().size();
        // set the field null
        matieres.setQuantiteMatieres(null);

        // Create the Matieres, which fails.

        restMatieresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matieres)))
            .andExpect(status().isBadRequest());

        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatieres() throws Exception {
        // Initialize the database
        matieresRepository.saveAndFlush(matieres);

        // Get all the matieresList
        restMatieresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matieres.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationMatieres").value(hasItem(DEFAULT_DESIGNATION_MATIERES)))
            .andExpect(jsonPath("$.[*].quantiteMatieres").value(hasItem(DEFAULT_QUANTITE_MATIERES)))
            .andExpect(jsonPath("$.[*].caracteristiquesMatieres").value(hasItem(DEFAULT_CARACTERISTIQUES_MATIERES.toString())))
            .andExpect(jsonPath("$.[*].statutSup").value(hasItem(DEFAULT_STATUT_SUP.booleanValue())));
    }

    @Test
    @Transactional
    void getMatieres() throws Exception {
        // Initialize the database
        matieresRepository.saveAndFlush(matieres);

        // Get the matieres
        restMatieresMockMvc
            .perform(get(ENTITY_API_URL_ID, matieres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matieres.getId().intValue()))
            .andExpect(jsonPath("$.designationMatieres").value(DEFAULT_DESIGNATION_MATIERES))
            .andExpect(jsonPath("$.quantiteMatieres").value(DEFAULT_QUANTITE_MATIERES))
            .andExpect(jsonPath("$.caracteristiquesMatieres").value(DEFAULT_CARACTERISTIQUES_MATIERES.toString()))
            .andExpect(jsonPath("$.statutSup").value(DEFAULT_STATUT_SUP.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMatieres() throws Exception {
        // Get the matieres
        restMatieresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatieres() throws Exception {
        // Initialize the database
        matieresRepository.saveAndFlush(matieres);

        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();

        // Update the matieres
        Matieres updatedMatieres = matieresRepository.findById(matieres.getId()).get();
        // Disconnect from session so that the updates on updatedMatieres are not directly saved in db
        em.detach(updatedMatieres);
        updatedMatieres
            .designationMatieres(UPDATED_DESIGNATION_MATIERES)
            .quantiteMatieres(UPDATED_QUANTITE_MATIERES)
            .caracteristiquesMatieres(UPDATED_CARACTERISTIQUES_MATIERES)
            .statutSup(UPDATED_STATUT_SUP);

        restMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMatieres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMatieres))
            )
            .andExpect(status().isOk());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
        Matieres testMatieres = matieresList.get(matieresList.size() - 1);
        assertThat(testMatieres.getDesignationMatieres()).isEqualTo(UPDATED_DESIGNATION_MATIERES);
        assertThat(testMatieres.getQuantiteMatieres()).isEqualTo(UPDATED_QUANTITE_MATIERES);
        assertThat(testMatieres.getCaracteristiquesMatieres()).isEqualTo(UPDATED_CARACTERISTIQUES_MATIERES);
        assertThat(testMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void putNonExistingMatieres() throws Exception {
        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();
        matieres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matieres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatieres() throws Exception {
        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();
        matieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatieres() throws Exception {
        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();
        matieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatieresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matieres)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatieresWithPatch() throws Exception {
        // Initialize the database
        matieresRepository.saveAndFlush(matieres);

        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();

        // Update the matieres using partial update
        Matieres partialUpdatedMatieres = new Matieres();
        partialUpdatedMatieres.setId(matieres.getId());

        partialUpdatedMatieres
            .designationMatieres(UPDATED_DESIGNATION_MATIERES)
            .quantiteMatieres(UPDATED_QUANTITE_MATIERES)
            .statutSup(UPDATED_STATUT_SUP);

        restMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatieres))
            )
            .andExpect(status().isOk());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
        Matieres testMatieres = matieresList.get(matieresList.size() - 1);
        assertThat(testMatieres.getDesignationMatieres()).isEqualTo(UPDATED_DESIGNATION_MATIERES);
        assertThat(testMatieres.getQuantiteMatieres()).isEqualTo(UPDATED_QUANTITE_MATIERES);
        assertThat(testMatieres.getCaracteristiquesMatieres()).isEqualTo(DEFAULT_CARACTERISTIQUES_MATIERES);
        assertThat(testMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void fullUpdateMatieresWithPatch() throws Exception {
        // Initialize the database
        matieresRepository.saveAndFlush(matieres);

        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();

        // Update the matieres using partial update
        Matieres partialUpdatedMatieres = new Matieres();
        partialUpdatedMatieres.setId(matieres.getId());

        partialUpdatedMatieres
            .designationMatieres(UPDATED_DESIGNATION_MATIERES)
            .quantiteMatieres(UPDATED_QUANTITE_MATIERES)
            .caracteristiquesMatieres(UPDATED_CARACTERISTIQUES_MATIERES)
            .statutSup(UPDATED_STATUT_SUP);

        restMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatieres))
            )
            .andExpect(status().isOk());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
        Matieres testMatieres = matieresList.get(matieresList.size() - 1);
        assertThat(testMatieres.getDesignationMatieres()).isEqualTo(UPDATED_DESIGNATION_MATIERES);
        assertThat(testMatieres.getQuantiteMatieres()).isEqualTo(UPDATED_QUANTITE_MATIERES);
        assertThat(testMatieres.getCaracteristiquesMatieres()).isEqualTo(UPDATED_CARACTERISTIQUES_MATIERES);
        assertThat(testMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void patchNonExistingMatieres() throws Exception {
        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();
        matieres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatieres() throws Exception {
        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();
        matieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatieres() throws Exception {
        int databaseSizeBeforeUpdate = matieresRepository.findAll().size();
        matieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatieresMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(matieres)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matieres in the database
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatieres() throws Exception {
        // Initialize the database
        matieresRepository.saveAndFlush(matieres);

        int databaseSizeBeforeDelete = matieresRepository.findAll().size();

        // Delete the matieres
        restMatieresMockMvc
            .perform(delete(ENTITY_API_URL_ID, matieres.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Matieres> matieresList = matieresRepository.findAll();
        assertThat(matieresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
