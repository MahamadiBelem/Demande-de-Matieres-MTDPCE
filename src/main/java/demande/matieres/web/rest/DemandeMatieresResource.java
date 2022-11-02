package demande.matieres.web.rest;

import demande.matieres.domain.DemandeMatieres;
import demande.matieres.repository.DemandeMatieresRepository;
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
 * REST controller for managing {@link demande.matieres.domain.DemandeMatieres}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DemandeMatieresResource {

    private final Logger log = LoggerFactory.getLogger(DemandeMatieresResource.class);

    private static final String ENTITY_NAME = "demandeMatieres";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeMatieresRepository demandeMatieresRepository;

    public DemandeMatieresResource(DemandeMatieresRepository demandeMatieresRepository) {
        this.demandeMatieresRepository = demandeMatieresRepository;
    }

    /**
     * {@code POST  /demande-matieres} : Create a new demandeMatieres.
     *
     * @param demandeMatieres the demandeMatieres to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeMatieres, or with status {@code 400 (Bad Request)} if the demandeMatieres has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-matieres")
    public ResponseEntity<DemandeMatieres> createDemandeMatieres(@Valid @RequestBody DemandeMatieres demandeMatieres)
        throws URISyntaxException {
        log.debug("REST request to save DemandeMatieres : {}", demandeMatieres);
        if (demandeMatieres.getId() != null) {
            throw new BadRequestAlertException("A new demandeMatieres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeMatieres result = demandeMatieresRepository.save(demandeMatieres);
        return ResponseEntity
            .created(new URI("/api/demande-matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-matieres/:id} : Updates an existing demandeMatieres.
     *
     * @param id the id of the demandeMatieres to save.
     * @param demandeMatieres the demandeMatieres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeMatieres,
     * or with status {@code 400 (Bad Request)} if the demandeMatieres is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeMatieres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-matieres/{id}")
    public ResponseEntity<DemandeMatieres> updateDemandeMatieres(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeMatieres demandeMatieres
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeMatieres : {}, {}", id, demandeMatieres);
        if (demandeMatieres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeMatieres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeMatieresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeMatieres result = demandeMatieresRepository.save(demandeMatieres);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeMatieres.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-matieres/:id} : Partial updates given fields of an existing demandeMatieres, field will ignore if it is null
     *
     * @param id the id of the demandeMatieres to save.
     * @param demandeMatieres the demandeMatieres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeMatieres,
     * or with status {@code 400 (Bad Request)} if the demandeMatieres is not valid,
     * or with status {@code 404 (Not Found)} if the demandeMatieres is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeMatieres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-matieres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeMatieres> partialUpdateDemandeMatieres(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeMatieres demandeMatieres
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeMatieres partially : {}, {}", id, demandeMatieres);
        if (demandeMatieres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeMatieres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeMatieresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeMatieres> result = demandeMatieresRepository
            .findById(demandeMatieres.getId())
            .map(existingDemandeMatieres -> {
                if (demandeMatieres.getDate() != null) {
                    existingDemandeMatieres.setDate(demandeMatieres.getDate());
                }
                if (demandeMatieres.getIndentiteSoumettant() != null) {
                    existingDemandeMatieres.setIndentiteSoumettant(demandeMatieres.getIndentiteSoumettant());
                }
                if (demandeMatieres.getFonction() != null) {
                    existingDemandeMatieres.setFonction(demandeMatieres.getFonction());
                }
                if (demandeMatieres.getDesignation() != null) {
                    existingDemandeMatieres.setDesignation(demandeMatieres.getDesignation());
                }
                if (demandeMatieres.getQuantite() != null) {
                    existingDemandeMatieres.setQuantite(demandeMatieres.getQuantite());
                }
                if (demandeMatieres.getObservation() != null) {
                    existingDemandeMatieres.setObservation(demandeMatieres.getObservation());
                }
                if (demandeMatieres.getStatutSup() != null) {
                    existingDemandeMatieres.setStatutSup(demandeMatieres.getStatutSup());
                }

                return existingDemandeMatieres;
            })
            .map(demandeMatieresRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeMatieres.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-matieres} : get all the demandeMatieres.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeMatieres in body.
     */
    @GetMapping("/demande-matieres")
    public List<DemandeMatieres> getAllDemandeMatieres(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DemandeMatieres");
        return demandeMatieresRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /demande-matieres/:id} : get the "id" demandeMatieres.
     *
     * @param id the id of the demandeMatieres to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeMatieres, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-matieres/{id}")
    public ResponseEntity<DemandeMatieres> getDemandeMatieres(@PathVariable Long id) {
        log.debug("REST request to get DemandeMatieres : {}", id);
        Optional<DemandeMatieres> demandeMatieres = demandeMatieresRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(demandeMatieres);
    }

    /**
     * {@code DELETE  /demande-matieres/:id} : delete the "id" demandeMatieres.
     *
     * @param id the id of the demandeMatieres to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-matieres/{id}")
    public ResponseEntity<Void> deleteDemandeMatieres(@PathVariable Long id) {
        log.debug("REST request to delete DemandeMatieres : {}", id);
        demandeMatieresRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
