package BackingBeans;

import EJB.Helper.FacturasResponse;
import EJB.Service.FacturaService;
import JPA.FacturaEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * Created by nabil on 01/11/15.
 */

@ManagedBean
@SessionScoped
public class Factura {

    @EJB
    private FacturaService facturaService;

    private List<FacturaEntity> facturas;

    private String fecha;
    private String monto;
    private String byAllAttributes;
    private String byFecha;
    private String byMonto;
    private Integer page=1;
    private Integer totalPages=0;
    private FacturasResponse facturasResponse;

    public Integer getPage() {
        return page;
    }

    public String getByAllAttributes() {
        return byAllAttributes;
    }

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
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

    public Integer getTotalPages() {
        if(facturasResponse == null)
            facturasResponse = facturaService.getFacturas(fecha, monto, byAllAttributes, byFecha,
                    byMonto, page);

        totalPages = facturasResponse.getMeta().getTotal_pages().intValue();

        return totalPages;
    }

    public void orderByFecha(){
        if(fecha==null)
            fecha="asc";
        else if(fecha.equals("asc"))
            fecha="desc";
        else
            fecha="asc";

        monto=null;
        facturasResponse = facturaService.getFacturas(fecha, monto, byAllAttributes, byFecha,
                byMonto, page);

        facturas = facturasResponse.getEntidades();
    }

    public void orderByMonto(){
        if(monto==null)
            monto="asc";
        else if(monto.equals("asc"))
            monto="desc";
        else
            monto="asc";

        fecha=null;
        facturasResponse = facturaService.getFacturas(fecha, monto, byAllAttributes, byFecha,
                byMonto, page);

        facturas = facturasResponse.getEntidades();
    }

    public void goNextPage() {
        if(page<totalPages) {
            page += 1;
            facturasResponse = facturaService.getFacturas(fecha, monto, byAllAttributes, byFecha,
                    byMonto, page);

            facturas = facturasResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;
            facturasResponse = facturaService.getFacturas(fecha, monto, byAllAttributes, byFecha,
                    byMonto, page);

            facturas = facturasResponse.getEntidades();
        }
    }

    public void resetPage(){
        page = 1;
    }

    public List<FacturaEntity> getFacturas() {
        facturasResponse = facturaService.getFacturas(fecha, monto, byAllAttributes, byFecha,
                byMonto, page);

        facturas = facturasResponse.getEntidades();
        return facturas;
    }
}
