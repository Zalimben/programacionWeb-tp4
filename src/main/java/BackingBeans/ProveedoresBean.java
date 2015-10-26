package BackingBeans;

import EJB.Service.ProveedorService;
import JPA.ProveedorEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 * Created by szalimben on 25/10/15.
 */
@ManagedBean(name = "proveedor")
@SessionScoped
public class ProveedoresBean {

	/* Variables & Dependencias */
	@EJB
	ProveedorService service;

	@Inject
	ProveedorEntity proveedorEntity;

	/* Metodos */

	/* Getter & Setter */

}
