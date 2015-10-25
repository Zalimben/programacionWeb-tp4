package BackingBeans;

import EJB.Helper.ClienteResponse;
import EJB.Service.ClienteService;
import JPA.ClienteEntity;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
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

    private static final String redirectTo = "http://localhost:8080/tp4/faces/views/clientes/";
    private static final String ABM = "abm.xhtml";
    private static final String LIST = "list.xhtml";
    private static final String CARGA = "carga_masiva.xhtml";
    @Inject
	ClienteEntity clienteEntity;
	@EJB
	ClienteService clienteService;
	private List<ClienteEntity> clientes;

	private String nombre;
	private String cedulaIdentidad;
	private String by_all_attributes;
	private String by_nombre;
	private String by_cedula;
	private Integer page=1;
	private Integer totalPages=0;
	private ClienteResponse clienteResponse;

	// Para Modificar
	private ClienteEntity selectedCliente;
	private String nombreModificar;
	private String cedulaModificar;
    private StreamedContent file;

	private FacesMessage message;

	/* Metodos */

	public void preCreate() {

		clienteEntity = new ClienteEntity();
	}

	public void eliminarCliente() {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			clienteService.deleteCliente(selectedCliente.getId().intValue());
			setMessage(new FacesMessage("Se ha eliminado el registro"));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede eliminar el registro"));
		}

		selectedCliente = new ClienteEntity();
		context.addMessage("messages", message);

	}

	/**
	 * Modificar un cliente
	 */
	public void doEditarCliente() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(validateString(nombreModificar)) {
			selectedCliente.setNombre(nombreModificar);
		}

		if(validateString(cedulaModificar)) {
			selectedCliente.setCedulaIdentidad(cedulaModificar);
		}
		try{
			clienteService.update(selectedCliente);
			setMessage(new FacesMessage("Cliente modificado exitosamente"));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede modificar el cliente"));
		}
		resetCampos();
		context.addMessage("messages", message);
	}

	private boolean validateString (String data) {

		if("".equals(data) || data == null ) {
			return false;
		}

		return true;

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
		setNombreModificar(null);
		setCedulaModificar(null);
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

    public FacesMessage getMessage() {
        return message;
    }

	public void setMessage(FacesMessage message) {
		this.message = message;
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

	public ClienteEntity getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(ClienteEntity selectedCliente) {
		this.selectedCliente = selectedCliente;
	}

    public String getCedulaModificar() {
        return cedulaModificar;
    }

	public void setCedulaModificar(String cedulaModificar) {
		this.cedulaModificar = cedulaModificar;
	}

    public String getNombreModificar() {
        return nombreModificar;
    }

	public void setNombreModificar(String nombreModificar) {
		this.nombreModificar = nombreModificar;
	}

    public StreamedContent getFile() throws IOException {
        clienteService.exportAllClientes(nombre, cedulaIdentidad, by_all_attributes, by_nombre, by_cedula, page);
        String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType("/tmp/clientes.json");
        file = new DefaultStreamedContent(new FileInputStream("/tmp/clientes.json"), contentType, "clientes.json");
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
