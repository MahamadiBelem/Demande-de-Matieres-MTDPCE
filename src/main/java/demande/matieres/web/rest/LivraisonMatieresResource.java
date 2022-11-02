package demande.matieres.web.rest;

import demande.matieres.domain.LivraisonMatieres;
import demande.matieres.repository.LivraisonMatieresRepository;
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
 * REST controller for managing {@link demande.matieres.domain.LivraisonMatieres}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LivraisonMatieresResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonMatieresResource.class);

    private static final String ENTITY_NAME = "livraisonMatieres";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivraisonMatieresRepository livraisonMatieresRepository;

    public LivraisonMatieresResource(LivraisonMatieresRepository livraisonMatieresRepository) {
        this.livraisonMatieresRepository = livraisonMatieresRepository;
    }

    /**
     * {@code POST  /livraison-matieres} : Create a new livraisonMatieres.
     *
     * @param livraisonMatieres the livraisonMatieres to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livraisonMatieres, or with status {@code 400 (Bad Request)} if the livraisonMatieres has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livraison-matieres")
    public ResponseEntity<LivraisonMatieres> createLivraisonMatieres(@Valid @RequestBody LivraisonMatieres livraisonMatieres)
        throws URISyntaxException {
        log.debug("REST request to save LivraisonMatieres : {}", livraisonMatieres);
        if (livraisonMatieres.getId() != null) {
            throw new BadRequestAlertException("A new livraisonMatieres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LivraisonMatieres result = livraisonMatieresRepository.save(livraisonMatieres);
        return ResponseEntity
            .created(new URI("/api/livraison-matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livraison-matieres/:id} : Updates an existing livraisonMatieres.
     *
     * @param id the id of the livraisonMatieres to save.
     * @param livraisonMatieres the livraisonMatieres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonMatieres,
     * or with status {@code 400 (Bad Request)} if the livraisonMatieres is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livraisonMatieres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livraison-matieres/{id}")
    public ResponseEntity<LivraisonMatieres> updateLivraisonMatieres(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivraisonMatieres livraisonMatieres
    ) throws URISyntaxException {
        log.debug("REST request to update LivraisonMatieres : {}, {}", id, livraisonMatieres);
        if (livraisonMatieres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livraisonMatieres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livraisonMatieresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LivraisonMatieres result = livraisonMatieresRepository.save(livraisonMatieres);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, livraisonMatieres.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /livraison-matieres/:id} : Partial updates given fields of an existing livraisonMatieres, field will ignore if it is null
     *
     * @param id the id of the livraisonMatieres to save.
     * @param livraisonMatieres the livraisonMatieres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonMatieres,
     * or with status {@code 400 (Bad Request)} if the livraisonMatieres is not valid,
     * or with status {@code 404 (Not Found)} if the livraisonMatieres is not found,
     * or with status {@code 500 (Internal Server Error)} if the livraisonMatieres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livraison-matieres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivraisonMatieres> partialUpdateLivraisonMatieres(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivraisonMatieres livraisonMatieres
    ) throws URISyntaxException {
        log.debug("REST request to partial update LivraisonMatieres partially : {}, {}", id, livraisonMatieres);
        if (livraisonMatieres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livraisonMatieres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livraisonMatieresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivraisonMatieres> result = livraisonMatieresRepository
            .findById(livraisonMatieres.getId())
            .map(existingLivraisonMatieres -> {
                if (livraisonMatieres.getDesignationMatiere() != null) {
                    existingLivraisonMatieres.setDesignationMatiere(livraisonMatieres.getDesignationMatiere());
                }
                if (livraisonMatieres.getQuantiteLivree() != null) {
                    existingLivraisonMatieres.setQuantiteLivree(livraisonMatieres.getQuantiteLivree());
                }
                if (livraisonMatieres.getDateLivree() != null) {
                    existingLivraisonMatieres.setDateLivree(livraisonMatieres.getDateLivree());
                }
                if (livraisonMatieres.getStatutSup() != null) {
                    existingLivraisonMatieres.setStatutSup(livraisonMatieres.getStatutSup());
                }

                return existingLivraisonMatieres;
            })
            .map(livraisonMatieresRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, livraisonMatieres.getId().toString())
        );
    }

    /**
     * {@code GET  /livraison-matieres} : get all the livraisonMatieres.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livraisonMatieres in body.
     */
    @GetMapping("/livraison-matieres")
    public List<LivraisonMatieres> getAllLivraisonMatieres(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LivraisonMatieres");
        return livraisonMatieresRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /livraison-matieres/:id} : get the "id" livraisonMatieres.
     *
     * @param id the id of the livraisonMatieres to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livraisonMatieres, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livraison-matieres/{id}")
    public ResponseEntity<LivraisonMatieres> getLivraisonMatieres(@PathVariable Long id) {
        log.debug("REST request to get LivraisonMatieres : {}", id);
        Optional<LivraisonMatieres> livraisonMatieres = livraisonMatieresRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(livraisonMatieres);
    }

    /**
     * {@code DELETE  /livraison-matieres/:id} : delete the "id" livraisonMatieres.
     *
     * @param id the id of the livraisonMatieres to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livraison-matieres/{id}")
    public ResponseEntity<Void> deleteLivraisonMatieres(@PathVariable Long id) {
        log.debug("REST request to delete LivraisonMatieres : {}", id);
        livraisonMatieresRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
