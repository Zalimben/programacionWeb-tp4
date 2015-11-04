package BackingBeans;

import EJB.Helper.VentasResponse;
import EJB.Service.ClienteService;
import EJB.Service.ProductoService;
import EJB.Service.VentaDetalleService;
import EJB.Service.VentasService;
import JPA.*;
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

	// variables saul
	/* Variables & Dependencias */
	@EJB
	VentasService service;
	@Inject
	VentaEntity ventaEntity;
	@EJB
	ClienteService clienteService;
	@EJB
	VentaDetalleService ventaDetalleService;
	@EJB
	ProductoService productoService;
	@Inject
	VentaDetalleEntity detalle;
	@Inject
	ProductoEntity productoEntity;

	private StreamedContent file;
	private Long clienteId;
	private FacesMessage message;

	private List<VentaDetalleEntity> detallesVenta;
	private Long cantidad;
	private Long producto;

	// variables nabil
	private List<VentaEntity> ventas;

	private String cliente;
	private String fecha;
	private String monto;
	private String byAllAttributes;
	private String byCliente;
	private String byFecha;
	private String byMonto;
	private Integer page=1;
	private Integer totalPages=0;
	private VentasResponse ventasResponse;
	private VentaEntity selectedVenta;

	// variables nabil
	public void goNextPage(){
		if(page<totalPages) {
			page += 1;
			ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
					byFecha, byMonto, page);
			ventas = ventasResponse.getEntidades();
		}
	}

	public void goBackPage(){
		if(page>1) {
			page -= 1;
			ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
					byFecha, byMonto, page);
			ventas = ventasResponse.getEntidades();
		}
	}


	public void orderByCliente(){
		if(cliente==null)
			cliente = "asc";
		else if(cliente.equals("asc"))
			cliente = "desc";
		else
			cliente = "asc";

		fecha = null;
		monto = null;

		ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
				byFecha, byMonto, page);
		ventas = ventasResponse.getEntidades();
	}

	public void orderByFecha(){
		if(fecha==null)
			fecha="asc";
		else if(fecha.equals("asc"))
			fecha="desc";
		else
			fecha="asc";

		cliente=null;
		monto=null;

		ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
				byFecha, byMonto, page);
		ventas = ventasResponse.getEntidades();
	}

	public void orderByMonto(){
		if(monto==null)
			monto="asc";
		else if(monto.equals("asc"))
			monto="desc";
		else
			monto="asc";

		cliente=null;
		fecha=null;

		ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
				byFecha, byMonto, page);
		ventas = ventasResponse.getEntidades();
	}


	public void resetPage(){
		page = 1;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getTotalPages() {
		if(ventasResponse == null)
			ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
					byFecha, byMonto, page);
		totalPages = ventasResponse.getMeta().getTotal_pages().intValue();
		return totalPages;
	}

	public String getByAllAttributes() {
		return byAllAttributes;
	}

	public void setByAllAttributes(String byAllAttributes) {
		this.byAllAttributes = byAllAttributes;
	}

	public String getByCliente() {
		return byCliente;
	}

	public void setByCliente(String byCliente) {
		this.byCliente = byCliente;
	}

	public String getByFecha() {
		return byFecha;
	}

	public void setByFecha(String byFecha) {
		this.byFecha = byFecha;
	}

	public String getByMonto() {
		return byMonto;
	}

	public void setByMonto(String byMonto) {
		this.byMonto = byMonto;
	}

	public List<VentaEntity> getVentas() {
		ventasResponse = service.getVentas(cliente, fecha, monto, byAllAttributes, byCliente,
				byFecha, byMonto, page);
		ventas = ventasResponse.getEntidades();
		return ventas;
	}

	// variables saul
	/* Metodos */
	public void doCrearVenta() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(clienteId != null) {
			ClienteEntity c = clienteService.find(clienteId.intValue(), ClienteEntity.class);
			ventaEntity.setCliente(c);
		}
		try{
			boolean flag = service.createVenta(ventaEntity, detallesVenta);
			if(flag) {
				setMessage(new FacesMessage("Venta registrada exitosamente", "Hola..."));
			} else {
				setMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos invalidos", "La venta no se ha creado"));
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

		detalle = new VentaDetalleEntity();
	}

	/**
	 * Elimina un elemento de la lista de Detalles de Compra
	 * @param detalle
	 *         Elemento a eliminar
	 */
	public void eliminarCompraDetalle(VentaDetalleEntity detalle) {

		detallesVenta.remove(detalle);

	}

	public List getViewDetalles(Long id) {

		List<VentaDetalleEntity> l = ventaDetalleService.getDetallesByVenta(id);

		for(VentaDetalleEntity det : l) {
			selectedVenta.setDetalles(det);
		}

		return l;

	}

	/**
	 * Limpia los campos del Formulario de Creacion y Modificacion
	 */
	public void resetCampos() {

		setClienteId(null);
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

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
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

	public void setSelectedVenta(VentaEntity selectedVenta) {
		this.selectedVenta = selectedVenta;
	}

	public VentaEntity getSelectedVenta() {
		return selectedVenta;
	}
}
