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
 * A Structure.
 */
@Entity
@Table(name = "structure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Structure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "libelle_structure", nullable = false)
    private String libelleStructure;

    @NotNull
    @Size(min = 2)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "code_structure", nullable = false)
    private String codeStructure;

    @OneToMany(mappedBy = "structure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "structure" }, allowSetters = true)
    private Set<DemandeMatieres> demandeMatieres = new HashSet<>();

    @OneToMany(mappedBy = "structure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeMatieres", "structure" }, allowSetters = true)
    private Set<DemandeReparations> demandeReparations = new HashSet<>();

    @OneToMany(mappedBy = "structure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matieres", "structure" }, allowSetters = true)
    private Set<LivraisonMatieres> livraisonMatieres = new HashSet<>();

    @ManyToMany(mappedBy = "structures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marqueVehicule", "structures" }, allowSetters = true)
    private Set<CarnetVehicule> carnetVehicules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Structure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleStructure() {
        return this.libelleStructure;
    }

    public Structure libelleStructure(String libelleStructure) {
        this.setLibelleStructure(libelleStructure);
        return this;
    }

    public void setLibelleStructure(String libelleStructure) {
        this.libelleStructure = libelleStructure;
    }

    public String getCodeStructure() {
        return this.codeStructure;
    }

    public Structure codeStructure(String codeStructure) {
        this.setCodeStructure(codeStructure);
        return this;
    }

    public void setCodeStructure(String codeStructure) {
        this.codeStructure = codeStructure;
    }

    public Set<DemandeMatieres> getDemandeMatieres() {
        return this.demandeMatieres;
    }

    public void setDemandeMatieres(Set<DemandeMatieres> demandeMatieres) {
        if (this.demandeMatieres != null) {
            this.demandeMatieres.forEach(i -> i.setStructure(null));
        }
        if (demandeMatieres != null) {
            demandeMatieres.forEach(i -> i.setStructure(this));
        }
        this.demandeMatieres = demandeMatieres;
    }

    public Structure demandeMatieres(Set<DemandeMatieres> demandeMatieres) {
        this.setDemandeMatieres(demandeMatieres);
        return this;
    }

    public Structure addDemandeMatieres(DemandeMatieres demandeMatieres) {
        this.demandeMatieres.add(demandeMatieres);
        demandeMatieres.setStructure(this);
        return this;
    }

    public Structure removeDemandeMatieres(DemandeMatieres demandeMatieres) {
        this.demandeMatieres.remove(demandeMatieres);
        demandeMatieres.setStructure(null);
        return this;
    }

    public Set<DemandeReparations> getDemandeReparations() {
        return this.demandeReparations;
    }

    public void setDemandeReparations(Set<DemandeReparations> demandeReparations) {
        if (this.demandeReparations != null) {
            this.demandeReparations.forEach(i -> i.setStructure(null));
        }
        if (demandeReparations != null) {
            demandeReparations.forEach(i -> i.setStructure(this));
        }
        this.demandeReparations = demandeReparations;
    }

    public Structure demandeReparations(Set<DemandeReparations> demandeReparations) {
        this.setDemandeReparations(demandeReparations);
        return this;
    }

    public Structure addDemandeReparations(DemandeReparations demandeReparations) {
        this.demandeReparations.add(demandeReparations);
        demandeReparations.setStructure(this);
        return this;
    }

    public Structure removeDemandeReparations(DemandeReparations demandeReparations) {
        this.demandeReparations.remove(demandeReparations);
        demandeReparations.setStructure(null);
        return this;
    }

    public Set<LivraisonMatieres> getLivraisonMatieres() {
        return this.livraisonMatieres;
    }

    public void setLivraisonMatieres(Set<LivraisonMatieres> livraisonMatieres) {
        if (this.livraisonMatieres != null) {
            this.livraisonMatieres.forEach(i -> i.setStructure(null));
        }
        if (livraisonMatieres != null) {
            livraisonMatieres.forEach(i -> i.setStructure(this));
        }
        this.livraisonMatieres = livraisonMatieres;
    }

    public Structure livraisonMatieres(Set<LivraisonMatieres> livraisonMatieres) {
        this.setLivraisonMatieres(livraisonMatieres);
        return this;
    }

    public Structure addLivraisonMatieres(LivraisonMatieres livraisonMatieres) {
        this.livraisonMatieres.add(livraisonMatieres);
        livraisonMatieres.setStructure(this);
        return this;
    }

    public Structure removeLivraisonMatieres(LivraisonMatieres livraisonMatieres) {
        this.livraisonMatieres.remove(livraisonMatieres);
        livraisonMatieres.setStructure(null);
        return this;
    }

    public Set<CarnetVehicule> getCarnetVehicules() {
        return this.carnetVehicules;
    }

    public void setCarnetVehicules(Set<CarnetVehicule> carnetVehicules) {
        if (this.carnetVehicules != null) {
            this.carnetVehicules.forEach(i -> i.removeStructure(this));
        }
        if (carnetVehicules != null) {
            carnetVehicules.forEach(i -> i.addStructure(this));
        }
        this.carnetVehicules = carnetVehicules;
    }

    public Structure carnetVehicules(Set<CarnetVehicule> carnetVehicules) {
        this.setCarnetVehicules(carnetVehicules);
        return this;
    }

    public Structure addCarnetVehicule(CarnetVehicule carnetVehicule) {
        this.carnetVehicules.add(carnetVehicule);
        carnetVehicule.getStructures().add(this);
        return this;
    }

    public Structure removeCarnetVehicule(CarnetVehicule carnetVehicule) {
        this.carnetVehicules.remove(carnetVehicule);
        carnetVehicule.getStructures().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Structure)) {
            return false;
        }
        return id != null && id.equals(((Structure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Structure{" +
            "id=" + getId() +
            ", libelleStructure='" + getLibelleStructure() + "'" +
            ", codeStructure='" + getCodeStructure() + "'" +
            "}";
    }
}
