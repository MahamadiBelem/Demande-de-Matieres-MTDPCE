package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RevisionVehiculeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionVehicule.class);
        RevisionVehicule revisionVehicule1 = new RevisionVehicule();
        revisionVehicule1.setId(1L);
        RevisionVehicule revisionVehicule2 = new RevisionVehicule();
        revisionVehicule2.setId(revisionVehicule1.getId());
        assertThat(revisionVehicule1).isEqualTo(revisionVehicule2);
        revisionVehicule2.setId(2L);
        assertThat(revisionVehicule1).isNotEqualTo(revisionVehicule2);
        revisionVehicule1.setId(null);
        assertThat(revisionVehicule1).isNotEqualTo(revisionVehicule2);
    }
}
