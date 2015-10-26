package BackingBeans;

import EJB.Service.ProductoService;
import JPA.ProductoEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 * Created by szalimben on 25/10/15.
 */
@ManagedBean(name = "productos")
@SessionScoped
public class ProductosBean {

	/* Variables & Dependencias */
	@EJB
	ProductoService service;

	@Inject
	ProductoEntity productoEntity;

	/* Metodos */

	/* Getter & Setter */


}
