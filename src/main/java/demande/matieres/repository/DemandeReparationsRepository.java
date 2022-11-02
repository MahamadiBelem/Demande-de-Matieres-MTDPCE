package demande.matieres.repository;

import demande.matieres.domain.DemandeReparations;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemandeReparations entity.
 */
@Repository
public interface DemandeReparationsRepository
    extends DemandeReparationsRepositoryWithBagRelationships, JpaRepository<DemandeReparations, Long> {
    default Optional<DemandeReparations> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<DemandeReparations> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<DemandeReparations> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct demandeReparations from DemandeReparations demandeReparations left join fetch demandeReparations.structure",
        countQuery = "select count(distinct demandeReparations) from DemandeReparations demandeReparations"
    )
    Page<DemandeReparations> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct demandeReparations from DemandeReparations demandeReparations left join fetch demandeReparations.structure")
    List<DemandeReparations> findAllWithToOneRelationships();

    @Query(
        "select demandeReparations from DemandeReparations demandeReparations left join fetch demandeReparations.structure where demandeReparations.id =:id"
    )
    Optional<DemandeReparations> findOneWithToOneRelationships(@Param("id") Long id);
}
