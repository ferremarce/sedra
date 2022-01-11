/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.reportes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author jmferreira
 */
public class ReporteClase {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    ServletContext servletContext;

    /**
     * Creates a new instance of ReporteController
     */
    public ReporteClase() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
        servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public void imprimir(List<?> lista) throws FileNotFoundException {
        File f = new File("/home/jmferreira/Documents/WEB/catequesis/src/main/java/reportes/rptFicha.jasper");
        stream = new FileInputStream(f);
        
        Map<String, Object> params = new HashMap<>();

        baos = new ByteArrayOutputStream();
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            JRBeanCollectionDataSource listaReporte = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(report, params, listaReporte);
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "inline; filename=reporteFicha.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (JRException | IOException ex) {
            Logger.getLogger(ReporteClase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
