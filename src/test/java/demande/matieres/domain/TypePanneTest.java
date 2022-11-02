package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePanneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePanne.class);
        TypePanne typePanne1 = new TypePanne();
        typePanne1.setId(1L);
        TypePanne typePanne2 = new TypePanne();
        typePanne2.setId(typePanne1.getId());
        assertThat(typePanne1).isEqualTo(typePanne2);
        typePanne2.setId(2L);
        assertThat(typePanne1).isNotEqualTo(typePanne2);
        typePanne1.setId(null);
        assertThat(typePanne1).isNotEqualTo(typePanne2);
    }
}
