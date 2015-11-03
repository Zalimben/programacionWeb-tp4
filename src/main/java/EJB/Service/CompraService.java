package EJB.Service;

import EJB.Helper.ComprasResponse;
import EJB.Helper.SolicitudCompraResponse;
import EJB.Jackson.Compra;
import EJB.Jackson.CompraDetalle;
import EJB.Util.StockInsuficienteException;
import JPA.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Servicios para la gestion de las compras
 * <p>
 * Created by szalimben on 23/09/15.
 */
@Stateless
public class CompraService extends Service<CompraEntity> {

    @EJB
    ProveedorService proveedorService;
    @EJB
    ProductoService productoService;


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean addCompra(Compra compra) throws StockInsuficienteException {

        CompraEntity compraEntity = new CompraEntity();
        compraEntity.setProveedor(proveedorService.find(compra.getProveedorId(), ProveedorEntity.class));
        compraEntity.setFecha(new Date().toString());

        long montoAcumulador = 0;

        for (CompraDetalle detalle : compra.getDetalles()) {
            ProductoEntity productoEntity = productoService.find(detalle.getProductoId(), ProductoEntity.class);
            if (productoEntity == null) {
                throw new StockInsuficienteException();
            } else {
                productoEntity.setStock(productoEntity.getStock() + detalle.getCantidad());
                montoAcumulador = montoAcumulador + productoEntity.getPrecio() * detalle.getCantidad();
                productoService.update(productoEntity);
            }
            CompraDetalleEntity compraDetalleEntity = new CompraDetalleEntity();
            compraDetalleEntity.setProducto(productoEntity);
            compraDetalleEntity.setCantidad(Long.valueOf(detalle.getCantidad()));
            compraDetalleEntity.setCompra(compraEntity);
            compraEntity.setDetalles(compraDetalleEntity);
        }

        compraEntity.setMonto(String.valueOf(montoAcumulador));
        return super.add(compraEntity);

    }

	/**
	 * Metodo para agregar una Compra Nueva
	 *
	 * @param entity
	 *          :Entidad ha ser persistida
	 * @param detalleEntityList
	 *          : Detalles de la Compra
	 * @return
	 *          : True si la compra se registro correctamente, Falso caso contrario
	 * @throws StockInsuficienteException
	 */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean createCompra(CompraEntity entity, List<CompraDetalleEntity> detalleEntityList) throws StockInsuficienteException{

        entity.setFecha(new Date().toString());
	    long montoAcumulador = 0;

        for (CompraDetalleEntity detalle : detalleEntityList) {
            ProductoEntity productoEntity = productoService.find(detalle.getProducto().getId().intValue(), ProductoEntity.class);
            if (productoEntity == null) {
                throw new StockInsuficienteException();
            } else {
                productoEntity.setStock(productoEntity.getStock() + detalle.getCantidad());
                montoAcumulador = montoAcumulador + productoEntity.getPrecio() * detalle.getCantidad();
                productoService.update(productoEntity);
            }
            CompraDetalleEntity compraDetalleEntity = new CompraDetalleEntity();
            compraDetalleEntity.setProducto(productoEntity);
            compraDetalleEntity.setCantidad(detalle.getCantidad());
            compraDetalleEntity.setCompra(entity);

            entity.setDetalles(compraDetalleEntity);
        }

        entity.setMonto(String.valueOf(montoAcumulador));
        return super.add(entity);

    }

    /**
     * Retorna la entidad buscada por Id
     *
     * @param id Identificador del elemento buscado
     * @return Elemento cuyo identificador corresponda
     */
    public Object getCompra(int id) {
        return find(id, CompraEntity.class);
    }


    /**
     * Cantidad de Registros
     *
     * @return Cantidad total de Registros
     */
    public Long getCount() {
        Query query = em.createNamedQuery("compra.totalRegisters");
        Long count = (Long) query.getSingleResult();
        return count;
    }

    /**
     * Metodo para obtener la lista de entidades por filtro y
     * orden aplicado a las columnas
     *
     * @param queryParams parametros de filtro y orden
     * @return Lista de Clientes que coinciden con los parametros de filtro y orden
     */
    public Object exportAllCompras(MultivaluedMap<String, String> queryParams) {

        ComprasResponse response = new ComprasResponse();
        ObjectMapper mapper = new ObjectMapper();
        String file = "/tmp/compras.json";

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
        } else if (queryParams.getFirst("monto") != null) {
            ordenarPorColumna = "monto";
            ordenDeOrdenacion = queryParams.getFirst("monto");
        } else if (queryParams.getFirst("fecha") != null) {
            ordenarPorColumna = "fecha";
            ordenDeOrdenacion = queryParams.getFirst("fecha");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_monto = queryParams.getFirst("by_monto");
        String by_proveedor = queryParams.getFirst("by_proveedor.descripcion");
        String by_fecha = queryParams.getFirst("by_fecha");

        if (by_proveedor == null) {
            by_proveedor = "";
        }

        if (by_fecha == null) {
            by_fecha = "";
        }

        if (by_monto == null) {
            by_monto = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CompraEntity> criteriaQuery = criteriaBuilder.createQuery(CompraEntity.class);
        Root<CompraEntity> compras = criteriaQuery.from(CompraEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(compras.<String>get("monto"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(compras.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                criteriaBuilder.like(compras.<String>get("fecha"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(compras.<String>get("monto"), "%" + by_monto + "%"),
                criteriaBuilder.like(compras.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                criteriaBuilder.like(compras.<String>get("fecha"), "%" + by_fecha + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(compras.get(ordenarPorColumna)));
        } else {
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(compras.get(ordenarPorColumna)));
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

    /**
     * Metodo para obtener la lista de entidades por filtro y
     * orden aplicado a las columnas
     *
     * @param queryParams parametros de filtro y orden
     * @return Lista de Clientes que coinciden con los parametros de filtro y orden
     */
    public Object getCompras(MultivaluedMap<String, String> queryParams) {

        ComprasResponse response = new ComprasResponse();
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
        } else if (queryParams.getFirst("monto") != null) {
            ordenarPorColumna = "monto";
            ordenDeOrdenacion = queryParams.getFirst("monto");
        } else if (queryParams.getFirst("fecha") != null) {
            ordenarPorColumna = "fecha";
            ordenDeOrdenacion = queryParams.getFirst("fecha");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_monto = queryParams.getFirst("by_monto");
        String by_proveedor = queryParams.getFirst("by_proveedor.descripcion");
        String by_fecha = queryParams.getFirst("by_fecha");

        if (by_proveedor == null) {
            by_proveedor = "";
        }

        if (by_fecha == null) {
            by_fecha = "";
        }

        if (by_monto == null) {
            by_monto = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CompraEntity> criteriaQuery = criteriaBuilder.createQuery(CompraEntity.class);
        Root<CompraEntity> compras = criteriaQuery.from(CompraEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(compras.<String>get("monto"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(compras.<String>get("proveedor").<String>get("descripcion"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(compras.<String>get("fecha"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(compras.<String>get("monto"), "%" + by_monto + "%"),
                criteriaBuilder.like(compras.<String>get("proveedor").<String>get("descripcion"), "%" + by_proveedor + "%"),
                criteriaBuilder.like(compras.<String>get("fecha"), "%" + by_fecha + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(compras.<String>get("id"),
                    compras.<String>get("proveedor"),
                    compras.<String>get("fecha"),
                    compras.<String>get("monto"));

            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(compras.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(compras.<String>get("id"),
                    compras.<String>get("proveedor"),
                    compras.<String>get("fecha"),
                    compras.<String>get("monto"));

            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(compras.get(ordenarPorColumna)));
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


    /**
     * Metodo para obtener la lista de entidades por filtro y
     * orden aplicado a las columnas
     *
     * @param queryParams parametros de filtro y orden
     * @return Lista de Clientes que coinciden con los parametros de filtro y orden
     */
    public Object getSolictudes(MultivaluedMap<String, String> queryParams) {

        SolicitudCompraResponse response = new SolicitudCompraResponse();
        inicializarMeta();
        getMeta().setTotal(this.getCount());
        getMeta().calculateToTalPages();

        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (queryParams.getFirst("producto.descripcion") != null) {
            ordenarPorColumna = "producto";
            ordenDeOrdenacion = queryParams.getFirst("producto.descripcion");
        } else if (queryParams.getFirst("fecha") != null) {
            ordenarPorColumna = "fecha";
            ordenDeOrdenacion = queryParams.getFirst("fecha");
        } else if (queryParams.getFirst("atendido") != null) {
            ordenarPorColumna = "atendido";
            ordenDeOrdenacion = queryParams.getFirst("atendido");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_fecha = queryParams.getFirst("by_fecha");
        String by_producto = queryParams.getFirst("by_producto.descripcion");
        String by_atendido = queryParams.getFirst("by_atendido");

        if (by_atendido == null) {
            by_atendido = "";
        }

        if (by_fecha == null) {
            by_fecha = "";
        }

        if (by_producto == null) {
            by_producto = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SolicitudCompraEntity> criteriaQuery = criteriaBuilder.createQuery(SolicitudCompraEntity.class);
        Root<SolicitudCompraEntity> solicitudes = criteriaQuery.from(SolicitudCompraEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(
//              criteriaBuilder.like(solicitudes.<String>get("atendido"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(solicitudes.<String>get("producto").<String>get("descripcion"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(solicitudes.<String>get("fecha"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(
//                criteriaBuilder.like(solicitudes.<String>get("atendido"), "%" + by_atendido + "%"),
                criteriaBuilder.like(solicitudes.<String>get("producto").<String>get("descripcion"), "%" + by_producto + "%"),
                criteriaBuilder.like(solicitudes.<String>get("fecha"), "%" + by_fecha + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
//            criteriaQuery.multiselect(solicitudes.<String>get("producto"),
//                    solicitudes.<String>get("fecha"),
//                    solicitudes.<String>get("atendido"));

            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(solicitudes.get(ordenarPorColumna)));
        } else {
//            criteriaQuery.multiselect(solicitudes.<String>get("producto"),
//                    solicitudes.<String>get("fecha"),
//                    solicitudes.<String>get("atendido"));

            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(solicitudes.get(ordenarPorColumna)));
        }

        Integer page;
        if (queryParams.getFirst("page") != null) {
            page = Integer.valueOf(queryParams.getFirst("page")) - 1;
        } else {
            page = 0;
        }

        response.setEntidades(em.createQuery(criteriaQuery).setMaxResults(getMeta().getPage_size().intValue()).setFirstResult(page * getMeta().getPage_size().intValue()).getResultList());
        response.setMeta(getMeta());
        return response;


    }

}
