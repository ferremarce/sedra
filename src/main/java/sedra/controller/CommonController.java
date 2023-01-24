/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import sedra.aditional.Alerta;
import sedra.aditional.ArchivoUpload;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "CommonController")
@SessionScoped
public class CommonController implements Serializable {

    private static final Logger LOG = Logger.getLogger(CommonController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());
    private Integer tiempoSesionActiva;

    @Inject
    TramitacionController tramitacionController;

    private List<Alerta> listaAlerta;
    private int number;

    /**
     * Creates a new instance of CommonController
     */
    public CommonController() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Alerta> getListaAlerta() {
        return listaAlerta;
    }

    public void setListaAlerta(List<Alerta> listaAlerta) {
        this.listaAlerta = listaAlerta;
    }

    @PostConstruct
    public void init() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        this.tiempoSesionActiva = session.getMaxInactiveInterval() * 1000;
        System.out.println("Tiempo de sesión: " + tiempoSesionActiva);
    }

    public Integer getTiempoSesionActiva() {
        return tiempoSesionActiva;
    }

    public void setTiempoSesionActiva(Integer tiempoSesionActiva) {
        this.tiempoSesionActiva = tiempoSesionActiva;
    }

    public void initError() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String urlError = (String) request.getAttribute("javax.servlet.error.request_uri");

        LOG.log(Level.SEVERE, "!URL Error: {0} from IP: {1}", new Object[]{urlError, JSFutil.getClientIpAddr(request)});
    }

    public void doExcepcion(Exception e) {
        String msg = "";
        if (e instanceof EJBException) {
            EJBException ex = (EJBException) e;
            Throwable t = ex.getCause();
            while ((t != null) && !(t instanceof DatabaseException)) {
                t = t.getCause();
            }
            if (t != null) {
                msg = t.getLocalizedMessage();
            }
            if (t instanceof DatabaseException) {
                msg = this.bundle.getString("UpdateError") + t.getMessage();
                JSFutil.addMessage(msg, JSFutil.StatusMessage.ERROR);
            } else {
                JSFutil.addMessage(this.bundle.getString("UpdateError") + " " + msg, JSFutil.StatusMessage.ERROR);
            }
        } else {
            msg = this.bundle.getString("UpdateError") + e.getMessage();
            JSFutil.addMessage(msg, JSFutil.StatusMessage.ERROR);
        }
        LOG.log(Level.SEVERE, null, e);
    }

    public StreamedContent downloadAdjuntoTMP(ArchivoUpload adjunto) throws IOException {
        if (adjunto.getData() != null) {
            InputStream input = new ByteArrayInputStream(adjunto.getData());
            return DefaultStreamedContent.builder().name(adjunto.getFileName()).contentType(adjunto.getMimeType()).stream(() -> input).build();
        } else if (!adjunto.getFolder().isEmpty()) { //Si se trata de adjuntos ya guardados en el filesystem
            File archivo = new File(adjunto.toNameFileSystem());
            if (archivo.exists()) {
                FileInputStream input = new FileInputStream(archivo);
                return DefaultStreamedContent.builder().name(adjunto.getFileName()).contentType(adjunto.getMimeType()).stream(() -> input).build();
            }
        }
        return null;
    }

    public String getServerURL() {
        //System.out.print("Web: "+JSFutil.getAbsoluteApplicationUrl());
        return JSFutil.getAbsoluteApplicationUrl();
    }

    public String getServerURLDownload() {
        return JSFutil.getAbsoluteApplicationUrl() + "/descarga";
    }

    public String doDiferenciaTiempo(Date finicio, Date hinicio, Date ffin, Date hfin) {
        return JSFutil.diferenciaTime(finicio, hinicio, ffin, hfin);
    }

    public Date calcularFechaMinima(Object ob) {
        Calendar cal = JSFutil.getCalendar();
        cal.add(Calendar.MONTH, -6);
        //LOG.log(Level.INFO, JSFutil.getFechaHoraLargo(cal.getTime()));
        if (ob != null) {
            return null;
        }
        return cal.getTime();
    }

    public void checkAlertas() {
        this.number++;
        this.listaAlerta = new ArrayList<>();
        this.tramitacionController.checkPendientes();
    }

    public String msgAlertaTitle() {
        if (this.listaAlerta==null || this.listaAlerta.isEmpty()) {
            return "";
        } else {
            Integer cantidad=this.listaAlerta.get(0).getCantidad();
            return "Alerta de mensajes [" + cantidad + "]";
        }
    }

}
