package EJB.Service;

import EJB.Helper.Meta;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.io.Serializable;


@TransactionManagement(TransactionManagementType.CONTAINER)
public class Service<T> implements IService<T>, Serializable {

    @PersistenceContext(unitName = "PU")
    protected EntityManager em;

    private Meta meta;


    /**
     * Metodo para borrar una entidad
     *
     * @param id    Identificador de la entidad
     * @param clazz Clase de la entidad
     * @return <b>True</b> Si el borrado fue exitoso, <b>False</b> caso contrario
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String delete(int id, Class<T> clazz) {
        try {
            T data = em.find(clazz, (long) id);
            if (data != null) {
                try {
                    em.remove(data);
                } catch (PersistenceException e2) {
                    return ("Error al borrar la entidad, la entidad es una FK en otra entidad");
                }
                return ("Borrado exitoso");
            }
        } catch (Exception e) {
            return ("Error al borrar la entidad, la entidad ya no existe");
        }
        return ("Error al borrar la entidad");
    }

    /**
     * Metodo para eliminar una entidad pasada como parametro la misma entidad
     *
     * @param entity Entidad que se desea borrar
     * @return <b>True</b> Si el borrado fue exitoso, <b>False</b> caso contrario
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteByEntity(T entity) {
        try {
            em.remove(entity);
            System.out.println("Borrado exitoso");
            return true;
        } catch (Exception e) {
            System.out.println("Error al borrar la entidad");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metodo para actualizar una entidad
     *
     * @param entity Entidad con los datos actualizados
     * @return <b>True</b> Si la actualizacion fue exitosa, <b>False</b> caso contrario
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean update(T entity) {
        try {
            em.merge(entity);
            System.out.println("La entidad se actualizo exitosamente");
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar la entidad");
        }

        return false;
    }

    /**
     * Metodo para agregar una nueva entidad
     *
     * @param entity Entidad que se desea agregar
     * @return <b>True</b> Si la creacion fue exitosa, <b>False</b> caso contrario
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean add(T entity) {
        try {
            em.persist(entity);
            System.out.println("Creacion exitosa");
            return true;
        } catch (Exception e) {
            System.out.println("Error al crear la entidad");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metodo para encontrar una entidad
     *
     * @param id    Identificador unico
     * @param clazz Clase de la entidad buscada
     * @return Entidad buscada
     */
    public T find(int id, Class<T> clazz) {
        return em.find(clazz, (long) id);
    }

    /**
     * Metodo para inicializar el META del response
     */
    public void inicializarMeta() {
        meta = new Meta();
        meta.setPage_size((long) 10);
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


}
