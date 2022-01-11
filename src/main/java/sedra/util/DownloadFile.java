/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sedra.fachada.ConfiguracionFacade;
import sedra.fachada.DocumentoAdjuntoFacade;
import sedra.fachada.DocumentoFacade;
import sedra.fachada.NotaSalidaFacade;
import sedra.fachada.TramitacionFacade;
import sedra.modelo.Configuracion;
import sedra.modelo.Documento;
import sedra.modelo.DocumentoAdjunto;
import sedra.modelo.NotaSalida;
import sedra.modelo.Tramitacion;

/**
 *
 * @author root
 */
@Named(value = "DownloadFile")
@SessionScoped
public class DownloadFile implements Serializable {

    private static final Logger LOG = Logger.getLogger(DownloadFile.class.getName());

    @Inject
    DocumentoFacade documentoFacade;
    @Inject
    DocumentoAdjuntoFacade documentoAdjuntoFacade;
    @Inject
    NotaSalidaFacade notaSalidaFacade;
    @Inject
    TramitacionFacade tramitacionFacade;
    @Inject
    ConfiguracionFacade configuracionFacade;

    private String pagina;
    private Integer id;

    /**
     * Creates a new instance of DownloadFile
     */
    public DownloadFile() {
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StreamedContent downloadDocumentoAdjunto(Integer id) throws FileNotFoundException {
        DocumentoAdjunto pa = documentoAdjuntoFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdDocumentoAdjunto() + "-" + pa.getNombreArchivo());
        System.out.println("ARCHIVO: " + pa.getClass() + " con ID " + id + pa.getNombreArchivo());
        //if (archivo.exists()) {
        FileInputStream input = new FileInputStream(archivo);
        return DefaultStreamedContent.builder().name(pa.getNombreArchivo()).contentType(pa.getTipoArchivoMime()).stream(() -> input).build();

    }

    public StreamedContent downloadDocumento(Integer id) throws FileNotFoundException {
        Documento pa = documentoFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdDocumento() + "-" + pa.getNombreArchivo());
        System.out.println("ARCHIVO: " + pa.getClass() + " con ID " + id + pa.getNombreArchivo());
        //if (archivo.exists()) {
        FileInputStream input = new FileInputStream(archivo);
        return DefaultStreamedContent.builder().name(pa.getNombreArchivo()).contentType(pa.getTipoArchivo()).stream(() -> input).build();
    }

    public StreamedContent downloadNotaSalida(Integer id) throws FileNotFoundException {
        NotaSalida pa = notaSalidaFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdNota() + "-" + pa.getNombreArchivo());
        System.out.println("ARCHIVO: " + pa.getClass() + " con ID " + id + pa.getNombreArchivo());
        //if (archivo.exists()) {
        FileInputStream input = new FileInputStream(archivo);
        return DefaultStreamedContent.builder().name(pa.getNombreArchivo()).contentType(pa.getTipoArchivo()).stream(() -> input).build();
    }

    public StreamedContent downloadDocumentoTramite(Integer id) throws FileNotFoundException {
        Tramitacion pa = tramitacionFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdTramitacion() + "-" + pa.getNombreArchivo());
        System.out.println("ARCHIVO: " + pa.getClass() + " con ID " + id + pa.getNombreArchivo());
        //if (archivo.exists()) {
        FileInputStream input = new FileInputStream(archivo);
        return DefaultStreamedContent.builder().name(pa.getNombreArchivo()).contentType(pa.getTipoArchivo()).stream(() -> input).build();

    }

    public StreamedContent logoToDisplay(Integer id) {
        Configuracion configuracion = configuracionFacade.find(id);
        if (configuracion.getArchivoLogo() != null) {
            InputStream input = new ByteArrayInputStream(configuracion.getArchivoLogo());
            return DefaultStreamedContent.builder().name(configuracion.getLogoFileName()).contentType(configuracion.getLogoFileType()).stream(() -> input).build();
        } else {
            return null;
        }
    }

}
