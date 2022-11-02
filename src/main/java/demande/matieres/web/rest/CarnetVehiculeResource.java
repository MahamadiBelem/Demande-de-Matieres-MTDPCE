package demande.matieres.web.rest;

import demande.matieres.domain.CarnetVehicule;
import demande.matieres.repository.CarnetVehiculeRepository;
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
 * REST controller for managing {@link demande.matieres.domain.CarnetVehicule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CarnetVehiculeResource {

    private final Logger log = LoggerFactory.getLogger(CarnetVehiculeResource.class);

    private static final String ENTITY_NAME = "carnetVehicule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarnetVehiculeRepository carnetVehiculeRepository;

    public CarnetVehiculeResource(CarnetVehiculeRepository carnetVehiculeRepository) {
        this.carnetVehiculeRepository = carnetVehiculeRepository;
    }

    /**
     * {@code POST  /carnet-vehicules} : Create a new carnetVehicule.
     *
     * @param carnetVehicule the carnetVehicule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carnetVehicule, or with status {@code 400 (Bad Request)} if the carnetVehicule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carnet-vehicules")
    public ResponseEntity<CarnetVehicule> createCarnetVehicule(@Valid @RequestBody CarnetVehicule carnetVehicule)
        throws URISyntaxException {
        log.debug("REST request to save CarnetVehicule : {}", carnetVehicule);
        if (carnetVehicule.getId() != null) {
            throw new BadRequestAlertException("A new carnetVehicule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarnetVehicule result = carnetVehiculeRepository.save(carnetVehicule);
        return ResponseEntity
            .created(new URI("/api/carnet-vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /carnet-vehicules/:id} : Updates an existing carnetVehicule.
     *
     * @param id the id of the carnetVehicule to save.
     * @param carnetVehicule the carnetVehicule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carnetVehicule,
     * or with status {@code 400 (Bad Request)} if the carnetVehicule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carnetVehicule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carnet-vehicules/{id}")
    public ResponseEntity<CarnetVehicule> updateCarnetVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarnetVehicule carnetVehicule
    ) throws URISyntaxException {
        log.debug("REST request to update CarnetVehicule : {}, {}", id, carnetVehicule);
        if (carnetVehicule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carnetVehicule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carnetVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarnetVehicule result = carnetVehiculeRepository.save(carnetVehicule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carnetVehicule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /carnet-vehicules/:id} : Partial updates given fields of an existing carnetVehicule, field will ignore if it is null
     *
     * @param id the id of the carnetVehicule to save.
     * @param carnetVehicule the carnetVehicule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carnetVehicule,
     * or with status {@code 400 (Bad Request)} if the carnetVehicule is not valid,
     * or with status {@code 404 (Not Found)} if the carnetVehicule is not found,
     * or with status {@code 500 (Internal Server Error)} if the carnetVehicule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carnet-vehicules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarnetVehicule> partialUpdateCarnetVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarnetVehicule carnetVehicule
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarnetVehicule partially : {}, {}", id, carnetVehicule);
        if (carnetVehicule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carnetVehicule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carnetVehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarnetVehicule> result = carnetVehiculeRepository
            .findById(carnetVehicule.getId())
            .map(existingCarnetVehicule -> {
                if (carnetVehicule.getDate() != null) {
                    existingCarnetVehicule.setDate(carnetVehicule.getDate());
                }
                if (carnetVehicule.getImmatriculationVehicule() != null) {
                    existingCarnetVehicule.setImmatriculationVehicule(carnetVehicule.getImmatriculationVehicule());
                }
                if (carnetVehicule.getIdentiteConducteur() != null) {
                    existingCarnetVehicule.setIdentiteConducteur(carnetVehicule.getIdentiteConducteur());
                }
                if (carnetVehicule.getNombreReparation() != null) {
                    existingCarnetVehicule.setNombreReparation(carnetVehicule.getNombreReparation());
                }
                if (carnetVehicule.getDateDerniereRevision() != null) {
                    existingCarnetVehicule.setDateDerniereRevision(carnetVehicule.getDateDerniereRevision());
                }
                if (carnetVehicule.getEtatvehicule() != null) {
                    existingCarnetVehicule.setEtatvehicule(carnetVehicule.getEtatvehicule());
                }
                if (carnetVehicule.getObservations() != null) {
                    existingCarnetVehicule.setObservations(carnetVehicule.getObservations());
                }
                if (carnetVehicule.getStatutSup() != null) {
                    existingCarnetVehicule.setStatutSup(carnetVehicule.getStatutSup());
                }

                return existingCarnetVehicule;
            })
            .map(carnetVehiculeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carnetVehicule.getId().toString())
        );
    }

    /**
     * {@code GET  /carnet-vehicules} : get all the carnetVehicules.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carnetVehicules in body.
     */
    @GetMapping("/carnet-vehicules")
    public List<CarnetVehicule> getAllCarnetVehicules(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CarnetVehicules");
        return carnetVehiculeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /carnet-vehicules/:id} : get the "id" carnetVehicule.
     *
     * @param id the id of the carnetVehicule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carnetVehicule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carnet-vehicules/{id}")
    public ResponseEntity<CarnetVehicule> getCarnetVehicule(@PathVariable Long id) {
        log.debug("REST request to get CarnetVehicule : {}", id);
        Optional<CarnetVehicule> carnetVehicule = carnetVehiculeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(carnetVehicule);
    }

    /**
     * {@code DELETE  /carnet-vehicules/:id} : delete the "id" carnetVehicule.
     *
     * @param id the id of the carnetVehicule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carnet-vehicules/{id}")
    public ResponseEntity<Void> deleteCarnetVehicule(@PathVariable Long id) {
        log.debug("REST request to delete CarnetVehicule : {}", id);
        carnetVehiculeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
