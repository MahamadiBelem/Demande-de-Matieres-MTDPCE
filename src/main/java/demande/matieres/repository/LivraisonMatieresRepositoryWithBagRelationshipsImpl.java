package demande.matieres.repository;

import demande.matieres.domain.LivraisonMatieres;
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
public class LivraisonMatieresRepositoryWithBagRelationshipsImpl implements LivraisonMatieresRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<LivraisonMatieres> fetchBagRelationships(Optional<LivraisonMatieres> livraisonMatieres) {
        return livraisonMatieres.map(this::fetchMatieres);
    }

    @Override
    public Page<LivraisonMatieres> fetchBagRelationships(Page<LivraisonMatieres> livraisonMatieres) {
        return new PageImpl<>(
            fetchBagRelationships(livraisonMatieres.getContent()),
            livraisonMatieres.getPageable(),
            livraisonMatieres.getTotalElements()
        );
    }

    @Override
    public List<LivraisonMatieres> fetchBagRelationships(List<LivraisonMatieres> livraisonMatieres) {
        return Optional.of(livraisonMatieres).map(this::fetchMatieres).orElse(Collections.emptyList());
    }

    LivraisonMatieres fetchMatieres(LivraisonMatieres result) {
        return entityManager
            .createQuery(
                "select livraisonMatieres from LivraisonMatieres livraisonMatieres left join fetch livraisonMatieres.matieres where livraisonMatieres is :livraisonMatieres",
                LivraisonMatieres.class
            )
            .setParameter("livraisonMatieres", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<LivraisonMatieres> fetchMatieres(List<LivraisonMatieres> livraisonMatieres) {
        return entityManager
            .createQuery(
                "select distinct livraisonMatieres from LivraisonMatieres livraisonMatieres left join fetch livraisonMatieres.matieres where livraisonMatieres in :livraisonMatieres",
                LivraisonMatieres.class
            )
            .setParameter("livraisonMatieres", livraisonMatieres)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
