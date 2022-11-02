package demande.matieres.web.rest;

import demande.matieres.domain.TypeMatiere;
import demande.matieres.repository.TypeMatiereRepository;
import demande.matieres.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link demande.matieres.domain.TypeMatiere}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeMatiereResource {

    private final Logger log = LoggerFactory.getLogger(TypeMatiereResource.class);

    private static final String ENTITY_NAME = "typeMatiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeMatiereRepository typeMatiereRepository;

    public TypeMatiereResource(TypeMatiereRepository typeMatiereRepository) {
        this.typeMatiereRepository = typeMatiereRepository;
    }

    /**
     * {@code POST  /type-matieres} : Create a new typeMatiere.
     *
     * @param typeMatiere the typeMatiere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeMatiere, or with status {@code 400 (Bad Request)} if the typeMatiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-matieres")
    public ResponseEntity<TypeMatiere> createTypeMatiere(@Valid @RequestBody TypeMatiere typeMatiere) throws URISyntaxException {
        log.debug("REST request to save TypeMatiere : {}", typeMatiere);
        if (typeMatiere.getId() != null) {
            throw new BadRequestAlertException("A new typeMatiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeMatiere result = typeMatiereRepository.save(typeMatiere);
        return ResponseEntity
            .created(new URI("/api/type-matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-matieres/:id} : Updates an existing typeMatiere.
     *
     * @param id the id of the typeMatiere to save.
     * @param typeMatiere the typeMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeMatiere,
     * or with status {@code 400 (Bad Request)} if the typeMatiere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-matieres/{id}")
    public ResponseEntity<TypeMatiere> updateTypeMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeMatiere typeMatiere
    ) throws URISyntaxException {
        log.debug("REST request to update TypeMatiere : {}, {}", id, typeMatiere);
        if (typeMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeMatiere result = typeMatiereRepository.save(typeMatiere);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeMatiere.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-matieres/:id} : Partial updates given fields of an existing typeMatiere, field will ignore if it is null
     *
     * @param id the id of the typeMatiere to save.
     * @param typeMatiere the typeMatiere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeMatiere,
     * or with status {@code 400 (Bad Request)} if the typeMatiere is not valid,
     * or with status {@code 404 (Not Found)} if the typeMatiere is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeMatiere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-matieres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeMatiere> partialUpdateTypeMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeMatiere typeMatiere
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeMatiere partially : {}, {}", id, typeMatiere);
        if (typeMatiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeMatiere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeMatiere> result = typeMatiereRepository
            .findById(typeMatiere.getId())
            .map(existingTypeMatiere -> {
                if (typeMatiere.getLibelleTypeMatiere() != null) {
                    existingTypeMatiere.setLibelleTypeMatiere(typeMatiere.getLibelleTypeMatiere());
                }

                return existingTypeMatiere;
            })
            .map(typeMatiereRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeMatiere.getId().toString())
        );
    }

    /**
     * {@code GET  /type-matieres} : get all the typeMatieres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeMatieres in body.
     */
    @GetMapping("/type-matieres")
    public List<TypeMatiere> getAllTypeMatieres() {
        log.debug("REST request to get all TypeMatieres");
        return typeMatiereRepository.findAll();
    }

    /**
     * {@code GET  /type-matieres/:id} : get the "id" typeMatiere.
     *
     * @param id the id of the typeMatiere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeMatiere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-matieres/{id}")
    public ResponseEntity<TypeMatiere> getTypeMatiere(@PathVariable Long id) {
        log.debug("REST request to get TypeMatiere : {}", id);
        Optional<TypeMatiere> typeMatiere = typeMatiereRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeMatiere);
    }

    /**
     * {@code DELETE  /type-matieres/:id} : delete the "id" typeMatiere.
     *
     * @param id the id of the typeMatiere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-matieres/{id}")
    public ResponseEntity<Void> deleteTypeMatiere(@PathVariable Long id) {
        log.debug("REST request to delete TypeMatiere : {}", id);
        typeMatiereRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
