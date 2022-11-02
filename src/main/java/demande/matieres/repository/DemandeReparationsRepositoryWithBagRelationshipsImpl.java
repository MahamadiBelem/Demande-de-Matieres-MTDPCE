package demande.matieres.repository;

import demande.matieres.domain.DemandeReparations;
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
public class DemandeReparationsRepositoryWithBagRelationshipsImpl implements DemandeReparationsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DemandeReparations> fetchBagRelationships(Optional<DemandeReparations> demandeReparations) {
        return demandeReparations.map(this::fetchTypeMatieres);
    }

    @Override
    public Page<DemandeReparations> fetchBagRelationships(Page<DemandeReparations> demandeReparations) {
        return new PageImpl<>(
            fetchBagRelationships(demandeReparations.getContent()),
            demandeReparations.getPageable(),
            demandeReparations.getTotalElements()
        );
    }

    @Override
    public List<DemandeReparations> fetchBagRelationships(List<DemandeReparations> demandeReparations) {
        return Optional.of(demandeReparations).map(this::fetchTypeMatieres).orElse(Collections.emptyList());
    }

    DemandeReparations fetchTypeMatieres(DemandeReparations result) {
        return entityManager
            .createQuery(
                "select demandeReparations from DemandeReparations demandeReparations left join fetch demandeReparations.typeMatieres where demandeReparations is :demandeReparations",
                DemandeReparations.class
            )
            .setParameter("demandeReparations", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DemandeReparations> fetchTypeMatieres(List<DemandeReparations> demandeReparations) {
        return entityManager
            .createQuery(
                "select distinct demandeReparations from DemandeReparations demandeReparations left join fetch demandeReparations.typeMatieres where demandeReparations in :demandeReparations",
                DemandeReparations.class
            )
            .setParameter("demandeReparations", demandeReparations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
