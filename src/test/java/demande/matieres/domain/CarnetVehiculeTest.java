package demande.matieres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import demande.matieres.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarnetVehiculeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarnetVehicule.class);
        CarnetVehicule carnetVehicule1 = new CarnetVehicule();
        carnetVehicule1.setId(1L);
        CarnetVehicule carnetVehicule2 = new CarnetVehicule();
        carnetVehicule2.setId(carnetVehicule1.getId());
        assertThat(carnetVehicule1).isEqualTo(carnetVehicule2);
        carnetVehicule2.setId(2L);
        assertThat(carnetVehicule1).isNotEqualTo(carnetVehicule2);
        carnetVehicule1.setId(null);
        assertThat(carnetVehicule1).isNotEqualTo(carnetVehicule2);
    }
}
