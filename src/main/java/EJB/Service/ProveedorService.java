package EJB.Service;

import EJB.Helper.ProveedorResponse;
import JPA.ProveedorEntity;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Servicios para la gestion de proveedores
 * <p>
 * Created by szalimben on 23/09/15.
 */
@Stateless
public class ProveedorService extends Service<ProveedorEntity> {

    public String deleteProveedor(int id) {
        return delete(id, ProveedorEntity.class);
    }
    /**
     * Cantidad de Registros
     *
     * @return Cantidad total de Registros
     */
    public Long getCount() {
        Query query = em.createNamedQuery("proveedor.totalRegisters");
        Long count = (Long) query.getSingleResult();
        return count;
    }

    public List getAllProveedores() {
        Query query = em.createNamedQuery("proveedor.findAll");
        return query.getResultList();
    }

    public Object exportAllProdveedores(MultivaluedMap<String, String> queryParams) {

        ProveedorResponse response = new ProveedorResponse();
        ObjectMapper mapper = new ObjectMapper();
        String file = "/tmp/proveedores.json";

        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (queryParams.getFirst("descripcion") != null) {
            ordenarPorColumna = "descripcion";
            ordenDeOrdenacion = queryParams.getFirst("descripcion");
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = queryParams.getFirst("by_all_attributes");
        String by_descripcion = queryParams.getFirst("by_descripcion");

        if (by_descripcion == null) {
            by_descripcion = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProveedorEntity> criteriaQuery = criteriaBuilder.createQuery(ProveedorEntity.class);
        Root<ProveedorEntity> proveedores = criteriaQuery.from(ProveedorEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(proveedores.<String>get("descripcion"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(proveedores.<String>get("descripcion"), "%" + by_descripcion + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(proveedores.get(ordenarPorColumna)));
        } else {
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(proveedores.get(ordenarPorColumna)));
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
        return "No se pudo generar el archivo";

    }

    public ProveedorResponse getProveedores(String descripcion, String byAllAttributes, String byDescripcion,
                                            Integer pageParametro) {

        ProveedorResponse response = new ProveedorResponse();
        inicializarMeta();


        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (descripcion != null) {
            ordenarPorColumna = "descripcion";
            ordenDeOrdenacion = descripcion;
        }

        // Iniciamos las varialles para el filtrado
        String by_all_attributes = byAllAttributes;
        String by_descripcion = byDescripcion;

        if (by_descripcion == null) {
            by_descripcion = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProveedorEntity> criteriaQuery = criteriaBuilder.createQuery(ProveedorEntity.class);
        Root<ProveedorEntity> proveedores = criteriaQuery.from(ProveedorEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(proveedores.<String>get("descripcion"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(proveedores.<String>get("descripcion"), "%" + by_descripcion + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(proveedores.<String>get("id"), proveedores.<String>get("descripcion"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(proveedores.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(proveedores.<String>get("id"), proveedores.<String>get("descripcion"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(proveedores.get(ordenarPorColumna)));
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
}
