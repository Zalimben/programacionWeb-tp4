package BackingBeans;

import EJB.Helper.ClienteResponse;
import EJB.Service.ClienteService;
import JPA.ClienteEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

/**
 * Created by nabil on 23/10/15.
 */

@ManagedBean
@SessionScoped
public class Cliente {

    @EJB
    private ClienteService clienteService;

    private List<ClienteEntity> clientes;

    private String nombre;
    private String cedulaIdentidad;
    private String by_all_attributes;
    private String by_nombre;
    private String by_cedula;
    private Integer page=1;
    private Integer totalPages=0;
    private ClienteResponse  clienteResponse;

    public Integer getTotalPages() {
        if(clienteResponse == null)
            clienteResponse = clienteService.getClientes(nombre, cedulaIdentidad, by_all_attributes,
                    by_nombre, by_cedula, page);

        totalPages = clienteResponse.getMeta().getTotal_pages().intValue();

        return totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedulaIdentidad() {
        return cedulaIdentidad;
    }

    public void setCedulaIdentidad(String cedulaIdentidad) {
        this.cedulaIdentidad = cedulaIdentidad;
    }

    public String getBy_all_attributes() {
        return by_all_attributes;
    }

    public void setBy_all_attributes(String by_all_attributes) {
        this.by_all_attributes = by_all_attributes;
    }

    public String getBy_nombre() {
        return by_nombre;
    }

    public void setBy_nombre(String by_nombre) {
        this.by_nombre = by_nombre;
    }


    public String getBy_cedula() {
        return by_cedula;
    }

    public void setBy_cedula(String by_cedula) {
        this.by_cedula = by_cedula;
    }

    public List<ClienteEntity> getClientes() {
        clienteResponse = clienteService.getClientes(nombre, cedulaIdentidad, by_all_attributes,
                by_nombre, by_cedula, page);

        clientes = clienteResponse.getEntidades();

        return clientes;
    }

    public void goNextPage(){
        if(page<totalPages) {
            page += 1;
            clienteResponse = clienteService.getClientes(nombre, cedulaIdentidad, by_all_attributes,
                    by_nombre, by_cedula, page);

            clientes = clienteResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;

            clienteResponse = clienteService.getClientes(nombre, cedulaIdentidad, by_all_attributes,
                    by_nombre, by_cedula, page);

            clientes = clienteResponse.getEntidades();
        }
    }

    public void resetPage(){
        page = 1;
    }
}
