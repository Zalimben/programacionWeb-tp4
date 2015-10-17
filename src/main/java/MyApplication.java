import org.reflections.Reflections;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex on 30/08/15.
 */

/**
 * Since we're not using a jax-rs servlet mapping, we must define an Application class that is
 * annotated with the @ApplicationPath annotation. If you return any empty set for by classes and singletons,
 * your WAR will be scanned for JAX-RS annotation resource and provider classes.
 */
@ApplicationPath("/service")
public class MyApplication extends Application {

    /**
     * Setup the classes that the application server will use for deployment.
     * Use Reflections to retrieve the classes annotated with @Path.
     *
     * @return the classes that the application server will use
     */
    public Set<Class<?>> getClasses() {

        Reflections reflections = new Reflections("REST");
        return new HashSet<>(reflections.getTypesAnnotatedWith(Path.class));

    }
}
