package EJB.Util;

import org.hibernate.exception.ConstraintViolationException;


public class ServiceUtil {

    public static String getConstraintName(Throwable exception) {
        Throwable t = exception.getCause();
        String mensaje = "Server Error";
        String errorMessage;
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException) {
            ConstraintViolationException causa = (ConstraintViolationException) t;
            errorMessage = causa.getCause().getLocalizedMessage();
            if (errorMessage.contains("cantidad_mayor_cero")) {
                mensaje = "No se cumple la restriccion [cantidad_mayor_cero].";
            } else {
                mensaje = "No se cumple alguna restriccion.";
            }
        }
        return mensaje;
    }
}


