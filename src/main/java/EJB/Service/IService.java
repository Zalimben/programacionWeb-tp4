package EJB.Service;

import javax.ejb.Local;

/**
 * Interfaz para almacenar todos los metodos comunes de los servicios
 * prestados por el sistema
 * <p>
 * Created by szalimben on 22/09/15.
 */
@Local
public interface IService<T> {

    String delete(int id, Class<T> clase);

    boolean add(T entity);

    boolean update(T entity);

    T find(int id, Class<T> clazz);

}
