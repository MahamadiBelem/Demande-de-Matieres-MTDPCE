package demande.matieres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demande.matieres.domain.enumeration.Etatvehicule;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CarnetVehicule.
 */
@Entity
@Table(name = "carnet_vehicule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CarnetVehicule implements Serializable {

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
    @Size(min = 3)
    @Column(name = "immatriculation_vehicule", nullable = false)
    private String immatriculationVehicule;

    @NotNull
    @Column(name = "identite_conducteur", nullable = false)
    private String identiteConducteur;

    @Column(name = "nombre_reparation")
    private Integer nombreReparation;

    @Column(name = "date_derniere_revision")
    private LocalDate dateDerniereRevision;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatvehicule")
    private Etatvehicule etatvehicule;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "observations")
    private String observations;

    @Column(name = "statut_sup")
    private Boolean statutSup;

    @JsonIgnoreProperties(value = { "carnetVehicule" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private MarqueVehicule marqueVehicule;

    @ManyToMany
    @JoinTable(
        name = "rel_carnet_vehicule__structure",
        joinColumns = @JoinColumn(name = "carnet_vehicule_id"),
        inverseJoinColumns = @JoinColumn(name = "structure_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandeMatieres", "demandeReparations", "livraisonMatieres", "carnetVehicules" }, allowSetters = true)
    private Set<Structure> structures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarnetVehicule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public CarnetVehicule date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getImmatriculationVehicule() {
        return this.immatriculationVehicule;
    }

    public CarnetVehicule immatriculationVehicule(String immatriculationVehicule) {
        this.setImmatriculationVehicule(immatriculationVehicule);
        return this;
    }

    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }

    public String getIdentiteConducteur() {
        return this.identiteConducteur;
    }

    public CarnetVehicule identiteConducteur(String identiteConducteur) {
        this.setIdentiteConducteur(identiteConducteur);
        return this;
    }

    public void setIdentiteConducteur(String identiteConducteur) {
        this.identiteConducteur = identiteConducteur;
    }

    public Integer getNombreReparation() {
        return this.nombreReparation;
    }

    public CarnetVehicule nombreReparation(Integer nombreReparation) {
        this.setNombreReparation(nombreReparation);
        return this;
    }

    public void setNombreReparation(Integer nombreReparation) {
        this.nombreReparation = nombreReparation;
    }

    public LocalDate getDateDerniereRevision() {
        return this.dateDerniereRevision;
    }

    public CarnetVehicule dateDerniereRevision(LocalDate dateDerniereRevision) {
        this.setDateDerniereRevision(dateDerniereRevision);
        return this;
    }

    public void setDateDerniereRevision(LocalDate dateDerniereRevision) {
        this.dateDerniereRevision = dateDerniereRevision;
    }

    public Etatvehicule getEtatvehicule() {
        return this.etatvehicule;
    }

    public CarnetVehicule etatvehicule(Etatvehicule etatvehicule) {
        this.setEtatvehicule(etatvehicule);
        return this;
    }

    public void setEtatvehicule(Etatvehicule etatvehicule) {
        this.etatvehicule = etatvehicule;
    }

    public String getObservations() {
        return this.observations;
    }

    public CarnetVehicule observations(String observations) {
        this.setObservations(observations);
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean getStatutSup() {
        return this.statutSup;
    }

    public CarnetVehicule statutSup(Boolean statutSup) {
        this.setStatutSup(statutSup);
        return this;
    }

    public void setStatutSup(Boolean statutSup) {
        this.statutSup = statutSup;
    }

    public MarqueVehicule getMarqueVehicule() {
        return this.marqueVehicule;
    }

    public void setMarqueVehicule(MarqueVehicule marqueVehicule) {
        this.marqueVehicule = marqueVehicule;
    }

    public CarnetVehicule marqueVehicule(MarqueVehicule marqueVehicule) {
        this.setMarqueVehicule(marqueVehicule);
        return this;
    }

    public Set<Structure> getStructures() {
        return this.structures;
    }

    public void setStructures(Set<Structure> structures) {
        this.structures = structures;
    }

    public CarnetVehicule structures(Set<Structure> structures) {
        this.setStructures(structures);
        return this;
    }

    public CarnetVehicule addStructure(Structure structure) {
        this.structures.add(structure);
        structure.getCarnetVehicules().add(this);
        return this;
    }

    public CarnetVehicule removeStructure(Structure structure) {
        this.structures.remove(structure);
        structure.getCarnetVehicules().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarnetVehicule)) {
            return false;
        }
        return id != null && id.equals(((CarnetVehicule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarnetVehicule{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", immatriculationVehicule='" + getImmatriculationVehicule() + "'" +
            ", identiteConducteur='" + getIdentiteConducteur() + "'" +
            ", nombreReparation=" + getNombreReparation() +
            ", dateDerniereRevision='" + getDateDerniereRevision() + "'" +
            ", etatvehicule='" + getEtatvehicule() + "'" +
            ", observations='" + getObservations() + "'" +
            ", statutSup='" + getStatutSup() + "'" +
            "}";
    }
}
