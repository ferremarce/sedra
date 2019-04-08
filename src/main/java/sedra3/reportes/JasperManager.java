/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sedra3.util.JSFutil;

/**
 *
 * @author jm_acer
 */
public class JasperManager {

    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());
    String userHome;
    private String pathweb;
    private final HttpServletResponse response;
    private final FacesContext context;
    private ByteArrayOutputStream baos;
    //private InputStream stream;
    private final ServletContext servletContext;
    //private HttpServletRequest request;
    private final String tituloReporte = bundle.getString("TituloSistema");
    private final String subTituloReporte = "--";
    Map<String, Object> params;

    public JasperManager() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
        servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        //userHome = System.getProperty("user.home");
        pathweb = servletContext.getRealPath("/");
        pathweb = pathweb.replace('\\', '/');
        String imagenSource = pathweb + "img/logoreporte.png";
        params = new HashMap<>();
        params.put("imagenPath", imagenSource);
        params.put("tituloReporte", this.tituloReporte);
        params.put("subTituloReporte", this.subTituloReporte);
        //params.put("author", JSFutil.);
    }

    public String getPathweb() {
        return pathweb;
    }

    public void setPathweb(String pathweb) {
        this.pathweb = pathweb;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void generarReporte(String reportSource, String tipoReporte, List<?> dataList) {
        try {
            params.put(JRParameter.REPORT_LOCALE, JSFutil.getmyLocale());
            params.put(JRParameter.REPORT_TIME_ZONE, JSFutil.getMyTimeZone());
            JasperReport report = JasperCompileManager.compileReport(reportSource);
            baos = new ByteArrayOutputStream();

            JRBeanCollectionDataSource listaReporte = new JRBeanCollectionDataSource(dataList);
            JasperPrint print = JasperFillManager.fillReport(report, params, listaReporte);

            switch (tipoReporte) {
                case "VPREVIA":
                    break;
                case "PDF":
                    JasperExportManager.exportReportToPdfStream(print, baos);
                    response.reset();
                    response.setContentType("application/pdf");
                    response.setContentLength(baos.size());
                    response.setHeader("Content-disposition", "inline; filename=reporteFicha.pdf");
                    response.getOutputStream().write(baos.toByteArray());
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    context.responseComplete();
                    break;
                case "XLS":
                    break;
                default:
                    break;
            }
        } catch (JRException | IOException ex) {
            Logger.getLogger(JasperManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
