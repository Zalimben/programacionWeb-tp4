package BackingBeans;

import EJB.Service.SolicitudCompraService;
import JPA.SolicitudCompraEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 * Created by szalimben on 25/10/15.
 */
@ManagedBean(name = "solicitud")
@SessionScoped
public class SolicitudesBean {


	/* Variables & Dependencias */
	@EJB
	SolicitudCompraService service;

	@Inject
	SolicitudCompraEntity solicitudCompraEntity;

	/* Metodos */

	/* Getter & Setter */
}
