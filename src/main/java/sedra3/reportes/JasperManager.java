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
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import sedra3.fachada.ConfiguracionFacade;
import sedra3.util.JSFutil;

/**
 *
 * @author jm_acer
 */
public class JasperManager {

    @Inject
    ConfiguracionFacade configuracionFacade;
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
    String pathLogo;

    public JasperManager() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
        servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        userHome = System.getProperty("user.home");
        System.out.println(userHome);
        pathweb = servletContext.getRealPath("/");
        pathweb = pathweb.replace('\\', '/');
        //pathLogo = pathweb + "img/logoreporte.png";
        pathLogo = JSFutil.getAbsoluteApplicationUrl() + "/logoServlet";
        params = new HashMap<>();
//        if (configuracionFacade.find(1).getArchivoLogo() != null) {
//            params.put("imagenPath", configuracionFacade.find(1).getArchivoLogo());
//        } else {
        params.put("imagenPath", pathLogo);
//        }
        params.put("tituloReporte", this.tituloReporte);
        params.put("subTituloReporte", this.subTituloReporte);
        params.put("author", JSFutil.getUsuarioConectado().toString());
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

    public void generarReporte(String reportSource, String tipoReporte, List<?> dataList) throws Exception {
        try {
            params.put(JRParameter.REPORT_LOCALE, JSFutil.getmyLocale());
            params.put(JRParameter.REPORT_TIME_ZONE, JSFutil.getMyTimeZone());
            JasperReport report = JasperCompileManager.compileReport(reportSource);
            baos = new ByteArrayOutputStream();

            JRBeanCollectionDataSource listaReporte = new JRBeanCollectionDataSource(dataList);
            JasperPrint print = JasperFillManager.fillReport(report, params, listaReporte);
            String contentType = "text/html";
            String fileName = "reporte.txt";
            Exporter exporter;

            switch (tipoReporte) {
                case "VPREVIA":
                    break;
                case "PDF":
                    contentType = "application/pdf";
                    fileName = "reportePDF.pdf";
                    exporter = new JRPdfExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
                    exporter.exportReport();

                    break;
                case "XLS":
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    fileName = "reporteExcel.xlsx";
                    exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
                    exporter.exportReport();
                    break;
                case "CSV":
                    contentType = "text/csv";
                    fileName = "reporteCSV.csv";
                    exporter = new JRCsvExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(baos));
                    exporter.exportReport();
                    break;
                case "DOCX":
                    contentType = "application/msword";
                    fileName = "reporteDOCX.docx";
                    exporter = new JRDocxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
                    exporter.exportReport();
                    break;
                case "HTML":
                    contentType = "text/html";
                    fileName = "reporteHTML.html";
                    exporter = new HtmlExporter();
                    SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(baos);
                    output.setImageHandler(new WebHtmlResourceHandler(pathLogo));

                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(output);
                    exporter.exportReport();
                    break;
                default:
                    throw new Exception("Formato no exportable");
            }

            response.reset();
            response.setContentType(contentType);
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "inline; filename=" + fileName);
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            context.responseComplete();
        } catch (JRException | IOException ex) {
            Logger.getLogger(JasperManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
