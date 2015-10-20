package EJB.Service;

import JPA.CompraDetalleEntity;
import JPA.CompraEntity;
import JPA.ProductoEntity;
import JPA.ProveedorEntity;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabil on 10/10/15.
 */

@Stateful
public class CompraFileService {

    private CompraEntity compra = new CompraEntity();

//    @Inject
//    private CompraDetalleEntity compraDetalle;

//    @Inject
//    private ProductoEntity producto;

    @PersistenceContext
    private EntityManager em;


    private JsonFactory jfactory;
    private JsonParser jParser;

    // datos compras.json
    private String fechaCompra;
    private String montoCompra;
    private String proveedorCompra;
    private String productoIdCompra;
    private String cantidadCompra;

    private List<CompraDetalleEntity> listaCompraDetalles = new ArrayList<>();


    /**
     * @param is
     */
    public void parsear(String is) {
        ObjectMapper objectMapper = new ObjectMapper();
        jfactory = objectMapper.getJsonFactory();
        String result = is;
        String archivo = result.substring(result.indexOf('{'));

        try {
            jParser = jfactory.createJsonParser(archivo);

            jParser.nextToken(); // token '{'
            jParser.nextToken(); // token 'compras'

            // se procesa cada compra individualmente, primer token '['
            while (!jParser.getText().equals("]") && jParser.nextToken() != JsonToken.END_ARRAY) {

                String fieldname = jParser.getText();
                if ("fecha".equals(fieldname)) {

                    // token 'fecha'
                    // vamos al siguiente token, el valor de 'fecha'
                    jParser.nextToken();
                    fechaCompra = jParser.getText();
                }

                if ("monto".equals(fieldname)) {

                    // token 'monto'
                    // vamos al siguiente token, el valor de 'monto'
                    jParser.nextToken();
                    montoCompra = jParser.getText();
                }

                if ("proveedor".equals(fieldname)) {

                    // token 'proveedor'
                    // vamos al siguiente token, el valor de 'proveedor'
                    jParser.nextToken();
                    proveedorCompra = jParser.getText();

                    addCabeceraCompra(fechaCompra, montoCompra, proveedorCompra);
                }

                if ("productos".equals(fieldname)) {

                    while (jParser.nextToken() != JsonToken.END_ARRAY) {
                        String field = jParser.getText();

                        if ("producto".equals(field)) {

                            // token 'producto'
                            // vamos al siguiente token, el valor de 'producto'
                            jParser.nextToken();
                            productoIdCompra = jParser.getText();
                        }

                        if ("cantidad".equals(field)) {

                            // token 'cantidad'
                            // vamos al siguiente token, el valor de 'cantidad'
                            jParser.nextToken();
                            cantidadCompra = jParser.getText();

                            // como es el ultimo campo procesamos el producto en persistencia
                            addCompraDetalle(productoIdCompra, cantidadCompra);
                        }
                    }
                }
            }
            // termino el file entonces persistimos
            addCompra();
            terminarStateful();
        } catch (Exception e) {
            // Procesamos la excepcion
        }
    }

    /*
    Metodos para almacenar compras
     */
    public void addCabeceraCompra(String fecha, String monto, String proveedorId) {
        compra.setFecha(fecha);
        compra.setMonto(monto);
        ProveedorEntity proveedor;
        proveedor = em.find(ProveedorEntity.class, Long.parseLong(proveedorId));
        compra.setProveedor(proveedor);
    }

    public void addCompraDetalle(String productoId, String cantidad) {
        ProductoEntity producto;
        producto = em.find(ProductoEntity.class, Long.parseLong(productoId));

        CompraDetalleEntity compraDetalle = new CompraDetalleEntity();
        compraDetalle.setCompra(compra);
        compraDetalle.setProducto(producto);
        compraDetalle.setCantidad(Long.parseLong(cantidad));

        listaCompraDetalles.add(compraDetalle);
    }

    public void addCompra() {
        for (CompraDetalleEntity detalle : listaCompraDetalles) {
            compra.setDetalles(detalle);
        }
        try {
            em.persist(compra);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Remove
    public void terminarStateful() {
        // este metodo finaliza la instancia creada del stateful bean con el
        // annotation @Remove
    }
}
