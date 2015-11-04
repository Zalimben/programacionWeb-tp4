package BackingBeans;

import EJB.Helper.ComprasResponse;
import EJB.Service.*;
import JPA.CompraDetalleEntity;
import JPA.CompraEntity;
import JPA.ProductoEntity;
import JPA.ProveedorEntity;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ManagedBean(name = "compra")
@SessionScoped
public class ComprasBean {

	/* Variables & Dependencias */
	@EJB
	CompraService service;
    @EJB
    CompraFileService fileService;
    @EJB
	ProveedorService proveedorService;
	@EJB
	ProductoService productoService;
	@EJB
	CompraDetalleService compraDetalleService;
	@Inject
	CompraDetalleEntity detalle;
	@Inject
	CompraEntity compraEntity;
	@Inject
	ProductoEntity productoEntity;

    private StreamedContent exportFile;
    private Long proveedorId;
	private FacesMessage message;

	private List<CompraDetalleEntity> detallesCompra;
	private Long cantidad;
	private Long producto;

	// variables nabil
	private List<CompraEntity> compras;

	private String proveedor;
	private String fecha;
	private String monto;
	private String byAllAttributes;
	private String byProveedor;
	private String byFecha;
	private String byMonto;
	private Integer page=1;
	private Integer totalPages =0;
	private ComprasResponse comprasResponse;
	private CompraEntity selectedCompra;

	/* Metodos */

	// metodos nabil
	public void goNextPage(){
		if(page<totalPages) {
			page += 1;
			comprasResponse = service.getCompras(proveedor, fecha, monto, byAllAttributes,
					byProveedor, byFecha, byMonto, page);
			compras = comprasResponse.getEntidades();
		}
	}

	public void goBackPage(){
		if(page>1) {
			page -= 1;
			comprasResponse = service.getCompras(proveedor,fecha, monto, byAllAttributes,
					byProveedor, byFecha, byMonto, page);
			compras = comprasResponse.getEntidades();
		}
	}


	public void orderByProveedor(){
		if(proveedor==null)
			proveedor = "asc";
		else if(proveedor.equals("asc"))
			proveedor = "desc";
		else
			proveedor = "asc";

		fecha = null;
		monto = null;

		comprasResponse = service.getCompras(proveedor,fecha, monto, byAllAttributes,
				byProveedor, byFecha, byMonto, page);
		compras = comprasResponse.getEntidades();
	}

	public void orderByFecha(){
		if(fecha==null)
			fecha = "asc";
		else if(fecha.equals("asc"))
			fecha = "desc";
		else
			fecha = "asc";

		proveedor = null;
		monto = null;

		comprasResponse = service.getCompras(proveedor,fecha, monto, byAllAttributes,
				byProveedor, byFecha, byMonto, page);
		compras = comprasResponse.getEntidades();
	}

	public void orderByMonto(){
		if(monto==null)
			monto = "asc";
		else if(monto.equals("asc"))
			monto = "desc";
		else
			monto = "asc";

		proveedor = null;
		fecha = null;

		comprasResponse = service.getCompras(proveedor,fecha, monto, byAllAttributes,
				byProveedor, byFecha, byMonto, page);
		compras = comprasResponse.getEntidades();
	}

	public Integer getTotalPages() {
		if(comprasResponse == null)
			comprasResponse = service.getCompras(proveedor,fecha, monto, byAllAttributes,
					byProveedor, byFecha, byMonto, page);

		totalPages = comprasResponse.getMeta().getTotal_pages().intValue();
		return totalPages;
	}

	public void resetPage(){
		page = 1;
	}

	public Integer getPage() {
		return page;
	}

	public String getByAllAttributes() {
		return byAllAttributes;
	}

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
    }

	public String getByProveedor() {
		return byProveedor;
	}

    public void setByProveedor(String byProveedor) {
        this.byProveedor = byProveedor;
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

	public List<CompraEntity> getCompras() {
		comprasResponse = service.getCompras(proveedor,fecha, monto, byAllAttributes,
				byProveedor, byFecha, byMonto, page);
		compras = comprasResponse.getEntidades();
		return compras;
	}


	// metodos saul
	/**
	 * Añade un cliente nuevo
	 */
	public void doCrearCompra() {

		FacesContext context = FacesContext.getCurrentInstance();

		if(proveedorId != null) {
			ProveedorEntity p = proveedorService.find(proveedorId.intValue(), ProveedorEntity.class);
			compraEntity.setProveedor(p);
		}
		try{
			boolean flag = service.createCompra(compraEntity, detallesCompra);
			if(flag) {
				setMessage(new FacesMessage("Compra registrada exitosamente", ""));
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
	 * Obtiene todos los productos de un Proveedor
	 *
	 * @return Lista de Productos
	 */
	public List getProductoByProveedor() {

		if(proveedorId != null) {
			return productoService.getProductosByProveedor(proveedorId.intValue());
		}

		return null;
	}

	/**
	 * Agrega el detalle al Compra Actual
	 *
	 */
	public void addDetalleCompra() {

		if(detallesCompra == null) {
			detallesCompra = new ArrayList<>();
		}

		if(producto != null) {
			detalle.setCantidad(cantidad);
			detalle.setProducto(productoService.find(producto.intValue(), ProductoEntity.class));
			detallesCompra.add(detalle);
		}
		detalle = new CompraDetalleEntity();
	}

	/**
	 * Elimina un elemento de la lista de Detalles de Compra
	 * @param detalle
	 *         Elemento a eliminar
	 */
	public void eliminarCompraDetalle(CompraDetalleEntity detalle) {

		detallesCompra.remove(detalle);

	}

	/**
	 * Limpia los campos del Formulario de Creacion y Modificacion
	 */
	public void resetCampos() {

		setProveedorId(null);
		setCantidad(null);

		setProducto(null);
		compraEntity = new CompraEntity();
		productoEntity = new ProductoEntity();
		detalle = new CompraDetalleEntity();
		detallesCompra = new ArrayList<>();
	}

	/**
	 * Obtiene la lista de todos los proveedores
	 * @return Lista de Proveedores
	 */
	public List getProveedores() {

		return proveedorService.getAllProveedores();

	}

	public List getViewDetalles(Long id) {

		List<CompraDetalleEntity> l = compraDetalleService.getDetallesByCompra(id);

		for(CompraDetalleEntity det : l) {
			selectedCompra.setDetalles(det);
		}

		return l;

	}


	/* Getter & Setter */

	public Long getProveedorId() {
		return proveedorId;
	}

	public void setProveedorId(Long proveedorId) {
		this.proveedorId = proveedorId;
	}



	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public CompraDetalleEntity getDetalle() {
		return detalle;
	}

	public void setDetalle(CompraDetalleEntity detalle) {
		this.detalle = detalle;
	}

	public CompraEntity getCompraEntity() {
		return compraEntity;
	}

	public void setCompraEntity(CompraEntity compraEntity) {
		this.compraEntity = compraEntity;
	}

	public List<CompraDetalleEntity> getDetallesCompra() {
		return detallesCompra;
	}

	public void setDetallesCompra(List<CompraDetalleEntity> detallesCompra) {
		this.detallesCompra = detallesCompra;
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

	public ProductoEntity getProductoEntity() {
		return productoEntity;
	}

	public void setProductoEntity(ProductoEntity productoEntity) {
		this.productoEntity = productoEntity;
	}

    public CompraEntity getSelectedCompra() {
        return selectedCompra;
    }

	public void setSelectedCompra(CompraEntity selectedCompra) {
		this.selectedCompra = selectedCompra;
	}

    public StreamedContent getExportFile() throws FileNotFoundException {
        service.exportAllCompras(proveedor, fecha, monto, byAllAttributes,
                byProveedor, byFecha, byMonto);
        String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType("/tmp/compras.json");
        exportFile = new DefaultStreamedContent(new FileInputStream("/tmp/compras.json"), contentType, "compras.json");
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

