/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import java.io.ByteArrayInputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.UploadedFile;
import sedra3.fachada.AuditaFacade;
import sedra3.fachada.DocumentoAdjuntoFacade;
import sedra3.fachada.DocumentoFacade;
import sedra3.fachada.TramitacionFacade;
import sedra3.modelo.Audita;
import sedra3.modelo.Clasificador;
import sedra3.modelo.Documento;
import sedra3.modelo.DocumentoAdjunto;
import sedra3.modelo.EstadoTramitacion;
import sedra3.modelo.Tramitacion;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "DocumentoController")
@SessionScoped
public class DocumentoController implements Serializable {

    private static final Logger LOG = Logger.getLogger(DocumentoController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    DocumentoFacade documentoFacade;
    @Inject
    CommonController commonController;
    @Inject
    ClasificadorController clasificadorController;
    @Inject
    AuditaFacade auditaFacade;
    @Inject
    DocumentoAdjuntoFacade documentoAdjuntoFacade;
    @Inject
    TramitacionFacade tramitacionFacade;

    private Documento documento;
    private List<Documento> listaDocumento;
    private List<UploadedFile> adjuntoDocumento;
    private String criterio;
    private Integer idClasificadorTmp;

    /**
     * Creates a new instance of DocumentoController
     */
    public DocumentoController() {
    }

    public Integer getIdClasificadorTmp() {
        return idClasificadorTmp;
    }

    public void setIdClasificadorTmp(Integer idClasificadorTmp) {
        this.idClasificadorTmp = idClasificadorTmp;
    }

    public List<UploadedFile> getAdjuntoDocumento() {
        return adjuntoDocumento;
    }

    public void setAdjuntoDocumento(List<UploadedFile> adjuntoDocumento) {
        this.adjuntoDocumento = adjuntoDocumento;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public List<Documento> getListaDocumento() {
        return listaDocumento;
    }

    public void setListaDocumento(List<Documento> listaDocumento) {
        this.listaDocumento = listaDocumento;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

///---------------------METODOS---------------------///
    public String listDocumentoSetup() {
        return "/documento/ListarDocumento";
    }

    public String createSetup() {
        this.documento = new Documento();
        this.idClasificadorTmp = null;
        this.adjuntoDocumento = new ArrayList<>();
        this.documento.setFechaDocumento(JSFutil.getFechaHoraActual());
        this.documento.setFechaIngreso(JSFutil.getFechaHoraActual());
        this.documento.setFechaLimite(JSFutil.getFechaHoraActual());
        this.documento.setAnho(Calendar.getInstance(JSFutil.getMyTimeZone()).get(Calendar.YEAR));
        this.clasificadorController.cargarTree();
        return "/documento/CrearDocumento";
    }

    public String editSetup(Integer idDocumento) {
        this.documento = documentoFacade.find(idDocumento);
        this.adjuntoDocumento = new ArrayList<>();
        this.clasificadorController.cargarTree();
        return "/documento/CrearDocumento";
    }

    public String delete(Integer idDocumento) {
        try {
            Documento u = documentoFacade.find(idDocumento);
            String name = u.getAsunto();
            documentoFacade.remove(u);
            this.criterio = name;
            this.doBuscar();
            JSFutil.addMessage("El Documento #" + name + "# fue eliminado.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "/documento/ListarDocumento";
    }

    public String create() {
        try {
            if (this.documento.getIdDocumento() == null) {
                Documento d = documentoFacade.getDocumentoByNroEntradaAnho(this.documento.getNroEntrada(), this.documento.getAnho());
                if (d != null) {
                    JSFutil.addMessage("El Nro. de entrada " + this.documento.getNroEntrada() + " ya existe en el año " + this.documento.getAnho(), JSFutil.StatusMessage.WARNING);
                    return "";
                }
                this.documento.setIdUsuario(JSFutil.getUsuarioConectado());
                this.documento.setCerrado(false);
                documentoFacade.create(documento);
                auditaFacade.create(new Audita("DOCUMENTO", "Documento creado exitosamente. ", JSFutil.getFechaHoraActual(), documento.toAudita(), JSFutil.getUsuarioConectado()));
                JSFutil.addMessage("Documento creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
                //Grabar el archivo a disco
                if (this.adjuntoDocumento.size() > 0) {
                    DocumentoAdjunto ap;
                    for (UploadedFile uf : this.adjuntoDocumento) {
                        ap = new DocumentoAdjunto();
                        ap.setTipoArchivoMime(uf.getContentType());
                        ap.setTamanhoArchivo(BigInteger.valueOf(uf.getSize()));
                        ap.setNombreArchivo(JSFutil.sanitizeFilename(uf.getFileName()));
                        ap.setIdDocumento(documento);
                        //ap.setTipoAdjunto("PROYECTO");
                        ap.setFechaRegistro(JSFutil.getFechaHoraActual());
                        documentoAdjuntoFacade.create(ap);
                        int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(uf.getContents()), JSFutil.folderDocumento + ap.getIdDocumentoAdjunto() + "-" + JSFutil.sanitizeFilename(uf.getFileName()));
                        if (resultado != 0) {
                            JSFutil.addMessage("No se ha podido guardar el adjunto debido a un error interno en el procesamiento del archivo. Se deshace el guardado del archivo.", JSFutil.StatusMessage.ERROR);
                            documentoAdjuntoFacade.remove(ap);
                        }
                    }
                }
                Tramitacion t = new Tramitacion();
                t.setIdDocumento(documento);
                t.setFechaDerivacion(documento.getFechaIngreso());
                t.setIdEstado(new EstadoTramitacion(1));
                t.setFechaRegistro(documento.getFechaRegistro());
                t.setHoraRegistro(documento.getHoraRegistro());
                //t.setArchivo(u.getArchivo());
//                t.setNombreArchivo(documento.getNombreArchivo());
//                t.setTamanhoArchivo(u.getTamanhoArchivo());
//                t.setTipoArchivo(u.getTipoArchivo());
                t.setIdUsuario(documento.getIdUsuario());
                t.setIdRol(documento.getIdUsuario().getIdRol());
                t.setRemitidoPor(documento.getIdUsuario().getUsuario());
                t.setRemitidoA(documento.getIdUsuario().getUsuario());
                t.setNotaBreve("Entrada del Documento");
                t.setIdUsuarioRemitente(documento.getIdUsuario());
                //t.setConfirmaRecepcion(Boolean.TRUE);
                tramitacionFacade.create(t);
            } else {
                documentoFacade.edit(documento);
                if (this.adjuntoDocumento.size() > 0) {
                    DocumentoAdjunto ap;
                    for (UploadedFile uf : this.adjuntoDocumento) {
                        ap = new DocumentoAdjunto();
                        ap.setTipoArchivoMime(uf.getContentType());
                        ap.setTamanhoArchivo(BigInteger.valueOf(uf.getSize()));
                        ap.setNombreArchivo(JSFutil.sanitizeFilename(uf.getFileName()));
                        ap.setIdDocumento(documento);
                        //ap.setTipoAdjunto("PROYECTO");
                        ap.setFechaRegistro(JSFutil.getFechaHoraActual());
                        documentoAdjuntoFacade.create(ap);
                        int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(uf.getContents()), JSFutil.folderDocumento + ap.getIdDocumentoAdjunto() + "-" + JSFutil.sanitizeFilename(uf.getFileName()));
                        if (resultado != 0) {
                            JSFutil.addMessage("No se ha podido guardar el adjunto debido a un error interno en el procesamiento del archivo. Se deshace el guardado del archivo.", JSFutil.StatusMessage.ERROR);
                            documentoAdjuntoFacade.remove(ap);
                        }
                    }
                }
            }
            this.criterio = documento.getAsunto();
            this.doBuscar();
            JSFutil.addMessage("Documento creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "/documento/ListarDocumento";
    }

    public String doBuscar() {
        if (this.criterio.isEmpty()) {
            JSFutil.addMessage("No hay criterios para buscar...", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listaDocumento = documentoFacade.getAllDocumento(this.criterio);
        if (this.listaDocumento.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaDocumento.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }

    public String doRefrescar() {
        this.listaDocumento = documentoFacade.getAllDocumento("%");
        if (this.listaDocumento.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaDocumento.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }

    public String doBorrarAdjunto(Integer id) {
        try {
            DocumentoAdjunto ea = documentoAdjuntoFacade.find(id);
            String name = ea.getNombreArchivo();
            Boolean resultado = JSFutil.deleteFileFromDisk(JSFutil.folderDocumento + ea.getIdDocumentoAdjunto() + "-" + ea.getNombreArchivo());
            if (!resultado) {
                JSFutil.addMessage("Pero no se ha podido procesar el adjunto debido a un error interno en el procesamiento", JSFutil.StatusMessage.WARNING);
            }
            //Solo se borra el registro si el archivo existe fisicamente en el servidor
            documentoAdjuntoFacade.remove(ea);
            JSFutil.addMessage("El Adjunto #" + name + "# ha sido eliminado.", JSFutil.StatusMessage.INFORMATION);
            this.documento = documentoFacade.find(this.documento.getIdDocumento());
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "";
    }

    public void handleFileUpload(FileUploadEvent event) {
        //LOG.log(Level.INFO, "Agregado el archivo {0}", event.getFile().getFileName());
        this.adjuntoDocumento.add(event.getFile());
    }

    public String doVerForm(Integer idExpediente) {
        this.documento = documentoFacade.find(idExpediente);
        return "/documento/VerDocumento?faces-redirect=true";
    }

    public void onNodeSelect(NodeSelectEvent event) {
        Clasificador c = (Clasificador) event.getTreeNode().getData();
        System.out.println("Seleccionado: " + c.toString());
        this.documento.setIdClasificador(c);
    }
}