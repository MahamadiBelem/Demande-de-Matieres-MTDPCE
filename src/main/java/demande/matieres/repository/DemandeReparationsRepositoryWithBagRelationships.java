package demande.matieres.repository;

import demande.matieres.domain.DemandeReparations;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DemandeReparationsRepositoryWithBagRelationships {
    Optional<DemandeReparations> fetchBagRelationships(Optional<DemandeReparations> demandeReparations);

    List<DemandeReparations> fetchBagRelationships(List<DemandeReparations> demandeReparations);

    Page<DemandeReparations> fetchBagRelationships(Page<DemandeReparations> demandeReparations);
}
