package demande.matieres.repository;

import demande.matieres.domain.CarnetVehicule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CarnetVehiculeRepositoryWithBagRelationships {
    Optional<CarnetVehicule> fetchBagRelationships(Optional<CarnetVehicule> carnetVehicule);

    List<CarnetVehicule> fetchBagRelationships(List<CarnetVehicule> carnetVehicules);

    Page<CarnetVehicule> fetchBagRelationships(Page<CarnetVehicule> carnetVehicules);
}
