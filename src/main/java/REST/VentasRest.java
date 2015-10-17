package REST;

import EJB.Jackson.Venta;
import EJB.Service.FilesService;
import EJB.Service.VentaFileService;
import EJB.Service.VentasService;
import EJB.Util.StockInsuficienteException;
import com.sun.jersey.multipart.FormDataParam;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@Path("/ventas")
public class VentasRest {

    @EJB
    VentaFileService ventaFileService;

    @EJB
    FilesService filesService;
    @EJB
    VentasService ventasService;

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllVentas() {
//        return Response.status(200).entity(ventasService.getVentas()).build();
//    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVenta(@PathParam("id") int id) {
        return Response.status(200).entity(ventasService.getVenta(id)).build();
    }

    @GET
    @Path("/exportar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportAllVentas(@Context UriInfo info) {
        return Response
                .ok(ventasService.exportAllVentas(info.getQueryParameters()))
                .header("Content-Disposition", "attachment; filename=ventas.json").build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVentas() {
        return Response.status(200).entity(ventasService.getAllVentas()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVentas(@Context UriInfo info) {
        return Response.status(200).entity(ventasService.getVentas(info.getQueryParameters())).build();
    }

//
//
//    @POST
//    @Path("/uploadFileVentas")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadFile(@FormDataParam("file") InputStream is) {
//        jfactory = new JsonFactory();
//
//        try {
//            jParser = jfactory.createParser(is);
//
//            jParser.nextToken(); // token '{'
//            jParser.nextToken(); // token 'ventas'
//
//            // se procesa cada compra individualmente, primer token '['
//            while (jParser.nextToken() != JsonToken.END_ARRAY) {
//
//                String fieldname = jParser.getCurrentName();
//                if ("fecha".equals(fieldname)) {
//
//                    // token 'fecha'
//                    // vamos al siguiente token, el valor de 'fecha'
//                    jParser.nextToken();
//                    fechaVenta = jParser.getText();
//                }
//
//                if ("factura".equals(fieldname)) {
//
//                    // token 'factura'
//                    // vamos al siguiente token, el valor de 'factura'
//                    jParser.nextToken();
//                    facturaIdVenta = jParser.getText();
//                }
//
//                if ("cliente".equals(fieldname)) {
//
//                    // token 'cliente'
//                    // vamos al siguiente token, el valor de 'cliente'
//                    jParser.nextToken();
//                    clienteIdVenta = jParser.getText();
//                }
//
//                if ("monto".equals(fieldname)) {
//
//                    // token 'monto'
//                    // vamos al siguiente token, el valor de 'monto'
//                    jParser.nextToken();
//                    montoVenta = jParser.getText();
//
//                    filesService.addCabeceraVenta(fechaVenta, facturaIdVenta,
//                            clienteIdVenta, montoVenta);
//                }
//
//                if ("ventas".equals(fieldname)) {
//
//                    while (jParser.nextToken() != JsonToken.END_ARRAY) {
//                        String field = jParser.getCurrentName();
//
//                        if ("producto".equals(field)) {
//
//                            // token 'producto'
//                            // vamos al siguiente token, el valor de 'producto'
//                            jParser.nextToken();
//                            productoIdVenta = jParser.getText();
//                        }
//
//                        if ("cantidad".equals(field)) {
//
//                            // token 'cantidad'
//                            // vamos al siguiente token, el valor de 'cantidad'
//                            jParser.nextToken();
//                            cantidadVenta = jParser.getText();
//
//                            // como es el ultimo campo procesamos el producto en persistencia
//                            filesService.addVentaDetalle(productoIdVenta, cantidadVenta);
//                        }
//                    }
//                }
//            }
//            // termino el file entonces persistimos
//            filesService.addVenta();
//            filesService.terminarStateful();
//        }catch(Exception e){
//            // Procesamos la excepcion
//        }
//
//        return Response.status(200).entity("ok").build();
//    }
//




    @POST
    @Path("/uploadFileVentas")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("fileVenta") InputStream is) {
        String result = getStringFromInputStream(is);
        ventaFileService.parsear(result);

        return Response.status(200).entity("ok").build();
    }

    @POST
    @Consumes("application/json")
    public Response crearVentas(String content) {
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Venta venta = mapper.readValue(content, Venta.class);
            System.out.println(venta.getClienteId());
            ventasService.addVenta(venta);

        } catch (IOException e) {
            e.printStackTrace();
            return Response
                    .status(409)
                    .entity(e.getMessage()).build();
        } catch (StockInsuficienteException e) {
            return Response
                    .status(409)
                    .entity(e.getMessage()).build();
        }
        return Response.status(201).build();
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

}
