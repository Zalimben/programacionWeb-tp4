package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una Venta
 * <p>
 * Created by szalimben on 22/09/15.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "venta.findAll", query = "select v from VentaEntity v"),
        @NamedQuery(name = "venta.totalRegisters", query = "select count(v.id) from VentaEntity v"),
        @NamedQuery(name = "venta.findById", query = "select v from VentaEntity v where v.id=:id"),
        @NamedQuery(name = "venta.getVentasFactura", query = "select v from VentaEntity v where v.factura = null")
})
@Table(name = "venta", schema = "public", catalog = "tienda")
public class VentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "seq_venta", sequenceName = "seq_venta")
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    private Long id;

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    @Expose
    private String fecha;

    @ManyToOne(optional = true)
    @JoinColumn(name = "factura_id")
    @Expose
    private FacturaEntity factura;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    @Expose
    private ClienteEntity cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Expose
    private List<VentaDetalleEntity> detalles = new ArrayList<>();

    @Basic
    @Column(name = "monto", nullable = true, insertable = true, updatable = true)
    @Expose
    private String monto;

    public VentaEntity() {
        // Constructor por defecto
    }

    public VentaEntity(Long id, ClienteEntity cliente, String fecha, String monto,FacturaEntity factura) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.monto = monto;
        this.factura = factura;
    }

    // Constructor
    public VentaEntity(ClienteEntity cliente, String fecha, String monto) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.monto = monto;
    }


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


    public FacturaEntity getFactura() {
        return factura;
    }

    public void setFactura(FacturaEntity factura) {
        this.factura = factura;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public List<VentaDetalleEntity> getDetalles() {
        return detalles;
    }

    public void setDetalles(VentaDetalleEntity detalle) {
        this.detalles.add(detalle);
    }
}
