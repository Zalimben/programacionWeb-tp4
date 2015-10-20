package JPA;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "factura.totalRegisters", query = "select count(f.id) from FacturaEntity f"),
        @NamedQuery(name = "factura.findById", query = "select f from FacturaEntity f where f.id=:id")
})
@Table(name = "factura", schema = "public", catalog = "tienda")
public class FacturaEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "monto", nullable = true, insertable = true, updatable = true)
    @Expose
    private String monto;

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    @Expose
    private String fecha;

    public FacturaEntity() {
        // Constructor por Defecto
    }

    // Constructor con parametros
    public FacturaEntity(String monto, String fecha) {
        this.monto = monto;
        this.fecha = fecha;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
