package demande.matieres.repository;

import demande.matieres.domain.TypePanne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypePanne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePanneRepository extends JpaRepository<TypePanne, Long> {}
