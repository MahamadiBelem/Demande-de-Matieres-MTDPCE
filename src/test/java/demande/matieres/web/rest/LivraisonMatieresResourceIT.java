package demande.matieres.web.rest;

import static demande.matieres.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.LivraisonMatieres;
import demande.matieres.repository.LivraisonMatieresRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LivraisonMatieresResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LivraisonMatieresResourceIT {

    private static final String DEFAULT_DESIGNATION_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_MATIERE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE_LIVREE = 1;
    private static final Integer UPDATED_QUANTITE_LIVREE = 2;

    private static final ZonedDateTime DEFAULT_DATE_LIVREE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_LIVREE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_STATUT_SUP = false;
    private static final Boolean UPDATED_STATUT_SUP = true;

    private static final String ENTITY_API_URL = "/api/livraison-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivraisonMatieresRepository livraisonMatieresRepository;

    @Mock
    private LivraisonMatieresRepository livraisonMatieresRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivraisonMatieresMockMvc;

    private LivraisonMatieres livraisonMatieres;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivraisonMatieres createEntity(EntityManager em) {
        LivraisonMatieres livraisonMatieres = new LivraisonMatieres()
            .designationMatiere(DEFAULT_DESIGNATION_MATIERE)
            .quantiteLivree(DEFAULT_QUANTITE_LIVREE)
            .dateLivree(DEFAULT_DATE_LIVREE)
            .statutSup(DEFAULT_STATUT_SUP);
        return livraisonMatieres;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivraisonMatieres createUpdatedEntity(EntityManager em) {
        LivraisonMatieres livraisonMatieres = new LivraisonMatieres()
            .designationMatiere(UPDATED_DESIGNATION_MATIERE)
            .quantiteLivree(UPDATED_QUANTITE_LIVREE)
            .dateLivree(UPDATED_DATE_LIVREE)
            .statutSup(UPDATED_STATUT_SUP);
        return livraisonMatieres;
    }

    @BeforeEach
    public void initTest() {
        livraisonMatieres = createEntity(em);
    }

    @Test
    @Transactional
    void createLivraisonMatieres() throws Exception {
        int databaseSizeBeforeCreate = livraisonMatieresRepository.findAll().size();
        // Create the LivraisonMatieres
        restLivraisonMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isCreated());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeCreate + 1);
        LivraisonMatieres testLivraisonMatieres = livraisonMatieresList.get(livraisonMatieresList.size() - 1);
        assertThat(testLivraisonMatieres.getDesignationMatiere()).isEqualTo(DEFAULT_DESIGNATION_MATIERE);
        assertThat(testLivraisonMatieres.getQuantiteLivree()).isEqualTo(DEFAULT_QUANTITE_LIVREE);
        assertThat(testLivraisonMatieres.getDateLivree()).isEqualTo(DEFAULT_DATE_LIVREE);
        assertThat(testLivraisonMatieres.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void createLivraisonMatieresWithExistingId() throws Exception {
        // Create the LivraisonMatieres with an existing ID
        livraisonMatieres.setId(1L);

        int databaseSizeBeforeCreate = livraisonMatieresRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivraisonMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationMatiereIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonMatieresRepository.findAll().size();
        // set the field null
        livraisonMatieres.setDesignationMatiere(null);

        // Create the LivraisonMatieres, which fails.

        restLivraisonMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteLivreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonMatieresRepository.findAll().size();
        // set the field null
        livraisonMatieres.setQuantiteLivree(null);

        // Create the LivraisonMatieres, which fails.

        restLivraisonMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLivraisonMatieres() throws Exception {
        // Initialize the database
        livraisonMatieresRepository.saveAndFlush(livraisonMatieres);

        // Get all the livraisonMatieresList
        restLivraisonMatieresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraisonMatieres.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationMatiere").value(hasItem(DEFAULT_DESIGNATION_MATIERE)))
            .andExpect(jsonPath("$.[*].quantiteLivree").value(hasItem(DEFAULT_QUANTITE_LIVREE)))
            .andExpect(jsonPath("$.[*].dateLivree").value(hasItem(sameInstant(DEFAULT_DATE_LIVREE))))
            .andExpect(jsonPath("$.[*].statutSup").value(hasItem(DEFAULT_STATUT_SUP.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLivraisonMatieresWithEagerRelationshipsIsEnabled() throws Exception {
        when(livraisonMatieresRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLivraisonMatieresMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(livraisonMatieresRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLivraisonMatieresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(livraisonMatieresRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLivraisonMatieresMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(livraisonMatieresRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLivraisonMatieres() throws Exception {
        // Initialize the database
        livraisonMatieresRepository.saveAndFlush(livraisonMatieres);

        // Get the livraisonMatieres
        restLivraisonMatieresMockMvc
            .perform(get(ENTITY_API_URL_ID, livraisonMatieres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livraisonMatieres.getId().intValue()))
            .andExpect(jsonPath("$.designationMatiere").value(DEFAULT_DESIGNATION_MATIERE))
            .andExpect(jsonPath("$.quantiteLivree").value(DEFAULT_QUANTITE_LIVREE))
            .andExpect(jsonPath("$.dateLivree").value(sameInstant(DEFAULT_DATE_LIVREE)))
            .andExpect(jsonPath("$.statutSup").value(DEFAULT_STATUT_SUP.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLivraisonMatieres() throws Exception {
        // Get the livraisonMatieres
        restLivraisonMatieresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivraisonMatieres() throws Exception {
        // Initialize the database
        livraisonMatieresRepository.saveAndFlush(livraisonMatieres);

        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();

        // Update the livraisonMatieres
        LivraisonMatieres updatedLivraisonMatieres = livraisonMatieresRepository.findById(livraisonMatieres.getId()).get();
        // Disconnect from session so that the updates on updatedLivraisonMatieres are not directly saved in db
        em.detach(updatedLivraisonMatieres);
        updatedLivraisonMatieres
            .designationMatiere(UPDATED_DESIGNATION_MATIERE)
            .quantiteLivree(UPDATED_QUANTITE_LIVREE)
            .dateLivree(UPDATED_DATE_LIVREE)
            .statutSup(UPDATED_STATUT_SUP);

        restLivraisonMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLivraisonMatieres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLivraisonMatieres))
            )
            .andExpect(status().isOk());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
        LivraisonMatieres testLivraisonMatieres = livraisonMatieresList.get(livraisonMatieresList.size() - 1);
        assertThat(testLivraisonMatieres.getDesignationMatiere()).isEqualTo(UPDATED_DESIGNATION_MATIERE);
        assertThat(testLivraisonMatieres.getQuantiteLivree()).isEqualTo(UPDATED_QUANTITE_LIVREE);
        assertThat(testLivraisonMatieres.getDateLivree()).isEqualTo(UPDATED_DATE_LIVREE);
        assertThat(testLivraisonMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void putNonExistingLivraisonMatieres() throws Exception {
        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();
        livraisonMatieres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivraisonMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livraisonMatieres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivraisonMatieres() throws Exception {
        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();
        livraisonMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivraisonMatieres() throws Exception {
        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();
        livraisonMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMatieresMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivraisonMatieresWithPatch() throws Exception {
        // Initialize the database
        livraisonMatieresRepository.saveAndFlush(livraisonMatieres);

        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();

        // Update the livraisonMatieres using partial update
        LivraisonMatieres partialUpdatedLivraisonMatieres = new LivraisonMatieres();
        partialUpdatedLivraisonMatieres.setId(livraisonMatieres.getId());

        partialUpdatedLivraisonMatieres
            .quantiteLivree(UPDATED_QUANTITE_LIVREE)
            .dateLivree(UPDATED_DATE_LIVREE)
            .statutSup(UPDATED_STATUT_SUP);

        restLivraisonMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivraisonMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraisonMatieres))
            )
            .andExpect(status().isOk());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
        LivraisonMatieres testLivraisonMatieres = livraisonMatieresList.get(livraisonMatieresList.size() - 1);
        assertThat(testLivraisonMatieres.getDesignationMatiere()).isEqualTo(DEFAULT_DESIGNATION_MATIERE);
        assertThat(testLivraisonMatieres.getQuantiteLivree()).isEqualTo(UPDATED_QUANTITE_LIVREE);
        assertThat(testLivraisonMatieres.getDateLivree()).isEqualTo(UPDATED_DATE_LIVREE);
        assertThat(testLivraisonMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void fullUpdateLivraisonMatieresWithPatch() throws Exception {
        // Initialize the database
        livraisonMatieresRepository.saveAndFlush(livraisonMatieres);

        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();

        // Update the livraisonMatieres using partial update
        LivraisonMatieres partialUpdatedLivraisonMatieres = new LivraisonMatieres();
        partialUpdatedLivraisonMatieres.setId(livraisonMatieres.getId());

        partialUpdatedLivraisonMatieres
            .designationMatiere(UPDATED_DESIGNATION_MATIERE)
            .quantiteLivree(UPDATED_QUANTITE_LIVREE)
            .dateLivree(UPDATED_DATE_LIVREE)
            .statutSup(UPDATED_STATUT_SUP);

        restLivraisonMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivraisonMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraisonMatieres))
            )
            .andExpect(status().isOk());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
        LivraisonMatieres testLivraisonMatieres = livraisonMatieresList.get(livraisonMatieresList.size() - 1);
        assertThat(testLivraisonMatieres.getDesignationMatiere()).isEqualTo(UPDATED_DESIGNATION_MATIERE);
        assertThat(testLivraisonMatieres.getQuantiteLivree()).isEqualTo(UPDATED_QUANTITE_LIVREE);
        assertThat(testLivraisonMatieres.getDateLivree()).isEqualTo(UPDATED_DATE_LIVREE);
        assertThat(testLivraisonMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void patchNonExistingLivraisonMatieres() throws Exception {
        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();
        livraisonMatieres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivraisonMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livraisonMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivraisonMatieres() throws Exception {
        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();
        livraisonMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivraisonMatieres() throws Exception {
        int databaseSizeBeforeUpdate = livraisonMatieresRepository.findAll().size();
        livraisonMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livraisonMatieres))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivraisonMatieres in the database
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivraisonMatieres() throws Exception {
        // Initialize the database
        livraisonMatieresRepository.saveAndFlush(livraisonMatieres);

        int databaseSizeBeforeDelete = livraisonMatieresRepository.findAll().size();

        // Delete the livraisonMatieres
        restLivraisonMatieresMockMvc
            .perform(delete(ENTITY_API_URL_ID, livraisonMatieres.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LivraisonMatieres> livraisonMatieresList = livraisonMatieresRepository.findAll();
        assertThat(livraisonMatieresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
