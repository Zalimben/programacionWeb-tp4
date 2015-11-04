package BackingBeans;

import EJB.Helper.SolicitudCompraResponse;
import EJB.Service.SolicitudCompraService;
import JPA.SolicitudCompraEntity;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

@ManagedBean(name = "solicitud")
@SessionScoped
public class SolicitudesBean {


	/* Variables & Dependencias */
	@EJB
	SolicitudCompraService service;

	@Inject
	SolicitudCompraEntity solicitudCompraEntity;

	// variables nabil
	private List<SolicitudCompraEntity> solicitudes;

	private String producto;
	private String fecha;
	private String byAllAttributes;
	private String byProducto;
	private String byFecha;
	private Integer page=1;
	private Integer totalPages=0;
    private StreamedContent exportFile;
    private SolicitudCompraResponse solicitudCompraResponse;

	/* Metodos */

    /**
     * Avanza a la siguiente Pagina
     */
    public void goNextPage(){
        if(page<totalPages) {
            page += 1;
            solicitudCompraResponse = service.getSolicitudes(producto, fecha, byAllAttributes,
                    byProducto, byFecha, page);
            solicitudes = solicitudCompraResponse.getEntidades();
        }
    }

    /**
     * Retrocede una pagina
     */
    public void goBackPage(){
        if(page>1) {
            page -= 1;
            solicitudCompraResponse = service.getSolicitudes(producto, fecha, byAllAttributes,
                    byProducto, byFecha, page);
            solicitudes = solicitudCompraResponse.getEntidades();
        }
    }

    public void resetPage(){
        page = 1;
    }

    public void orderByFecha(){
        if(fecha==null)
            fecha="asc";
        else if(fecha.equals("asc"))
            fecha="desc";
        else
            fecha="asc";
        producto=null;
        solicitudCompraResponse = service.getSolicitudes(producto, fecha, byAllAttributes,
                byProducto, byFecha, page);
        solicitudes = solicitudCompraResponse.getEntidades();
    }

    public void orderByProducto(){
        if(producto==null)
            producto="asc";
        else if(producto.equals("asc"))
            producto="desc";
        else
            producto="asc";
        fecha=null;
        solicitudCompraResponse = service.getSolicitudes(producto, fecha, byAllAttributes,
                byProducto, byFecha, page);
        solicitudes = solicitudCompraResponse.getEntidades();
    }

    public List<SolicitudCompraEntity> getSolicitudes() {
        solicitudCompraResponse = service.getSolicitudes(producto, fecha, byAllAttributes,
                byProducto, byFecha, page);
        solicitudes = solicitudCompraResponse.getEntidades();
        return solicitudes;
    }


    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
        if(solicitudCompraResponse==null)
            solicitudCompraResponse = service.getSolicitudes(producto, fecha, byAllAttributes,
                    byProducto, byFecha, page);
        totalPages = solicitudCompraResponse.getMeta().getTotal_pages().intValue();
        return totalPages;
    }

    public String getByAllAttributes() {
        return byAllAttributes;
    }

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
    }

    public String getByProducto() {
        return byProducto;
    }

    public void setByProducto(String byProducto) {
        this.byProducto = byProducto;
    }

    public String getByFecha() {
        return byFecha;
    }

    public void setByFecha(String byFecha) {
        this.byFecha = byFecha;
    }

}
