package REST;

import EJB.Service.SolicitudCompraService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Rest para Solicitudes de Compra
 * Created by szalimben on 28/09/15.
 */
@Path("/solicitudes")
public class SolicitudesCompraRest {

    @EJB
    SolicitudCompraService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVentas(@Context UriInfo info) {
        return Response.status(200).entity(service.getSolicitudes(info.getQueryParameters())).build();
    }
}



