package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "proveedor.findAll", query = "select v from ProveedorEntity v"),
        @NamedQuery(name = "proveedor.totalRegisters", query = "select count(v.id) from ProveedorEntity v"),
        @NamedQuery(name = "proveedor.findById", query = "select v from ProveedorEntity v where v.id=:id")
})
@Table(name = "proveedor", schema = "public", catalog = "tienda")
public class ProveedorEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "descripcion", nullable = true, insertable = true, updatable = true, length = 100)
    @Expose
    private String descripcion;


    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private List<ProductoEntity> productos;


    public ProveedorEntity() {
        // Constructor por defecto
    }

    public ProveedorEntity(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public ProveedorEntity(String descripcion) {
        this.descripcion = descripcion;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
