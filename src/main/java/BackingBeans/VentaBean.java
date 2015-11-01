package BackingBeans;

import EJB.Helper.VentasResponse;
import EJB.Service.VentasService;
import JPA.VentaEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * Created by nabil on 26/10/15.
 */

@ManagedBean(name = "venta")
@SessionScoped
public class VentaBean {

    @EJB
    private VentasService ventasService;
    private List<VentaEntity> ventas;

    private String cliente;
    private String fecha;
    private String monto;
    private String byAllAttributes;
    private String byCliente;
    private String byFecha;
    private String byMonto;
    private Integer page=1;
    private Integer totalPages=0;
    private VentasResponse ventasResponse;

    public void goNextPage(){
        if(page<totalPages) {
            page += 1;
            ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                    byFecha, byMonto, page);
            ventas = ventasResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;
            ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                    byFecha, byMonto, page);
            ventas = ventasResponse.getEntidades();
        }
    }


    public void orderByCliente(){
        if(cliente==null)
            cliente = "asc";
        else if(cliente.equals("asc"))
            cliente = "desc";
        else
            cliente = "asc";

        fecha = null;
        monto = null;

        ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                byFecha, byMonto, page);
        ventas = ventasResponse.getEntidades();
    }

    public void orderByFecha(){
        if(fecha==null)
            fecha="asc";
        else if(fecha.equals("asc"))
            fecha="desc";
        else
            fecha="asc";

        cliente=null;
        monto=null;

        ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                byFecha, byMonto, page);
        ventas = ventasResponse.getEntidades();
    }

    public void orderByMonto(){
        if(monto==null)
            monto="asc";
        else if(monto.equals("asc"))
            monto="desc";
        else
            monto="asc";

        cliente=null;
        fecha=null;

        ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                byFecha, byMonto, page);
        ventas = ventasResponse.getEntidades();
    }


    public void resetPage(){
        page = 1;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
        if(ventasResponse == null)
            ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                    byFecha, byMonto, page);
        totalPages = ventasResponse.getMeta().getTotal_pages().intValue();
        return totalPages;
    }

    public String getByAllAttributes() {
        return byAllAttributes;
    }

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
    }

    public String getByCliente() {
        return byCliente;
    }

    public void setByCliente(String byCliente) {
        this.byCliente = byCliente;
    }

    public String getByFecha() {
        return byFecha;
    }

    public void setByFecha(String byFecha) {
        this.byFecha = byFecha;
    }

    public String getByMonto() {
        return byMonto;
    }

    public void setByMonto(String byMonto) {
        this.byMonto = byMonto;
    }

    public List<VentaEntity> getVentas() {
        ventasResponse = ventasService.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
                byFecha, byMonto, page);
        ventas = ventasResponse.getEntidades();
        return ventas;
    }
}
