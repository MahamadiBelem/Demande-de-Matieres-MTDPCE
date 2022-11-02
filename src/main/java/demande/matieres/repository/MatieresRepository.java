package demande.matieres.repository;

import demande.matieres.domain.Matieres;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Matieres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatieresRepository extends JpaRepository<Matieres, Long> {}
