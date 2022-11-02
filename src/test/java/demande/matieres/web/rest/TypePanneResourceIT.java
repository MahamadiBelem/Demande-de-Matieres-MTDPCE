package demande.matieres.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.TypePanne;
import demande.matieres.repository.TypePanneRepository;
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
 * Integration tests for the {@link TypePanneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypePanneResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_PANNE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_PANNE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT_SUP = false;
    private static final Boolean UPDATED_STATUT_SUP = true;

    private static final String ENTITY_API_URL = "/api/type-pannes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePanneRepository typePanneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypePanneMockMvc;

    private TypePanne typePanne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePanne createEntity(EntityManager em) {
        TypePanne typePanne = new TypePanne().libelleTypePanne(DEFAULT_LIBELLE_TYPE_PANNE).statutSup(DEFAULT_STATUT_SUP);
        return typePanne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePanne createUpdatedEntity(EntityManager em) {
        TypePanne typePanne = new TypePanne().libelleTypePanne(UPDATED_LIBELLE_TYPE_PANNE).statutSup(UPDATED_STATUT_SUP);
        return typePanne;
    }

    @BeforeEach
    public void initTest() {
        typePanne = createEntity(em);
    }

    @Test
    @Transactional
    void createTypePanne() throws Exception {
        int databaseSizeBeforeCreate = typePanneRepository.findAll().size();
        // Create the TypePanne
        restTypePanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePanne)))
            .andExpect(status().isCreated());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeCreate + 1);
        TypePanne testTypePanne = typePanneList.get(typePanneList.size() - 1);
        assertThat(testTypePanne.getLibelleTypePanne()).isEqualTo(DEFAULT_LIBELLE_TYPE_PANNE);
        assertThat(testTypePanne.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void createTypePanneWithExistingId() throws Exception {
        // Create the TypePanne with an existing ID
        typePanne.setId(1L);

        int databaseSizeBeforeCreate = typePanneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePanne)))
            .andExpect(status().isBadRequest());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleTypePanneIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePanneRepository.findAll().size();
        // set the field null
        typePanne.setLibelleTypePanne(null);

        // Create the TypePanne, which fails.

        restTypePanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePanne)))
            .andExpect(status().isBadRequest());

        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypePannes() throws Exception {
        // Initialize the database
        typePanneRepository.saveAndFlush(typePanne);

        // Get all the typePanneList
        restTypePanneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePanne.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypePanne").value(hasItem(DEFAULT_LIBELLE_TYPE_PANNE)))
            .andExpect(jsonPath("$.[*].statutSup").value(hasItem(DEFAULT_STATUT_SUP.booleanValue())));
    }

    @Test
    @Transactional
    void getTypePanne() throws Exception {
        // Initialize the database
        typePanneRepository.saveAndFlush(typePanne);

        // Get the typePanne
        restTypePanneMockMvc
            .perform(get(ENTITY_API_URL_ID, typePanne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typePanne.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypePanne").value(DEFAULT_LIBELLE_TYPE_PANNE))
            .andExpect(jsonPath("$.statutSup").value(DEFAULT_STATUT_SUP.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTypePanne() throws Exception {
        // Get the typePanne
        restTypePanneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypePanne() throws Exception {
        // Initialize the database
        typePanneRepository.saveAndFlush(typePanne);

        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();

        // Update the typePanne
        TypePanne updatedTypePanne = typePanneRepository.findById(typePanne.getId()).get();
        // Disconnect from session so that the updates on updatedTypePanne are not directly saved in db
        em.detach(updatedTypePanne);
        updatedTypePanne.libelleTypePanne(UPDATED_LIBELLE_TYPE_PANNE).statutSup(UPDATED_STATUT_SUP);

        restTypePanneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypePanne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypePanne))
            )
            .andExpect(status().isOk());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
        TypePanne testTypePanne = typePanneList.get(typePanneList.size() - 1);
        assertThat(testTypePanne.getLibelleTypePanne()).isEqualTo(UPDATED_LIBELLE_TYPE_PANNE);
        assertThat(testTypePanne.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void putNonExistingTypePanne() throws Exception {
        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();
        typePanne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePanneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePanne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePanne))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypePanne() throws Exception {
        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();
        typePanne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePanneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePanne))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypePanne() throws Exception {
        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();
        typePanne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePanneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePanne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypePanneWithPatch() throws Exception {
        // Initialize the database
        typePanneRepository.saveAndFlush(typePanne);

        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();

        // Update the typePanne using partial update
        TypePanne partialUpdatedTypePanne = new TypePanne();
        partialUpdatedTypePanne.setId(typePanne.getId());

        restTypePanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePanne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePanne))
            )
            .andExpect(status().isOk());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
        TypePanne testTypePanne = typePanneList.get(typePanneList.size() - 1);
        assertThat(testTypePanne.getLibelleTypePanne()).isEqualTo(DEFAULT_LIBELLE_TYPE_PANNE);
        assertThat(testTypePanne.getStatutSup()).isEqualTo(DEFAULT_STATUT_SUP);
    }

    @Test
    @Transactional
    void fullUpdateTypePanneWithPatch() throws Exception {
        // Initialize the database
        typePanneRepository.saveAndFlush(typePanne);

        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();

        // Update the typePanne using partial update
        TypePanne partialUpdatedTypePanne = new TypePanne();
        partialUpdatedTypePanne.setId(typePanne.getId());

        partialUpdatedTypePanne.libelleTypePanne(UPDATED_LIBELLE_TYPE_PANNE).statutSup(UPDATED_STATUT_SUP);

        restTypePanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePanne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePanne))
            )
            .andExpect(status().isOk());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
        TypePanne testTypePanne = typePanneList.get(typePanneList.size() - 1);
        assertThat(testTypePanne.getLibelleTypePanne()).isEqualTo(UPDATED_LIBELLE_TYPE_PANNE);
        assertThat(testTypePanne.getStatutSup()).isEqualTo(UPDATED_STATUT_SUP);
    }

    @Test
    @Transactional
    void patchNonExistingTypePanne() throws Exception {
        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();
        typePanne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typePanne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePanne))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypePanne() throws Exception {
        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();
        typePanne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePanne))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypePanne() throws Exception {
        int databaseSizeBeforeUpdate = typePanneRepository.findAll().size();
        typePanne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePanneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typePanne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePanne in the database
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypePanne() throws Exception {
        // Initialize the database
        typePanneRepository.saveAndFlush(typePanne);

        int databaseSizeBeforeDelete = typePanneRepository.findAll().size();

        // Delete the typePanne
        restTypePanneMockMvc
            .perform(delete(ENTITY_API_URL_ID, typePanne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePanne> typePanneList = typePanneRepository.findAll();
        assertThat(typePanneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
