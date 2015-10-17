package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase que representa una Compra
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "compra.findAll", query = "select v from CompraEntity v"),
        @NamedQuery(name = "compra.totalRegisters", query = "select count(v.id) from CompraEntity v"),
        @NamedQuery(name = "compra.findById", query = "select v from CompraEntity v where v.id=:id")
})
@Table(name = "compra", schema = "public", catalog = "tienda")
public class CompraEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    @Expose
    private String fecha;

    @Basic
    @Column(name = "monto", nullable = true, insertable = true, updatable = true)
    @Expose
    private String monto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id")
    private ProveedorEntity proveedor;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<CompraDetalleEntity> detalles = new ArrayList<>();

    public CompraEntity() {
        // Constructor por defecto
    }

    // Constructor Sin detalles
    public CompraEntity(ProveedorEntity proveedor, String fecha, String monto) {
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.monto = monto;
    }

    // Constructor Sin detalles
    public CompraEntity(Long id, ProveedorEntity proveedor, String fecha, String monto) {
        this.id = id;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.monto = monto;
    }

    // Constructor con detalles
//    public CompraEntity(String fecha, ProveedorEntity proveedor, String monto, List<CompraDetalleEntity> detalles) {
//        this.fecha = fecha;
//        this.proveedor = proveedor;
//        this.monto = monto;
////        this.detalles = detalles;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }

    public List<CompraDetalleEntity> getDetalles() {
        return detalles;
    }

    public void setDetalles(CompraDetalleEntity detalle) {
        this.detalles.add(detalle);
    }
}
