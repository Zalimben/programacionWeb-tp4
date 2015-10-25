package EJB.Service;

import EJB.Helper.ClienteResponse;
import JPA.ClienteEntity;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Servicios para la gestion de Clientes
 * <p>
 * Created by szalimben on 23/09/15.
 */
@Stateless
public class ClienteService extends Service<ClienteEntity> {

    /**
     * Retorna la entidad buscada por Id
     *
     * @param id Identificador del elemento buscado
     * @return Elemento cuyo identificador corresponda
     */
    public Object getCliente(int id) {
        return find(id, ClienteEntity.class);
    }

    /**
     * Retorna la entidad buscada por Id
     *
     * @return Elemento cuyo identificador corresponda
     */
    public Object exportAllClientes(String nombre, String cedula, String por_all_attributes,
                                    String por_nombre, String por_cedula, Integer page_parametro) {
        ClienteResponse response = new ClienteResponse();
        ObjectMapper mapper = new ObjectMapper();
        String file = "/tmp/clientes.json";

        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (nombre != null) {
            ordenarPorColumna = "nombre";
            ordenDeOrdenacion = nombre;
        } else if (cedula != null) {
            ordenarPorColumna = "cedulaIdentidad";
            ordenDeOrdenacion = cedula;
        }

        // Iniciamos las variables parael filtrado
        String by_all_attributes = por_all_attributes;
        String by_cedula_identidad = por_cedula;
        String by_nombre = por_nombre;

        if (by_nombre == null) {
            by_nombre = "";
        }

        if (by_cedula_identidad == null) {
            by_cedula_identidad = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ClienteEntity> criteriaQuery = criteriaBuilder.createQuery(ClienteEntity.class);
        Root<ClienteEntity> clientes = criteriaQuery.from(ClienteEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(clientes.<String>get("nombre"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(clientes.<String>get("cedulaIdentidad"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(clientes.<String>get("nombre"), "%" + by_nombre + "%"),
                criteriaBuilder.like(clientes.<String>get("cedulaIdentidad"), "%" + by_cedula_identidad + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(clientes.<String>get("nombre"), clientes.<String>get("cedulaIdentidad"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(clientes.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(clientes.<String>get("nombre"), clientes.<String>get("cedulaIdentidad"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(clientes.get(ordenarPorColumna)));
        }


        response.setEntidades(em.createQuery(criteriaQuery).getResultList());
        File fileResponse = new File(file);

        try {
            // convert user object to json string, and save to a file
            mapper.writeValue(fileResponse, response.getEntidades());
        } catch (JsonGenerationException e) {

            e.printStackTrace();

        } catch (JsonMappingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return fileResponse;
    }

    public String deleteCliente(int id) {
        return delete(id, ClienteEntity.class);
    }

    /**
     * Cantidad de Registros
     *
     * @return Cantidad total de Registros
     */
    public Long getCount() {
        Query query = em.createNamedQuery("cliente.totalRegisters");
        Long count = (Long) query.getSingleResult();
        return count;
    }

    public List getAllClientes() {
        Query query = em.createNamedQuery("cliente.findAll");
        return query.getResultList();
    }

    /**
     * Metodo para obtener la lista de clientes por filtro y
     * orden aplicado a las columnas
     */
    public ClienteResponse getClientes(String nombre, String cedula, String por_all_attributes,
                              String por_nombre, String por_cedula, Integer page_parametro) {

        ClienteResponse response = new ClienteResponse();
        inicializarMeta();

        /**
         * Variables default values for the column sort
         */
        String ordenarPorColumna = "id";
        String ordenDeOrdenacion = "asc";

        /**
         * Retrieve one or none of the URI query params that have the column name and sort order values
         */
        if (nombre != null) {
            ordenarPorColumna = "nombre";
            ordenDeOrdenacion = nombre;
        } else if (cedula != null) {
            ordenarPorColumna = "cedulaIdentidad";
            ordenDeOrdenacion = cedula;
        }

        // Iniciamos las variables parael filtrado
        String by_all_attributes = por_all_attributes;
        String by_cedula_identidad = por_cedula;
        String by_nombre = por_nombre;

        if (by_nombre == null) {
            by_nombre = "";
        }

        if (by_cedula_identidad == null) {
            by_cedula_identidad = "";
        }

        if (by_all_attributes == null) {
            by_all_attributes = "";
        }

        /* Creamos el query para la consulta */
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ClienteEntity> criteriaQuery = criteriaBuilder.createQuery(ClienteEntity.class);
        Root<ClienteEntity> clientes = criteriaQuery.from(ClienteEntity.class);

        // Filtrado por todas las columnas
        Predicate filtradoPorAllAttributes = criteriaBuilder.or(criteriaBuilder.like(clientes.<String>get("nombre"), "%" + by_all_attributes + "%"),
                criteriaBuilder.like(clientes.<String>get("cedulaIdentidad"), "%" + by_all_attributes + "%"));

        // Filtrado por columna
        Predicate filtradoPorColumna = criteriaBuilder.and(criteriaBuilder.like(clientes.<String>get("nombre"), "%" + by_nombre + "%"),
                criteriaBuilder.like(clientes.<String>get("cedulaIdentidad"), "%" + by_cedula_identidad + "%"));

        // Fijamos la Ordenacion
        if ("asc".equals(ordenDeOrdenacion)) {
            criteriaQuery.multiselect(clientes.<String>get("nombre"), clientes.<String>get("cedulaIdentidad"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.asc(clientes.get(ordenarPorColumna)));
        } else {
            criteriaQuery.multiselect(clientes.<String>get("nombre"), clientes.<String>get("cedulaIdentidad"));
            criteriaQuery.where(filtradoPorAllAttributes, filtradoPorColumna).orderBy(criteriaBuilder.desc(clientes.get(ordenarPorColumna)));
        }

        Integer page;
        if (page_parametro != null) {
            page = page_parametro - 1;
        } else {
            page = 0;
        }
//        Integer page;
//        page=0; // mientras desarrollo

        response.setEntidades(em.createQuery(criteriaQuery).setMaxResults(getMeta().getPage_size().intValue()).setFirstResult(page * getMeta().getPage_size().intValue()).getResultList());
        getMeta().setTotal((long) em.createQuery(criteriaQuery).getResultList().size());
        getMeta().calculateToTalPages();
        response.setMeta(getMeta());
        return response;
    }


}
