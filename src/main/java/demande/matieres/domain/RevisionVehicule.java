package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RevisionVehicule.
 */
@Entity
@Table(name = "revision_vehicule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RevisionVehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "libelle_revision_vehicule", nullable = false)
    private String libelleRevisionVehicule;

    @ManyToOne
    @JsonIgnoreProperties(value = { "marqueVehicule", "structures" }, allowSetters = true)
    private CarnetVehicule carnetVehicule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RevisionVehicule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleRevisionVehicule() {
        return this.libelleRevisionVehicule;
    }

    public RevisionVehicule libelleRevisionVehicule(String libelleRevisionVehicule) {
        this.setLibelleRevisionVehicule(libelleRevisionVehicule);
        return this;
    }

    public void setLibelleRevisionVehicule(String libelleRevisionVehicule) {
        this.libelleRevisionVehicule = libelleRevisionVehicule;
    }

    public CarnetVehicule getCarnetVehicule() {
        return this.carnetVehicule;
    }

    public void setCarnetVehicule(CarnetVehicule carnetVehicule) {
        this.carnetVehicule = carnetVehicule;
    }

    public RevisionVehicule carnetVehicule(CarnetVehicule carnetVehicule) {
        this.setCarnetVehicule(carnetVehicule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RevisionVehicule)) {
            return false;
        }
        return id != null && id.equals(((RevisionVehicule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RevisionVehicule{" +
            "id=" + getId() +
            ", libelleRevisionVehicule='" + getLibelleRevisionVehicule() + "'" +
            "}";
    }
}
