package demande.matieres.web.rest;

import static demande.matieres.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.DemandeReparations;
import demande.matieres.repository.DemandeReparationsRepository;
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
 * Integration tests for the {@link DemandeReparationsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeReparationsResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INDENTITE_SOUMETTANT = "AAAAAAAAAA";
    private static final String UPDATED_INDENTITE_SOUMETTANT = "BBBBBBBBBB";

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT_SUP = false;
    private static final Boolean UPDATED_STATUT_SUP = true;

    private static final String ENTITY_API_URL = "/api/demande-reparations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeReparationsRepository demandeReparationsRepository;

    @Mock
    private DemandeReparationsRepository demandeReparationsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeReparationsMockMvc;

    private DemandeReparations demandeReparations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeReparations createEntity(EntityManager em) {
        DemandeReparations demandeReparations = new DemandeReparations()
            .date(DEFAULT_DATE)
            .indentiteSoumettant(DEFAULT_INDENTITE_SOUMETTANT)
            .fonction(DEFAULT_FONCTION)
            .designation(DEFAULT_DESIGNATION)
            .observation(DEFAULT_OBSERVATION)
            .statutSup(DEFAULT_STATUT_SUP);
        return demandeReparations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeReparations createUpdatedEntity(EntityManager em) {
        DemandeReparations demandeReparations = new DemandeReparations()
            .date(UPDATED_DATE)
            .indentiteSoumettant(UPDATED_INDENTITE_SOUMETTANT)
            .fonction(UPDATED_FONCTION)
            .designation(UPDATED_DESIGNATION)
            .observation(UPDATED_OBSERVATION)
            .statutSup(UPDATED_STATUT_SUP);
        return demandeReparations;
    }

    @BeforeEach
    public void initTest() {
        demandeReparations = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeReparations() throws Exception {
        int databaseSizeBeforeCreate = demandeReparationsRepository.findAll().size();
        // Create the DemandeReparations
        restDemandeReparationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeReparations testDemandeReparations = demandeReparationsList.get(demandeReparationsList.size() - 1);
        assertThat(testDemandeReparations.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemandeReparations.getIndentiteSoumettant()).isEqualTo(DEFAULT_INDENTITE_SOUMETTANT);
        assertThat(testDemandeReparations.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testDemandeReparations.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testDemandeReparations.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testDemandeReparations.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void createDemandeReparationsWithExistingId() throws Exception {
        // Create the DemandeReparations with an existing ID
        demandeReparations.setId(1L);

        int databaseSizeBeforeCreate = demandeReparationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeReparationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeReparationsRepository.findAll().size();
        // set the field null
        demandeReparations.setDate(null);

        // Create the DemandeReparations, which fails.

        restDemandeReparationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIndentiteSoumettantIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeReparationsRepository.findAll().size();
        // set the field null
        demandeReparations.setIndentiteSoumettant(null);

        // Create the DemandeReparations, which fails.

        restDemandeReparationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFonctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeReparationsRepository.findAll().size();
        // set the field null
        demandeReparations.setFonction(null);

        // Create the DemandeReparations, which fails.

        restDemandeReparationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeReparationsRepository.findAll().size();
        // set the field null
        demandeReparations.setDesignation(null);

        // Create the DemandeReparations, which fails.

        restDemandeReparationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandeReparations() throws Exception {
        // Initialize the database
        demandeReparationsRepository.saveAndFlush(demandeReparations);

        // Get all the demandeReparationsList
        restDemandeReparationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeReparations.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].indentiteSoumettant").value(hasItem(DEFAULT_INDENTITE_SOUMETTANT)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())))
            .andExpect(jsonPath("$.[*].statutSup").value(hasItem(DEFAULT_STATUT_SUP.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeReparationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeReparationsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeReparationsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeReparationsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeReparationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeReparationsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeReparationsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeReparationsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDemandeReparations() throws Exception {
        // Initialize the database
        demandeReparationsRepository.saveAndFlush(demandeReparations);

        // Get the demandeReparations
        restDemandeReparationsMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeReparations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeReparations.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.indentiteSoumettant").value(DEFAULT_INDENTITE_SOUMETTANT))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()))
            .andExpect(jsonPath("$.statutSup").value(DEFAULT_STATUT_SUP.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDemandeReparations() throws Exception {
        // Get the demandeReparations
        restDemandeReparationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeReparations() throws Exception {
        // Initialize the database
        demandeReparationsRepository.saveAndFlush(demandeReparations);

        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();

        // Update the demandeReparations
        DemandeReparations updatedDemandeReparations = demandeReparationsRepository.findById(demandeReparations.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeReparations are not directly saved in db
        em.detach(updatedDemandeReparations);
        updatedDemandeReparations
            .date(UPDATED_DATE)
            .indentiteSoumettant(UPDATED_INDENTITE_SOUMETTANT)
            .fonction(UPDATED_FONCTION)
            .designation(UPDATED_DESIGNATION)
            .observation(UPDATED_OBSERVATION)
            .statutSup(UPDATED_STATUT_SUP);

        restDemandeReparationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeReparations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeReparations))
            )
            .andExpect(status().isOk());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
        DemandeReparations testDemandeReparations = demandeReparationsList.get(demandeReparationsList.size() - 1);
        assertThat(testDemandeReparations.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeReparations.getIndentiteSoumettant()).isEqualTo(UPDATED_INDENTITE_SOUMETTANT);
        assertThat(testDemandeReparations.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testDemandeReparations.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testDemandeReparations.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testDemandeReparations.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void putNonExistingDemandeReparations() throws Exception {
        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();
        demandeReparations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeReparationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeReparations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeReparations() throws Exception {
        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();
        demandeReparations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeReparationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeReparations() throws Exception {
        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();
        demandeReparations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeReparationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeReparationsWithPatch() throws Exception {
        // Initialize the database
        demandeReparationsRepository.saveAndFlush(demandeReparations);

        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();

        // Update the demandeReparations using partial update
        DemandeReparations partialUpdatedDemandeReparations = new DemandeReparations();
        partialUpdatedDemandeReparations.setId(demandeReparations.getId());

        partialUpdatedDemandeReparations.date(UPDATED_DATE).fonction(UPDATED_FONCTION).statutSup(UPDATED_STATUT_SUP);

        restDemandeReparationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeReparations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeReparations))
            )
            .andExpect(status().isOk());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
        DemandeReparations testDemandeReparations = demandeReparationsList.get(demandeReparationsList.size() - 1);
        assertThat(testDemandeReparations.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeReparations.getIndentiteSoumettant()).isEqualTo(DEFAULT_INDENTITE_SOUMETTANT);
        assertThat(testDemandeReparations.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testDemandeReparations.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testDemandeReparations.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testDemandeReparations.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void fullUpdateDemandeReparationsWithPatch() throws Exception {
        // Initialize the database
        demandeReparationsRepository.saveAndFlush(demandeReparations);

        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();

        // Update the demandeReparations using partial update
        DemandeReparations partialUpdatedDemandeReparations = new DemandeReparations();
        partialUpdatedDemandeReparations.setId(demandeReparations.getId());

        partialUpdatedDemandeReparations
            .date(UPDATED_DATE)
            .indentiteSoumettant(UPDATED_INDENTITE_SOUMETTANT)
            .fonction(UPDATED_FONCTION)
            .designation(UPDATED_DESIGNATION)
            .observation(UPDATED_OBSERVATION)
            .statutSup(UPDATED_STATUT_SUP);

        restDemandeReparationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeReparations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeReparations))
            )
            .andExpect(status().isOk());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
        DemandeReparations testDemandeReparations = demandeReparationsList.get(demandeReparationsList.size() - 1);
        assertThat(testDemandeReparations.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeReparations.getIndentiteSoumettant()).isEqualTo(UPDATED_INDENTITE_SOUMETTANT);
        assertThat(testDemandeReparations.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testDemandeReparations.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testDemandeReparations.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testDemandeReparations.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeReparations() throws Exception {
        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();
        demandeReparations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeReparationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeReparations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeReparations() throws Exception {
        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();
        demandeReparations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeReparationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeReparations() throws Exception {
        int databaseSizeBeforeUpdate = demandeReparationsRepository.findAll().size();
        demandeReparations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeReparationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeReparations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeReparations in the database
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeReparations() throws Exception {
        // Initialize the database
        demandeReparationsRepository.saveAndFlush(demandeReparations);

        int databaseSizeBeforeDelete = demandeReparationsRepository.findAll().size();

        // Delete the demandeReparations
        restDemandeReparationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeReparations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeReparations> demandeReparationsList = demandeReparationsRepository.findAll();
        assertThat(demandeReparationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
