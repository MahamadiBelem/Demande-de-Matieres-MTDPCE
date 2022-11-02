package demande.matieres.repository;

import demande.matieres.domain.CarnetVehicule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CarnetVehicule entity.
 */
@Repository
public interface CarnetVehiculeRepository extends CarnetVehiculeRepositoryWithBagRelationships, JpaRepository<CarnetVehicule, Long> {
    default Optional<CarnetVehicule> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<CarnetVehicule> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<CarnetVehicule> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct carnetVehicule from CarnetVehicule carnetVehicule left join fetch carnetVehicule.marqueVehicule",
        countQuery = "select count(distinct carnetVehicule) from CarnetVehicule carnetVehicule"
    )
    Page<CarnetVehicule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct carnetVehicule from CarnetVehicule carnetVehicule left join fetch carnetVehicule.marqueVehicule")
    List<CarnetVehicule> findAllWithToOneRelationships();

    @Query(
        "select carnetVehicule from CarnetVehicule carnetVehicule left join fetch carnetVehicule.marqueVehicule where carnetVehicule.id =:id"
    )
    Optional<CarnetVehicule> findOneWithToOneRelationships(@Param("id") Long id);
}
