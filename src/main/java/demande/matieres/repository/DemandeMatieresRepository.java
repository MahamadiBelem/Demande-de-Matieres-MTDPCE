package demande.matieres.repository;

import demande.matieres.domain.DemandeMatieres;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemandeMatieres entity.
 */
@Repository
public interface DemandeMatieresRepository extends JpaRepository<DemandeMatieres, Long> {
    default Optional<DemandeMatieres> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DemandeMatieres> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DemandeMatieres> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct demandeMatieres from DemandeMatieres demandeMatieres left join fetch demandeMatieres.structure",
        countQuery = "select count(distinct demandeMatieres) from DemandeMatieres demandeMatieres"
    )
    Page<DemandeMatieres> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct demandeMatieres from DemandeMatieres demandeMatieres left join fetch demandeMatieres.structure")
    List<DemandeMatieres> findAllWithToOneRelationships();

    @Query(
        "select demandeMatieres from DemandeMatieres demandeMatieres left join fetch demandeMatieres.structure where demandeMatieres.id =:id"
    )
    Optional<DemandeMatieres> findOneWithToOneRelationships(@Param("id") Long id);
}
