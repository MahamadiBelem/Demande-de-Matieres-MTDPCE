package demande.matieres.repository;

import demande.matieres.domain.Structure;
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
public class StructureRepositoryWithBagRelationshipsImpl implements StructureRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Structure> fetchBagRelationships(Optional<Structure> structure) {
        return structure.map(this::fetchRelationstructurematieres);
    }

    @Override
    public Page<Structure> fetchBagRelationships(Page<Structure> structures) {
        return new PageImpl<>(fetchBagRelationships(structures.getContent()), structures.getPageable(), structures.getTotalElements());
    }

    @Override
    public List<Structure> fetchBagRelationships(List<Structure> structures) {
        return Optional.of(structures).map(this::fetchRelationstructurematieres).orElse(Collections.emptyList());
    }

    Structure fetchRelationstructurematieres(Structure result) {
        return entityManager
            .createQuery(
                "select structure from Structure structure left join fetch structure.relationstructurematieres where structure is :structure",
                Structure.class
            )
            .setParameter("structure", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Structure> fetchRelationstructurematieres(List<Structure> structures) {
        return entityManager
            .createQuery(
                "select distinct structure from Structure structure left join fetch structure.relationstructurematieres where structure in :structures",
                Structure.class
            )
            .setParameter("structures", structures)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
