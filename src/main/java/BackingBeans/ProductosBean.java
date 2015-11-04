package BackingBeans;

import EJB.Helper.ProductoResponse;
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

	// variables saul
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
	private Long proveedorId;

	private FacesMessage message;


    // variables nabil
    private List<ProductoEntity> productos;

    private String descripcion;
    private String proveedor;
    private String stock;
    private String precio;
    private String byAllAttributes;
    private String byDescripcion;
    private String byProveedor;
    private String byStock;
    private String byPrecio;
    private Integer page=1;
    private Integer totalPages=0;
    private ProductoResponse productoResponse;


    // metodos nabil
	public void orderByDescripcion(){
		if(descripcion==null)
			descripcion="asc";
		else if(descripcion.equals("asc"))
			descripcion="desc";
		else
			descripcion="asc";

		proveedor=null;
		productoResponse = service.getProductos(descripcion, proveedor, stock, precio,
				byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

		productos = productoResponse.getEntidades();
	}

	public void orderByProveedor(){
		if(proveedor==null)
			proveedor="asc";
		else if(proveedor.equals("asc"))
			proveedor="desc";
		else
			proveedor="asc";

		descripcion=null;
		productoResponse = service.getProductos(descripcion, proveedor, stock, precio,
				byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

		productos = productoResponse.getEntidades();
	}

    public void goNextPage(){
        if(page<totalPages) {
            page += 1;
            productoResponse = service.getProductos(descripcion, proveedor, stock, precio,
                    byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

            productos = productoResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;
            productoResponse = service.getProductos(descripcion, proveedor, stock, precio,
                    byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

            productos = productoResponse.getEntidades();
        }
    }

    public void resetPage(){
        page = 1;
    }


    public List<ProductoEntity> getProductos() {
//        byStock="52";
//        byProveedor="Vipe";
//        byPrecio="16778";

        productoResponse = service.getProductos(descripcion, proveedor, stock, precio,
                byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

        productos = productoResponse.getEntidades();
        return productos;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
        if(productoResponse == null)
            productoResponse = service.getProductos(descripcion, proveedor, stock, precio,
                    byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

        totalPages = productoResponse.getMeta().getTotal_pages().intValue();
        return totalPages;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getByAllAttributes() {
        return byAllAttributes;
    }

    public void setByAllAttributes(String byAllAttributes) {
        this.byAllAttributes = byAllAttributes;
    }

    public String getByDescripcion() {
        return byDescripcion;
    }

    public void setByDescripcion(String byDescripcion) {
        this.byDescripcion = byDescripcion;
    }

    public String getByProveedor() {
        return byProveedor;
    }

    public void setByProveedor(String byProveedor) {
        this.byProveedor = byProveedor;
    }

    public String getByStock() {
        return byStock;
    }

    public void setByStock(String byStock) {
        this.byStock = byStock;
    }

    public String getByPrecio() {
        return byPrecio;
    }

    public void setByPrecio(String byPrecio) {
        this.byPrecio = byPrecio;
    }


    // metodos saul

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

		if(proveedorId != null) {
			ProveedorEntity p = proveedorService.find(proveedorId.intValue(), ProveedorEntity.class);
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
		setProveedorId(null);
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

	public void setProveedorId(Long proveedorId) {
		this.proveedorId = proveedorId;
	}

	public Long getProveedorId() {
		return proveedorId;
	}
}
