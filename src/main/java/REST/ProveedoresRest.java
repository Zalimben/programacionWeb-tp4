package REST;

import EJB.Jackson.Proveedor;
import EJB.Service.ProveedorService;
import JPA.ProveedorEntity;
import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

/**
 * Rest para Proveedores
 * Created by szalimben on 28/09/15.
 */
@Path("/proveedores")
public class ProveedoresRest {

    @EJB
    ProveedorService service;

    @GET
    @Path("/exportar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportAllProdveedores(@Context UriInfo info) {
        return Response
                .ok(service.exportAllProdveedores(info.getQueryParameters()))
                .header("Content-Disposition", "attachment; filename=proveedores.json").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVentas(@Context UriInfo info) {
        return Response.status(200).entity(service.getProveedores(info.getQueryParameters())).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVentas() {
        return Response.status(200).entity(service.getAllProveedores()).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCliente(@PathParam("id") int id) {
        return new Gson().toJson(service.deleteProveedor(id));
    }
    @POST
    @Consumes("application/json")
    public Response crearProveedor(String content) {
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Proveedor proveedor = mapper.readValue(content, Proveedor.class);
            ProveedorEntity proveedorEntity = new ProveedorEntity();
            proveedorEntity.setDescripcion(proveedor.getDescripcion());
            service.add(proveedorEntity);

        } catch (IOException e) {
            e.printStackTrace();
            return Response
                    .status(409)
                    .entity(e.getMessage()).build();
        }
        return Response.status(201).build();
    }
}
