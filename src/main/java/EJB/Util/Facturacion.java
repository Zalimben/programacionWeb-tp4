package EJB.Util;


import EJB.Service.FacturaService;
import EJB.Service.VentasService;
import JPA.FacturaEntity;
import JPA.VentaDetalleEntity;
import JPA.VentaDetalleReporte;
import JPA.VentaEntity;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.annotation.Resource;
import javax.ejb.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Clase para realizar el proceso de facturacion de las ventas
 * <p>
 * Created by szalimben on 03/10/15.
 */
@Singleton
public class Facturacion {

    private static Facturacion instancia = new Facturacion();
    @EJB
    FacturaService facturaService;
    @EJB
    VentasService ventasService;

    @Resource
    private SessionContext context;

    @Resource
    private EJBContext contextBD;

    private Future<String> estadoFacturacion;
    private File carpeta;

    /**
     * Metodo que lanza el proceso de facturacion
     *
     * @return Estado del proceso de facturacion
     */
    @Asynchronous
    public Future<String> facturacion() throws Exception {

        System.out.println("Inicio de Proceso de Facturacion");

        List<VentaEntity> ventas = ventasService.getVentasFactura();

        carpeta = new File("/tmp/reportes/reporte_" + generarNumero());
        carpeta.mkdirs();

        int contador = 0;

        for (VentaEntity venta : ventas) {
            if (!context.wasCancelCalled()) {
                if (venta.getId() != null) {
                    try {
                        Thread.sleep(SECONDS.toMillis(2));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Facturacion.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    FacturaEntity factura = new FacturaEntity();
                    Date dNow = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy");
                    factura.setFecha(ft.format(dNow));
                    factura.setMonto(venta.getMonto());
                    facturaService.add(factura);

                    venta.setFactura(factura);
                    ventasService.update(venta);
                    pdf(venta, factura.getId().intValue());
                    contador++;
                    System.out.println("Procesando ventas: " + contador + "/" + ventas.size());
                }
            } else {
                borrarDirectorio(carpeta);
                carpeta.delete();
                contextBD.setRollbackOnly();
                System.out.println("Se aborto la exportacion");
                return new AsyncResult<>("");
            }
        }


        //pdf((VentaEntity) ventasService.getVenta(1L), 978);
        System.out.println("Termino la exportacion");

        if (contador == 0) {
            carpeta.delete();
        }

        instancia.estadoFacturacion = new AsyncResult<>("");
        return new AsyncResult<>("");
    }

    /**
     * Borra el directorio donde se guardan los reportes
     *
     * @param directorio El directorio que sera borrado
     */
    public void borrarDirectorio(File directorio) {
        System.out.println("BORRANDO");
        File[] ficheros = directorio.listFiles();
        for (File fichero : ficheros) {
            if (fichero.isDirectory()) {
                borrarDirectorio(fichero);
            }
            fichero.delete();
        }
    }


    /**
     * Metodo que crea un reporte en formato pdf
     *
     * @param venta       VentaEntity que formara parte del reporte
     * @param nro_factura Numero de la factura asociada a dicha venta
     */
    @SuppressWarnings("unchecked")
    public void pdf(VentaEntity venta, Integer nro_factura) throws Exception {

        JasperPrint jasperPrint;

        try {
            System.out.println("LLAMANDO A CREAR PDF");
            String ubicacionCarpeta = carpeta.getAbsolutePath();

            //se carga el reporte

            InputStream inputStream = new FileInputStream("/tmp/Jasper/Detalles.jrxml");
            File in = new File("/tmp/Jasper/Detalles.jasper");

            //se procesa el archivo jasper
            HashMap parametros = new HashMap();
            parametros.put("venta", venta.getId().toString());
            parametros.put("fecha", new Date());
            parametros.put("total", venta.getMonto());
            parametros.put("cliente", venta.getCliente().getNombre());

            List<VentaDetalleEntity> dv = venta.getDetalles();
            List<VentaDetalleReporte> lista = new ArrayList();
            for (VentaDetalleEntity detalle : dv) {
                VentaDetalleReporte det = new VentaDetalleReporte();
                det.setId(detalle.getId().intValue());
                det.setIdProducto(detalle.getProducto().getId().intValue());
                det.setProducto(detalle.getProducto().getDescripcion());
                det.setCantidad(detalle.getCantidad().intValue());
                det.setPrecio(detalle.getProducto().getPrecio());
                det.setventa(venta.getId().intValue());
                det.setCantidad(detalle.getCantidad().intValue());
                det.setCliente(venta.getCliente().getNombre());
                det.setFecha(venta.getFecha());
                det.setTotal(Integer.parseInt(venta.getMonto()));
                lista.add(det);
            }

            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lista));
            //se crea el archivo PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, ubicacionCarpeta + "/factura_" + nro_factura.toString() + ".pdf");
            System.out.println("Reporte Generado Correctamente");
        } catch (JRException ex) {
            ex.printStackTrace();
            System.err.println("Error iReport: " + ex.getMessage());
        }
    }

    // @Lock(READ)
    public boolean isRun() {
        System.out.println(instancia.estadoFacturacion.isDone());
        if (instancia.estadoFacturacion == null) {
            return false;
        }
        if (instancia.estadoFacturacion.isCancelled()) {
            return false;
        }
        return !instancia.estadoFacturacion.isDone();
    }


    private String generarNumero() {
        Long id = System.currentTimeMillis();
        String s = id.toString();
        return s;
    }

    // @Lock(READ)
    public boolean detener() {
        System.out.println("LLAMADO A DETENER");
        return instancia.estadoFacturacion.cancel(true);
    }


}
