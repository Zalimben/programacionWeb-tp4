package EJB.Service;

import EJB.Helper.FacturasResponse;
import EJB.Util.Facturacion;
import JPA.FacturaEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.concurrent.Future;

/**
 * Servicios para la gestion de facturas
 * <p>
 * Created by szalimben on 23/09/15.
 */
@Stateless
public class FacturaService extends Service<FacturaEntity> {

    // Variable que verifica si el proceso se encuentra corriendo actualmente
    private static Future estadoFacturacion;
    @Inject
    Facturacion facturacion;

    /**
     * Cantidad total de registros
     *
     * @return Cantidad total de registros
     */
    public Long getCount() {
        Query query = em.createNamedQuery("factura.totalRegisters");
        return (Long) query.getSingleResult();
    }

    public Object getFactura(int id) {
        Query query = em.createNamedQuery("factura.findById");
        return query.getSingleResult();
    }

    /**
     * Metodo para obtener el listado de facturas ordenadas y filtradas
     *
     * @param queryParams Parametros de ordenacion y filtrado
     * @return Lista de las facturas filtradas y ordenas segun los parametros
     */
    public FacturasResponse getFacturas(String fecha, String monto, String byAllAttributes,
                                        String byFecha, String byMonto, Integer pageParametro) {

        FacturasResponse response = new FacturasResponse();
        inicializarMeta();

        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (monto != null) {
            ordenarPorColumna = "monto";
            ordenDeOrdenacion = monto;
        } else if (fecha != null) {
            ordenarPorColumna = "fecha";
            ordenDeOrdenacion = fecha;
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = byAllAttributes;
        String by_monto = byMonto;
        String by_fecha = byFecha;

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
        CriteriaQuery<FacturaEntity> criteriaQuery = criteriaBuilder.createQuery(FacturaEntity.class);
        Root<FacturaEntity> facturas = criteriaQuery.from(FacturaEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(facturas.<String>get("monto"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(facturas.<String>get("fecha"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(facturas.<String>get("monto"), "%" + by_monto + "%"),
                criteriaBuilder.like(facturas.<String>get("fecha"), "%" + by_fecha + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(facturas.get(ordenarPorColumna)));
        } else {
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(facturas.get(ordenarPorColumna)));
        }

        Integer page;
        if (pageParametro != null) {
            page = pageParametro - 1;
        } else {
            page = 0;
        }

        response.setEntidades(em.createQuery(criteriaQuery).setMaxResults(getMeta().getPage_size().intValue()).setFirstResult(page * getMeta().getPage_size().intValue()).getResultList());
        getMeta().setTotal((long) em.createQuery(criteriaQuery).getResultList().size());
        getMeta().calculateToTalPages();
        response.setMeta(getMeta());
        return response;

    }

    public String facturar() {
        try {
            estadoFacturacion = facturacion.facturacion();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Facturando";
    }

    public String isRun() {
        if (estadoFacturacion == null || estadoFacturacion.isDone() || estadoFacturacion.isCancelled())
            return "Detenido";
        else
            return "Corriendo";
    }

    public String detener() {
        if (estadoFacturacion != null) {
            estadoFacturacion.cancel(true);
            return "Detenido";
        }
        return "No se puede detener";
    }

}
