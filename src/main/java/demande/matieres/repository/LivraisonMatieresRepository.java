package demande.matieres.repository;

import demande.matieres.domain.LivraisonMatieres;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LivraisonMatieres entity.
 */
@Repository
public interface LivraisonMatieresRepository
    extends LivraisonMatieresRepositoryWithBagRelationships, JpaRepository<LivraisonMatieres, Long> {
    default Optional<LivraisonMatieres> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<LivraisonMatieres> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<LivraisonMatieres> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct livraisonMatieres from LivraisonMatieres livraisonMatieres left join fetch livraisonMatieres.structure",
        countQuery = "select count(distinct livraisonMatieres) from LivraisonMatieres livraisonMatieres"
    )
    Page<LivraisonMatieres> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct livraisonMatieres from LivraisonMatieres livraisonMatieres left join fetch livraisonMatieres.structure")
    List<LivraisonMatieres> findAllWithToOneRelationships();

    @Query(
        "select livraisonMatieres from LivraisonMatieres livraisonMatieres left join fetch livraisonMatieres.structure where livraisonMatieres.id =:id"
    )
    Optional<LivraisonMatieres> findOneWithToOneRelationships(@Param("id") Long id);
}
