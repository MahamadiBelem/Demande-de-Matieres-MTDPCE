package demande.matieres.web.rest;

import demande.matieres.domain.Matieres;
import demande.matieres.repository.MatieresRepository;
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
 * REST controller for managing {@link demande.matieres.domain.Matieres}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MatieresResource {

    private final Logger log = LoggerFactory.getLogger(MatieresResource.class);

    private static final String ENTITY_NAME = "matieres";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatieresRepository matieresRepository;

    public MatieresResource(MatieresRepository matieresRepository) {
        this.matieresRepository = matieresRepository;
    }

    /**
     * {@code POST  /matieres} : Create a new matieres.
     *
     * @param matieres the matieres to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matieres, or with status {@code 400 (Bad Request)} if the matieres has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/matieres")
    public ResponseEntity<Matieres> createMatieres(@Valid @RequestBody Matieres matieres) throws URISyntaxException {
        log.debug("REST request to save Matieres : {}", matieres);
        if (matieres.getId() != null) {
            throw new BadRequestAlertException("A new matieres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Matieres result = matieresRepository.save(matieres);
        return ResponseEntity
            .created(new URI("/api/matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /matieres/:id} : Updates an existing matieres.
     *
     * @param id the id of the matieres to save.
     * @param matieres the matieres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matieres,
     * or with status {@code 400 (Bad Request)} if the matieres is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matieres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/matieres/{id}")
    public ResponseEntity<Matieres> updateMatieres(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Matieres matieres
    ) throws URISyntaxException {
        log.debug("REST request to update Matieres : {}, {}", id, matieres);
        if (matieres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matieres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matieresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Matieres result = matieresRepository.save(matieres);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, matieres.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /matieres/:id} : Partial updates given fields of an existing matieres, field will ignore if it is null
     *
     * @param id the id of the matieres to save.
     * @param matieres the matieres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matieres,
     * or with status {@code 400 (Bad Request)} if the matieres is not valid,
     * or with status {@code 404 (Not Found)} if the matieres is not found,
     * or with status {@code 500 (Internal Server Error)} if the matieres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/matieres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Matieres> partialUpdateMatieres(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Matieres matieres
    ) throws URISyntaxException {
        log.debug("REST request to partial update Matieres partially : {}, {}", id, matieres);
        if (matieres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matieres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matieresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Matieres> result = matieresRepository
            .findById(matieres.getId())
            .map(existingMatieres -> {
                if (matieres.getDesignationMatieres() != null) {
                    existingMatieres.setDesignationMatieres(matieres.getDesignationMatieres());
                }
                if (matieres.getQuantiteMatieres() != null) {
                    existingMatieres.setQuantiteMatieres(matieres.getQuantiteMatieres());
                }
                if (matieres.getCaracteristiquesMatieres() != null) {
                    existingMatieres.setCaracteristiquesMatieres(matieres.getCaracteristiquesMatieres());
                }
                if (matieres.getStatutSup() != null) {
                    existingMatieres.setStatutSup(matieres.getStatutSup());
                }

                return existingMatieres;
            })
            .map(matieresRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, matieres.getId().toString())
        );
    }

    /**
     * {@code GET  /matieres} : get all the matieres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matieres in body.
     */
    @GetMapping("/matieres")
    public List<Matieres> getAllMatieres() {
        log.debug("REST request to get all Matieres");
        return matieresRepository.findAll();
    }

    /**
     * {@code GET  /matieres/:id} : get the "id" matieres.
     *
     * @param id the id of the matieres to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matieres, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/matieres/{id}")
    public ResponseEntity<Matieres> getMatieres(@PathVariable Long id) {
        log.debug("REST request to get Matieres : {}", id);
        Optional<Matieres> matieres = matieresRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(matieres);
    }

    /**
     * {@code DELETE  /matieres/:id} : delete the "id" matieres.
     *
     * @param id the id of the matieres to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/matieres/{id}")
    public ResponseEntity<Void> deleteMatieres(@PathVariable Long id) {
        log.debug("REST request to delete Matieres : {}", id);
        matieresRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
