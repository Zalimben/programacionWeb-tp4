package EJB.Service;

import JPA.*;
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
public class VentaFileService {

    @PersistenceContext
    private EntityManager em;

    private VentaEntity venta = new VentaEntity();

    private JsonFactory jfactory;
    private JsonParser jParser;

    // datos ventas.json
    private String fechaVenta;
    private String facturaIdVenta;
    private String clienteIdVenta;
    private String montoVenta;
    private String productoIdVenta;
    private String cantidadVenta;

    private List<VentaDetalleEntity> listaVentaDetalles = new ArrayList<>();

    public void parsear(String is){
        ObjectMapper objectMapper = new ObjectMapper();
        jfactory = objectMapper.getJsonFactory();
        String result = is;
        String archivo = result.substring(result.indexOf('{'));

        try {
            jParser = jfactory.createJsonParser(archivo);

            jParser.nextToken(); // token '{'
            jParser.nextToken(); // token 'ventas'

            // se procesa cada compra individualmente, primer token '['
            while (!jParser.getText().equals("]") && jParser.nextToken() != JsonToken.END_ARRAY) {

                String fieldname = jParser.getText();
                if ("fecha".equals(fieldname)) {

                    // token 'fecha'
                    // vamos al siguiente token, el valor de 'fecha'
                    jParser.nextToken();
                    fechaVenta = jParser.getText();
                }

                if ("factura".equals(fieldname)) {

                    // token 'factura'
                    // vamos al siguiente token, el valor de 'factura'
                    jParser.nextToken();
                    facturaIdVenta = jParser.getText();
                }

                if ("cliente".equals(fieldname)) {

                    // token 'cliente'
                    // vamos al siguiente token, el valor de 'cliente'
                    jParser.nextToken();
                    clienteIdVenta = jParser.getText();
                }

                if ("monto".equals(fieldname)) {

                    // token 'monto'
                    // vamos al siguiente token, el valor de 'monto'
                    jParser.nextToken();
                    montoVenta = jParser.getText();

                    addCabeceraVenta(fechaVenta, facturaIdVenta,
                            clienteIdVenta, montoVenta);
                }

                if ("ventas".equals(fieldname)) {

                    while (jParser.nextToken() != JsonToken.END_ARRAY) {
                        String field = jParser.getText();

                        if ("producto".equals(field)) {

                            // token 'producto'
                            // vamos al siguiente token, el valor de 'producto'
                            jParser.nextToken();
                            productoIdVenta = jParser.getText();
                        }

                        if ("cantidad".equals(field)) {

                            // token 'cantidad'
                            // vamos al siguiente token, el valor de 'cantidad'
                            jParser.nextToken();
                            cantidadVenta = jParser.getText();

                            // como es el ultimo campo procesamos el producto en persistencia
                            addVentaDetalle(productoIdVenta, cantidadVenta);
                        }
                    }
                }
            }
            // termino el file entonces persistimos
            addVenta();
            terminarStateful();
        }catch(Exception e){
            // Procesamos la excepcion
        }
    }

    /*
    Metodos para almacenar ventas
     */
    public void addCabeceraVenta(String fecha, String facturaId, String clienteId, String monto){
        venta.setFecha(fecha);

        FacturaEntity factura;
        factura = em.find(FacturaEntity.class, Long.parseLong(facturaId));
        venta.setFactura(factura);

        ClienteEntity cliente;
        cliente = em.find(ClienteEntity.class, Long.parseLong(clienteId));
        venta.setCliente(cliente);

        venta.setMonto(monto);
    }

    public void addVentaDetalle(String productoId, String cantidad){
        ProductoEntity producto;
        producto = em.find(ProductoEntity.class, Long.parseLong(productoId));

        VentaDetalleEntity ventaDetalle = new VentaDetalleEntity();
        ventaDetalle.setVenta(venta);
        ventaDetalle.setProducto(producto);
        ventaDetalle.setCantidad(Long.parseLong(cantidad));

        listaVentaDetalles.add(ventaDetalle);
    }

    public void addVenta() {
        for (VentaDetalleEntity detalle : listaVentaDetalles) {
            venta.setDetalles(detalle);
        }

        try{
            em.persist(venta);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Remove
    public void terminarStateful(){
        // este metodo finaliza la instancia creada del stateful bean con el
        // annotation @Remove
    }
}
