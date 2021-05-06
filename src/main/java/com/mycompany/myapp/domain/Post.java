package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "jhi_like")
    private Instant like;

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

    public Post id(Long id) {
        this.id = id;
        return this;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Post imagen(String imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Instant getLike() {
        return this.like;
    }

    public Post like(Instant like) {
        this.like = like;
        return this;
    }

    public void setLike(Instant like) {
        this.like = like;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public Post usuario(Usuario usuario) {
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
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", imagen='" + getImagen() + "'" +
            ", like='" + getLike() + "'" +
            "}";
    }
}
