package BackingBeans;

import EJB.Helper.ClienteResponse;
import EJB.Service.ClienteService;
import JPA.ClienteEntity;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * BackingBeans de Cliente
 *
 * Created by szalimben on 25/10/15.
 */

@ManagedBean(name = "cliente")
@SessionScoped
public class ClientesBean implements Serializable {

	@Inject
	ClienteEntity clienteEntity;

	@EJB
	ClienteService clienteService;

	private static final String redirectTo = "http://localhost:8080/tp4/faces/views/clientes/";
	private static final String ABM = "abm.xhtml";
	private static final String LIST = "list.xhtml";
	private static final String CARGA = "carga_masiva.xhtml";

	private List<ClienteEntity> clientes;

	private String nombre;
	private String cedulaIdentidad;
	private String by_all_attributes;
	private String by_nombre;
	private String by_cedula;
	private Integer page=1;
	private Integer totalPages=0;
	private ClienteResponse clienteResponse;

	private FacesMessage message;

	/* Metodos */

	public void preCreate() {

		clienteEntity = new ClienteEntity();
	}

	/**
	 * Añade un cliente nuevo
	 */
	public void doCrearCliente() {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			clienteService.add(clienteEntity);
				setMessage(new FacesMessage("Cliente creado exitosamente"));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede crear el cliente"));
		}
		resetCampos();
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

		clienteEntity.setNombre(null);
		clienteEntity.setCedulaIdentidad(null);
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

	/* Getter & Setter */
	public ClienteEntity getClienteEntity() {

		if(clienteEntity.getNombre() == null) {
			setClienteEntity(new ClienteEntity("", ""));
		}

		return clienteEntity;
	}

	public void setClienteEntity(ClienteEntity cliente) {
		this.clienteEntity = cliente;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public FacesMessage getMessage() {
		return message;
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

	public Integer getTotalPages() {
		if(clienteResponse == null)
			clienteResponse = clienteService.getClientes(nombre, cedulaIdentidad, by_all_attributes,
			                                             by_nombre, by_cedula, page);

		totalPages = clienteResponse.getMeta().getTotal_pages().intValue();

		return totalPages;
	}
}
