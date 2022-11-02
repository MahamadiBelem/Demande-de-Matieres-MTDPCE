package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeMatieresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeMatieres.class);
        DemandeMatieres demandeMatieres1 = new DemandeMatieres();
        demandeMatieres1.setId(1L);
        DemandeMatieres demandeMatieres2 = new DemandeMatieres();
        demandeMatieres2.setId(demandeMatieres1.getId());
        assertThat(demandeMatieres1).isEqualTo(demandeMatieres2);
        demandeMatieres2.setId(2L);
        assertThat(demandeMatieres1).isNotEqualTo(demandeMatieres2);
        demandeMatieres1.setId(null);
        assertThat(demandeMatieres1).isNotEqualTo(demandeMatieres2);
    }
}
