package EJB.Service;

import JPA.*;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabil on 24/09/15.
 */

@Stateful
public class FilesService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ClienteEntity cliente;

    @Inject
    private CompraEntity compra;

    @Inject
    private ProveedorEntity proveedor;

    @Inject
    private CompraDetalleEntity compraDetalle;

    @Inject
    private ProductoEntity producto;

    @Inject
    private VentaEntity venta;

    @Inject
    private FacturaEntity factura;

    @Inject
    private VentaDetalleEntity ventaDetalle;

    private List<CompraDetalleEntity> listaCompraDetalles = new ArrayList<>();
    private List<VentaDetalleEntity> listaVentaDetalles = new ArrayList<>();

    /*
    Se almacen los clientes
     */
    public void addCliente(String nombre, String cedula) {
        try {
            cliente.setNombre(nombre);
            cliente.setCedulaIdentidad(cedula);
            em.persist(cliente);
        } catch (Exception e) {
            // se procesa la excepcion
        }
    }

    /*
    Metodos para almacenar compras
     */
    public void addCabeceraCompra(String fecha, String monto, String proveedorId) {
        compra.setFecha(fecha);
        compra.setMonto(monto);
        proveedor = em.find(ProveedorEntity.class, Long.parseLong(proveedorId));
        compra.setProveedor(proveedor);
    }

    public void addCompraDetalle(String productoId, String cantidad) {
        producto = em.find(ProductoEntity.class, Long.parseLong(productoId));
        compraDetalle.setProducto(producto);
        compraDetalle.setCantidad(Long.parseLong(cantidad));
        listaCompraDetalles.add(compraDetalle);
    }

    public void addCompra() {
        for (CompraDetalleEntity detalle : listaCompraDetalles) {
            compra.setDetalles(detalle);
        }
        em.persist(compra);
    }

    /*
    Metodos para almacenar ventas
     */
    public void addCabeceraVenta(String fecha, String facturaId, String clienteId, String monto) {
        venta.setFecha(fecha);
        factura = em.find(FacturaEntity.class, Long.parseLong(facturaId));
        venta.setFactura(factura);
        cliente = em.find(ClienteEntity.class, Long.parseLong(clienteId));
        venta.setCliente(cliente);
        venta.setMonto(monto);
    }

    public void addVentaDetalle(String productoId, String cantidad) {
        producto = em.find(ProductoEntity.class, Long.parseLong(productoId));
        ventaDetalle.setProducto(producto);
        ventaDetalle.setCantidad(Long.parseLong(cantidad));
        listaVentaDetalles.add(ventaDetalle);
    }

    public void addVenta() {
        for (VentaDetalleEntity detalle : listaVentaDetalles) {
            venta.setDetalles(detalle);
        }

        em.persist(venta);
    }

    @Remove
    public void terminarStateful() {
        // este metodo finaliza la instancia creada del stateful bean con el
        // annotation @Remove
    }
}
