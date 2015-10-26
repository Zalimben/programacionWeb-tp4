package BackingBeans;

import EJB.Service.VentasService;
import JPA.VentaEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 * Created by szalimben on 25/10/15.
 */
@ManagedBean(name = "venta")
@SessionScoped
public class VentasBean {

	/* Variables & Dependencias */
	@EJB
	VentasService service;

	@Inject
	VentaEntity ventaEntity;

	/* Metodos */

	/* Getter & Setter */

}
