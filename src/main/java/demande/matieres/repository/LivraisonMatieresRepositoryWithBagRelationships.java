package demande.matieres.repository;

import demande.matieres.domain.LivraisonMatieres;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LivraisonMatieresRepositoryWithBagRelationships {
    Optional<LivraisonMatieres> fetchBagRelationships(Optional<LivraisonMatieres> livraisonMatieres);

    List<LivraisonMatieres> fetchBagRelationships(List<LivraisonMatieres> livraisonMatieres);

    Page<LivraisonMatieres> fetchBagRelationships(Page<LivraisonMatieres> livraisonMatieres);
}
