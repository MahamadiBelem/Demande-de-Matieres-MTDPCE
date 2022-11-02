package demande.matieres.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import demande.matieres.IntegrationTest;
import demande.matieres.domain.TypeMatiere;
import demande.matieres.repository.TypeMatiereRepository;
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
 * Integration tests for the {@link TypeMatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeMatiereResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_MATIERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeMatiereRepository typeMatiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeMatiereMockMvc;

    private TypeMatiere typeMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeMatiere createEntity(EntityManager em) {
        TypeMatiere typeMatiere = new TypeMatiere().libelleTypeMatiere(DEFAULT_LIBELLE_TYPE_MATIERE);
        return typeMatiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeMatiere createUpdatedEntity(EntityManager em) {
        TypeMatiere typeMatiere = new TypeMatiere().libelleTypeMatiere(UPDATED_LIBELLE_TYPE_MATIERE);
        return typeMatiere;
    }

    @BeforeEach
    public void initTest() {
        typeMatiere = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeMatiere() throws Exception {
        int databaseSizeBeforeCreate = typeMatiereRepository.findAll().size();
        // Create the TypeMatiere
        restTypeMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeMatiere)))
            .andExpect(status().isCreated());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeCreate + 1);
        TypeMatiere testTypeMatiere = typeMatiereList.get(typeMatiereList.size() - 1);
        assertThat(testTypeMatiere.getLibelleTypeMatiere()).isEqualTo(DEFAULT_LIBELLE_TYPE_MATIERE);
    }

    @Test
    @Transactional
    void createTypeMatiereWithExistingId() throws Exception {
        // Create the TypeMatiere with an existing ID
        typeMatiere.setId(1L);

        int databaseSizeBeforeCreate = typeMatiereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeMatiere)))
            .andExpect(status().isBadRequest());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleTypeMatiereIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeMatiereRepository.findAll().size();
        // set the field null
        typeMatiere.setLibelleTypeMatiere(null);

        // Create the TypeMatiere, which fails.

        restTypeMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeMatiere)))
            .andExpect(status().isBadRequest());

        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeMatieres() throws Exception {
        // Initialize the database
        typeMatiereRepository.saveAndFlush(typeMatiere);

        // Get all the typeMatiereList
        restTypeMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeMatiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeMatiere").value(hasItem(DEFAULT_LIBELLE_TYPE_MATIERE)));
    }

    @Test
    @Transactional
    void getTypeMatiere() throws Exception {
        // Initialize the database
        typeMatiereRepository.saveAndFlush(typeMatiere);

        // Get the typeMatiere
        restTypeMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, typeMatiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeMatiere.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeMatiere").value(DEFAULT_LIBELLE_TYPE_MATIERE));
    }

    @Test
    @Transactional
    void getNonExistingTypeMatiere() throws Exception {
        // Get the typeMatiere
        restTypeMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeMatiere() throws Exception {
        // Initialize the database
        typeMatiereRepository.saveAndFlush(typeMatiere);

        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();

        // Update the typeMatiere
        TypeMatiere updatedTypeMatiere = typeMatiereRepository.findById(typeMatiere.getId()).get();
        // Disconnect from session so that the updates on updatedTypeMatiere are not directly saved in db
        em.detach(updatedTypeMatiere);
        updatedTypeMatiere.libelleTypeMatiere(UPDATED_LIBELLE_TYPE_MATIERE);

        restTypeMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeMatiere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeMatiere))
            )
            .andExpect(status().isOk());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
        TypeMatiere testTypeMatiere = typeMatiereList.get(typeMatiereList.size() - 1);
        assertThat(testTypeMatiere.getLibelleTypeMatiere()).isEqualTo(UPDATED_LIBELLE_TYPE_MATIERE);
    }

    @Test
    @Transactional
    void putNonExistingTypeMatiere() throws Exception {
        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();
        typeMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeMatiere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeMatiere() throws Exception {
        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();
        typeMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeMatiere() throws Exception {
        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();
        typeMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMatiereMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeMatiere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeMatiereWithPatch() throws Exception {
        // Initialize the database
        typeMatiereRepository.saveAndFlush(typeMatiere);

        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();

        // Update the typeMatiere using partial update
        TypeMatiere partialUpdatedTypeMatiere = new TypeMatiere();
        partialUpdatedTypeMatiere.setId(typeMatiere.getId());

        restTypeMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeMatiere))
            )
            .andExpect(status().isOk());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
        TypeMatiere testTypeMatiere = typeMatiereList.get(typeMatiereList.size() - 1);
        assertThat(testTypeMatiere.getLibelleTypeMatiere()).isEqualTo(DEFAULT_LIBELLE_TYPE_MATIERE);
    }

    @Test
    @Transactional
    void fullUpdateTypeMatiereWithPatch() throws Exception {
        // Initialize the database
        typeMatiereRepository.saveAndFlush(typeMatiere);

        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();

        // Update the typeMatiere using partial update
        TypeMatiere partialUpdatedTypeMatiere = new TypeMatiere();
        partialUpdatedTypeMatiere.setId(typeMatiere.getId());

        partialUpdatedTypeMatiere.libelleTypeMatiere(UPDATED_LIBELLE_TYPE_MATIERE);

        restTypeMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeMatiere))
            )
            .andExpect(status().isOk());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
        TypeMatiere testTypeMatiere = typeMatiereList.get(typeMatiereList.size() - 1);
        assertThat(testTypeMatiere.getLibelleTypeMatiere()).isEqualTo(UPDATED_LIBELLE_TYPE_MATIERE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeMatiere() throws Exception {
        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();
        typeMatiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeMatiere() throws Exception {
        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();
        typeMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeMatiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeMatiere() throws Exception {
        int databaseSizeBeforeUpdate = typeMatiereRepository.findAll().size();
        typeMatiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeMatiere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeMatiere in the database
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeMatiere() throws Exception {
        // Initialize the database
        typeMatiereRepository.saveAndFlush(typeMatiere);

        int databaseSizeBeforeDelete = typeMatiereRepository.findAll().size();

        // Delete the typeMatiere
        restTypeMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeMatiere.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeMatiere> typeMatiereList = typeMatiereRepository.findAll();
        assertThat(typeMatiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
