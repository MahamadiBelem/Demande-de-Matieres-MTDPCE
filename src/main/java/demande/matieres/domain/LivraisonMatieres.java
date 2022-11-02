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

/**
 * A LivraisonMatieres.
 */
@Entity
@Table(name = "livraison_matieres")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LivraisonMatieres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "designation_matiere", nullable = false)
    private String designationMatiere;

    @NotNull
    @Column(name = "quantite_livree", nullable = false)
    private Integer quantiteLivree;

    @Column(name = "date_livree")
    private ZonedDateTime dateLivree;

    @Column(name = "statut_sup")
    private Boolean statutSup;

    @ManyToMany
    @JoinTable(
        name = "rel_livraison_matieres__matieres",
        joinColumns = @JoinColumn(name = "livraison_matieres_id"),
        inverseJoinColumns = @JoinColumn(name = "matieres_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "livraisonMatieres" }, allowSetters = true)
    private Set<Matieres> matieres = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandeMatieres", "demandeReparations", "livraisonMatieres", "carnetVehicules" }, allowSetters = true)
    private Structure structure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LivraisonMatieres id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignationMatiere() {
        return this.designationMatiere;
    }

    public LivraisonMatieres designationMatiere(String designationMatiere) {
        this.setDesignationMatiere(designationMatiere);
        return this;
    }

    public void setDesignationMatiere(String designationMatiere) {
        this.designationMatiere = designationMatiere;
    }

    public Integer getQuantiteLivree() {
        return this.quantiteLivree;
    }

    public LivraisonMatieres quantiteLivree(Integer quantiteLivree) {
        this.setQuantiteLivree(quantiteLivree);
        return this;
    }

    public void setQuantiteLivree(Integer quantiteLivree) {
        this.quantiteLivree = quantiteLivree;
    }

    public ZonedDateTime getDateLivree() {
        return this.dateLivree;
    }

    public LivraisonMatieres dateLivree(ZonedDateTime dateLivree) {
        this.setDateLivree(dateLivree);
        return this;
    }

    public void setDateLivree(ZonedDateTime dateLivree) {
        this.dateLivree = dateLivree;
    }

    public Boolean getStatutSup() {
        return this.statutSup;
    }

    public LivraisonMatieres statutSup(Boolean statutSup) {
        this.setStatutSup(statutSup);
        return this;
    }

    public void setStatutSup(Boolean statutSup) {
        this.statutSup = statutSup;
    }

    public Set<Matieres> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matieres> matieres) {
        this.matieres = matieres;
    }

    public LivraisonMatieres matieres(Set<Matieres> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public LivraisonMatieres addMatieres(Matieres matieres) {
        this.matieres.add(matieres);
        matieres.getLivraisonMatieres().add(this);
        return this;
    }

    public LivraisonMatieres removeMatieres(Matieres matieres) {
        this.matieres.remove(matieres);
        matieres.getLivraisonMatieres().remove(this);
        return this;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public LivraisonMatieres structure(Structure structure) {
        this.setStructure(structure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivraisonMatieres)) {
            return false;
        }
        return id != null && id.equals(((LivraisonMatieres) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivraisonMatieres{" +
            "id=" + getId() +
            ", designationMatiere='" + getDesignationMatiere() + "'" +
            ", quantiteLivree=" + getQuantiteLivree() +
            ", dateLivree='" + getDateLivree() + "'" +
            ", statutSup='" + getStatutSup() + "'" +
            "}";
    }
}
