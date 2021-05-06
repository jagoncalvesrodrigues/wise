package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "importe")
    private Double importe;

    @Column(name = "fecha")
    private Instant fecha;

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Camiseta> camisetas = new HashSet<>();

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Sudadera> sudaderas = new HashSet<>();

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Accesorio> accesorios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "posts", "ventas" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta id(Long id) {
        this.id = id;
        return this;
    }

    public Double getImporte() {
        return this.importe;
    }

    public Venta importe(Double importe) {
        this.importe = importe;
        return this;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Venta fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Set<Camiseta> getCamisetas() {
        return this.camisetas;
    }

    public Venta camisetas(Set<Camiseta> camisetas) {
        this.setCamisetas(camisetas);
        return this;
    }

    public Venta addCamiseta(Camiseta camiseta) {
        this.camisetas.add(camiseta);
        camiseta.setVenta(this);
        return this;
    }

    public Venta removeCamiseta(Camiseta camiseta) {
        this.camisetas.remove(camiseta);
        camiseta.setVenta(null);
        return this;
    }

    public void setCamisetas(Set<Camiseta> camisetas) {
        if (this.camisetas != null) {
            this.camisetas.forEach(i -> i.setVenta(null));
        }
        if (camisetas != null) {
            camisetas.forEach(i -> i.setVenta(this));
        }
        this.camisetas = camisetas;
    }

    public Set<Sudadera> getSudaderas() {
        return this.sudaderas;
    }

    public Venta sudaderas(Set<Sudadera> sudaderas) {
        this.setSudaderas(sudaderas);
        return this;
    }

    public Venta addSudadera(Sudadera sudadera) {
        this.sudaderas.add(sudadera);
        sudadera.setVenta(this);
        return this;
    }

    public Venta removeSudadera(Sudadera sudadera) {
        this.sudaderas.remove(sudadera);
        sudadera.setVenta(null);
        return this;
    }

    public void setSudaderas(Set<Sudadera> sudaderas) {
        if (this.sudaderas != null) {
            this.sudaderas.forEach(i -> i.setVenta(null));
        }
        if (sudaderas != null) {
            sudaderas.forEach(i -> i.setVenta(this));
        }
        this.sudaderas = sudaderas;
    }

    public Set<Accesorio> getAccesorios() {
        return this.accesorios;
    }

    public Venta accesorios(Set<Accesorio> accesorios) {
        this.setAccesorios(accesorios);
        return this;
    }

    public Venta addAccesorio(Accesorio accesorio) {
        this.accesorios.add(accesorio);
        accesorio.setVenta(this);
        return this;
    }

    public Venta removeAccesorio(Accesorio accesorio) {
        this.accesorios.remove(accesorio);
        accesorio.setVenta(null);
        return this;
    }

    public void setAccesorios(Set<Accesorio> accesorios) {
        if (this.accesorios != null) {
            this.accesorios.forEach(i -> i.setVenta(null));
        }
        if (accesorios != null) {
            accesorios.forEach(i -> i.setVenta(this));
        }
        this.accesorios = accesorios;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public Venta usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", importe=" + getImporte() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
