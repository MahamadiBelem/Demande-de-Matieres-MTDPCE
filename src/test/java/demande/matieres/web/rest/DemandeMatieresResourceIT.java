package demande.matieres.web.rest;

import static demande.matieres.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.DemandeMatieres;
import demande.matieres.repository.DemandeMatieresRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DemandeMatieresResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeMatieresResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INDENTITE_SOUMETTANT = "AAAAAAAAAA";
    private static final String UPDATED_INDENTITE_SOUMETTANT = "BBBBBBBBBB";

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT_SUP = false;
    private static final Boolean UPDATED_STATUT_SUP = true;

    private static final String ENTITY_API_URL = "/api/demande-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeMatieresRepository demandeMatieresRepository;

    @Mock
    private DemandeMatieresRepository demandeMatieresRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeMatieresMockMvc;

    private DemandeMatieres demandeMatieres;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeMatieres createEntity(EntityManager em) {
        DemandeMatieres demandeMatieres = new DemandeMatieres()
            .date(DEFAULT_DATE)
            .indentiteSoumettant(DEFAULT_INDENTITE_SOUMETTANT)
            .fonction(DEFAULT_FONCTION)
            .designation(DEFAULT_DESIGNATION)
            .quantite(DEFAULT_QUANTITE)
            .observation(DEFAULT_OBSERVATION)
            .statutSup(DEFAULT_STATUT_SUP);
        return demandeMatieres;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeMatieres createUpdatedEntity(EntityManager em) {
        DemandeMatieres demandeMatieres = new DemandeMatieres()
            .date(UPDATED_DATE)
            .indentiteSoumettant(UPDATED_INDENTITE_SOUMETTANT)
            .fonction(UPDATED_FONCTION)
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .observation(UPDATED_OBSERVATION)
            .statutSup(UPDATED_STATUT_SUP);
        return demandeMatieres;
    }

    @BeforeEach
    public void initTest() {
        demandeMatieres = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeMatieres() throws Exception {
        int databaseSizeBeforeCreate = demandeMatieresRepository.findAll().size();
        // Create the DemandeMatieres
        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeMatieres testDemandeMatieres = demandeMatieresList.get(demandeMatieresList.size() - 1);
        assertThat(testDemandeMatieres.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemandeMatieres.getIndentiteSoumettant()).isEqualTo(DEFAULT_INDENTITE_SOUMETTANT);
        assertThat(testDemandeMatieres.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testDemandeMatieres.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testDemandeMatieres.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testDemandeMatieres.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testDemandeMatieres.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void createDemandeMatieresWithExistingId() throws Exception {
        // Create the DemandeMatieres with an existing ID
        demandeMatieres.setId(1L);

        int databaseSizeBeforeCreate = demandeMatieresRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeMatieresRepository.findAll().size();
        // set the field null
        demandeMatieres.setDate(null);

        // Create the DemandeMatieres, which fails.

        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIndentiteSoumettantIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeMatieresRepository.findAll().size();
        // set the field null
        demandeMatieres.setIndentiteSoumettant(null);

        // Create the DemandeMatieres, which fails.

        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFonctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeMatieresRepository.findAll().size();
        // set the field null
        demandeMatieres.setFonction(null);

        // Create the DemandeMatieres, which fails.

        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeMatieresRepository.findAll().size();
        // set the field null
        demandeMatieres.setDesignation(null);

        // Create the DemandeMatieres, which fails.

        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeMatieresRepository.findAll().size();
        // set the field null
        demandeMatieres.setQuantite(null);

        // Create the DemandeMatieres, which fails.

        restDemandeMatieresMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandeMatieres() throws Exception {
        // Initialize the database
        demandeMatieresRepository.saveAndFlush(demandeMatieres);

        // Get all the demandeMatieresList
        restDemandeMatieresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeMatieres.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].indentiteSoumettant").value(hasItem(DEFAULT_INDENTITE_SOUMETTANT)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())))
            .andExpect(jsonPath("$.[*].statutSup").value(hasItem(DEFAULT_STATUT_SUP.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeMatieresWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeMatieresRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeMatieresMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeMatieresRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeMatieresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeMatieresRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeMatieresMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeMatieresRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDemandeMatieres() throws Exception {
        // Initialize the database
        demandeMatieresRepository.saveAndFlush(demandeMatieres);

        // Get the demandeMatieres
        restDemandeMatieresMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeMatieres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeMatieres.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.indentiteSoumettant").value(DEFAULT_INDENTITE_SOUMETTANT))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()))
            .andExpect(jsonPath("$.statutSup").value(DEFAULT_STATUT_SUP.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDemandeMatieres() throws Exception {
        // Get the demandeMatieres
        restDemandeMatieresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeMatieres() throws Exception {
        // Initialize the database
        demandeMatieresRepository.saveAndFlush(demandeMatieres);

        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();

        // Update the demandeMatieres
        DemandeMatieres updatedDemandeMatieres = demandeMatieresRepository.findById(demandeMatieres.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeMatieres are not directly saved in db
        em.detach(updatedDemandeMatieres);
        updatedDemandeMatieres
            .date(UPDATED_DATE)
            .indentiteSoumettant(UPDATED_INDENTITE_SOUMETTANT)
            .fonction(UPDATED_FONCTION)
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .observation(UPDATED_OBSERVATION)
            .statutSup(UPDATED_STATUT_SUP);

        restDemandeMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeMatieres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeMatieres))
            )
            .andExpect(status().isOk());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
        DemandeMatieres testDemandeMatieres = demandeMatieresList.get(demandeMatieresList.size() - 1);
        assertThat(testDemandeMatieres.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeMatieres.getIndentiteSoumettant()).isEqualTo(UPDATED_INDENTITE_SOUMETTANT);
        assertThat(testDemandeMatieres.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testDemandeMatieres.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testDemandeMatieres.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testDemandeMatieres.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testDemandeMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void putNonExistingDemandeMatieres() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();
        demandeMatieres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeMatieres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeMatieres() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();
        demandeMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatieresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeMatieres() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();
        demandeMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatieresMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeMatieresWithPatch() throws Exception {
        // Initialize the database
        demandeMatieresRepository.saveAndFlush(demandeMatieres);

        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();

        // Update the demandeMatieres using partial update
        DemandeMatieres partialUpdatedDemandeMatieres = new DemandeMatieres();
        partialUpdatedDemandeMatieres.setId(demandeMatieres.getId());

        partialUpdatedDemandeMatieres.date(UPDATED_DATE).fonction(UPDATED_FONCTION).observation(UPDATED_OBSERVATION);

        restDemandeMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeMatieres))
            )
            .andExpect(status().isOk());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
        DemandeMatieres testDemandeMatieres = demandeMatieresList.get(demandeMatieresList.size() - 1);
        assertThat(testDemandeMatieres.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeMatieres.getIndentiteSoumettant()).isEqualTo(DEFAULT_INDENTITE_SOUMETTANT);
        assertThat(testDemandeMatieres.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testDemandeMatieres.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testDemandeMatieres.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testDemandeMatieres.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testDemandeMatieres.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void fullUpdateDemandeMatieresWithPatch() throws Exception {
        // Initialize the database
        demandeMatieresRepository.saveAndFlush(demandeMatieres);

        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();

        // Update the demandeMatieres using partial update
        DemandeMatieres partialUpdatedDemandeMatieres = new DemandeMatieres();
        partialUpdatedDemandeMatieres.setId(demandeMatieres.getId());

        partialUpdatedDemandeMatieres
            .date(UPDATED_DATE)
            .indentiteSoumettant(UPDATED_INDENTITE_SOUMETTANT)
            .fonction(UPDATED_FONCTION)
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .observation(UPDATED_OBSERVATION)
            .statutSup(UPDATED_STATUT_SUP);

        restDemandeMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeMatieres))
            )
            .andExpect(status().isOk());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
        DemandeMatieres testDemandeMatieres = demandeMatieresList.get(demandeMatieresList.size() - 1);
        assertThat(testDemandeMatieres.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeMatieres.getIndentiteSoumettant()).isEqualTo(UPDATED_INDENTITE_SOUMETTANT);
        assertThat(testDemandeMatieres.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testDemandeMatieres.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testDemandeMatieres.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testDemandeMatieres.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testDemandeMatieres.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeMatieres() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();
        demandeMatieres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeMatieres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeMatieres() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();
        demandeMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeMatieres() throws Exception {
        int databaseSizeBeforeUpdate = demandeMatieresRepository.findAll().size();
        demandeMatieres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMatieresMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeMatieres))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeMatieres in the database
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeMatieres() throws Exception {
        // Initialize the database
        demandeMatieresRepository.saveAndFlush(demandeMatieres);

        int databaseSizeBeforeDelete = demandeMatieresRepository.findAll().size();

        // Delete the demandeMatieres
        restDemandeMatieresMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeMatieres.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeMatieres> demandeMatieresList = demandeMatieresRepository.findAll();
        assertThat(demandeMatieresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
