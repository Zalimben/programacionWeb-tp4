package BackingBean;

import EJB.Service.ClienteService;
import JPA.ClienteEntity;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * backingBeans de Cliente
 *
 * Created by szalimben on 25/10/15.
 */

@ManagedBean(name = "clientes")
@SessionScoped
public class ClientesBean {

    private static final String redirectTo = "http://localhost:8080/tp4/faces/views/clientes/";
    private static final String ABM = "abm.xhtml";
    private static final String LIST = "list.xhtml";
    private static final String CARGA = "carga_masiva.xhtml";
    @Inject
	ClienteEntity cliente;
	@EJB
	ClienteService service;
    private FacesMessage message;

	/* Metodos */
	/**
	 * Añade un cliente nuevo
	 */
    public void doCrearCliente() {

		try{
			service.add(cliente);
			resetCampos();
			FacesContext context = FacesContext.getCurrentInstance();
			message = new FacesMessage("Cliente creado exitosamente");
			context.addMessage("messages", message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* RedirectTO */
	public String crearCliente() {

		return redirectTo.concat(ABM);
	}

	public String listaCliente() {
		return redirectTo.concat(LIST);
	}

	public String cargaMasiva() {
		return redirectTo.concat(CARGA);
	}

	public void resetCampos() {

		cliente.setNombre(null);
		cliente.setCedulaIdentidad(null);
	}

	/* Getter & Setter */
	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

}
