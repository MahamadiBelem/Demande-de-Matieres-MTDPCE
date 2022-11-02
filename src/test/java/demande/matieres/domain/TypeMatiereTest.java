package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeMatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeMatiere.class);
        TypeMatiere typeMatiere1 = new TypeMatiere();
        typeMatiere1.setId(1L);
        TypeMatiere typeMatiere2 = new TypeMatiere();
        typeMatiere2.setId(typeMatiere1.getId());
        assertThat(typeMatiere1).isEqualTo(typeMatiere2);
        typeMatiere2.setId(2L);
        assertThat(typeMatiere1).isNotEqualTo(typeMatiere2);
        typeMatiere1.setId(null);
        assertThat(typeMatiere1).isNotEqualTo(typeMatiere2);
    }
}
