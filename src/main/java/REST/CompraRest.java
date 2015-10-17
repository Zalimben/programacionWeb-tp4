package REST;

import EJB.Jackson.Compra;
import EJB.Service.CompraFileService;
import EJB.Service.CompraService;
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

/**
 * Rest para Compras
 * <p>
 * Created by szalimben on 28/09/15.
 */
@Path("/compras")
public class CompraRest {

    @EJB
    CompraService service;

    @EJB
    CompraFileService compraFileService;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompras(@Context UriInfo info) {
        return Response.status(200).entity(service.getCompras(info.getQueryParameters())).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompra(@PathParam("id") int id) {
        return Response.status(200).entity(service.getCompra(id)).build();
    }

    @GET
    @Path("/solicitudes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComprasSolicitudes(@Context UriInfo info) {
        return Response.status(200).entity(service.getSolictudes(info.getQueryParameters())).build();
    }

    @GET
    @Path("/exportar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportAllCompras(@Context UriInfo info) {
        return Response
                .ok(service.exportAllCompras(info.getQueryParameters()))
                .header("Content-Disposition", "attachment; filename=compras.json").build();
    }

    @POST
    @Consumes("application/json")
    public Response crearCompras(String content) {
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Compra compra = mapper.readValue(content, Compra.class);
            service.addCompra(compra);

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

    @POST
    @Path("/uploadFileCompras")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("fileCompra") InputStream is) {
        String result = getStringFromInputStream(is);
        compraFileService.parsear(result);

        return Response.status(200).entity("ok").build();
    }

}
