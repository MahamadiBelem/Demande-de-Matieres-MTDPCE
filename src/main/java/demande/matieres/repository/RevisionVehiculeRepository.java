package demande.matieres.repository;

import demande.matieres.domain.RevisionVehicule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RevisionVehicule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionVehiculeRepository extends JpaRepository<RevisionVehicule, Long> {}
