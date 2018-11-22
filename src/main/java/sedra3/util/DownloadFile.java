/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sedra3.fachada.ConfiguracionFacade;
import sedra3.fachada.DocumentoAdjuntoFacade;
import sedra3.fachada.DocumentoFacade;
import sedra3.fachada.NotaSalidaFacade;
import sedra3.fachada.TramitacionFacade;
import sedra3.modelo.Configuracion;
import sedra3.modelo.Documento;
import sedra3.modelo.DocumentoAdjunto;
import sedra3.modelo.NotaSalida;
import sedra3.modelo.Tramitacion;

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
        if (archivo.exists()) {
            StreamedContent file = new DefaultStreamedContent(new FileInputStream(archivo), pa.getTipoArchivoMime(), pa.getNombreArchivo());
            return file;
        } else {
            System.out.println("ARCHIVO_NO_ENCONTRADO: " + pa.getClass() + " con ID " + pa.getIdDocumentoAdjunto());
            JSFutil.addMessage("No dispone de adjuntos para visualizar...", JSFutil.StatusMessage.WARNING);
            String noContent = "<html><h1>Sin adjunto...</></html>";
            return new DefaultStreamedContent(new ByteArrayInputStream(noContent.getBytes()), "text/html", "No existe Archivo");
        }
    }

    public StreamedContent downloadDocumento(Integer id) throws FileNotFoundException {
        Documento pa = documentoFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdDocumento() + "-" + pa.getNombreArchivo());
        if (archivo.exists()) {
            StreamedContent file = new DefaultStreamedContent(new FileInputStream(archivo), pa.getTipoArchivo(), pa.getNombreArchivo());
            return file;
        } else {
            System.out.println("ARCHIVO_NO_ENCONTRADO: " + pa.getClass() + " con ID " + pa.getIdDocumento());
            JSFutil.addMessage("No dispone de adjuntos para visualizar...", JSFutil.StatusMessage.WARNING);
            String noContent = "<html><h1>Sin adjunto...</></html>";
            return new DefaultStreamedContent(new ByteArrayInputStream(noContent.getBytes()), "text/html", "No existe Archivo");
        }
    }

    public StreamedContent downloadNotaSalida(Integer id) throws FileNotFoundException {
        NotaSalida pa = notaSalidaFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdNota() + "-" + pa.getNombreArchivo());
        if (archivo.exists()) {
            StreamedContent file = new DefaultStreamedContent(new FileInputStream(archivo), pa.getTipoArchivo(), pa.getNombreArchivo());
            return file;
        } else {
            System.out.println("ARCHIVO_NO_ENCONTRADO: " + pa.getClass() + " con ID " + pa.getIdNota());
            JSFutil.addMessage("No dispone de adjuntos para visualizar...", JSFutil.StatusMessage.WARNING);
            String noContent = "<html><h1>Sin adjunto...</></html>";
            return new DefaultStreamedContent(new ByteArrayInputStream(noContent.getBytes()), "text/html", "No existe Archivo");
        }
    }

    public StreamedContent downloadDocumentoTramite(Integer id) throws FileNotFoundException {
        Tramitacion pa = tramitacionFacade.find(id);
        File archivo = new File(JSFutil.folderDocumento + pa.getIdTramitacion() + "-" + pa.getNombreArchivo());
        if (archivo.exists()) {
            StreamedContent file = new DefaultStreamedContent(new FileInputStream(archivo), pa.getTipoArchivo(), pa.getNombreArchivo());
            return file;
        } else {
            System.out.println("ARCHIVO_NO_ENCONTRADO: " + pa.getClass() + " con ID " + pa.getIdDocumento());
            JSFutil.addMessage("No dispone de adjuntos para visualizar...", JSFutil.StatusMessage.WARNING);
            String noContent = "<html><h1>Sin adjunto...</></html>";
            return new DefaultStreamedContent(new ByteArrayInputStream(noContent.getBytes()), "text/html", "No existe Archivo");
        }
    }
    public StreamedContent logoToDisplay(Integer id) {
        Configuracion configuracion=configuracionFacade.find(id);
        if (configuracion.getArchivoLogo() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(configuracion.getArchivoLogo()), configuracion.getLogoFileType(), configuracion.getLogoFileName());
        } else {
            return null;
        }
    }

}
