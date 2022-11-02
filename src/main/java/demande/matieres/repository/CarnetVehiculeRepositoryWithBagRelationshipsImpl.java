package demande.matieres.repository;

import demande.matieres.domain.CarnetVehicule;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CarnetVehiculeRepositoryWithBagRelationshipsImpl implements CarnetVehiculeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CarnetVehicule> fetchBagRelationships(Optional<CarnetVehicule> carnetVehicule) {
        return carnetVehicule.map(this::fetchStructures);
    }

    @Override
    public Page<CarnetVehicule> fetchBagRelationships(Page<CarnetVehicule> carnetVehicules) {
        return new PageImpl<>(
            fetchBagRelationships(carnetVehicules.getContent()),
            carnetVehicules.getPageable(),
            carnetVehicules.getTotalElements()
        );
    }

    @Override
    public List<CarnetVehicule> fetchBagRelationships(List<CarnetVehicule> carnetVehicules) {
        return Optional.of(carnetVehicules).map(this::fetchStructures).orElse(Collections.emptyList());
    }

    CarnetVehicule fetchStructures(CarnetVehicule result) {
        return entityManager
            .createQuery(
                "select carnetVehicule from CarnetVehicule carnetVehicule left join fetch carnetVehicule.structures where carnetVehicule is :carnetVehicule",
                CarnetVehicule.class
            )
            .setParameter("carnetVehicule", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<CarnetVehicule> fetchStructures(List<CarnetVehicule> carnetVehicules) {
        return entityManager
            .createQuery(
                "select distinct carnetVehicule from CarnetVehicule carnetVehicule left join fetch carnetVehicule.structures where carnetVehicule in :carnetVehicules",
                CarnetVehicule.class
            )
            .setParameter("carnetVehicules", carnetVehicules)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
