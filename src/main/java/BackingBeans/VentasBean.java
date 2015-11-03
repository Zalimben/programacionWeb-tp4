package BackingBeans;

import EJB.Service.ClienteService;
import EJB.Service.ProductoService;
import EJB.Service.VentasService;
import JPA.ClienteEntity;
import JPA.ProductoEntity;
import JPA.VentaDetalleEntity;
import JPA.VentaEntity;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@ManagedBean(name = "venta")
@SessionScoped
public class VentasBean {

	/* Variables & Dependencias */
	@EJB
	VentasService service;
	@Inject
	VentaEntity ventaEntity;
	@EJB
	ClienteService clienteService;
	@EJB
	ProductoService productoService;
	@Inject
	VentaDetalleEntity detalle;
	@Inject
	ProductoEntity productoEntity;

	private StreamedContent file;
	private Long cliente;
	private FacesMessage message;

	private List<VentaDetalleEntity> detallesVenta;
	private Long cantidad;
	private Long producto;

	/* Metodos */
	public void doCrearVenta() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(cliente != null) {
			ClienteEntity c = clienteService.find(cliente.intValue(), ClienteEntity.class);
		}
		try{
			boolean flag = service.createVenta(ventaEntity, detallesVenta);
			if(flag) {
				setMessage(new FacesMessage("Compra registrada exitosamente"));
			} else {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos invalidos", "La compra no se ha creado"));
			}
		} catch(Exception e) {
			setMessage(new FacesMessage("No se puede crear la Compra"));
		}
		resetCampos();
		context.addMessage("messages", message);

	}

	/**
	 * Agrega el detalle al Compra Actual
	 *
	 */
	public void addDetalleVenta() {

		if(detallesVenta == null) {
			detallesVenta = new ArrayList<>();
		}

		if(producto != null) {
			detalle.setCantidad(cantidad);
			detalle.setProducto(productoService.find(producto.intValue(), ProductoEntity.class));
			detallesVenta.add(detalle);
		}

	}

	/**
	 * Elimina un elemento de la lista de Detalles de Compra
	 * @param detalle
	 *         Elemento a eliminar
	 */
	public void eliminarCompraDetalle(VentaDetalleEntity detalle) {

		detallesVenta.remove(detalle);
		resetCampos();

	}

	/**
	 * Limpia los campos del Formulario de Creacion y Modificacion
	 */
	public void resetCampos() {

		setCliente(null);
		setCantidad(null);
		setProducto(null);
		ventaEntity = new VentaEntity();
		productoEntity = new ProductoEntity();
		detalle = new VentaDetalleEntity();
		detallesVenta = new ArrayList<>();
	}

	/**
	 * Obtiene la lista de todos los proveedores
	 * @return Lista de Proveedores
	 */
	public List getClientes() {

		return clienteService.getAllClientes();

	}

	/* Getter & Setter */

	public VentaEntity getVentaEntity() {
		return ventaEntity;
	}

	public void setVentaEntity(VentaEntity ventaEntity) {
		this.ventaEntity = ventaEntity;
	}

	public VentaDetalleEntity getDetalle() {
		return detalle;
	}

	public void setDetalle(VentaDetalleEntity detalle) {
		this.detalle = detalle;
	}

	public ProductoEntity getProductoEntity() {
		return productoEntity;
	}

	public void setProductoEntity(ProductoEntity productoEntity) {
		this.productoEntity = productoEntity;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Long getCliente() {
		return cliente;
	}

	public void setCliente(Long cliente) {
		this.cliente = cliente;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public List<VentaDetalleEntity> getDetallesVenta() {
		return detallesVenta;
	}

	public void setDetallesVenta(List<VentaDetalleEntity> detallesVenta) {
		this.detallesVenta = detallesVenta;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public Long getProducto() {
		return producto;
	}

	public void setProducto(Long producto) {
		this.producto = producto;
	}
}
