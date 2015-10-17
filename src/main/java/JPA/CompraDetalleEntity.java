package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Clase que representa los detalles que pertenecen a una CompraEntity
 * <p>
 * Created by szalimben on 22/09/15.
 */
@Entity
@Table(name = "compra_detalle", schema = "public", catalog = "tienda")
public class CompraDetalleEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "cantidad", nullable = true, insertable = true, updatable = true)
    @Expose
    private long cantidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "compra_id")
    @Expose
    private CompraEntity compra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    @Expose
    private ProductoEntity producto;

    public CompraDetalleEntity() {
        // Constructor por defecto
    }

    public CompraDetalleEntity(long cantidad, CompraEntity compra, ProductoEntity producto) {
        this.cantidad = cantidad;
        this.compra = compra;
        this.producto = producto;
    }

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

//    public CompraEntity getCompra() {
//        return compra;
//    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }
}
