package EJB.Jackson;

/**
 * Created by alex on 04/10/15.
 */
public class Producto {
    private Integer proveedorId;
    private Integer precio;
    private String descripcion;

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer stock) {
        this.proveedorId = stock;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
