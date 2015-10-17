package EJB.Service;

import JPA.ClienteEntity;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;

/**
 * Created by nabil on 08/10/15.
 */

@Stateful
public class ClienteFileService {

    @PersistenceContext
    private EntityManager em;

//    @Inject
//    private ClienteEntity cliente;

    private JsonFactory jfactory;
    private JsonParser jParser;

    // datos clientes.json
    private String nombre;
    private String cedula;

    public void parsear(String is) {
        ObjectMapper objectMapper = new ObjectMapper();
        jfactory = objectMapper.getJsonFactory();
        String result = is;
        String archivo = result.substring(result.indexOf('{'));

        try {
//            String archivo = "{\n" +
//                    "\t\"clientes\": [\n" +
//                    "\t\t{\n" +
//                    "\t\t\t\"nombre\" : \"Nabil\",\n" +
//                    "\t\t\t\"cedula\" : \"4123842\"\n" +
//                    "\t\t},\n" +
//                    "\t\t{\n" +
//                    "\t\t\t\"nombre\" : \"Alexis\",\n" +
//                    "\t\t\t\"cedula\" : \"4208953\"\n" +
//                    "\t\t},\n" +
//                    "\t\t{\n" +
//                    "\t\t\t\"nombre\" : \"Saul\",\n" +
//                    "\t\t\t\"cedula\" : \"4238495\"\n" +
//                    "\t\t}\n" +
//                    "\t]\n" +
//                    "}";
            jParser = jfactory.createJsonParser(archivo);

            jParser.nextToken(); // token '{'
//            String texto1 = jParser.getText();
            jParser.nextToken(); // token 'clientes'

            // se procesa cada objeto cliente, primer token '['
            while (jParser.nextToken() != JsonToken.END_ARRAY) {

                String fieldname = jParser.getText();
                if ("nombre".equals(fieldname)) {

                    // token 'nombre'
                    // vamos al siguiente token, el valor de 'nombre'
                    jParser.nextToken();
                    nombre = jParser.getText();
                }

                if ("cedula".equals(fieldname)) {

                    // token 'cedula'
                    // vamos al siguiente token, el valor de 'cedula'
                    jParser.nextToken();
                    cedula = jParser.getText();

                    // como es el ultimo campo procesamos el cliente en persistencia
                    addCliente(nombre, cedula);
                }
            }
            terminarStateful();
        } catch (IOException e) {
            // Procesamos la excepcion
        }
    }

    private void addCliente(String nombre, String cedula) {
        try {
            ClienteEntity cliente = new ClienteEntity();
            cliente.setNombre(nombre);
            cliente.setCedulaIdentidad(cedula);
            em.persist(cliente);
        } catch (Exception e) {
            // se procesa la excepcion
        }
    }

    @Remove
    private void terminarStateful() {
        // este metodo finaliza la instancia creada del stateful bean con el
        // annotation @Remove
    }

}
