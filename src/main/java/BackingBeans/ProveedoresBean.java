package BackingBeans;

import EJB.Helper.ProveedorResponse;
import EJB.Service.ProveedorService;
import JPA.ProveedorEntity;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;

@ManagedBean(name = "proveedor")
@SessionScoped
public class ProveedoresBean {

	// variables saul

	/* Variables & Dependencias */
	@EJB
	ProveedorService service;

	@Inject
	ProveedorEntity proveedorEntity;

	private ProveedorEntity selectedProveedor;
	private String descripcionModificar;
	private StreamedContent file;

	private FacesMessage message;


	// variables nabil
	private List<ProveedorEntity> proveedores;

	private String descripcion;
	private String byAllAttributes;
	private String byDescripcion;
	private Integer page=1;
	private Integer totalPages=0;
	private ProveedorResponse proveedorResponse;

	// metodos nabil

	public void goNextPage(){
		if(page<totalPages){
			page += 1;
			proveedorResponse = service.getProveedores(descripcion, byAllAttributes,
					byDescripcion,page);

			proveedores = proveedorResponse.getEntidades();
		}
	}

	public void goBackPage(){
		if(page>1) {
			page -= 1;
			proveedorResponse = service.getProveedores(descripcion, byAllAttributes,
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
			proveedorResponse = service.getProveedores(descripcion, byAllAttributes,
					byDescripcion,page);

		totalPages = proveedorResponse.getMeta().getTotal_pages().intValue();
		return totalPages;
	}

	public List<ProveedorEntity> getProveedores() {
		proveedorResponse = service.getProveedores(descripcion, byAllAttributes,
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

		proveedorResponse = service.getProveedores(descripcion, byAllAttributes,
				byDescripcion,page);

		proveedores = proveedorResponse.getEntidades();
	}

	public String getByDescripcion() {
		return byDescripcion;
	}

	public void setByDescripcion(String byDescripcion) {
		this.byDescripcion = byDescripcion;
	}



	// metodos saul

	/* Metodos */
	/**
	 * Elimina un cliente dado el id del mismo
	 * @param id
	 *         : Id de la entidad a eliminar
	 */
	public void eliminarProveedor(long id) {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			String m = service.deleteProveedor((int) id);
			setMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, m, ""));
		} catch(Exception e) {
			setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar el registro", "El proveedor esta siendo utlizado"));
		}

		context.addMessage("messages", message);

	}

	/**
	 * Modificar un cliente
	 */
	public void doEditarProveedor() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(validateString(descripcionModificar)) {
			selectedProveedor.setDescripcion(descripcionModificar);
		}
		try{
			service.update(selectedProveedor);
			setMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor modificado exitosamente", ""));
		} catch(Exception e) {
			setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede modificar el Proveedor", ""));
		}
		resetCampos();
		context.addMessage("messages", message);
	}

	/**
	 * Añade un cliente nuevo
	 */
	public void doCrearProveedor() {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			boolean flag = service.add(proveedorEntity);
			if(flag) {
				setMessage(new FacesMessage("Cliente creado exitosamente", ""));
			} else {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Cliente creado exitosamente", ""));
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

		return !("".equals(data) || data == null) ;

	}

	/**
	 * Limpia los campos del Formulario de Creacion y Modificacion
	 */
	public void resetCampos() {

		proveedorEntity = new ProveedorEntity();
		setDescripcionModificar(null);
	}

	/* Getter & Setter */

	public ProveedorEntity getProveedorEntity() {
		return proveedorEntity;
	}

	public void setProveedorEntity(ProveedorEntity proveedorEntity) {
		this.proveedorEntity = proveedorEntity;
	}

	public ProveedorEntity getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(ProveedorEntity selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public String getDescripcionModificar() {
		return descripcionModificar;
	}

	public void setDescripcionModificar(String descripcionModificar) {
		this.descripcionModificar = descripcionModificar;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}
}
