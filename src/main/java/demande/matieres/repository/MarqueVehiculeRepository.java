package demande.matieres.repository;

import demande.matieres.domain.MarqueVehicule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MarqueVehicule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarqueVehiculeRepository extends JpaRepository<MarqueVehicule, Long> {}
