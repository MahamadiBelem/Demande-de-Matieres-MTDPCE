package demande.matieres.web.rest;

import static demande.matieres.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.CarnetVehicule;
import demande.matieres.domain.enumeration.Etatvehicule;
import demande.matieres.repository.CarnetVehiculeRepository;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link CarnetVehiculeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarnetVehiculeResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_IMMATRICULATION_VEHICULE = "AAAAAAAAAA";
    private static final String UPDATED_IMMATRICULATION_VEHICULE = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTITE_CONDUCTEUR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITE_CONDUCTEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOMBRE_REPARATION = 1;
    private static final Integer UPDATED_NOMBRE_REPARATION = 2;

    private static final LocalDate DEFAULT_DATE_DERNIERE_REVISION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DERNIERE_REVISION = LocalDate.now(ZoneId.systemDefault());

    private static final Etatvehicule DEFAULT_ETATVEHICULE = Etatvehicule.Operationel;
    private static final Etatvehicule UPDATED_ETATVEHICULE = Etatvehicule.NonOperationel;

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT_SUP = false;
    private static final Boolean UPDATED_STATUT_SUP = true;

    private static final String ENTITY_API_URL = "/api/carnet-vehicules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarnetVehiculeRepository carnetVehiculeRepository;

    @Mock
    private CarnetVehiculeRepository carnetVehiculeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarnetVehiculeMockMvc;

    private CarnetVehicule carnetVehicule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarnetVehicule createEntity(EntityManager em) {
        CarnetVehicule carnetVehicule = new CarnetVehicule()
            .date(DEFAULT_DATE)
            .immatriculationVehicule(DEFAULT_IMMATRICULATION_VEHICULE)
            .identiteConducteur(DEFAULT_IDENTITE_CONDUCTEUR)
            .nombreReparation(DEFAULT_NOMBRE_REPARATION)
            .dateDerniereRevision(DEFAULT_DATE_DERNIERE_REVISION)
            .etatvehicule(DEFAULT_ETATVEHICULE)
            .observations(DEFAULT_OBSERVATIONS)
            .statutSup(DEFAULT_STATUT_SUP);
        return carnetVehicule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarnetVehicule createUpdatedEntity(EntityManager em) {
        CarnetVehicule carnetVehicule = new CarnetVehicule()
            .date(UPDATED_DATE)
            .immatriculationVehicule(UPDATED_IMMATRICULATION_VEHICULE)
            .identiteConducteur(UPDATED_IDENTITE_CONDUCTEUR)
            .nombreReparation(UPDATED_NOMBRE_REPARATION)
            .dateDerniereRevision(UPDATED_DATE_DERNIERE_REVISION)
            .etatvehicule(UPDATED_ETATVEHICULE)
            .observations(UPDATED_OBSERVATIONS)
            .statutSup(UPDATED_STATUT_SUP);
        return carnetVehicule;
    }

    @BeforeEach
    public void initTest() {
        carnetVehicule = createEntity(em);
    }

    @Test
    @Transactional
    void createCarnetVehicule() throws Exception {
        int databaseSizeBeforeCreate = carnetVehiculeRepository.findAll().size();
        // Create the CarnetVehicule
        restCarnetVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isCreated());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        CarnetVehicule testCarnetVehicule = carnetVehiculeList.get(carnetVehiculeList.size() - 1);
        assertThat(testCarnetVehicule.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCarnetVehicule.getImmatriculationVehicule()).isEqualTo(DEFAULT_IMMATRICULATION_VEHICULE);
        assertThat(testCarnetVehicule.getIdentiteConducteur()).isEqualTo(DEFAULT_IDENTITE_CONDUCTEUR);
        assertThat(testCarnetVehicule.getNombreReparation()).isEqualTo(DEFAULT_NOMBRE_REPARATION);
        assertThat(testCarnetVehicule.getDateDerniereRevision()).isEqualTo(DEFAULT_DATE_DERNIERE_REVISION);
        assertThat(testCarnetVehicule.getEtatvehicule()).isEqualTo(DEFAULT_ETATVEHICULE);
        assertThat(testCarnetVehicule.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testCarnetVehicule.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void createCarnetVehiculeWithExistingId() throws Exception {
        // Create the CarnetVehicule with an existing ID
        carnetVehicule.setId(1L);

        int databaseSizeBeforeCreate = carnetVehiculeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarnetVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = carnetVehiculeRepository.findAll().size();
        // set the field null
        carnetVehicule.setDate(null);

        // Create the CarnetVehicule, which fails.

        restCarnetVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImmatriculationVehiculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = carnetVehiculeRepository.findAll().size();
        // set the field null
        carnetVehicule.setImmatriculationVehicule(null);

        // Create the CarnetVehicule, which fails.

        restCarnetVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentiteConducteurIsRequired() throws Exception {
        int databaseSizeBeforeTest = carnetVehiculeRepository.findAll().size();
        // set the field null
        carnetVehicule.setIdentiteConducteur(null);

        // Create the CarnetVehicule, which fails.

        restCarnetVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarnetVehicules() throws Exception {
        // Initialize the database
        carnetVehiculeRepository.saveAndFlush(carnetVehicule);

        // Get all the carnetVehiculeList
        restCarnetVehiculeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carnetVehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].immatriculationVehicule").value(hasItem(DEFAULT_IMMATRICULATION_VEHICULE)))
            .andExpect(jsonPath("$.[*].identiteConducteur").value(hasItem(DEFAULT_IDENTITE_CONDUCTEUR)))
            .andExpect(jsonPath("$.[*].nombreReparation").value(hasItem(DEFAULT_NOMBRE_REPARATION)))
            .andExpect(jsonPath("$.[*].dateDerniereRevision").value(hasItem(DEFAULT_DATE_DERNIERE_REVISION.toString())))
            .andExpect(jsonPath("$.[*].etatvehicule").value(hasItem(DEFAULT_ETATVEHICULE.toString())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].statutSup").value(hasItem(DEFAULT_STATUT_SUP.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarnetVehiculesWithEagerRelationshipsIsEnabled() throws Exception {
        when(carnetVehiculeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarnetVehiculeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carnetVehiculeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarnetVehiculesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carnetVehiculeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarnetVehiculeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carnetVehiculeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCarnetVehicule() throws Exception {
        // Initialize the database
        carnetVehiculeRepository.saveAndFlush(carnetVehicule);

        // Get the carnetVehicule
        restCarnetVehiculeMockMvc
            .perform(get(ENTITY_API_URL_ID, carnetVehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carnetVehicule.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.immatriculationVehicule").value(DEFAULT_IMMATRICULATION_VEHICULE))
            .andExpect(jsonPath("$.identiteConducteur").value(DEFAULT_IDENTITE_CONDUCTEUR))
            .andExpect(jsonPath("$.nombreReparation").value(DEFAULT_NOMBRE_REPARATION))
            .andExpect(jsonPath("$.dateDerniereRevision").value(DEFAULT_DATE_DERNIERE_REVISION.toString()))
            .andExpect(jsonPath("$.etatvehicule").value(DEFAULT_ETATVEHICULE.toString()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.statutSup").value(DEFAULT_STATUT_SUP.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCarnetVehicule() throws Exception {
        // Get the carnetVehicule
        restCarnetVehiculeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCarnetVehicule() throws Exception {
        // Initialize the database
        carnetVehiculeRepository.saveAndFlush(carnetVehicule);

        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();

        // Update the carnetVehicule
        CarnetVehicule updatedCarnetVehicule = carnetVehiculeRepository.findById(carnetVehicule.getId()).get();
        // Disconnect from session so that the updates on updatedCarnetVehicule are not directly saved in db
        em.detach(updatedCarnetVehicule);
        updatedCarnetVehicule
            .date(UPDATED_DATE)
            .immatriculationVehicule(UPDATED_IMMATRICULATION_VEHICULE)
            .identiteConducteur(UPDATED_IDENTITE_CONDUCTEUR)
            .nombreReparation(UPDATED_NOMBRE_REPARATION)
            .dateDerniereRevision(UPDATED_DATE_DERNIERE_REVISION)
            .etatvehicule(UPDATED_ETATVEHICULE)
            .observations(UPDATED_OBSERVATIONS)
            .statutSup(UPDATED_STATUT_SUP);

        restCarnetVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarnetVehicule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCarnetVehicule))
            )
            .andExpect(status().isOk());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
        CarnetVehicule testCarnetVehicule = carnetVehiculeList.get(carnetVehiculeList.size() - 1);
        assertThat(testCarnetVehicule.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCarnetVehicule.getImmatriculationVehicule()).isEqualTo(UPDATED_IMMATRICULATION_VEHICULE);
        assertThat(testCarnetVehicule.getIdentiteConducteur()).isEqualTo(UPDATED_IDENTITE_CONDUCTEUR);
        assertThat(testCarnetVehicule.getNombreReparation()).isEqualTo(UPDATED_NOMBRE_REPARATION);
        assertThat(testCarnetVehicule.getDateDerniereRevision()).isEqualTo(UPDATED_DATE_DERNIERE_REVISION);
        assertThat(testCarnetVehicule.getEtatvehicule()).isEqualTo(UPDATED_ETATVEHICULE);
        assertThat(testCarnetVehicule.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testCarnetVehicule.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void putNonExistingCarnetVehicule() throws Exception {
        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();
        carnetVehicule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarnetVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carnetVehicule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarnetVehicule() throws Exception {
        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();
        carnetVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarnetVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarnetVehicule() throws Exception {
        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();
        carnetVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarnetVehiculeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carnetVehicule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarnetVehiculeWithPatch() throws Exception {
        // Initialize the database
        carnetVehiculeRepository.saveAndFlush(carnetVehicule);

        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();

        // Update the carnetVehicule using partial update
        CarnetVehicule partialUpdatedCarnetVehicule = new CarnetVehicule();
        partialUpdatedCarnetVehicule.setId(carnetVehicule.getId());

        partialUpdatedCarnetVehicule
            .date(UPDATED_DATE)
            .nombreReparation(UPDATED_NOMBRE_REPARATION)
            .dateDerniereRevision(UPDATED_DATE_DERNIERE_REVISION)
            .etatvehicule(UPDATED_ETATVEHICULE)
            .statutSup(UPDATED_STATUT_SUP);

        restCarnetVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarnetVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarnetVehicule))
            )
            .andExpect(status().isOk());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
        CarnetVehicule testCarnetVehicule = carnetVehiculeList.get(carnetVehiculeList.size() - 1);
        assertThat(testCarnetVehicule.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCarnetVehicule.getImmatriculationVehicule()).isEqualTo(DEFAULT_IMMATRICULATION_VEHICULE);
        assertThat(testCarnetVehicule.getIdentiteConducteur()).isEqualTo(DEFAULT_IDENTITE_CONDUCTEUR);
        assertThat(testCarnetVehicule.getNombreReparation()).isEqualTo(UPDATED_NOMBRE_REPARATION);
        assertThat(testCarnetVehicule.getDateDerniereRevision()).isEqualTo(UPDATED_DATE_DERNIERE_REVISION);
        assertThat(testCarnetVehicule.getEtatvehicule()).isEqualTo(UPDATED_ETATVEHICULE);
        assertThat(testCarnetVehicule.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testCarnetVehicule.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void fullUpdateCarnetVehiculeWithPatch() throws Exception {
        // Initialize the database
        carnetVehiculeRepository.saveAndFlush(carnetVehicule);

        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();

        // Update the carnetVehicule using partial update
        CarnetVehicule partialUpdatedCarnetVehicule = new CarnetVehicule();
        partialUpdatedCarnetVehicule.setId(carnetVehicule.getId());

        partialUpdatedCarnetVehicule
            .date(UPDATED_DATE)
            .immatriculationVehicule(UPDATED_IMMATRICULATION_VEHICULE)
            .identiteConducteur(UPDATED_IDENTITE_CONDUCTEUR)
            .nombreReparation(UPDATED_NOMBRE_REPARATION)
            .dateDerniereRevision(UPDATED_DATE_DERNIERE_REVISION)
            .etatvehicule(UPDATED_ETATVEHICULE)
            .observations(UPDATED_OBSERVATIONS)
            .statutSup(UPDATED_STATUT_SUP);

        restCarnetVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarnetVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarnetVehicule))
            )
            .andExpect(status().isOk());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
        CarnetVehicule testCarnetVehicule = carnetVehiculeList.get(carnetVehiculeList.size() - 1);
        assertThat(testCarnetVehicule.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCarnetVehicule.getImmatriculationVehicule()).isEqualTo(UPDATED_IMMATRICULATION_VEHICULE);
        assertThat(testCarnetVehicule.getIdentiteConducteur()).isEqualTo(UPDATED_IDENTITE_CONDUCTEUR);
        assertThat(testCarnetVehicule.getNombreReparation()).isEqualTo(UPDATED_NOMBRE_REPARATION);
        assertThat(testCarnetVehicule.getDateDerniereRevision()).isEqualTo(UPDATED_DATE_DERNIERE_REVISION);
        assertThat(testCarnetVehicule.getEtatvehicule()).isEqualTo(UPDATED_ETATVEHICULE);
        assertThat(testCarnetVehicule.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testCarnetVehicule.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void patchNonExistingCarnetVehicule() throws Exception {
        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();
        carnetVehicule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarnetVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carnetVehicule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarnetVehicule() throws Exception {
        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();
        carnetVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarnetVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarnetVehicule() throws Exception {
        int databaseSizeBeforeUpdate = carnetVehiculeRepository.findAll().size();
        carnetVehicule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarnetVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carnetVehicule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarnetVehicule in the database
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarnetVehicule() throws Exception {
        // Initialize the database
        carnetVehiculeRepository.saveAndFlush(carnetVehicule);

        int databaseSizeBeforeDelete = carnetVehiculeRepository.findAll().size();

        // Delete the carnetVehicule
        restCarnetVehiculeMockMvc
            .perform(delete(ENTITY_API_URL_ID, carnetVehicule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarnetVehicule> carnetVehiculeList = carnetVehiculeRepository.findAll();
        assertThat(carnetVehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
