package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase que representa un Producto
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "producto.findAll", query = "select p from ProductoEntity p"),
        @NamedQuery(name = "producto.totalRegisters", query = "select count(p.id) from ProductoEntity p"),
        @NamedQuery(name = "producto.findById", query = "select p from ProductoEntity p where p.id=:id")
})
@Table(name = "producto", schema = "public", catalog = "tienda")
public class ProductoEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "stock", nullable = true, insertable = true, updatable = true)
    @Expose
    private long stock;

    @Basic
    @Column(name = "precio", nullable = true, insertable = true, updatable = true)
    @Expose
    private long precio;

    @Basic
    @Column(name = "descripcion", nullable = true, insertable = true, updatable = true, length = 100)
    @Expose
    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id")
    private ProveedorEntity proveedor;

    @OneToMany(mappedBy = "producto")
    private List<SolicitudCompraEntity> solicitudes = new ArrayList<>();

    public ProductoEntity() {
        // Constructor por defecto
    }

    public ProductoEntity(Long id, ProveedorEntity proveedor, long stock, long precio, String descripcion) {
        this.id = id;
        this.stock = stock;
        this.descripcion = descripcion;
        this.precio = precio;
        this.proveedor = proveedor;
    }

    public ProductoEntity(long stock, String descripcion, long precio) {
        this.stock = stock;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public ProductoEntity(ProveedorEntity proveedor, long stock, long precio, String descripcion) {
        this.stock = stock;
        this.descripcion = descripcion;
        this.precio = precio;
        this.proveedor = proveedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

//    public List<SolicitudCompraEntity> getSolicitudes() {
//        return solicitudes;
//    }
//
//    public void setSolicitudes(List<SolicitudCompraEntity> solicitudes) {
//        this.solicitudes = solicitudes;
//    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }
}
