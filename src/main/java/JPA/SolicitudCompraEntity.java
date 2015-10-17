package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Clase que representa una Solicitud de Compra
 * <p>
 * Created by szalimben on 22/09/15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "solicitud.findAll", query = "select v from SolicitudCompraEntity v"),
        @NamedQuery(name = "solicitud.totalRegisters", query = "select count(v.id) from SolicitudCompraEntity v"),
        @NamedQuery(name = "solicitud.findById", query = "select v from SolicitudCompraEntity v where v.id=:id")
})
@Table(name = "solicitud_compra", schema = "public", catalog = "tienda")
public class SolicitudCompraEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    @Expose
    private String fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    @Expose
    private ProductoEntity producto;

    @Column(name = "atendido", nullable = false, insertable = true, updatable = true)
    @Expose
    private boolean atendido;

    public SolicitudCompraEntity() {
        // por defecto
    }

    public SolicitudCompraEntity(ProductoEntity producto, String fecha, boolean atendido) {
        this.fecha = fecha;
        this.producto = producto;
        this.atendido = atendido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }
}
