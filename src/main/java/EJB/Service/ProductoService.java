package EJB.Service;

import EJB.Helper.ProductoResponse;
import JPA.ProductoEntity;
import JPA.ProveedorEntity;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicios para la gestion de Productos
 * <p>
 * Created by szalimben on 23/09/15.
 */
@Stateless
public class ProductoService extends Service<ProductoEntity> {
    @EJB
    ProductoService productoService;

    public String deleteProducto(int id) {
        return delete(id, ProductoEntity.class);
    }

    /**
     * Metodo para obtener los productos cuyo stock sean menores al stock minimo
     *
     * @param stockMinimo Cantidad minima del stock que se desea mantenar
     * @return Lista de productos que
     */
    @SuppressWarnings("unchecked")
    public List<ProductoEntity> getProductosCantidadMinima(int stockMinimo) {

        long stock = ((long) stockMinimo);
        Query query = em.createQuery("SELECT p FROM ProductoEntity p WHERE p.stock <= :stockMinimo");
        query.setParameter("stockMinimo", stock);
        return query.getResultList();

    }

    /**
     * Metodo para obtener la lista de todos los productos que posee un proveedor
     *
     * @param id Identificador del ProveedorEntity
     * @return Lista de los productos que pertenecen al proveedor pasado como parametro
     */
    @SuppressWarnings("unchecked")
    public List<ProductoEntity> getProductosByProveedor(int id) {

        ProveedorEntity proveedor = em.find(ProveedorEntity.class, (long) id);

        Query query = em.createQuery("SELECT p FROM ProductoEntity  p WHERE p.proveedor = :proveedor");
        query.setParameter("proveedor", proveedor);

        List<ProductoEntity> productos = query.getResultList();
        if (productos.isEmpty()) {
            return new ArrayList<>();
        }
        return productos;

    }

    /**
     * Cantidad de Registros
     *
     * @return Cantidad total de Registros
     */
    public Long getCount() {
        Query query = em.createNamedQuery("producto.totalRegisters");
        Long count = (Long) query.getSingleResult();
        return count;
    }

    public List getAllProductos() {
        Query query = em.createNamedQuery("producto.findAll");
        return query.getResultList();
    }

    /**
     * Retorna la entidad buscada por Id
     *
     * @param id Identificador del elemento buscado
     * @return Elemento cuyo identificador corresponda
     */
    public Object getProducto(int id) {
        return find(id, ProductoEntity.class);
    }


    public Object exportAllProductos(MultivaluedMap<String, String> queryParams) {

        ProductoResponse response = new ProductoResponse();
        ObjectMapper mapper = new ObjectMapper();
        String file = "/tmp/productos.json";

        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (queryParams.getFirst("proveedor.descipcion") != null) {
            ordenarPorColumna = "proveedor";
            ordenDeOrdenacion = queryParams.getFirst("proveedor.descripcion");
        } else if (queryParams.getFirst("stock") != null) {
            ordenarPorColumna = "stock";
            ordenDeOrdenacion = queryParams.getFirst("stock");
        } else if (queryParams.getFirst("precio") != null) {
            ordenarPorColumna = "precio";
            ordenDeOrdenacion = queryParams.getFirst("precio");
        } else if (queryParams.getFirst("descripcion") != null) {
            ordenarPorColumna = "descripcion";
            ordenDeOrdenacion = queryParams.getFirst("descripcion");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_stock = queryParams.getFirst("by_stock");
        String by_proveedor = queryParams.getFirst("by_proveedor.descripcion");
        String by_precio = queryParams.getFirst("by_precio");
        String by_descripcion = queryParams.getFirst("by_descripcion");

        if (by_proveedor == null) {
            by_proveedor = "";
        }


        if (by_descripcion == null) {
            by_descripcion = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProductoEntity> criteriaQuery = criteriaBuilder.createQuery(ProductoEntity.class);
        Root<ProductoEntity> productos = criteriaQuery.from(ProductoEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes;

        if (by_stock != null && by_precio != null) {
            filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                    criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"),
                    criteriaBuilder.equal(productos.<Long>get("precio"), by_precio),
                    criteriaBuilder.equal(productos.<Long>get("stock"), by_stock));
        } else {
            if (by_precio == null && by_stock == null) {
                filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                        criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"));
                ;
            } else if (by_precio != null) {
                filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                        criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"),
                        criteriaBuilder.equal(productos.<Long>get("precio"), by_precio));

            } else {
                filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                        criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"),
                        criteriaBuilder.equal(productos.<Long>get("stock"), by_stock));
            }
        }

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_descripcion + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(productos.<String>get("proveedor"), productos.<String>get("precio"), productos.<String>get("stock"), productos.<String>get("descripcion"));

            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(productos.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(productos.<String>get("proveedor"), productos.<String>get("precio"), productos.<String>get("stock"), productos.<String>get("descripcion"));

            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(productos.get(ordenarPorColumna)));
        }


        response.setEntidades(em.createQuery(criteriaQuery).getResultList());
        try {

            File fileResponse = new File(file);
            // convert user object to json string, and save to a file
            mapper.writeValue(fileResponse, response.getEntidades());

            return fileResponse;

        } catch (JsonGenerationException e) {

            e.printStackTrace();

        } catch (JsonMappingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return response;

    }

    public Object getProductos(MultivaluedMap<String, String> queryParams) {

        ProductoResponse response = new ProductoResponse();
        inicializarMeta();


        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (queryParams.getFirst("proveedor.descripcion") != null) {
            ordenarPorColumna = "proveedor";
            ordenDeOrdenacion = queryParams.getFirst("proveedor.descripcion");
        } else if (queryParams.getFirst("stock") != null) {
            ordenarPorColumna = "stock";
            ordenDeOrdenacion = queryParams.getFirst("stock");
        } else if (queryParams.getFirst("precio") != null) {
            ordenarPorColumna = "precio";
            ordenDeOrdenacion = queryParams.getFirst("precio");
        } else if (queryParams.getFirst("descripcion") != null) {
            ordenarPorColumna = "descripcion";
            ordenDeOrdenacion = queryParams.getFirst("descripcion");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_stock = queryParams.getFirst("by_stock");
        String by_proveedor = queryParams.getFirst("by_proveedor.descripcion");
        String by_precio = queryParams.getFirst("by_precio");
        String by_descripcion = queryParams.getFirst("by_descripcion");

        if (by_proveedor == null) {
            by_proveedor = "";
        }


        if (by_descripcion == null) {
            by_descripcion = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProductoEntity> criteriaQuery = criteriaBuilder.createQuery(ProductoEntity.class);
        Root<ProductoEntity> productos = criteriaQuery.from(ProductoEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes;

        if (by_stock != null && by_precio != null) {
            filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                    criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"),
                    criteriaBuilder.equal(productos.<Long>get("precio"), Integer.parseInt(by_precio)),
                    criteriaBuilder.equal(productos.<Long>get("stock"), Integer.parseInt(by_stock)));
        } else {
            if (by_precio == null && by_stock == null) {
                filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                        criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"));
                ;
            } else if (by_precio != null) {
                filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                        criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"),
                        criteriaBuilder.equal(productos.<Long>get("precio"), by_precio));

            } else {
                filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                        criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_all_attributes + "%"),
                        criteriaBuilder.equal(productos.<Long>get("stock"), by_stock));
            }
        }

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(productos.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                criteriaBuilder.like(productos.<String>get("descripcion"), "%" + by_descripcion + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(productos.<String>get("proveedor"), productos.<Long>get("stock"), productos.<Long>get("precio"), productos.<String>get("descripcion"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(productos.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(productos.<String>get("proveedor"), productos.<Long>get("stock"), productos.<Long>get("precio"), productos.<String>get("descripcion"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(productos.get(ordenarPorColumna)));
        }

        Integer page;
        if (queryParams.getFirst("page") != null) {
            page = Integer.valueOf(queryParams.getFirst("page")) - 1;
        } else {
            page = 0;
        }

        response.setEntidades(em.createQuery(criteriaQuery).setMaxResults(getMeta().getPage_size().intValue()).setFirstResult(page * getMeta().getPage_size().intValue()).getResultList());
        getMeta().setTotal((long) em.createQuery(criteriaQuery).getResultList().size());
        getMeta().calculateToTalPages();
        response.setMeta(getMeta());
        return response;

    }

}




