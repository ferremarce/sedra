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
import java.util.Date;
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
import sedra3.fachada.NotaSalidaFacade;
import sedra3.fachada.TramitacionFacade;
import sedra3.modelo.Audita;
import sedra3.modelo.Clasificador;
import sedra3.modelo.Documento;
import sedra3.modelo.DocumentoAdjunto;
import sedra3.modelo.EstadoTramitacion;
import sedra3.modelo.NotaSalida;
import sedra3.modelo.Rol;
import sedra3.modelo.TipoDocumento;
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
    @Inject
    NotaSalidaFacade notaSalidaFacade;
    
    private Documento documento;
    private List<Documento> listaDocumento;
    private List<UploadedFile> adjuntoDocumento;
    private String criterio;
    private Integer idClasificadorTmp;
    private Clasificador clasificadorSeleccionado;
    private List<NotaSalida> listaNotaSalida;
    private Integer tmpConNota = 1; //Se utiliza para filtrar la busqueda de documentos para llavear/desllavear
    private String selectedOption = "d.asunto";
    private Date tmpFechaDesde = new Date();
    private Date tmpFechaHasta = new Date();

    /**
     * Creates a new instance of DocumentoController
     */
    public DocumentoController() {
    }
    
    public List<NotaSalida> getListaNotaSalida() {
        return listaNotaSalida;
    }
    
    public void setListaNotaSalida(List<NotaSalida> listaNotaSalida) {
        this.listaNotaSalida = listaNotaSalida;
    }
    
    public Clasificador getClasificadorSeleccionado() {
        return clasificadorSeleccionado;
    }
    
    public void setClasificadorSeleccionado(Clasificador clasificadorSeleccionado) {
        this.clasificadorSeleccionado = clasificadorSeleccionado;
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
    
    public Integer getTmpConNota() {
        return tmpConNota;
    }
    
    public void setTmpConNota(Integer tmpConNota) {
        this.tmpConNota = tmpConNota;
    }
    
    public String getSelectedOption() {
        return selectedOption;
    }
    
    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
    
    public Date getTmpFechaDesde() {
        return tmpFechaDesde;
    }
    
    public void setTmpFechaDesde(Date tmpFechaDesde) {
        this.tmpFechaDesde = tmpFechaDesde;
    }
    
    public Date getTmpFechaHasta() {
        return tmpFechaHasta;
    }
    
    public void setTmpFechaHasta(Date tmpFechaHasta) {
        this.tmpFechaHasta = tmpFechaHasta;
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
        this.clasificadorController.cargarTree(Boolean.FALSE);
        return "/documento/CrearDocumento";
    }
    
    public String editSetup(Integer idDocumento) {
        this.documento = documentoFacade.find(idDocumento);
        this.adjuntoDocumento = new ArrayList<>();
        this.clasificadorController.cargarTree(Boolean.FALSE);
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
    
    public String doBuscarSeguimiento() {
        if (this.criterio.isEmpty()) {
            JSFutil.addMessage("No hay criterios para buscar...", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listaDocumento = documentoFacade.getAllDocumentoParaSeguimiento(this.criterio);
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
    
    public void verNodeSelect(NodeSelectEvent event) {
        JSFutil.addMessage("Seleccionado: " + event.getTreeNode().toString(), JSFutil.StatusMessage.INFORMATION);
        this.clasificadorSeleccionado = (Clasificador) event.getTreeNode().getData();
        this.listaDocumento = documentoFacade.getAllDocumentoPlanArchivo(this.clasificadorSeleccionado.getIdClasificador());
        this.listaNotaSalida = notaSalidaFacade.getAllNotaSalidaPlanArchivo(this.clasificadorSeleccionado.getIdClasificador());
        if (this.listaDocumento.isEmpty() && this.listaNotaSalida.isEmpty()) {
            JSFutil.addMessage("No hay Entradas ni Salidas...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaDocumento.size() + " Entradas y " + this.listaNotaSalida.size() + " Salidas", JSFutil.StatusMessage.INFORMATION);
        }
    }
    
    public void insertarTramitacion(Integer idDocumento) {
        try {
            Documento d = documentoFacade.find(idDocumento);
            Tramitacion tramitacion = new Tramitacion();
            //tramitacion.setArchivo(d.getArchivo());
            tramitacion.setTipoArchivo(d.getTipoArchivo());
            tramitacion.setTamanhoArchivo(d.getTamanhoArchivo());
            tramitacion.setNombreArchivo(d.getNombreArchivo());
            tramitacion.setIdRol(new Rol(18));//Rol de Archivo
            tramitacion.setProcesadoArchivo(false);
            tramitacion.setIdDocumento(d);
            tramitacion.setFechaDerivacion(JSFutil.getFechaHoraActual());
            //tramitacion.setIdUsuario(user);
            tramitacion.setIdUsuarioRemitente(JSFutil.getUsuarioConectado());
            tramitacion.setIdEstado(new EstadoTramitacion(1));
            tramitacionFacade.create(tramitacion);
            this.listaDocumento = documentoFacade.getAllDocumentoPlanArchivo(this.clasificadorSeleccionado.getIdClasificador());
            JSFutil.addMessage("Se ha enviado el documento a Archivo. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            //e.printStackTrace();
            this.commonController.doExcepcion(ex);
        }
        //this.tramitacion = new Tramitacion();
        //return null;
    }
    
    public void handleDirectFileUpload(FileUploadEvent event) {
        this.adjuntoDocumento = new ArrayList<>();
        this.adjuntoDocumento.add(event.getFile());
    }
    
    public void save() {
        for (UploadedFile adjunto : this.adjuntoDocumento) {
            this.documento = new Documento();
//        this.documento.setArchivo(this.adjunto.getContents());
            this.documento.setCerrado(false);
            this.documento.setFechaDocumento(JSFutil.getFechaHoraActual());
            this.documento.setNroEntrada("xxxx");
            this.documento.setAsunto("Documento suelto: " + adjunto.getFileName());
            this.documento.setFechaRegistro(JSFutil.getFechaHoraActual());
            this.documento.setHoraRegistro(JSFutil.getFechaHoraActual());
            this.documento.setIdClasificador(clasificadorSeleccionado);
            this.documento.setIdTipoDocumento(new TipoDocumento(4));
            this.documento.setIdUsuario(JSFutil.getUsuarioConectado());
            
            try {
                documentoFacade.create(documento);
                this.listaDocumento = documentoFacade.getAllDocumentoPlanArchivo(this.clasificadorSeleccionado.getIdClasificador());
                JSFutil.addMessage("Documento creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);

                //Grabar el archivo a disco
                DocumentoAdjunto ap = new DocumentoAdjunto();
                ap.setTipoArchivoMime(adjunto.getContentType());
                ap.setTamanhoArchivo(BigInteger.valueOf(adjunto.getSize()));
                ap.setNombreArchivo(JSFutil.sanitizeFilename(adjunto.getFileName()));
                ap.setIdDocumento(documento);
                //ap.setTipoAdjunto("PROYECTO");
                ap.setFechaRegistro(JSFutil.getFechaHoraActual());
                documentoAdjuntoFacade.create(ap);
                int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(adjunto.getContents()), JSFutil.folderDocumento + ap.getIdDocumentoAdjunto() + "-" + JSFutil.sanitizeFilename(adjunto.getFileName()));
                if (resultado != 0) {
                    JSFutil.addMessage("No se ha podido guardar el adjunto debido a un error interno en el procesamiento del archivo. Se deshace el guardado del archivo.", JSFutil.StatusMessage.ERROR);
                    documentoAdjuntoFacade.remove(ap);
                }
            } catch (Exception ex) {
                this.commonController.doExcepcion(ex);
            }
        }
    }
    
    public String listAdjuntaDocumentoSetup() {
        this.listaDocumento = new ArrayList<>();
        return "/tramitacion/ListarDocumentoAdjunto";
    }
    
    public String listLocalizarDocumentoSetup() {
//        this.criterioBusqueda = "";
//        this.model = null;
//        this.tmpFechaDesde = new Date();
//        this.tmpFechaHasta = new Date();
        return "/documento/LocalizarDocumento";
    }
    
    public void buscarDocumentoParaArchivo() {
        this.listaDocumento = documentoFacade.getAllDocumentoByExpediente(this.criterio);
        if (this.listaDocumento.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaDocumento.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
    }
    
    public void localizarDocumento() {
        this.listaDocumento = documentoFacade.getAllDocumento(this.getCriterio(), this.selectedOption, this.tmpFechaDesde, this.tmpFechaHasta);
        if (this.listaDocumento.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaDocumento.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
    }
    
    public void desbloquearDocumento(Integer id) {
        try {
            this.documento = documentoFacade.find(id);
            this.documento.setCerrado(false);
            documentoFacade.edit(this.documento);
            auditaFacade.create(new Audita("DOCUMENTO", "Documento desbloqueado exitosamente. ", JSFutil.getFechaHoraActual(), documento.toAudita(), JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("Documento desbloqueado exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        this.buscarDocumentoSinNota();
        //return "";
    }
    
    public void buscarDocumentoSinNota() {
        this.listaDocumento = documentoFacade.getAllDocumentoSinNota(this.criterio);
        if (this.listaDocumento.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaDocumento.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
    }

    public void bloquearDocumento(Integer id) {
        try {
            this.documento = documentoFacade.find(id);
            this.documento.setCerrado(true);
            documentoFacade.edit(this.documento);
            auditaFacade.create(new Audita("DOCUMENTO", "Documento bloqueado exitosamente. ", JSFutil.getFechaHoraActual(), documento.toAudita(), JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("Documento bloqueado exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        this.buscarDocumentoSinNota();
        // return "";
    }
}
