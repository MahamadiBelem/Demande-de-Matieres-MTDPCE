package demande.matieres.web.rest;

import demande.matieres.domain.RevisionVehicule;
import demande.matieres.repository.RevisionVehiculeRepository;
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
 * REST controller for managing {@link demande.matieres.domain.RevisionVehicule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RevisionVehiculeResource {

    private final Logger log = LoggerFactory.getLogger(RevisionVehiculeResource.class);

    private static final String ENTITY_NAME = "revisionVehicule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RevisionVehiculeRepository revisionVehiculeRepository;

    public RevisionVehiculeResource(RevisionVehiculeRepository revisionVehiculeRepository) {
        this.revisionVehiculeRepository = revisionVehiculeRepository;
    }

    /**
     * {@code POST  /revision-vehicules} : Create a new revisionVehicule.
     *
     * @param revisionVehicule the revisionVehicule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new revisionVehicule, or with status {@code 400 (Bad Request)} if the revisionVehicule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/revision-vehicules")
    public ResponseEntity<RevisionVehicule> createRevisionVehicule(@Valid @RequestBody RevisionVehicule revisionVehicule)
        throws URISyntaxException {
        log.debug("REST request to save RevisionVehicule : {}", revisionVehicule);
        if (revisionVehicule.getId() != null) {
            throw new BadRequestAlertException("A new revisionVehicule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RevisionVehicule result = revisionVehiculeRepository.save(revisionVehicule);
        return ResponseEntity
            .created(new URI("/api/revision-vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /revision-vehicules/:id} : Updates an existing revisionVehicule.
     *
     * @param id the id of the revisionVehicule to save.
     * @param revisionVehicule the revisionVehicule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revisionVehicule,
     * or with status {@code 400 (Bad Request)} if the revisionVehicule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the revisionVehicule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/revision-vehicules/{id}")
    public ResponseEntity<RevisionVehicule> updateRevisionVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RevisionVehicule revisionVehicule
    ) throws URISyntaxException {
        log.debug("REST request to update RevisionVehicule : {}, {}", id, revisionVehicule);
        if (revisionVehicule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revisionVehicule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revisionVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RevisionVehicule result = revisionVehiculeRepository.save(revisionVehicule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, revisionVehicule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /revision-vehicules/:id} : Partial updates given fields of an existing revisionVehicule, field will ignore if it is null
     *
     * @param id the id of the revisionVehicule to save.
     * @param revisionVehicule the revisionVehicule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revisionVehicule,
     * or with status {@code 400 (Bad Request)} if the revisionVehicule is not valid,
     * or with status {@code 404 (Not Found)} if the revisionVehicule is not found,
     * or with status {@code 500 (Internal Server Error)} if the revisionVehicule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/revision-vehicules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RevisionVehicule> partialUpdateRevisionVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RevisionVehicule revisionVehicule
    ) throws URISyntaxException {
        log.debug("REST request to partial update RevisionVehicule partially : {}, {}", id, revisionVehicule);
        if (revisionVehicule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revisionVehicule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revisionVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RevisionVehicule> result = revisionVehiculeRepository
            .findById(revisionVehicule.getId())
            .map(existingRevisionVehicule -> {
                if (revisionVehicule.getLibelleRevisionVehicule() != null) {
                    existingRevisionVehicule.setLibelleRevisionVehicule(revisionVehicule.getLibelleRevisionVehicule());
                }

                return existingRevisionVehicule;
            })
            .map(revisionVehiculeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, revisionVehicule.getId().toString())
        );
    }

    /**
     * {@code GET  /revision-vehicules} : get all the revisionVehicules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revisionVehicules in body.
     */
    @GetMapping("/revision-vehicules")
    public List<RevisionVehicule> getAllRevisionVehicules() {
        log.debug("REST request to get all RevisionVehicules");
        return revisionVehiculeRepository.findAll();
    }

    /**
     * {@code GET  /revision-vehicules/:id} : get the "id" revisionVehicule.
     *
     * @param id the id of the revisionVehicule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the revisionVehicule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/revision-vehicules/{id}")
    public ResponseEntity<RevisionVehicule> getRevisionVehicule(@PathVariable Long id) {
        log.debug("REST request to get RevisionVehicule : {}", id);
        Optional<RevisionVehicule> revisionVehicule = revisionVehiculeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(revisionVehicule);
    }

    /**
     * {@code DELETE  /revision-vehicules/:id} : delete the "id" revisionVehicule.
     *
     * @param id the id of the revisionVehicule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revision-vehicules/{id}")
    public ResponseEntity<Void> deleteRevisionVehicule(@PathVariable Long id) {
        log.debug("REST request to delete RevisionVehicule : {}", id);
        revisionVehiculeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
