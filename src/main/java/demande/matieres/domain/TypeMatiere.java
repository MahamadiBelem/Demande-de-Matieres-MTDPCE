package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeMatiere.
 */
@Entity
@Table(name = "type_matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeMatiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "libelle_type_matiere", nullable = false)
    private String libelleTypeMatiere;

    @ManyToMany(mappedBy = "typeMatieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeMatieres", "structure" }, allowSetters = true)
    private Set<DemandeReparations> demandeReparations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeMatiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeMatiere() {
        return this.libelleTypeMatiere;
    }

    public TypeMatiere libelleTypeMatiere(String libelleTypeMatiere) {
        this.setLibelleTypeMatiere(libelleTypeMatiere);
        return this;
    }

    public void setLibelleTypeMatiere(String libelleTypeMatiere) {
        this.libelleTypeMatiere = libelleTypeMatiere;
    }

    public Set<DemandeReparations> getDemandeReparations() {
        return this.demandeReparations;
    }

    public void setDemandeReparations(Set<DemandeReparations> demandeReparations) {
        if (this.demandeReparations != null) {
            this.demandeReparations.forEach(i -> i.removeTypeMatiere(this));
        }
        if (demandeReparations != null) {
            demandeReparations.forEach(i -> i.addTypeMatiere(this));
        }
        this.demandeReparations = demandeReparations;
    }

    public TypeMatiere demandeReparations(Set<DemandeReparations> demandeReparations) {
        this.setDemandeReparations(demandeReparations);
        return this;
    }

    public TypeMatiere addDemandeReparations(DemandeReparations demandeReparations) {
        this.demandeReparations.add(demandeReparations);
        demandeReparations.getTypeMatieres().add(this);
        return this;
    }

    public TypeMatiere removeDemandeReparations(DemandeReparations demandeReparations) {
        this.demandeReparations.remove(demandeReparations);
        demandeReparations.getTypeMatieres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeMatiere)) {
            return false;
        }
        return id != null && id.equals(((TypeMatiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeMatiere{" +
            "id=" + getId() +
            ", libelleTypeMatiere='" + getLibelleTypeMatiere() + "'" +
            "}";
    }
}
