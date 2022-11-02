package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A DemandeMatieres.
 */
@Entity
@Table(name = "demande_matieres")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeMatieres implements Serializable {

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

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "observation")
    private String observation;

    @Column(name = "statut_sup")
    private Boolean statutSup;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandeMatieres", "demandeReparations", "livraisonMatieres", "carnetVehicules" }, allowSetters = true)
    private Structure structure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeMatieres id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public DemandeMatieres date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getIndentiteSoumettant() {
        return this.indentiteSoumettant;
    }

    public DemandeMatieres indentiteSoumettant(String indentiteSoumettant) {
        this.setIndentiteSoumettant(indentiteSoumettant);
        return this;
    }

    public void setIndentiteSoumettant(String indentiteSoumettant) {
        this.indentiteSoumettant = indentiteSoumettant;
    }

    public String getFonction() {
        return this.fonction;
    }

    public DemandeMatieres fonction(String fonction) {
        this.setFonction(fonction);
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getDesignation() {
        return this.designation;
    }

    public DemandeMatieres designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public DemandeMatieres quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public String getObservation() {
        return this.observation;
    }

    public DemandeMatieres observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Boolean getStatutSup() {
        return this.statutSup;
    }

    public DemandeMatieres statutSup(Boolean statutSup) {
        this.setStatutSup(statutSup);
        return this;
    }

    public void setStatutSup(Boolean statutSup) {
        this.statutSup = statutSup;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public DemandeMatieres structure(Structure structure) {
        this.setStructure(structure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeMatieres)) {
            return false;
        }
        return id != null && id.equals(((DemandeMatieres) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeMatieres{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", indentiteSoumettant='" + getIndentiteSoumettant() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", quantite=" + getQuantite() +
            ", observation='" + getObservation() + "'" +
            ", statutSup='" + getStatutSup() + "'" +
            "}";
    }
}
