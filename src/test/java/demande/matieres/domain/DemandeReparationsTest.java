package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeReparationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeReparations.class);
        DemandeReparations demandeReparations1 = new DemandeReparations();
        demandeReparations1.setId(1L);
        DemandeReparations demandeReparations2 = new DemandeReparations();
        demandeReparations2.setId(demandeReparations1.getId());
        assertThat(demandeReparations1).isEqualTo(demandeReparations2);
        demandeReparations2.setId(2L);
        assertThat(demandeReparations1).isNotEqualTo(demandeReparations2);
        demandeReparations1.setId(null);
        assertThat(demandeReparations1).isNotEqualTo(demandeReparations2);
    }
}
