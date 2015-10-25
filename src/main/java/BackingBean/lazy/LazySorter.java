package BackingBean.lazy;

import java.util.Comparator;

import JPA.ClienteEntity;
import org.primefaces.model.SortOrder;

public class LazySorter implements Comparator<ClienteEntity> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(ClienteEntity clienteEntity1, ClienteEntity clienteEntity2) {
        try {
            Object value1 = ClienteEntity.class.getField(this.sortField).get(clienteEntity1);
            Object value2 = ClienteEntity.class.getField(this.sortField).get(clienteEntity2);

            int value = ((Comparable)value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
