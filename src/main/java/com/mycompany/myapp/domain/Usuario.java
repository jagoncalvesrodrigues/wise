package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono")
    private Integer telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "cp")
    private Integer cp;

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuario" }, allowSetters = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "camisetas", "sudaderas", "accesorios", "usuario" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Usuario nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Usuario apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Usuario correo(String correo) {
        this.correo = correo;
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public Usuario telefono(Integer telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Usuario direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCp() {
        return this.cp;
    }

    public Usuario cp(Integer cp) {
        this.cp = cp;
        return this;
    }

    public void setCp(Integer cp) {
        this.cp = cp;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public Usuario posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public Usuario addPost(Post post) {
        this.posts.add(post);
        post.setUsuario(this);
        return this;
    }

    public Usuario removePost(Post post) {
        this.posts.remove(post);
        post.setUsuario(null);
        return this;
    }

    public void setPosts(Set<Post> posts) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setUsuario(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setUsuario(this));
        }
        this.posts = posts;
    }

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public Usuario ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Usuario addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.setUsuario(this);
        return this;
    }

    public Usuario removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.setUsuario(null);
        return this;
    }

    public void setVentas(Set<Venta> ventas) {
        if (this.ventas != null) {
            this.ventas.forEach(i -> i.setUsuario(null));
        }
        if (ventas != null) {
            ventas.forEach(i -> i.setUsuario(this));
        }
        this.ventas = ventas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", telefono=" + getTelefono() +
            ", direccion='" + getDireccion() + "'" +
            ", cp=" + getCp() +
            "}";
    }
}
