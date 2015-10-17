package EJB.Helper;

/**
 * Contiene los valores para el paginado de las listas.
 * Incluye la cantidad total de registros, la cantidad total de paginas
 * y el tamano de cada pagina
 *
 * @author szalimben
 */
public class Meta {

    /* Cantidad  */
    Long total;
    Long total_pages;
    Long page_size;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPage_size() {
        return page_size;
    }

    public void setPage_size(Long page_size) {
        this.page_size = page_size;
    }

    public Long getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Long total_pages) {
        this.total_pages = total_pages;
    }

    public void calculateToTalPages() {

        Long var = getTotal() / getPage_size();
        if (getTotal() <= getPage_size()) {
            setTotal_pages(1L);
        } else {
            setTotal_pages(var + 1L);
        }
    }
}
