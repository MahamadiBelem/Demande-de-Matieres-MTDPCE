package demande.matieres.web.rest;

import demande.matieres.domain.TypePanne;
import demande.matieres.repository.TypePanneRepository;
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
 * REST controller for managing {@link demande.matieres.domain.TypePanne}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypePanneResource {

    private final Logger log = LoggerFactory.getLogger(TypePanneResource.class);

    private static final String ENTITY_NAME = "typePanne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePanneRepository typePanneRepository;

    public TypePanneResource(TypePanneRepository typePanneRepository) {
        this.typePanneRepository = typePanneRepository;
    }

    /**
     * {@code POST  /type-pannes} : Create a new typePanne.
     *
     * @param typePanne the typePanne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePanne, or with status {@code 400 (Bad Request)} if the typePanne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-pannes")
    public ResponseEntity<TypePanne> createTypePanne(@Valid @RequestBody TypePanne typePanne) throws URISyntaxException {
        log.debug("REST request to save TypePanne : {}", typePanne);
        if (typePanne.getId() != null) {
            throw new BadRequestAlertException("A new typePanne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePanne result = typePanneRepository.save(typePanne);
        return ResponseEntity
            .created(new URI("/api/type-pannes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-pannes/:id} : Updates an existing typePanne.
     *
     * @param id the id of the typePanne to save.
     * @param typePanne the typePanne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePanne,
     * or with status {@code 400 (Bad Request)} if the typePanne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePanne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-pannes/{id}")
    public ResponseEntity<TypePanne> updateTypePanne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypePanne typePanne
    ) throws URISyntaxException {
        log.debug("REST request to update TypePanne : {}, {}", id, typePanne);
        if (typePanne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePanne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePanneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypePanne result = typePanneRepository.save(typePanne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typePanne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-pannes/:id} : Partial updates given fields of an existing typePanne, field will ignore if it is null
     *
     * @param id the id of the typePanne to save.
     * @param typePanne the typePanne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePanne,
     * or with status {@code 400 (Bad Request)} if the typePanne is not valid,
     * or with status {@code 404 (Not Found)} if the typePanne is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePanne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-pannes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypePanne> partialUpdateTypePanne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypePanne typePanne
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePanne partially : {}, {}", id, typePanne);
        if (typePanne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePanne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePanneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypePanne> result = typePanneRepository
            .findById(typePanne.getId())
            .map(existingTypePanne -> {
                if (typePanne.getLibelleTypePanne() != null) {
                    existingTypePanne.setLibelleTypePanne(typePanne.getLibelleTypePanne());
                }
                if (typePanne.getStatutSup() != null) {
                    existingTypePanne.setStatutSup(typePanne.getStatutSup());
                }

                return existingTypePanne;
            })
            .map(typePanneRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typePanne.getId().toString())
        );
    }

    /**
     * {@code GET  /type-pannes} : get all the typePannes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePannes in body.
     */
    @GetMapping("/type-pannes")
    public List<TypePanne> getAllTypePannes() {
        log.debug("REST request to get all TypePannes");
        return typePanneRepository.findAll();
    }

    /**
     * {@code GET  /type-pannes/:id} : get the "id" typePanne.
     *
     * @param id the id of the typePanne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePanne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-pannes/{id}")
    public ResponseEntity<TypePanne> getTypePanne(@PathVariable Long id) {
        log.debug("REST request to get TypePanne : {}", id);
        Optional<TypePanne> typePanne = typePanneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typePanne);
    }

    /**
     * {@code DELETE  /type-pannes/:id} : delete the "id" typePanne.
     *
     * @param id the id of the typePanne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-pannes/{id}")
    public ResponseEntity<Void> deleteTypePanne(@PathVariable Long id) {
        log.debug("REST request to delete TypePanne : {}", id);
        typePanneRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
