package EJB.Service;

import JPA.CompraDetalleEntity;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;


@Stateless
public class CompraDetalleService extends Service<CompraDetalleEntity> {

	public List getDetallesByCompra(Long id) {
		Query query = em.createQuery("SELECT d FROM CompraDetalleEntity d WHERE compra.id = :id");
		query.setParameter("id", id);
		return query.getResultList();
	}




}
