package BackingBeans;

import EJB.Helper.ClienteResponse;
import EJB.Service.ClienteFileService;
import EJB.Service.ClienteService;
import JPA.ClienteEntity;
import org.primefaces.event.FileUploadEvent;
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

    @Inject
	ClienteEntity clienteEntity;
	@EJB
	ClienteService service;
    @EJB
    ClienteFileService fileService;
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
    private StreamedContent exportFile;

	private FacesMessage message;

	/* Metodos */
	/**
	 * Elimina un cliente dado el id del mismo
	 * @param id
	 *         : Id de la entidad a eliminar
	 */
	public void eliminarCliente(long id) {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			String m = service.deleteCliente((int)id);
			setMessage(new FacesMessage(m));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede eliminar el registro", ""));
		}

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
			boolean flag = service.update(selectedCliente);
			if(flag) {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente modificado exitosamente", ""));
			} else {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede modificar el cliente:", "Los datos no son validos"));
			}
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede modificar el cliente"));
		}
		resetCampos();
		context.addMessage("messages", message);
	}

	/**
	 * Añade un cliente nuevo
	 */
	public void doCrearCliente() {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			boolean flag = service.add(clienteEntity);
			if(flag) {
				setMessage(new FacesMessage("Cliente creado exitosamente", ""));
			} else {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede crear el cliente:", "CI Duplicado"));
			}
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede crear el cliente"));
		}
		resetCampos();
		context.addMessage("messages", message);
	}

	/**
	 * Verifica que un String sea valido
	 *
	 * @param data: String que sera verificado
	 * @return <b>True</b> si es valido <br />
	 *          <b>False</b> caso contrario
	 */
	private boolean validateString (String data) {

		return !("".equals(data) || data == null);

	}

	/**
	 * Limpia los campos del Formulario de Creacion y Modificacion
	 */
	public void resetCampos() {

		clienteEntity = new ClienteEntity();
		setNombreModificar(null);
		setCedulaModificar(null);
	}

	public List getAllClientes() {

		return service.getAllClientes();
	}

	/**
	 * Lista de Clientes
	 * @return Obtiene la lista de Clientes de forma Lazy
	 */
	public List<ClienteEntity> getClientes() {
		clienteResponse = service.getClientes(nombre, cedulaIdentidad, by_all_attributes,
		                                             by_nombre, by_cedula, page);

		clientes = clienteResponse.getEntidades();

		return clientes;
	}

	/**
	 * Avanza a la siguiente Pagina
	 */
	public void goNextPage(){
		if(page<totalPages) {
			page += 1;
			clienteResponse = service.getClientes(nombre, cedulaIdentidad, by_all_attributes,
			                                             by_nombre, by_cedula, page);

			clientes = clienteResponse.getEntidades();
		}
	}

	/**
	 * Retrocede una pagina
	 */
	public void goBackPage(){
		if(page>1) {
			page -= 1;

			clienteResponse = service.getClientes(nombre, cedulaIdentidad, by_all_attributes,
			                                             by_nombre, by_cedula, page);
			clientes = clienteResponse.getEntidades();
		}
	}

	/**
	 * Inicializa las paginas
	 */
	public void resetPage(){
		page = 1;
	}

	/**
	 * Ordenacion por la columna del nombre
	 */
	public void orderByNombre(){

		if(nombre==null)
			nombre = "asc";
		else if(nombre.equals("asc"))
			nombre = "desc";
		else
			nombre = "asc";

		cedulaIdentidad = null;
		clienteResponse = service.getClientes(nombre, null, by_all_attributes,
		                                             by_nombre, by_cedula, page);

		clientes = clienteResponse.getEntidades();
	}

	/**
	 * Ordenacion por la columna de cedula
	 */
	public void orderByCedula(){
		if(cedulaIdentidad==null)
			cedulaIdentidad = "asc";
		else if(cedulaIdentidad.equals("asc"))
			cedulaIdentidad = "desc";
		else
			cedulaIdentidad = "asc";

		nombre = null;
		clienteResponse = service.getClientes(null, cedulaIdentidad, by_all_attributes,
		                                             by_nombre, by_cedula, page);
		clientes = clienteResponse.getEntidades();
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
			clienteResponse = service.getClientes(nombre, cedulaIdentidad, by_all_attributes,
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

    public StreamedContent getExportFile() throws IOException {
        service.exportAllClientes(nombre, cedulaIdentidad, by_all_attributes, by_nombre, by_cedula);
        String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType("/tmp/clientes.json");
        exportFile = new DefaultStreamedContent(new FileInputStream("/tmp/clientes.json"), contentType, "clientes.json");
        return exportFile;
    }

    public void setExportFile(StreamedContent exportFile) {
        this.exportFile = exportFile;
    }

    public void upload(FileUploadEvent event) {
        try {
            fileService.parsear(event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
