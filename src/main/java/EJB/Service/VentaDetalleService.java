package EJB.Service;

import JPA.VentaDetalleEntity;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;


@Stateless
public class VentaDetalleService extends Service<VentaDetalleEntity> {

	public List getDetallesByVenta(Long id) {

		Query query = em.createQuery("SELECT d FROM VentaDetalleEntity d WHERE venta.id = :id");
		query.setParameter("id", id);
		return query.getResultList();

	}
}
