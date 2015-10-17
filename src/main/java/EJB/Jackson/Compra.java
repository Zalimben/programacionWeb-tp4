package EJB.Jackson;

import java.util.List;

/**
 * Created by alex on 04/10/15.
 */
public class Compra {
    String fecha;
    Integer proveedorId;
    private List<CompraDetalle> detalles;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public List<CompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }


}
