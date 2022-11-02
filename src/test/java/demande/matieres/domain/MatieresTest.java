package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatieresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matieres.class);
        Matieres matieres1 = new Matieres();
        matieres1.setId(1L);
        Matieres matieres2 = new Matieres();
        matieres2.setId(matieres1.getId());
        assertThat(matieres1).isEqualTo(matieres2);
        matieres2.setId(2L);
        assertThat(matieres1).isNotEqualTo(matieres2);
        matieres1.setId(null);
        assertThat(matieres1).isNotEqualTo(matieres2);
    }
}
