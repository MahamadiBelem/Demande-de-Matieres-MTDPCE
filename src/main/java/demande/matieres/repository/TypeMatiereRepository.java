package demande.matieres.repository;

import demande.matieres.domain.TypeMatiere;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeMatiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeMatiereRepository extends JpaRepository<TypeMatiere, Long> {}
