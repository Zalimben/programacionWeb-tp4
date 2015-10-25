package BackingBean;

import EJB.Service.FacturaService;
import JPA.FacturaEntity;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Created by alex on 25/10/15.
 */
@ManagedBean(name = "facturas")
@SessionScoped
public class FacturasBean {
    @EJB
    FacturaService facturaService;

    @Inject
    FacturaEntity facturaEntity;

    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void doFacturar() {
        mensaje = facturaService.facturar();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("messages", new FacesMessage(facturaService.facturar()));

    }

    public void doCancelarFacturacion() {
        mensaje = facturaService.facturar();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("messages", new FacesMessage(facturaService.detener()));

    }

    public void getEstadoFacturacion() {
        mensaje = facturaService.facturar();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("messages", new FacesMessage(facturaService.isRun()));
    }

    public Object getFacturas() {
        return facturaService.getFactura(1000);
    }
}
