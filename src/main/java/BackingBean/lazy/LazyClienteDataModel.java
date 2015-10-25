package BackingBean.lazy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import EJB.Service.ClienteService;
import JPA.ClienteEntity;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real datasource like a database.
 */
public class LazyClienteDataModel extends LazyDataModel<ClienteEntity> {

    private List<ClienteEntity> datasource;



    public LazyClienteDataModel(List<ClienteEntity> datasource) {
        this.datasource = datasource;
    }

    @Override
    public ClienteEntity getRowData(String rowKey) {
        for(ClienteEntity clienteEntity : datasource) {
            if(clienteEntity.getId().equals(rowKey))
                return clienteEntity;
        }

        return null;
    }

    @Override
    public Object getRowKey(ClienteEntity clienteEntity) {
        return clienteEntity.getId();
    }

    @Override
    public List<ClienteEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<ClienteEntity> data = new ArrayList<ClienteEntity>();

        //filter
        for(ClienteEntity clienteEntity : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        Field field = clienteEntity.getClass().getDeclaredField(filterProperty);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(clienteEntity);

                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        }
                        else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }
            }

            if(match) {
                data.add(clienteEntity);
            }
        }

        //sort
        if(sortField != null) {
            Collections.sort(data, new LazySorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }
}
