package BackingBeans;

import EJB.Service.ProductoService;
import EJB.Service.ProveedorService;
import JPA.ProductoEntity;
import JPA.ProveedorEntity;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;


@ManagedBean(name = "producto")
@SessionScoped
public class ProductosBean {

	/* Variables & Dependencias */
	@EJB
	ProductoService service;
	@EJB
	ProveedorService proveedorService;
	@Inject
	ProductoEntity productoEntity;

	// Para Modificar
	private ProductoEntity selectedProducto;
	private String stockModificar;
	private Long precioModificar;
	private String descripcionModificar;
	private StreamedContent file;
	private Long proveedor;

	private FacesMessage message;

	/* Metodos */
	/**
	 * Elimina un cliente dado el id del mismo
	 * @param id
	 *         : Id de la entidad a eliminar
	 */
	public void eliminarProducto(long id) {

		FacesContext context = FacesContext.getCurrentInstance();

		try{
			String m = service.deleteProducto((int) id);
			setMessage(new FacesMessage(m));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede eliminar el registro"));
		}

		context.addMessage("messages", message);

	}

	/**
	 * Modificar un cliente
	 */
	public void doEditarProducto() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(validateString(descripcionModificar)) {
			selectedProducto.setDescripcion(descripcionModificar);
		}

		if(validateLong(precioModificar)) {
			selectedProducto.setPrecio(precioModificar);
		}
		try{
			service.update(selectedProducto);
			setMessage(new FacesMessage("Producto modificado exitosamente"));
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede modificar el producto"));
		}
		resetCampos();
		context.addMessage("messages", message);
	}

	/**
	 * Añade un cliente nuevo
	 */
	public void doCrearProducto() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(proveedor != null) {
			ProveedorEntity p = proveedorService.find(proveedor.intValue(), ProveedorEntity.class);
			productoEntity.setProveedor(p);
		}
		try{
			if(service.addProducto(productoEntity)) {
				setMessage(new FacesMessage("Producto creado exitosamente"));
			} else {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos invalidos", "EL producto no se ha creado"));
			}
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede crear el Producto"));
		}
		resetCampos();
		context.addMessage("messages", message);
	}

	public List getAllProducto() {

		return service.getAllProductos();

	}

	/**
	 * Limpia los campos del Formulario de Creacion y Modificacion
	 */
	public void resetCampos() {

		productoEntity = new ProductoEntity();
		setStockModificar(null);
		setDescripcionModificar(null);
		setPrecioModificar(null);
		setProveedor(null);
	}

	/**
	 * Obtiene la lista de todos los proveedores
	 * @return Lista de Proveedores
	 */
	public List getProveedores() {

		return proveedorService.getAllProveedores();

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

	private boolean validateLong (Long number)  {

		return number < 0;
	}

	/* Getter & Setter */

	public ProductoEntity getProductoEntity() {
		return productoEntity;
	}

	public void setProductoEntity(ProductoEntity productoEntity) {
		this.productoEntity = productoEntity;
	}

	public ProductoEntity getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(ProductoEntity selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public Long getPrecioModificar() {
		return precioModificar;
	}

	public void setPrecioModificar(Long precioModificar) {
		this.precioModificar = precioModificar;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public String getDescripcionModificar() {
		return descripcionModificar;
	}

	public void setDescripcionModificar(String descripcionModificar) {
		this.descripcionModificar = descripcionModificar;
	}

	public String getStockModificar() {
		return stockModificar;
	}

	public void setStockModificar(String stockModificar) {
		this.stockModificar = stockModificar;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public void setProveedor(Long proveedor) {
		this.proveedor = proveedor;
	}

	public Long getProveedor() {
		return proveedor;
	}
}
