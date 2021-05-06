package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Camiseta.
 */
@Entity
@Table(name = "camiseta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Camiseta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "talla")
    private String talla;

    @Column(name = "color")
    private String color;

    @Column(name = "coleccion")
    private Integer coleccion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "camisetas", "sudaderas", "accesorios", "usuario" }, allowSetters = true)
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Camiseta id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Camiseta stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Camiseta imagen(String imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTalla() {
        return this.talla;
    }

    public Camiseta talla(String talla) {
        this.talla = talla;
        return this;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return this.color;
    }

    public Camiseta color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getColeccion() {
        return this.coleccion;
    }

    public Camiseta coleccion(Integer coleccion) {
        this.coleccion = coleccion;
        return this;
    }

    public void setColeccion(Integer coleccion) {
        this.coleccion = coleccion;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public Camiseta venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Camiseta)) {
            return false;
        }
        return id != null && id.equals(((Camiseta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Camiseta{" +
            "id=" + getId() +
            ", stock=" + getStock() +
            ", imagen='" + getImagen() + "'" +
            ", talla='" + getTalla() + "'" +
            ", color='" + getColor() + "'" +
            ", coleccion=" + getColeccion() +
            "}";
    }
}
