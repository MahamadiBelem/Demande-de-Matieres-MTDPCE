package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MarqueVehicule.
 */
@Entity
@Table(name = "marque_vehicule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MarqueVehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "libelle_marque_vehicule", nullable = false)
    private String libelleMarqueVehicule;

    @JsonIgnoreProperties(value = { "marqueVehicule", "structures" }, allowSetters = true)
    @OneToOne(mappedBy = "marqueVehicule")
    private CarnetVehicule carnetVehicule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarqueVehicule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleMarqueVehicule() {
        return this.libelleMarqueVehicule;
    }

    public MarqueVehicule libelleMarqueVehicule(String libelleMarqueVehicule) {
        this.setLibelleMarqueVehicule(libelleMarqueVehicule);
        return this;
    }

    public void setLibelleMarqueVehicule(String libelleMarqueVehicule) {
        this.libelleMarqueVehicule = libelleMarqueVehicule;
    }

    public CarnetVehicule getCarnetVehicule() {
        return this.carnetVehicule;
    }

    public void setCarnetVehicule(CarnetVehicule carnetVehicule) {
        if (this.carnetVehicule != null) {
            this.carnetVehicule.setMarqueVehicule(null);
        }
        if (carnetVehicule != null) {
            carnetVehicule.setMarqueVehicule(this);
        }
        this.carnetVehicule = carnetVehicule;
    }

    public MarqueVehicule carnetVehicule(CarnetVehicule carnetVehicule) {
        this.setCarnetVehicule(carnetVehicule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarqueVehicule)) {
            return false;
        }
        return id != null && id.equals(((MarqueVehicule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarqueVehicule{" +
            "id=" + getId() +
            ", libelleMarqueVehicule='" + getLibelleMarqueVehicule() + "'" +
            "}";
    }
}
