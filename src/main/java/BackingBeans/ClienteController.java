package BackingBeans;

import EJB.Service.ClienteService;
import JPA.ClienteEntity;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * BackingBeans de Cliente
 *
 * Created by szalimben on 25/10/15.
 */

@ManagedBean(name = "cliente")
@SessionScoped
public class ClienteController implements Serializable {

	@Inject
	ClienteEntity cliente;

	@EJB
	ClienteService service;

	private static final String redirectTo = "http://localhost:8080/tp4/faces/views/clientes/";
	private static final String ABM = "abm.xhtml";
	private static final String LIST = "list.xhtml";
	private static final String CARGA = "carga_masiva.xhtml";

	private FacesMessage message;

	/* Metodos */
	/**
	 * Añade un cliente nuevo
	 */
	public void crear() {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			service.add(cliente);
			resetCampos();
			setMessage(new FacesMessage("Cliente creado exitosamente"));

		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede crear el cliente"));
		}
		context.addMessage("messages", message);
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

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public FacesMessage getMessage() {
		return message;
	}
}
