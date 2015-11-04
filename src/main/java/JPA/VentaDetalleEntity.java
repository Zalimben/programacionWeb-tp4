package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Clase que representa los detalles que pertenecen a la VentaEntity
 * Created by szalimben on 22/09/15.
 */
@NamedQueries({
      @NamedQuery(name = "ventaDetalle.findDetalleVenta", query = "select vd from VentaDetalleEntity vd where vd.venta = :id")
})
@Entity
@Table(name = "venta_detalle", schema = "public", catalog = "tienda")
public class VentaDetalleEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_venta_detalle")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @SequenceGenerator(name = "seq_venta_detalle", sequenceName = "seq_venta_detalle")
    private Long id;

    @Basic
    @Column(name = "cantidad", nullable = true, insertable = true, updatable = true)
    @Expose
    private Long cantidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    @Expose
    private ProductoEntity producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venta_id")
    @Expose
    private VentaEntity venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    //    public VentaEntity getVenta() {
//        return venta;
//    }
//
    public void setVenta(VentaEntity venta) {
        this.venta = venta;
    }
}
