package EJB.Service;

import REST.JsonObjectIterator;

import javax.ejb.Stateful;
import java.io.InputStream;

/**
 * Created by alex on 08/10/15.
 */
@Stateful
public class FileStatefulService {

    JsonObjectIterator jsonObjectIterator;

    public void testStatefull(InputStream inputStream) {
        jsonObjectIterator = new JsonObjectIterator(inputStream);
        jsonObjectIterator.next();
    }


}
