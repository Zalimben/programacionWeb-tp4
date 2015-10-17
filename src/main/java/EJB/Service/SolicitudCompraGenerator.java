package EJB.Service;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import java.io.IOException;


@javax.ejb.Singleton
public class SolicitudCompraGenerator {

    @EJB
    SolicitudCompraService solicitudCompraService;

    /**
     * Metodo para crear la solicitud de compra, este proceso es lanzado cada 3 minutos, y son creados solicitudes para
     * todos aquellos productos cuyo stock sea menor o igual al minimo
     */
    @Schedule(minute = "*/3", hour = "*", persistent = false)
    public void generarSolicitud() throws IOException {

        solicitudCompraService.crear();

    }
}
