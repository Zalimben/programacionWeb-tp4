package BackingBeans;

import EJB.Helper.ProveedorResponse;
import EJB.Service.ProveedorService;
import JPA.ProveedorEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * Created by nabil on 25/10/15.
 */

@ManagedBean(name = "proveedor")
@SessionScoped
public class ProveedorBean {

    @EJB
    private ProveedorService proveedorService;

    private List<ProveedorEntity> proveedores;

    private String descripcion;
    private String byAllAttributes;
    private String byDescripcion;
    private Integer page=1;
    private Integer totalPages=0;
    private ProveedorResponse proveedorResponse;



    public void goNextPage(){
        if(page<totalPages){
            page += 1;
            proveedorResponse = proveedorService.getProveedores(descripcion, byAllAttributes,
                    byDescripcion,page);

            proveedores = proveedorResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;
            proveedorResponse = proveedorService.getProveedores(descripcion, byAllAttributes,
                    byDescripcion, page);

            proveedores = proveedorResponse.getEntidades();
        }
    }

    public String getByAllAttributes() {
        return byAllAttributes;
    }

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
    }

    public void resetPage(){
        page = 1;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
        if(proveedorResponse == null)
            proveedorResponse = proveedorService.getProveedores(descripcion, byAllAttributes,
                    byDescripcion,page);

        totalPages = proveedorResponse.getMeta().getTotal_pages().intValue();
        return totalPages;
    }

    public List<ProveedorEntity> getProveedores() {
        proveedorResponse = proveedorService.getProveedores(descripcion, byAllAttributes,
                byDescripcion,page);

        proveedores = proveedorResponse.getEntidades();
        return proveedores;
    }

    public void orderByDescripcion(){
        if(descripcion==null)
            descripcion = "asc";
        else if(descripcion.equals("asc"))
            descripcion = "desc";
        else
            descripcion = "asc";

        proveedorResponse = proveedorService.getProveedores(descripcion, byAllAttributes,
                byDescripcion,page);

        proveedores = proveedorResponse.getEntidades();
    }

    public String getByDescripcion() {
        return byDescripcion;
    }

    public void setByDescripcion(String byDescripcion) {
        this.byDescripcion = byDescripcion;
    }
}
