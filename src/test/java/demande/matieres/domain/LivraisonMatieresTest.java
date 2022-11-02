package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivraisonMatieresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivraisonMatieres.class);
        LivraisonMatieres livraisonMatieres1 = new LivraisonMatieres();
        livraisonMatieres1.setId(1L);
        LivraisonMatieres livraisonMatieres2 = new LivraisonMatieres();
        livraisonMatieres2.setId(livraisonMatieres1.getId());
        assertThat(livraisonMatieres1).isEqualTo(livraisonMatieres2);
        livraisonMatieres2.setId(2L);
        assertThat(livraisonMatieres1).isNotEqualTo(livraisonMatieres2);
        livraisonMatieres1.setId(null);
        assertThat(livraisonMatieres1).isNotEqualTo(livraisonMatieres2);
    }
}
