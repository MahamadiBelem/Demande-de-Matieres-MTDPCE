package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypePanne.
 */
@Entity
@Table(name = "type_panne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypePanne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "libelle_type_panne", nullable = false)
    private String libelleTypePanne;

    @Column(name = "statut_sup")
    private Boolean statutSup;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeMatieres", "structure" }, allowSetters = true)
    private DemandeReparations demandeReparations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypePanne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypePanne() {
        return this.libelleTypePanne;
    }

    public TypePanne libelleTypePanne(String libelleTypePanne) {
        this.setLibelleTypePanne(libelleTypePanne);
        return this;
    }

    public void setLibelleTypePanne(String libelleTypePanne) {
        this.libelleTypePanne = libelleTypePanne;
    }

    public Boolean getStatutSup() {
        return this.statutSup;
    }

    public TypePanne statutSup(Boolean statutSup) {
        this.setStatutSup(statutSup);
        return this;
    }

    public void setStatutSup(Boolean statutSup) {
        this.statutSup = statutSup;
    }

    public DemandeReparations getDemandeReparations() {
        return this.demandeReparations;
    }

    public void setDemandeReparations(DemandeReparations demandeReparations) {
        this.demandeReparations = demandeReparations;
    }

    public TypePanne demandeReparations(DemandeReparations demandeReparations) {
        this.setDemandeReparations(demandeReparations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypePanne)) {
            return false;
        }
        return id != null && id.equals(((TypePanne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePanne{" +
            "id=" + getId() +
            ", libelleTypePanne='" + getLibelleTypePanne() + "'" +
            ", statutSup='" + getStatutSup() + "'" +
            "}";
    }
}
