package BackingBeans;

import EJB.Helper.ProductoResponse;
import EJB.Service.ProductoService;
import JPA.ProductoEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 * Created by nabil on 25/10/15.
 */

@ManagedBean
@SessionScoped
public class Producto {

    @EJB
    ProductoService productoService;

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

    public void goNextPage(){
        page+=1;

        if(page<totalPages) {
            page += 1;
//            productoResponse = productoService.getProductos(descripcion, proveedor, stock, precio,
//                    byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);
//
//            productos = productoResponse.getEntidades();
        }
    }

    public void goBackPage(){
        if(page>1) {
            page -= 1;
            productoResponse = productoService.getProductos(descripcion, proveedor, stock, precio,
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

        productoResponse = productoService.getProductos(descripcion, proveedor, stock, precio,
                byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);

        productos = productoResponse.getEntidades();
        return productos;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
//        if(productoResponse == null)
//            productoResponse = productoService.getProductos(descripcion, proveedor, stock, precio,
//                    byAllAttributes, byDescripcion, byProveedor, byStock, byPrecio, page);
//
//        totalPages = productoResponse.getMeta().getTotal_pages().intValue();
        totalPages=2;

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
}
