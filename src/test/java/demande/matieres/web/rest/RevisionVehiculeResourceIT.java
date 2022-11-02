package demande.matieres.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.RevisionVehicule;
import demande.matieres.repository.RevisionVehiculeRepository;
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
 * Integration tests for the {@link RevisionVehiculeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RevisionVehiculeResourceIT {

    private static final String DEFAULT_LIBELLE_REVISION_VEHICULE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_REVISION_VEHICULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/revision-vehicules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RevisionVehiculeRepository revisionVehiculeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRevisionVehiculeMockMvc;

    private RevisionVehicule revisionVehicule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevisionVehicule createEntity(EntityManager em) {
        RevisionVehicule revisionVehicule = new RevisionVehicule().libelleRevisionVehicule(DEFAULT_LIBELLE_REVISION_VEHICULE);
        return revisionVehicule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevisionVehicule createUpdatedEntity(EntityManager em) {
        RevisionVehicule revisionVehicule = new RevisionVehicule().libelleRevisionVehicule(UPDATED_LIBELLE_REVISION_VEHICULE);
        return revisionVehicule;
    }

    @BeforeEach
    public void initTest() {
        revisionVehicule = createEntity(em);
    }

    @Test
    @Transactional
    void createRevisionVehicule() throws Exception {
        int databaseSizeBeforeCreate = revisionVehiculeRepository.findAll().size();
        // Create the RevisionVehicule
        restRevisionVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isCreated());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        RevisionVehicule testRevisionVehicule = revisionVehiculeList.get(revisionVehiculeList.size() - 1);
        assertThat(testRevisionVehicule.getLibelleRevisionVehicule()).isEqualTo(DEFAULT_LIBELLE_REVISION_VEHICULE);
    }

    @Test
    @Transactional
    void createRevisionVehiculeWithExistingId() throws Exception {
        // Create the RevisionVehicule with an existing ID
        revisionVehicule.setId(1L);

        int databaseSizeBeforeCreate = revisionVehiculeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleRevisionVehiculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionVehiculeRepository.findAll().size();
        // set the field null
        revisionVehicule.setLibelleRevisionVehicule(null);

        // Create the RevisionVehicule, which fails.

        restRevisionVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isBadRequest());

        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRevisionVehicules() throws Exception {
        // Initialize the database
        revisionVehiculeRepository.saveAndFlush(revisionVehicule);

        // Get all the revisionVehiculeList
        restRevisionVehiculeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revisionVehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleRevisionVehicule").value(hasItem(DEFAULT_LIBELLE_REVISION_VEHICULE)));
    }

    @Test
    @Transactional
    void getRevisionVehicule() throws Exception {
        // Initialize the database
        revisionVehiculeRepository.saveAndFlush(revisionVehicule);

        // Get the revisionVehicule
        restRevisionVehiculeMockMvc
            .perform(get(ENTITY_API_URL_ID, revisionVehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(revisionVehicule.getId().intValue()))
            .andExpect(jsonPath("$.libelleRevisionVehicule").value(DEFAULT_LIBELLE_REVISION_VEHICULE));
    }

    @Test
    @Transactional
    void getNonExistingRevisionVehicule() throws Exception {
        // Get the revisionVehicule
        restRevisionVehiculeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRevisionVehicule() throws Exception {
        // Initialize the database
        revisionVehiculeRepository.saveAndFlush(revisionVehicule);

        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();

        // Update the revisionVehicule
        RevisionVehicule updatedRevisionVehicule = revisionVehiculeRepository.findById(revisionVehicule.getId()).get();
        // Disconnect from session so that the updates on updatedRevisionVehicule are not directly saved in db
        em.detach(updatedRevisionVehicule);
        updatedRevisionVehicule.libelleRevisionVehicule(UPDATED_LIBELLE_REVISION_VEHICULE);

        restRevisionVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRevisionVehicule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRevisionVehicule))
            )
            .andExpect(status().isOk());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
        RevisionVehicule testRevisionVehicule = revisionVehiculeList.get(revisionVehiculeList.size() - 1);
        assertThat(testRevisionVehicule.getLibelleRevisionVehicule()).isEqualTo(UPDATED_LIBELLE_REVISION_VEHICULE);
    }

    @Test
    @Transactional
    void putNonExistingRevisionVehicule() throws Exception {
        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();
        revisionVehicule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevisionVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, revisionVehicule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRevisionVehicule() throws Exception {
        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();
        revisionVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRevisionVehicule() throws Exception {
        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();
        revisionVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRevisionVehiculeWithPatch() throws Exception {
        // Initialize the database
        revisionVehiculeRepository.saveAndFlush(revisionVehicule);

        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();

        // Update the revisionVehicule using partial update
        RevisionVehicule partialUpdatedRevisionVehicule = new RevisionVehicule();
        partialUpdatedRevisionVehicule.setId(revisionVehicule.getId());

        restRevisionVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRevisionVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRevisionVehicule))
            )
            .andExpect(status().isOk());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
        RevisionVehicule testRevisionVehicule = revisionVehiculeList.get(revisionVehiculeList.size() - 1);
        assertThat(testRevisionVehicule.getLibelleRevisionVehicule()).isEqualTo(DEFAULT_LIBELLE_REVISION_VEHICULE);
    }

    @Test
    @Transactional
    void fullUpdateRevisionVehiculeWithPatch() throws Exception {
        // Initialize the database
        revisionVehiculeRepository.saveAndFlush(revisionVehicule);

        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();

        // Update the revisionVehicule using partial update
        RevisionVehicule partialUpdatedRevisionVehicule = new RevisionVehicule();
        partialUpdatedRevisionVehicule.setId(revisionVehicule.getId());

        partialUpdatedRevisionVehicule.libelleRevisionVehicule(UPDATED_LIBELLE_REVISION_VEHICULE);

        restRevisionVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRevisionVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRevisionVehicule))
            )
            .andExpect(status().isOk());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
        RevisionVehicule testRevisionVehicule = revisionVehiculeList.get(revisionVehiculeList.size() - 1);
        assertThat(testRevisionVehicule.getLibelleRevisionVehicule()).isEqualTo(UPDATED_LIBELLE_REVISION_VEHICULE);
    }

    @Test
    @Transactional
    void patchNonExistingRevisionVehicule() throws Exception {
        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();
        revisionVehicule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevisionVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, revisionVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRevisionVehicule() throws Exception {
        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();
        revisionVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRevisionVehicule() throws Exception {
        int databaseSizeBeforeUpdate = revisionVehiculeRepository.findAll().size();
        revisionVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revisionVehicule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RevisionVehicule in the database
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRevisionVehicule() throws Exception {
        // Initialize the database
        revisionVehiculeRepository.saveAndFlush(revisionVehicule);

        int databaseSizeBeforeDelete = revisionVehiculeRepository.findAll().size();

        // Delete the revisionVehicule
        restRevisionVehiculeMockMvc
            .perform(delete(ENTITY_API_URL_ID, revisionVehicule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RevisionVehicule> revisionVehiculeList = revisionVehiculeRepository.findAll();
        assertThat(revisionVehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
