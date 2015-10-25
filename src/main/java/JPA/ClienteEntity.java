package JPA;

import com.google.gson.annotations.Expose;

import javax.ejb.LocalBean;
import javax.persistence.*;
import java.util.List;

/**
 * Clase que representa un Cliente
 * <p>
 * Created by szalimben on 22/09/15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "cliente.findAll", query = "select v from ClienteEntity v"),
        @NamedQuery(name = "cliente.totalRegisters", query = "select count(v.id) from ClienteEntity v"),
        @NamedQuery(name = "cliente.findById", query = "select v from ClienteEntity v where v.id=:id")
})
@Table(name = "cliente", schema = "public", catalog = "tienda")
public class ClienteEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "nombre", nullable = true, insertable = true, updatable = true, length = 100)
    @Expose
    private String nombre;

    @Basic
    @Column(name = "cedula_identidad", nullable = false, insertable = true, updatable = true)
    @Expose
    private String cedulaIdentidad;

    @OneToMany(mappedBy = "cliente")
    private List<VentaEntity> ventas;

    public ClienteEntity() {
        // Constructor por defecto
    }

    public ClienteEntity(Long id, String nombre, String cedulaIdentidad) {
        this.id = id;
        this.nombre = nombre;
        this.cedulaIdentidad = cedulaIdentidad;
    }

    // Constructor sin ventas
    public ClienteEntity(String nombre, String cedulaIdentidad) {
        this.nombre = nombre;
        this.cedulaIdentidad = cedulaIdentidad;
    }

    // Constructor con ventas
    public ClienteEntity(String nombre, String cedulaIdentidad, List<VentaEntity> ventas) {
        this.nombre = nombre;
        this.cedulaIdentidad = cedulaIdentidad;
        this.ventas = ventas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedulaIdentidad() {
        return cedulaIdentidad;
    }

    public void setCedulaIdentidad(String cedulaIdentidad) {
        this.cedulaIdentidad = cedulaIdentidad;
    }

}
