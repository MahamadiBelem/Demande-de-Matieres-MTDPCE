package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A DemandeReparations.
 */
@Entity
@Table(name = "demande_reparations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeReparations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Size(min = 5)
    @Column(name = "indentite_soumettant", nullable = false)
    private String indentiteSoumettant;

    @NotNull
    @Size(min = 3)
    @Column(name = "fonction", nullable = false)
    private String fonction;

    @NotNull
    @Size(min = 3)
    @Column(name = "designation", nullable = false)
    private String designation;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "observation")
    private String observation;

    @Column(name = "statut_sup")
    private Boolean statutSup;

    @ManyToMany
    @JoinTable(
        name = "rel_demande_reparations__type_matiere",
        joinColumns = @JoinColumn(name = "demande_reparations_id"),
        inverseJoinColumns = @JoinColumn(name = "type_matiere_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandeReparations" }, allowSetters = true)
    private Set<TypeMatiere> typeMatieres = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandeMatieres", "demandeReparations", "livraisonMatieres", "carnetVehicules" }, allowSetters = true)
    private Structure structure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeReparations id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public DemandeReparations date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getIndentiteSoumettant() {
        return this.indentiteSoumettant;
    }

    public DemandeReparations indentiteSoumettant(String indentiteSoumettant) {
        this.setIndentiteSoumettant(indentiteSoumettant);
        return this;
    }

    public void setIndentiteSoumettant(String indentiteSoumettant) {
        this.indentiteSoumettant = indentiteSoumettant;
    }

    public String getFonction() {
        return this.fonction;
    }

    public DemandeReparations fonction(String fonction) {
        this.setFonction(fonction);
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getDesignation() {
        return this.designation;
    }

    public DemandeReparations designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getObservation() {
        return this.observation;
    }

    public DemandeReparations observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Boolean getStatutSup() {
        return this.statutSup;
    }

    public DemandeReparations statutSup(Boolean statutSup) {
        this.setStatutSup(statutSup);
        return this;
    }

    public void setStatutSup(Boolean statutSup) {
        this.statutSup = statutSup;
    }

    public Set<TypeMatiere> getTypeMatieres() {
        return this.typeMatieres;
    }

    public void setTypeMatieres(Set<TypeMatiere> typeMatieres) {
        this.typeMatieres = typeMatieres;
    }

    public DemandeReparations typeMatieres(Set<TypeMatiere> typeMatieres) {
        this.setTypeMatieres(typeMatieres);
        return this;
    }

    public DemandeReparations addTypeMatiere(TypeMatiere typeMatiere) {
        this.typeMatieres.add(typeMatiere);
        typeMatiere.getDemandeReparations().add(this);
        return this;
    }

    public DemandeReparations removeTypeMatiere(TypeMatiere typeMatiere) {
        this.typeMatieres.remove(typeMatiere);
        typeMatiere.getDemandeReparations().remove(this);
        return this;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public DemandeReparations structure(Structure structure) {
        this.setStructure(structure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeReparations)) {
            return false;
        }
        return id != null && id.equals(((DemandeReparations) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeReparations{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", indentiteSoumettant='" + getIndentiteSoumettant() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", observation='" + getObservation() + "'" +
            ", statutSup='" + getStatutSup() + "'" +
            "}";
    }
}
