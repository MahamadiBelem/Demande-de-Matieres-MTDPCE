package demande.matieres.web.rest;

import demande.matieres.domain.MarqueVehicule;
import demande.matieres.repository.MarqueVehiculeRepository;
import demande.matieres.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link demande.matieres.domain.MarqueVehicule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MarqueVehiculeResource {

    private final Logger log = LoggerFactory.getLogger(MarqueVehiculeResource.class);

    private static final String ENTITY_NAME = "marqueVehicule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarqueVehiculeRepository marqueVehiculeRepository;

    public MarqueVehiculeResource(MarqueVehiculeRepository marqueVehiculeRepository) {
        this.marqueVehiculeRepository = marqueVehiculeRepository;
    }

    /**
     * {@code POST  /marque-vehicules} : Create a new marqueVehicule.
     *
     * @param marqueVehicule the marqueVehicule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marqueVehicule, or with status {@code 400 (Bad Request)} if the marqueVehicule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marque-vehicules")
    public ResponseEntity<MarqueVehicule> createMarqueVehicule(@Valid @RequestBody MarqueVehicule marqueVehicule)
        throws URISyntaxException {
        log.debug("REST request to save MarqueVehicule : {}", marqueVehicule);
        if (marqueVehicule.getId() != null) {
            throw new BadRequestAlertException("A new marqueVehicule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarqueVehicule result = marqueVehiculeRepository.save(marqueVehicule);
        return ResponseEntity
            .created(new URI("/api/marque-vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marque-vehicules/:id} : Updates an existing marqueVehicule.
     *
     * @param id the id of the marqueVehicule to save.
     * @param marqueVehicule the marqueVehicule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marqueVehicule,
     * or with status {@code 400 (Bad Request)} if the marqueVehicule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marqueVehicule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marque-vehicules/{id}")
    public ResponseEntity<MarqueVehicule> updateMarqueVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarqueVehicule marqueVehicule
    ) throws URISyntaxException {
        log.debug("REST request to update MarqueVehicule : {}, {}", id, marqueVehicule);
        if (marqueVehicule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marqueVehicule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marqueVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarqueVehicule result = marqueVehiculeRepository.save(marqueVehicule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marqueVehicule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /marque-vehicules/:id} : Partial updates given fields of an existing marqueVehicule, field will ignore if it is null
     *
     * @param id the id of the marqueVehicule to save.
     * @param marqueVehicule the marqueVehicule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marqueVehicule,
     * or with status {@code 400 (Bad Request)} if the marqueVehicule is not valid,
     * or with status {@code 404 (Not Found)} if the marqueVehicule is not found,
     * or with status {@code 500 (Internal Server Error)} if the marqueVehicule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marque-vehicules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarqueVehicule> partialUpdateMarqueVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarqueVehicule marqueVehicule
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarqueVehicule partially : {}, {}", id, marqueVehicule);
        if (marqueVehicule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marqueVehicule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marqueVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarqueVehicule> result = marqueVehiculeRepository
            .findById(marqueVehicule.getId())
            .map(existingMarqueVehicule -> {
                if (marqueVehicule.getLibelleMarqueVehicule() != null) {
                    existingMarqueVehicule.setLibelleMarqueVehicule(marqueVehicule.getLibelleMarqueVehicule());
                }

                return existingMarqueVehicule;
            })
            .map(marqueVehiculeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marqueVehicule.getId().toString())
        );
    }

    /**
     * {@code GET  /marque-vehicules} : get all the marqueVehicules.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marqueVehicules in body.
     */
    @GetMapping("/marque-vehicules")
    public List<MarqueVehicule> getAllMarqueVehicules(@RequestParam(required = false) String filter) {
        if ("carnetvehicule-is-null".equals(filter)) {
            log.debug("REST request to get all MarqueVehicules where carnetVehicule is null");
            return StreamSupport
                .stream(marqueVehiculeRepository.findAll().spliterator(), false)
                .filter(marqueVehicule -> marqueVehicule.getCarnetVehicule() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all MarqueVehicules");
        return marqueVehiculeRepository.findAll();
    }

    /**
     * {@code GET  /marque-vehicules/:id} : get the "id" marqueVehicule.
     *
     * @param id the id of the marqueVehicule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marqueVehicule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marque-vehicules/{id}")
    public ResponseEntity<MarqueVehicule> getMarqueVehicule(@PathVariable Long id) {
        log.debug("REST request to get MarqueVehicule : {}", id);
        Optional<MarqueVehicule> marqueVehicule = marqueVehiculeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marqueVehicule);
    }

    /**
     * {@code DELETE  /marque-vehicules/:id} : delete the "id" marqueVehicule.
     *
     * @param id the id of the marqueVehicule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marque-vehicules/{id}")
    public ResponseEntity<Void> deleteMarqueVehicule(@PathVariable Long id) {
        log.debug("REST request to delete MarqueVehicule : {}", id);
        marqueVehiculeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
