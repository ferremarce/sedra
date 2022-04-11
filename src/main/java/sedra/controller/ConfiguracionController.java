/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import sedra.fachada.ConfiguracionFacade;
import sedra.modelo.Configuracion;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "ConfiguracionController")
@SessionScoped
public class ConfiguracionController implements Serializable {

    @Inject
    CommonController commonController;
    @Inject
    ConfiguracionFacade configuracionFacade;

    private Configuracion configuracion;

    /**
     * Creates a new instance of ConfiguracionController
     */
    public ConfiguracionController() {
    }

    @PostConstruct
    public void initConfig() {
        this.configuracion = configuracionFacade.find(1);
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    public void handleFileUpload(FileUploadEvent event) {
//        if (this.imagenLogo == null || this.imagenLogo.isEmpty()) {
//            this.imagenLogo = new ArrayList<>();
//        }
//        this.imagenLogo.add(event.getFile());
        UploadedFile uf = event.getFile();
        this.configuracion.setArchivoLogo(uf.getContent());
        this.configuracion.setLogoFileType(uf.getContentType());
        this.configuracion.setLogoFileName(JSFutil.sanitizeFilename(uf.getFileName()));

//PrimeFaces.current().ajax().update("formMain:acordeon:logoCargado");
    }

    public String editSetup() {
        this.configuracion = configuracionFacade.find(1);
        return "/configuracion/EditarConfiguracion";
    }

    public void edit() {
        try {
            if (this.configuracion.getTiempoAlerta() < 20) {
                JSFutil.addMessage("El tiempo de chequeo mínimo es 20seg", JSFutil.StatusMessage.ERROR);
                return;
            }
            configuracionFacade.edit(configuracion);
            //this.configuracion = configuracionFacade.find(this.configuracion.getIdConfiguracion());
            JSFutil.addMessage("Configuración guardada exitosamente", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
    }

    public void removeLogo() {
        try {
            this.configuracion.setArchivoLogo(null);
            this.configuracion.setLogoFileName(null);
            this.configuracion.setLogoFileType(null);
            configuracionFacade.edit(configuracion);
            //this.configuracion = configuracionFacade.find(this.configuracion.getIdConfiguracion());
            JSFutil.addMessage("Configuración guardada exitosamente", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
    }

    public StreamedContent logoToDisplay() {
        if (this.configuracion.getArchivoLogo() != null) {
            InputStream input = new ByteArrayInputStream(this.configuracion.getArchivoLogo());
            return DefaultStreamedContent.builder().name(this.configuracion.getLogoFileName()).contentType(this.configuracion.getLogoFileType()).stream(() -> input).build();
        } else {
            return null;
        }
    }

}
