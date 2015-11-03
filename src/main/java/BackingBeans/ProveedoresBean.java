package BackingBeans;

import EJB.Service.ProveedorService;
import JPA.ProveedorEntity;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean(name = "proveedor")
@SessionScoped
public class ProveedoresBean {

	/* Variables & Dependencias */
	@EJB
	ProveedorService service;

	@Inject
	ProveedorEntity proveedorEntity;

	private ProveedorEntity selectedProveedor;
	private String descripcionModificar;
	private StreamedContent file;

	private FacesMessage message;

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
			setMessage(new FacesMessage(m));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede eliminar el registro"));
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
			setMessage(new FacesMessage("Cliente modificado exitosamente"));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede modificar el cliente"));
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
			service.add(proveedorEntity);
			setMessage(new FacesMessage("Cliente creado exitosamente"));
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
