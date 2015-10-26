package BackingBeans;

import EJB.Service.CompraService;
import JPA.CompraEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 * Created by szalimben on 25/10/15.
 */
@ManagedBean(name = "compra")
@SessionScoped
public class ComprasBean {

	/* Variables & Dependencias */
	@EJB
	CompraService service;

	@Inject
	CompraEntity compraEntity;

	/* Metodos */

	/* Getter & Setter */
}
