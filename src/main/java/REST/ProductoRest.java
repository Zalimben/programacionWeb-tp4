package REST;

import EJB.Jackson.Producto;
import EJB.Service.ProductoService;
import EJB.Service.ProveedorService;
import JPA.ProductoEntity;
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
 * Rest para Producto
 * Created by szalimben on 28/09/15.
 */
@Path("/productos")
public class ProductoRest {

    @EJB
    ProductoService service;

    @EJB
    ProveedorService proveedorService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductos(@Context UriInfo info) {
        return Response.status(200).entity(service.getProductos(info.getQueryParameters())).build();
    }

    @GET
    @Path("/proveedor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductosByProveedor(@PathParam("id") int id) {
        return Response.status(200).entity(service.getProductosByProveedor(id)).build();
    }

    @GET
    @Path("/exportar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportAllProductos(@Context UriInfo info) {
        return Response
                .ok(service.exportAllProductos(info.getQueryParameters()))
                .header("Content-Disposition", "attachment; filename=productos.json").build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCliente(@PathParam("id") int id) {
        return new Gson().toJson(service.deleteProducto(id));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducto(@PathParam("id") int id) {
        return Response.status(200).entity(service.getProducto(id)).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProductos() {
        return Response.status(200).entity(service.getAllProductos()).build();
    }

    @POST
    @Consumes("application/json")
    public Response crearProducto(String content) {
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Producto producto = mapper.readValue(content, Producto.class);
            ProductoEntity productoEntity = new ProductoEntity();
            productoEntity.setProveedor(proveedorService.find(producto.getProveedorId(), ProveedorEntity.class));
            productoEntity.setDescripcion(producto.getDescripcion());
            service.add(productoEntity);

        } catch (IOException e) {
            e.printStackTrace();
            return Response
                    .status(409)
                    .entity(e.getMessage()).build();
        }
        return Response.status(201).build();
    }
}
