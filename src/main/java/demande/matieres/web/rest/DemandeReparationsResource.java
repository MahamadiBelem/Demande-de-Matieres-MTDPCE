package demande.matieres.web.rest;

import demande.matieres.domain.DemandeReparations;
import demande.matieres.repository.DemandeReparationsRepository;
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
 * REST controller for managing {@link demande.matieres.domain.DemandeReparations}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DemandeReparationsResource {

    private final Logger log = LoggerFactory.getLogger(DemandeReparationsResource.class);

    private static final String ENTITY_NAME = "demandeReparations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeReparationsRepository demandeReparationsRepository;

    public DemandeReparationsResource(DemandeReparationsRepository demandeReparationsRepository) {
        this.demandeReparationsRepository = demandeReparationsRepository;
    }

    /**
     * {@code POST  /demande-reparations} : Create a new demandeReparations.
     *
     * @param demandeReparations the demandeReparations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeReparations, or with status {@code 400 (Bad Request)} if the demandeReparations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-reparations")
    public ResponseEntity<DemandeReparations> createDemandeReparations(@Valid @RequestBody DemandeReparations demandeReparations)
        throws URISyntaxException {
        log.debug("REST request to save DemandeReparations : {}", demandeReparations);
        if (demandeReparations.getId() != null) {
            throw new BadRequestAlertException("A new demandeReparations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeReparations result = demandeReparationsRepository.save(demandeReparations);
        return ResponseEntity
            .created(new URI("/api/demande-reparations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-reparations/:id} : Updates an existing demandeReparations.
     *
     * @param id the id of the demandeReparations to save.
     * @param demandeReparations the demandeReparations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeReparations,
     * or with status {@code 400 (Bad Request)} if the demandeReparations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeReparations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-reparations/{id}")
    public ResponseEntity<DemandeReparations> updateDemandeReparations(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeReparations demandeReparations
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeReparations : {}, {}", id, demandeReparations);
        if (demandeReparations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeReparations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeReparationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeReparations result = demandeReparationsRepository.save(demandeReparations);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeReparations.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-reparations/:id} : Partial updates given fields of an existing demandeReparations, field will ignore if it is null
     *
     * @param id the id of the demandeReparations to save.
     * @param demandeReparations the demandeReparations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeReparations,
     * or with status {@code 400 (Bad Request)} if the demandeReparations is not valid,
     * or with status {@code 404 (Not Found)} if the demandeReparations is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeReparations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-reparations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeReparations> partialUpdateDemandeReparations(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeReparations demandeReparations
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeReparations partially : {}, {}", id, demandeReparations);
        if (demandeReparations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeReparations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeReparationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeReparations> result = demandeReparationsRepository
            .findById(demandeReparations.getId())
            .map(existingDemandeReparations -> {
                if (demandeReparations.getDate() != null) {
                    existingDemandeReparations.setDate(demandeReparations.getDate());
                }
                if (demandeReparations.getIndentiteSoumettant() != null) {
                    existingDemandeReparations.setIndentiteSoumettant(demandeReparations.getIndentiteSoumettant());
                }
                if (demandeReparations.getFonction() != null) {
                    existingDemandeReparations.setFonction(demandeReparations.getFonction());
                }
                if (demandeReparations.getDesignation() != null) {
                    existingDemandeReparations.setDesignation(demandeReparations.getDesignation());
                }
                if (demandeReparations.getObservation() != null) {
                    existingDemandeReparations.setObservation(demandeReparations.getObservation());
                }
                if (demandeReparations.getStatutSup() != null) {
                    existingDemandeReparations.setStatutSup(demandeReparations.getStatutSup());
                }

                return existingDemandeReparations;
            })
            .map(demandeReparationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeReparations.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-reparations} : get all the demandeReparations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeReparations in body.
     */
    @GetMapping("/demande-reparations")
    public List<DemandeReparations> getAllDemandeReparations(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DemandeReparations");
        return demandeReparationsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /demande-reparations/:id} : get the "id" demandeReparations.
     *
     * @param id the id of the demandeReparations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeReparations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-reparations/{id}")
    public ResponseEntity<DemandeReparations> getDemandeReparations(@PathVariable Long id) {
        log.debug("REST request to get DemandeReparations : {}", id);
        Optional<DemandeReparations> demandeReparations = demandeReparationsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(demandeReparations);
    }

    /**
     * {@code DELETE  /demande-reparations/:id} : delete the "id" demandeReparations.
     *
     * @param id the id of the demandeReparations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-reparations/{id}")
    public ResponseEntity<Void> deleteDemandeReparations(@PathVariable Long id) {
        log.debug("REST request to delete DemandeReparations : {}", id);
        demandeReparationsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
