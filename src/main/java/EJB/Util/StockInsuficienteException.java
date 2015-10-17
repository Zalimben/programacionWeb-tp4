package EJB.Util;

import javax.ejb.ApplicationException;

/**
 * Metodo para capturar la exception lanzada cuando no existe el stock suficiente
 * para realizar la venta
 */
@ApplicationException(rollback = true)
public class StockInsuficienteException extends Exception {

    public StockInsuficienteException() {
        super();
    }

    public StockInsuficienteException(String message) {
        super(message);
        System.out.println(message);
    }

    public StockInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockInsuficienteException(Throwable cause) {
        super(cause);
    }


}
