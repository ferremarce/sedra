/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import sedra3.fachada.ConfiguracionFacade;
import sedra3.modelo.Configuracion;
import sedra3.util.JSFutil;

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
    private List<UploadedFile> imagenLogo;

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
        this.configuracion.setArchivoLogo(uf.getContents());
        this.configuracion.setLogoFileType(uf.getContentType());
        this.configuracion.setLogoFileName(JSFutil.sanitizeFilename(uf.getFileName()));

//PrimeFaces.current().ajax().update("formMain:acordeon:logoCargado");
    }
    
    public String editSetup() {
        this.imagenLogo = new ArrayList<>();
        this.configuracion = configuracionFacade.find(1);
        return "/configuracion/EditarConfiguracion";
    }
    
    public void edit() {
        try {
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
            return new DefaultStreamedContent(new ByteArrayInputStream(this.configuracion.getArchivoLogo()), this.configuracion.getLogoFileType(), this.configuracion.getLogoFileName());
        } else {
            return null;
        }
    }
    
}
