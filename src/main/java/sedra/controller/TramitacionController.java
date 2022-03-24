/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import java.io.ByteArrayInputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import sedra.aditional.Alerta;
import sedra.fachada.AuditaFacade;
import sedra.fachada.ClasificadorFacade;
import sedra.fachada.DetalleNotaSalidaFacade;
import sedra.fachada.DocumentoFacade;
import sedra.fachada.NotaSalidaFacade;
import sedra.fachada.TramitacionFacade;
import sedra.modelo.Audita;
import sedra.modelo.DetalleNotaSalida;
import sedra.modelo.Documento;
import sedra.modelo.EstadoTramitacion;
import sedra.modelo.NotaSalida;
import sedra.modelo.Rol;
import sedra.modelo.Tramitacion;
import sedra.util.Codigo;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "TramitacionController")
@SessionScoped
public class TramitacionController implements Serializable {

    private static final Logger LOG = Logger.getLogger(TramitacionController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    ClasificadorController clasificadorController;
    @Inject
    ClasificadorFacade clasificadorFacade;
    @Inject
    DocumentoController documentoController;
    @Inject
    TramitacionFacade tramitacionFacade;
    @Inject
    CommonController commonController;
    @Inject
    AuditaFacade auditaFacade;
    @Inject
    DocumentoFacade documentoFacade;
    @Inject
    NotaSalidaFacade notaSalidaFacade;
    @Inject
    DetalleNotaSalidaFacade detalleNotaSalidaFacade;

    private Tramitacion tramitacion;
    private Tramitacion tramitacionRechazo;
    private List<Tramitacion> listaTramitacionPendiente;
    private List<Tramitacion> listaTramitacionConfirmado;
    private String criterioBusqueda = "%";
    private Tramitacion[] selectedTramitacion;
    private List<Tramitacion> listSelectedTramitacion;
    private List<Tramitacion> listaTramitacionSeguimiento;
    private Rol[] selectedRol;
    private Rol rolDerivado;
    private UploadedFile adjunto;
    private Documento documento;

    /**
     * Creates a new instance of TramitacionController
     */
    public TramitacionController() {
    }

    public Rol getRolDerivado() {
        return rolDerivado;
    }

    public void setRolDerivado(Rol rolDerivado) {
        this.rolDerivado = rolDerivado;
    }

    public Rol[] getSelectedRol() {
        return selectedRol;
    }

    public void setSelectedRol(Rol[] selectedRol) {
        this.selectedRol = selectedRol;
    }

    public UploadedFile getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(UploadedFile adjunto) {
        this.adjunto = adjunto;
    }

    public List<Tramitacion> getListSelectedTramitacion() {
        return listSelectedTramitacion;
    }

    public void setListSelectedTramitacion(List<Tramitacion> listSelectedTramitacion) {
        this.listSelectedTramitacion = listSelectedTramitacion;
    }

    public Tramitacion[] getSelectedTramitacion() {
        return selectedTramitacion;
    }

    public void setSelectedTramitacion(Tramitacion[] selectedTramitacion) {
        this.selectedTramitacion = selectedTramitacion;
    }

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    public Tramitacion getTramitacion() {
        return tramitacion;
    }

    public void setTramitacion(Tramitacion tramitacion) {
        this.tramitacion = tramitacion;
    }

    public Tramitacion getTramitacionRechazo() {
        return tramitacionRechazo;
    }

    public void setTramitacionRechazo(Tramitacion tramitacionRechazo) {
        this.tramitacionRechazo = tramitacionRechazo;
    }

    public List<Tramitacion> getListaTramitacionPendiente() {
        return listaTramitacionPendiente;
    }

    public void setListaTramitacionPendiente(List<Tramitacion> listaTramitacionPendiente) {
        this.listaTramitacionPendiente = listaTramitacionPendiente;
    }

    public List<Tramitacion> getListaTramitacionConfirmado() {
        return listaTramitacionConfirmado;
    }

    public void setListaTramitacionConfirmado(List<Tramitacion> listaTramitacionConfirmado) {
        this.listaTramitacionConfirmado = listaTramitacionConfirmado;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public List<Tramitacion> getListaTramitacionSeguimiento() {
        return listaTramitacionSeguimiento;
    }

    public void setListaTramitacionSeguimiento(List<Tramitacion> listaTramitacionSeguimiento) {
        this.listaTramitacionSeguimiento = listaTramitacionSeguimiento;
    }

    public String listPendientesSetup() {
        this.listaTramitacionPendiente = this.buscarPendiente("%", 1);
        this.listaTramitacionConfirmado = this.buscarPendiente("%", 3);
        if (this.listaTramitacionPendiente.isEmpty()) {
            JSFutil.addMessage("No hay pendientes de confirmación", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaTramitacionPendiente.size() + " pendientes no confirmados", JSFutil.StatusMessage.INFORMATION);
        }
        if (this.listaTramitacionConfirmado.isEmpty()) {
            JSFutil.addMessage("No hay pendientes para tramitación", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaTramitacionConfirmado.size() + " pendientes para tramitación", JSFutil.StatusMessage.INFORMATION);
        }
        return "/tramitacion/ListarDocumentoPendiente";
    }

    public List<Tramitacion> buscarPendiente(String criterio, Integer estado) {
        if (estado.compareTo(1) == 0) {
            return tramitacionFacade.getAllTramitacionPendientes(criterio, estado);
        } else {
            return tramitacionFacade.getAllTramitacionPendientes(criterio, estado);
        }
    }

    public void buscarAllPendiente() {
        if (this.criterioBusqueda.isEmpty()) {
            JSFutil.addMessage("No ha especificado un criterio para buscar", JSFutil.StatusMessage.WARNING);
            this.listaTramitacionPendiente = null;
            this.listaTramitacionConfirmado = null;
            return;
        }
        this.listaTramitacionPendiente = this.buscarPendiente(this.criterioBusqueda, 1);
        this.listaTramitacionConfirmado = this.buscarPendiente(this.criterioBusqueda, 3);
    }

    public String rechazaSetup(Integer idTramitacion) {
        this.tramitacion = tramitacionFacade.find(idTramitacion);
        this.tramitacionRechazo = new Tramitacion();
        return "/tramitacion/RechazarDocumento";
    }

    public String confirmaTramite(Integer idTramitacion) {
        try {
            this.tramitacion = tramitacionFacade.find(idTramitacion);
            this.tramitacion.setIdEstado(new EstadoTramitacion(3));
            this.tramitacion.setFechaConfirmacion(JSFutil.getFechaHoraActual());
            this.tramitacion.setHoraConfirmacion(JSFutil.getFechaHoraActual());
            this.tramitacion.setIdUsuarioConfirmacion(JSFutil.getUsuarioConectado());
            tramitacionFacade.edit(tramitacion);
            this.buscarPendiente("%", 1);
            this.buscarPendiente("%", 3);
            JSFutil.addMessage("Tramite confirmado exitosamente", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "";
    }

    public String rechazar() {
        try {

            this.tramitacionRechazo.setFechaDerivacion(JSFutil.getFechaHoraActual());
            this.tramitacionRechazo.setIdDocumento(this.tramitacion.getIdDocumento());
            this.tramitacionRechazo.setIdRol(this.tramitacion.getIdUsuarioRemitente().getIdRol());
            this.tramitacionRechazo.setNotaBreve("Rechazado según observaciones ");
            this.tramitacionRechazo.setRemitidoA(this.tramitacion.getIdUsuarioRemitente().getUsuario());

            this.tramitacionRechazo.setRemitidoPor(JSFutil.getUsuarioConectado().getUsuario());
            this.tramitacionRechazo.setFechaRegistro(JSFutil.getFechaHoraActual());
            this.tramitacionRechazo.setHoraRegistro(JSFutil.getFechaHoraActual());
            this.tramitacionRechazo.setIdUsuarioRemitente(JSFutil.getUsuarioConectado());
            //Automaticamente ya confirma la recepción porque la derivación ha sido rechazada
            this.tramitacionRechazo.setIdEstado(new EstadoTramitacion(3));
            this.tramitacionRechazo.setFechaConfirmacion(JSFutil.getFechaHoraActual());
            this.tramitacionRechazo.setHoraConfirmacion(JSFutil.getFechaHoraActual());
            this.tramitacionRechazo.setIdUsuarioConfirmacion(JSFutil.getUsuarioConectado());

            tramitacionFacade.create(this.tramitacionRechazo);
            auditaFacade.create(new Audita("TRAMITACION", "Tramitacion Rechazo creada exitosamente.", JSFutil.getFechaHoraActual(), this.tramitacionRechazo.toAudita(), JSFutil.getUsuarioConectado()));
            this.selectedTramitacion = null;

            //Sacar del pendiente
            this.tramitacion.setIdEstado(new EstadoTramitacion(2)); //Rechazado
            this.tramitacion.setIdUsuario(JSFutil.getUsuarioConectado());
            this.tramitacion.setFechaSalida(JSFutil.getFechaHoraActual());
            this.tramitacion.setHoraSalida(JSFutil.getFechaHoraActual());
            tramitacionFacade.edit(this.tramitacion);

            JSFutil.addMessage("Tramitacion rechazada exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            commonController.doExcepcion(ex);
        }
        this.buscarPendiente("%", 1);
        return "/tramitacion/ListarDocumentoPendiente";
        //return null;
    }

    public String derivaMultipleSetup() {
        if (this.selectedTramitacion.length == 0) {
            JSFutil.addMessage("Debe seleccionar al menos un documento para tramitar", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listSelectedTramitacion = (List<Tramitacion>) JSFutil.arrayToList(this.selectedTramitacion);
        this.tramitacion = new Tramitacion();
        this.selectedRol = new Rol[0];
        this.adjunto = null;
        this.tramitacion.setFechaDerivacion(JSFutil.getFechaHoraActual());
        return "/tramitacion/DerivarDocumento";
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.adjunto = event.getFile();
    }

    public String create() {
        try {
            Tramitacion tramTemp;
            for (Tramitacion tram : this.selectedTramitacion) {

                tram.setIdEstado(new EstadoTramitacion(100));
                tram.setIdUsuario(JSFutil.getUsuarioConectado());
                tram.setFechaSalida(JSFutil.getFechaHoraActual());
                tram.setHoraSalida(JSFutil.getFechaHoraActual());
                tramitacionFacade.edit(tram);

//                for (Rol rolDerivado : selectedRol) {
                tramTemp = new Tramitacion();
                if (this.adjunto != null) {
                    //tramTemp.setArchivo(this.adjunto.getContents());
                    tramTemp.setTipoArchivo(this.adjunto.getContentType());
                    tramTemp.setTamanhoArchivo(BigInteger.valueOf(this.adjunto.getSize()));
                    tramTemp.setNombreArchivo(this.adjunto.getFileName());
                }
                tramTemp.setFechaDerivacion(this.tramitacion.getFechaDerivacion());
                tramTemp.setIdDocumento(tram.getIdDocumento());
                tramTemp.setIdRol(rolDerivado);
                tramTemp.setNotaBreve(this.tramitacion.getNotaBreve());
                tramTemp.setRemitidoA(this.tramitacion.getRemitidoA());
                tramTemp.setObservacion(this.tramitacion.getObservacion());
                tramTemp.setProcesadoArchivo(false);

                tramTemp.setIdEstado(new EstadoTramitacion(1));
                tramTemp.setRemitidoPor(JSFutil.getUsuarioConectado().getUsuario());
                tramTemp.setIdUsuarioRemitente(JSFutil.getUsuarioConectado());
                tramTemp.setFechaRegistro(JSFutil.getFechaHoraActual());
                tramTemp.setHoraRegistro(JSFutil.getFechaHoraActual());

                tramitacionFacade.create(tramTemp);
                auditaFacade.create(new Audita("TRAMITACION", "Tramitacion creada exitosamente.", JSFutil.getFechaHoraActual(), tramTemp.toAudita(), JSFutil.getUsuarioConectado()));
                //Grabar el archivo a disco
                if (this.adjunto != null) {
                    int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(this.adjunto.getContent()), JSFutil.folderDocumento + tramTemp.getIdTramitacion() + "-" + this.adjunto.getFileName());
                    if (resultado != 0) {
                        JSFutil.addMessage("No se ha podido guardar el adjunto debido a un error interno en el procesamiento del archivo. Se deshace el guardado del archivo.", JSFutil.StatusMessage.ERROR);
                    }
                }
//                }
            }
            this.selectedTramitacion = null;
            JSFutil.addMessage("Tramitacion creada exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            commonController.doExcepcion(ex);
        }
        this.buscarPendiente("%", 3);
        return "/tramitacion/ListarDocumentoPendiente";
        //return null;
    }

    public String listSeguimientoSetup() {
//        this.model = null;
//        this.modelDocumento = null;
        this.criterioBusqueda = "";
        return "/tramitacion/ListarSeguimiento";
    }

    public String listDesbloqueoSetup() {
//        this.model = null;
//        this.modelDocumento = null;
//        this.criterioBusqueda = "";
        return "/tramitacion/ListarDesbloqueoDocumento";
    }

    public void updateCP() {
        try {
            if (this.documento.getComprobantePago().isEmpty()) {
                JSFutil.addMessage("El comprobante de pago está vacío... Por favor, verifíquelo y vuelva a intentar", JSFutil.StatusMessage.ERROR);
                return;
            }
            this.documento.setIdUsuario(JSFutil.getUsuarioConectado());
            documentoFacade.edit(documento);
            this.documento = documentoFacade.find(documento.getIdDocumento());
            JSFutil.addMessage("Documento actualizado exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
            //JSFutil.addMessage("Ocurrió un error de persistencia.", JSFutil.StatusMessage.ERROR);
        }
    }

    public String adjuntaSetup(Integer id) {
        this.documento = documentoFacade.find(id);
        return "/tramitacion/ArchivarDocumento";
    }

    public void tramitacionAnexo(Integer idTramitacion) {
        this.tramitacion = tramitacionFacade.find(idTramitacion);
        JSFutil.addMessage("Tramitación recuperada para Anexo...", JSFutil.StatusMessage.INFORMATION);
    }

    public void handleAnexoDocumento(FileUploadEvent event) {
        try {
            //this.tramitacion.setArchivo(event.getFile().getContents());
            this.tramitacion.setNombreArchivo(event.getFile().getFileName());
            this.tramitacion.setTamanhoArchivo(BigInteger.valueOf(event.getFile().getSize()));
            this.tramitacion.setTipoArchivo(event.getFile().getContentType());

            tramitacionFacade.edit(tramitacion);
            auditaFacade.create(new Audita("TRAMITACION", "Anexo agregado exitosamente para Archivo.", JSFutil.getFechaHoraActual(), "[Id=" + tramitacion.getIdTramitacion() + "] [NombreArchivo=" + tramitacion.getNombreArchivo() + "]", JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("El anexo se ha agregado exitosamente.", JSFutil.StatusMessage.INFORMATION);
            //Grabar el archivo a disco
            int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(event.getFile().getContent()), JSFutil.folderDocumento + tramitacion.getIdTramitacion() + "-" + event.getFile().getFileName());
            if (resultado != 0) {
                JSFutil.addMessage("Pero no se ha podido guardar el adjunto debido a un error interno en el procesamiento", JSFutil.StatusMessage.ERROR);
            }
            this.documento = documentoFacade.find(this.tramitacion.getIdDocumento().getIdDocumento());
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
    }

    public String archivarSinNota() {
        try {
            for (Tramitacion t : this.documento.getTramitacionList()) {
                if (t.getIdEstado().getIdEstado() != 100) { //Existe todavia pendientes
                    t.setFechaSalida(JSFutil.getFechaHoraActual());
                    t.setHoraSalida(JSFutil.getFechaHoraActual());
                    t.setIdEstado(new EstadoTramitacion(100));
                    this.tramitacionFacade.edit(t);
                }
            }
            this.documento.setCerrado(Boolean.TRUE);
            documentoFacade.edit(this.documento);
            this.documentoController.buscarDocumentoParaArchivo();
            auditaFacade.create(new Audita("DOCUMENTO", "Documento archivado exitosamente.", JSFutil.getFechaHoraActual(), this.documento.toAudita(), JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("Documento archivado exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
            //JSFutil.addMessage("Ocurrió un error de persistencia.", JSFutil.StatusMessage.ERROR);
            return "";
        }
        return "/tramitacion/ListarDocumentoAdjunto";
    }

    public String enlazarAnotaSalida(Integer idNota) {
        NotaSalida ns = notaSalidaFacade.find(idNota);
        try {
            DetalleNotaSalida dnt = new DetalleNotaSalida();
            dnt.setIdNota(ns);
            dnt.setIdDocumento(documento);
            dnt.setFechaEnlace(JSFutil.getFechaHoraActual());
            detalleNotaSalidaFacade.create(dnt);
            if (ns.getCerrado()) {
                this.archivarSinNota();
            }
            this.buscarAllPendiente();
            auditaFacade.create(new Audita("DETALLE_NOTA_SALIDA", "Enlace creado exitosamente.", JSFutil.getFechaHoraActual(), dnt.toAudita(), JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("Enlace creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
            return "/tramitacion/ListarDocumentoAdjunto";
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
            return "";
        }
    }

    public String seguirSetup(Integer id) {
        this.documento = documentoFacade.find(id);
        this.listaTramitacionSeguimiento = this.documento.getTramitacionList();
        return "/tramitacion/SeguirDocumento";
    }

    public String updateAll() {
        try {
            String cadena = "";
            for (Tramitacion t : this.listaTramitacionSeguimiento) {
                cadena = cadena + t.toAudita() + "\n";
                tramitacionFacade.edit(t);
            }
            auditaFacade.create(new Audita("TRAMITACION", "Seguimiento actualizado exitosamente.", JSFutil.getFechaHoraActual(), cadena, JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("Tramitacion actualizado exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        return "";
    }

    public void checkPendientes() {
        List<Tramitacion> lista = this.buscarPendiente("%", 1);
        if (!lista.isEmpty()) {
            this.commonController.getListaAlerta().add(new Alerta(Codigo.ALERTA_DOCUMENTO_PENDIENTE, lista.size(), "documento/s pendiente/s..."));
        }
    }

    public void init() {
        try {
            String id = JSFutil.getRequestParameter("alerta");

            if (id != null) {
                this.listPendientesSetup();
            }

            //this.detalleProyecto(this.sesion.getIdSesion());
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
    }
}
