package EJB.Service;

import EJB.Helper.SolicitudCompraResponse;
import JPA.ProductoEntity;
import JPA.SolicitudCompraEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Date;
import java.util.List;

/**
 * Created by szalimben on 23/09/15.
 */
@Stateless
public class SolicitudCompraService extends Service<SolicitudCompraEntity> {


    private static final int cantidadMinima = 10;

    @EJB
    ProductoService productoService;

    @EJB
    SolicitudCompraService solicitudCompraService;

    /**
     * Metodo para crear una nueva solicitud de compra
     */
    @Transactional
    public void crear() {

        List<ProductoEntity> productos = productoService.getProductosCantidadMinima(cantidadMinima);
        System.out.println("Creando Ordenes de compra");
        for (ProductoEntity producto : productos) {
            if (!yaExiste(producto)) {
                SolicitudCompraEntity entity = new SolicitudCompraEntity();
                entity.setFecha(new Date().toString());
                entity.setProducto(producto);
                em.persist(entity);
            }
        }
    }

    /**
     * Metodo que verifica que el producto no posee ya una solicitud de compra activa,
     * en caso de que el producto posea una solicitud de compra ya no se creara
     * una nueva
     *
     * @param producto ProductoEntity asociado
     * @return Retorna <b> True</b> si ya existe una solicitud de compra asociada
     * a dicho producto, <b>False</b> caso contrario
     */
    public boolean yaExiste(ProductoEntity producto) {

        Query query = em.createQuery("SELECT sc FROM SolicitudCompraEntity sc WHERE sc.producto = :producto AND sc.atendido = false ");
        query.setParameter("producto", producto);

        return !query.getResultList().isEmpty();
    }

    /**
     * Cantidad de Registros
     *
     * @return Cantidad total de Registros
     */
    public Long getCount() {
        Query query = em.createNamedQuery("solicitud.totalRegisters");
        Long count = (Long) query.getSingleResult();
        return count;
    }

    public Object getSolicitudes(MultivaluedMap<String, String> queryParams) {

        SolicitudCompraResponse response = new SolicitudCompraResponse();
        inicializarMeta();


        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (queryParams.getFirst("producto") != null) {
            ordenarPorColumna = "producto";
            ordenDeOrdenacion = queryParams.getFirst("producto");
        } else if (queryParams.getFirst("fecha") != null) {
            ordenarPorColumna = "fecha";
            ordenDeOrdenacion = queryParams.getFirst("fecha");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_producto = queryParams.getFirst("by_producto");
        String by_fecha = queryParams.getFirst("by_fecha");

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
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(solicitudes.<String>get("producto").<String>get("descripcion"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(solicitudes.<String>get("fecha"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(solicitudes.<String>get("producto").<String>get("descripcion"), "%" + by_producto + "%"),
                criteriaBuilder.like(solicitudes.<String>get("fecha"), "%" + by_fecha + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(solicitudes.<String>get("producto"), solicitudes.<String>get("fecha"), solicitudes.get("atendido"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(solicitudes.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(solicitudes.<String>get("producto"), solicitudes.<String>get("fecha"), solicitudes.get("atendido"));
            criteriaQuery.select(solicitudes).where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(solicitudes.get(ordenarPorColumna)));
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
