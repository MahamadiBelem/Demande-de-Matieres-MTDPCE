package demande.matieres.repository;

import demande.matieres.domain.Structure;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StructureRepositoryWithBagRelationships {
    Optional<Structure> fetchBagRelationships(Optional<Structure> structure);

    List<Structure> fetchBagRelationships(List<Structure> structures);

    Page<Structure> fetchBagRelationships(Page<Structure> structures);
}
