package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Matieres.
 */
@Entity
@Table(name = "matieres")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Matieres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "designation_matieres", nullable = false)
    private String designationMatieres;

    @NotNull
    @Column(name = "quantite_matieres", nullable = false)
    private Integer quantiteMatieres;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "caracteristiques_matieres")
    private String caracteristiquesMatieres;

    @Column(name = "statut_sup")
    private Boolean statutSup;

    @ManyToMany(mappedBy = "matieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matieres", "structure" }, allowSetters = true)
    private Set<LivraisonMatieres> livraisonMatieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matieres id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignationMatieres() {
        return this.designationMatieres;
    }

    public Matieres designationMatieres(String designationMatieres) {
        this.setDesignationMatieres(designationMatieres);
        return this;
    }

    public void setDesignationMatieres(String designationMatieres) {
        this.designationMatieres = designationMatieres;
    }

    public Integer getQuantiteMatieres() {
        return this.quantiteMatieres;
    }

    public Matieres quantiteMatieres(Integer quantiteMatieres) {
        this.setQuantiteMatieres(quantiteMatieres);
        return this;
    }

    public void setQuantiteMatieres(Integer quantiteMatieres) {
        this.quantiteMatieres = quantiteMatieres;
    }

    public String getCaracteristiquesMatieres() {
        return this.caracteristiquesMatieres;
    }

    public Matieres caracteristiquesMatieres(String caracteristiquesMatieres) {
        this.setCaracteristiquesMatieres(caracteristiquesMatieres);
        return this;
    }

    public void setCaracteristiquesMatieres(String caracteristiquesMatieres) {
        this.caracteristiquesMatieres = caracteristiquesMatieres;
    }

    public Boolean getStatutSup() {
        return this.statutSup;
    }

    public Matieres statutSup(Boolean statutSup) {
        this.setStatutSup(statutSup);
        return this;
    }

    public void setStatutSup(Boolean statutSup) {
        this.statutSup = statutSup;
    }

    public Set<LivraisonMatieres> getLivraisonMatieres() {
        return this.livraisonMatieres;
    }

    public void setLivraisonMatieres(Set<LivraisonMatieres> livraisonMatieres) {
        if (this.livraisonMatieres != null) {
            this.livraisonMatieres.forEach(i -> i.removeMatieres(this));
        }
        if (livraisonMatieres != null) {
            livraisonMatieres.forEach(i -> i.addMatieres(this));
        }
        this.livraisonMatieres = livraisonMatieres;
    }

    public Matieres livraisonMatieres(Set<LivraisonMatieres> livraisonMatieres) {
        this.setLivraisonMatieres(livraisonMatieres);
        return this;
    }

    public Matieres addLivraisonMatieres(LivraisonMatieres livraisonMatieres) {
        this.livraisonMatieres.add(livraisonMatieres);
        livraisonMatieres.getMatieres().add(this);
        return this;
    }

    public Matieres removeLivraisonMatieres(LivraisonMatieres livraisonMatieres) {
        this.livraisonMatieres.remove(livraisonMatieres);
        livraisonMatieres.getMatieres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matieres)) {
            return false;
        }
        return id != null && id.equals(((Matieres) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matieres{" +
            "id=" + getId() +
            ", designationMatieres='" + getDesignationMatieres() + "'" +
            ", quantiteMatieres=" + getQuantiteMatieres() +
            ", caracteristiquesMatieres='" + getCaracteristiquesMatieres() + "'" +
            ", statutSup='" + getStatutSup() + "'" +
            "}";
    }
}
