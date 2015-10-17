package REST;

import EJB.Service.FacturaService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Rest para Factura
 * <p>
 * Created by szalimben on 28/09/15.
 */
@Path("/facturas")
public class FacturaRest {

    @EJB
    FacturaService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFacturas(@Context UriInfo info) {
        return Response.status(200).entity(service.getFacturas(info.getQueryParameters())).build();
    }

    @GET
    @Path("/facturar")
    @Produces(MediaType.APPLICATION_JSON)
    public String facturar() {
        return service.facturar();
    }

    @GET
    @Path("/isRun")
    @Produces(MediaType.APPLICATION_JSON)
    public String isRun() {
        return service.isRun();
    }

    @GET
    @Path("/detener")
    @Produces(MediaType.APPLICATION_JSON)
    public String detener() {
        return service.detener();
    }

}
