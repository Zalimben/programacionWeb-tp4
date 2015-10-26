package BackingBeans;

import EJB.Helper.ComprasResponse;
import EJB.Service.CompraService;
import JPA.CompraEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * Created by nabil on 26/10/15.
 */

@ManagedBean(name = "compra")
@SessionScoped
public class CompraBean {

    @EJB
    private CompraService compraService;
    private List<CompraEntity> compras;

    private String proveedor;
    private String fecha;
    private String monto;
    private String byAllAttributes;
    private String byProveedor;
    private String byFecha;
    private String byMonto;
    private Integer page=1;
    private Integer totalPages =0;
    private ComprasResponse comprasResponse;

    public void goNextPage(){
        if(page<totalPages) {
            page += 1;
            comprasResponse = compraService.getCompras(proveedor, fecha, monto, byAllAttributes,
                    byProveedor, byFecha, byMonto, page);
            compras = comprasResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;
            comprasResponse = compraService.getCompras(proveedor,fecha, monto, byAllAttributes,
                    byProveedor, byFecha, byMonto, page);
            compras = comprasResponse.getEntidades();
        }
    }


    public void orderByProveedor(){
        if(proveedor==null)
            proveedor = "asc";
        else if(proveedor.equals("asc"))
            proveedor = "desc";
        else
            proveedor = "asc";

        fecha = null;
        monto = null;

        comprasResponse = compraService.getCompras(proveedor,fecha, monto, byAllAttributes,
                byProveedor, byFecha, byMonto, page);
        compras = comprasResponse.getEntidades();
    }

    public void orderByFecha(){
        if(fecha==null)
            fecha = "asc";
        else if(fecha.equals("asc"))
            fecha = "desc";
        else
            fecha = "asc";

        proveedor = null;
        monto = null;

        comprasResponse = compraService.getCompras(proveedor,fecha, monto, byAllAttributes,
                byProveedor, byFecha, byMonto, page);
        compras = comprasResponse.getEntidades();
    }

    public void orderByMonto(){
        if(monto==null)
            monto = "asc";
        else if(monto.equals("asc"))
            monto = "desc";
        else
            monto = "asc";

        proveedor = null;
        fecha = null;

        comprasResponse = compraService.getCompras(proveedor,fecha, monto, byAllAttributes,
                byProveedor, byFecha, byMonto, page);
        compras = comprasResponse.getEntidades();
    }

    public Integer getTotalPages() {
        if(comprasResponse == null)
            comprasResponse = compraService.getCompras(proveedor,fecha, monto, byAllAttributes,
                    byProveedor, byFecha, byMonto, page);

        totalPages = comprasResponse.getMeta().getTotal_pages().intValue();
        return totalPages;
    }

    public void resetPage(){
        page = 1;
    }

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
    }

    public void setByProveedor(String byProveedor) {
        this.byProveedor = byProveedor;
    }

    public void setByFecha(String byFecha) {
        this.byFecha = byFecha;
    }

    public void setByMonto(String byMonto) {
        this.byMonto = byMonto;
    }

    public Integer getPage() {
        return page;
    }

    public String getByAllAttributes() {
        return byAllAttributes;
    }

    public String getByProveedor() {
        return byProveedor;
    }

    public String getByFecha() {
        return byFecha;
    }

    public String getByMonto() {
        return byMonto;
    }

    public List<CompraEntity> getCompras() {
        comprasResponse = compraService.getCompras(proveedor,fecha, monto, byAllAttributes,
                byProveedor, byFecha, byMonto, page);
        compras = comprasResponse.getEntidades();
        return compras;
    }
}
