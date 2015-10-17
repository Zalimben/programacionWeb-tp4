package EJB.Helper;

import java.util.List;

/**
 * Clase que representa el response que se realizara desde
 * los servicios al browser.
 * <p>
 * Sera utilizado para el listado de las entidades.
 * Contiene la lista de entidades que seran mostradas
 * y el meta para los archivos paginados
 *
 * @author szalimben
 */
public class Response<T> {

    private List<T> entidades;
    private Meta meta;

    public List<T> getEntidades() {
        return this.entidades;
    }

    public void setEntidades(List<T> entidades) {
        this.entidades = entidades;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


}
