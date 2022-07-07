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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.LangUtils;
import sedra.aditional.Alerta;
import sedra.aditional.TreeTramitacion;
import sedra.fachada.AuditaFacade;
import sedra.fachada.ClasificadorFacade;
import sedra.fachada.DetalleNotaSalidaFacade;
import sedra.fachada.DocumentoFacade;
import sedra.fachada.EstadoTramitacionFacade;
import sedra.fachada.NotaSalidaFacade;
import sedra.fachada.PrioridadFacade;
import sedra.fachada.TramitacionFacade;
import sedra.modelo.Audita;
import sedra.modelo.DetalleNotaSalida;
import sedra.modelo.Documento;
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
    @Inject
    EstadoTramitacionFacade estadoTramitacionFacade;
    @Inject
    PrioridadFacade prioridadFacade;

    private Tramitacion tramitacion;
    private Tramitacion tramitacionRechazo;
    private List<Tramitacion> listaTramitacionEstado;
    private String criterioBusqueda = "%";
    private Tramitacion[] arraySelectedTramitacion;
    private List<Tramitacion> listSelectedTramitacion;
    private List<Tramitacion> listaTramitacionSeguimiento;
    private Rol[] arrayRol;
    private TreeNode rootTramitacion;
    private TreeNode selectedNode;

    private UploadedFile adjunto;
    private Documento documento;
    private String observaciones;
    private Integer tipoBandeja;
    private Integer cantPendiente;

    /**
     * Creates a new instance of TramitacionController
     */
    public TramitacionController() {
    }

    public Integer getCantPendiente() {
        return cantPendiente;
    }

    public void setCantPendiente(Integer cantPendiente) {
        this.cantPendiente = cantPendiente;
    }

    public TreeNode getRootTramitacion() {
        return rootTramitacion;
    }

    public void setRootTramitacion(TreeNode rootTramitacion) {
        this.rootTramitacion = rootTramitacion;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Integer getTipoBandeja() {
        return tipoBandeja;
    }

    public void setTipoBandeja(Integer tipoBandeja) {
        this.tipoBandeja = tipoBandeja;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Rol[] getArrayRol() {
        return arrayRol;
    }

    public void setArrayRol(Rol[] arrayRol) {
        this.arrayRol = arrayRol;
    }

    public UploadedFile getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(UploadedFile adjunto) {
        this.adjunto = adjunto;
    }

    public List<Tramitacion> getListaTramitacionEstado() {
        return listaTramitacionEstado;
    }

    public void setListaTramitacionEstado(List<Tramitacion> listaTramitacionEstado) {
        this.listaTramitacionEstado = listaTramitacionEstado;
    }

    public List<Tramitacion> getListSelectedTramitacion() {
        return listSelectedTramitacion;
    }

    public void setListSelectedTramitacion(List<Tramitacion> listSelectedTramitacion) {
        this.listSelectedTramitacion = listSelectedTramitacion;
    }

    public Tramitacion[] getArraySelectedTramitacion() {
        return arraySelectedTramitacion;
    }

    public void setArraySelectedTramitacion(Tramitacion[] arraySelectedTramitacion) {
        this.arraySelectedTramitacion = arraySelectedTramitacion;
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
        //Obtenemos solo los pendientes de entrada (confirmación)
        this.buscarAllPendienteAjax();

        if (this.listaTramitacionEstado.isEmpty()) {
            JSFutil.addMessage("No hay pendientes de confirmación", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaTramitacionEstado.size() + " pendientes no confirmados", JSFutil.StatusMessage.INFORMATION);
        }
        return "/tramitacion/ListarDocumentoPendiente";
    }

    private List<Tramitacion> buscarPendienteEstado(Integer estado) {
        return tramitacionFacade.getAllTramitacionPendientes("%", estado);
    }

    public void buscarAllPendienteAjax() {
        this.listaTramitacionEstado = this.buscarPendienteEstado(Codigo.ESTADO_TRAMITE_PENDIENTE);
        this.arraySelectedTramitacion = null;
        this.tipoBandeja = Codigo.BANDEJA_PEDIENTES_CONFIRMACION;
        this.cantPendiente = this.listaTramitacionEstado.size();
    }

    public void buscarAllConfirmadoAjax() {
        this.listaTramitacionEstado = this.buscarPendienteEstado(Codigo.ESTADO_TRAMITE_INGRESADO);
        this.listaTramitacionEstado.addAll(this.buscarPendienteEstado(Codigo.ESTADO_TRAMITE_RECIBIDO));
        Collections.sort(this.listaTramitacionEstado);
        this.arraySelectedTramitacion = null;
        this.tipoBandeja = Codigo.BANDEJA_PENDIENTES_TRAMITACION;
    }

    public void buscarAllDerivadoAjax() {
        this.listaTramitacionEstado = this.buscarPendienteEstado(Codigo.ESTADO_TRAMITE_DERIVADO);
        this.listaTramitacionEstado.addAll(this.buscarPendienteEstado(Codigo.ESTADO_TRAMITE_ARCHIVADO));
        this.listaTramitacionEstado.addAll(this.buscarPendienteEstado(Codigo.ESTADO_TRAMITE_RECHAZADO));
        Collections.sort(this.listaTramitacionEstado);
        this.arraySelectedTramitacion = null;
        this.tipoBandeja = Codigo.BANDEJA_TRAMITADOS;
    }

    public String rechazaMultipleSetup() {
        this.observaciones = null;
        this.listSelectedTramitacion = (List<Tramitacion>) JSFutil.arrayToList(this.arraySelectedTramitacion);
        return "/tramitacion/RechazarDocumento";
    }

    private void confirmaTramite(Integer idTramitacion) {
        try {
            this.tramitacion = tramitacionFacade.find(idTramitacion);
            this.tramitacion.setIdEstado(this.estadoTramitacionFacade.find(Codigo.ESTADO_TRAMITE_RECIBIDO));
            this.tramitacion.setFechaConfirmacion(JSFutil.getFechaHoraActual());
            this.tramitacion.setHoraConfirmacion(JSFutil.getFechaHoraActual());
            this.tramitacion.setIdUsuarioConfirmacion(JSFutil.getUsuarioConectado());
            this.tramitacion.setLeido(Boolean.TRUE);
            tramitacionFacade.edit(tramitacion);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
    }

    public String confirmaMultipleSetup() {
        if (this.arraySelectedTramitacion.length == 0) {
            JSFutil.addMessage("Debe seleccionar al menos un documento para tramitar", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listSelectedTramitacion = (List<Tramitacion>) JSFutil.arrayToList(this.arraySelectedTramitacion);
        for (Tramitacion t : this.listSelectedTramitacion) {
            this.confirmaTramite(t.getIdTramitacion());
        }
        JSFutil.addMessage("Tramitacion procesada exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        this.buscarAllPendienteAjax();
        return "";
    }

    public String rechazar() {
        try {
            for (Tramitacion tramita : this.listSelectedTramitacion) {
                this.tramitacionRechazo = new Tramitacion();
                this.tramitacionRechazo.setFechaDerivacion(JSFutil.getFechaHoraActual());
                this.tramitacionRechazo.setIdDocumento(tramita.getIdDocumento());
                this.tramitacionRechazo.setIdRol(tramita.getIdTramitacionPadre().getIdRol());
                this.tramitacionRechazo.setIdCreador(JSFutil.getUsuarioConectado());
                this.tramitacionRechazo.setNotaBreve("Rechazado según observaciones ");
                this.tramitacionRechazo.setObservacion(observaciones);
                this.tramitacionRechazo.setFechaRegistro(JSFutil.getFechaHoraActual());
                this.tramitacionRechazo.setHoraRegistro(JSFutil.getFechaHoraActual());
                this.tramitacionRechazo.setIdEstado(this.estadoTramitacionFacade.find(Codigo.ESTADO_TRAMITE_PENDIENTE));
                this.tramitacionRechazo.setIdTramitacionPadre(tramita);
                this.tramitacionRechazo.setIdPrioridad(this.prioridadFacade.find(Codigo.PRIORIDAD_NORMAL));
                this.tramitacionRechazo.setLeido(Boolean.TRUE);
                tramitacionFacade.create(this.tramitacionRechazo);
                auditaFacade.create(new Audita("TRAMITACION", "Tramitacion Rechazo creada exitosamente.", JSFutil.getFechaHoraActual(), this.tramitacionRechazo.toAudita(), JSFutil.getUsuarioConectado()));
                this.arraySelectedTramitacion = null;

                //Sacar del pendiente
                tramita.setIdEstado(this.estadoTramitacionFacade.find(Codigo.ESTADO_TRAMITE_RECHAZADO)); //Rechazado
                tramita.setIdCreador(JSFutil.getUsuarioConectado());
                tramita.setFechaSalida(JSFutil.getFechaHoraActual());
                tramita.setHoraSalida(JSFutil.getFechaHoraActual());
                tramita.setLeido(Boolean.TRUE);
                tramitacionFacade.edit(tramita);
            }
            JSFutil.addMessage("Tramitacion rechazada exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            commonController.doExcepcion(ex);
        }
        this.buscarAllPendienteAjax();
        return "/tramitacion/ListarDocumentoPendiente";
        //return null;
    }

    public String derivaMultipleSetup() {
        if (this.arraySelectedTramitacion.length == 0) {
            JSFutil.addMessage("Debe seleccionar al menos un documento para tramitar", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listSelectedTramitacion = (List<Tramitacion>) JSFutil.arrayToList(this.arraySelectedTramitacion);
        this.tramitacion = new Tramitacion();
        this.tramitacion.setIdPrioridad(this.prioridadFacade.find(Codigo.PRIORIDAD_NORMAL));
        this.arrayRol = null;
        this.adjunto = null;
        this.tramitacion.setFechaDerivacion(JSFutil.getFechaHoraActual());
        return "/tramitacion/DerivarDocumento";
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.adjunto = event.getFile();
    }

    public String create() {
        try {
            if (this.arrayRol.length == 0) {
                JSFutil.addMessage("Debe seleccionar al menos una dependencia para realizar la derivación", JSFutil.StatusMessage.WARNING);
                return "";
            }
            Tramitacion tramTemp;
            for (Tramitacion tram : this.arraySelectedTramitacion) {

                tram.setIdEstado(this.estadoTramitacionFacade.find(Codigo.ESTADO_TRAMITE_DERIVADO));
                tram.setIdCreador(JSFutil.getUsuarioConectado());
                tram.setFechaSalida(JSFutil.getFechaHoraActual());
                tram.setHoraSalida(JSFutil.getFechaHoraActual());
                tramitacionFacade.edit(tram);

                for (Rol rolDerivado : arrayRol) {
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

                    tramTemp.setIdEstado(this.estadoTramitacionFacade.find(Codigo.ESTADO_TRAMITE_PENDIENTE));
                    tramTemp.setIdUsuarioRemitente(JSFutil.getUsuarioConectado());
                    tramTemp.setFechaRegistro(JSFutil.getFechaHoraActual());
                    tramTemp.setHoraRegistro(JSFutil.getFechaHoraActual());
                    tramTemp.setIdPrioridad(this.tramitacion.getIdPrioridad());
                    tramTemp.setIdTramitacionPadre(tram);
                    tramTemp.setLeido(Boolean.FALSE);
                    tramitacionFacade.create(tramTemp);
                    auditaFacade.create(new Audita("TRAMITACION", "Tramitacion creada exitosamente.", JSFutil.getFechaHoraActual(), tramTemp.toAudita(), JSFutil.getUsuarioConectado()));
                    //Grabar el archivo a disco
                    if (this.adjunto != null) {
                        int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(this.adjunto.getContent()), JSFutil.folderDocumento + tramTemp.getIdTramitacion() + "-" + this.adjunto.getFileName());
                        if (resultado != 0) {
                            JSFutil.addMessage("No se ha podido guardar el adjunto debido a un error interno en el procesamiento del archivo. Se deshace el guardado del archivo.", JSFutil.StatusMessage.ERROR);
                        }
                    }
                }
            }
            this.arraySelectedTramitacion = null;
            JSFutil.addMessage("Tramitacion creada exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            commonController.doExcepcion(ex);
        }
        this.buscarAllPendienteAjax();
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
                    t.setIdEstado(this.estadoTramitacionFacade.find(Codigo.ESTADO_TRAMITE_ARCHIVADO));
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
            this.buscarAllPendienteAjax();
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
        TreeTramitacion tt = new TreeTramitacion();
        this.rootTramitacion = tt.init(tramitacionFacade.findFirstTramitacion(this.documento.getIdDocumento()));
        if (this.rootTramitacion != null) {
            tt.expandedAll(this.rootTramitacion, true);
        }
        return "/tramitacion/SeguirDocumento";
    }

    public String updateAll() {
        try {
            String cadena = "";
            for (Tramitacion t : this.listaTramitacionSeguimiento) {
                if (!t.getFlagBorrado()) {
                    cadena = cadena + "UPDATE: " + t.toAudita() + "\n";
                    if (t.getIdEstado().getIdEstado().compareTo(Codigo.ESTADO_TRAMITE_ARCHIVADO) == 0) {
                        t.setFechaSalida(JSFutil.getFechaHoraActual());
                        t.setHoraSalida(JSFutil.getFechaHoraActual());
                    }
                    tramitacionFacade.edit(t);
                } else {
                    cadena = cadena + " DELETE: " + t.toAudita() + "\n";
                    Tramitacion tramitaBorra = this.tramitacionFacade.find(t.getIdTramitacion());
                    this.tramitacionFacade.remove(tramitaBorra);
                }
            }
            auditaFacade.create(new Audita("TRAMITACION", "Seguimiento actualizado exitosamente.", JSFutil.getFechaHoraActual(), cadena, JSFutil.getUsuarioConectado()));
            this.seguirSetup(this.documento.getIdDocumento());
            JSFutil.addMessage("Tramitacion actualizado exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        return "";
    }

    public void checkPendientes() {
        List<Tramitacion> lista = this.tramitacionFacade.getAllTramitacionPendientesNoLeidos("%", Codigo.ESTADO_TRAMITE_PENDIENTE);
        if (!lista.isEmpty()) {
            this.commonController.getListaAlerta().add(new Alerta(Codigo.ALERTA_DOCUMENTO_PENDIENTE, lista.size(), "documento/s no leído/s..."));
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

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        filterText = JSFutil.replaceWildcardRegex(filterText); //Reemplazamos los wildcard de JPA por REGEX
        
        if (LangUtils.isBlank(filterText)) {
            return true;
        }
        //int filterInt = getInteger(filterText);
        String cadenaBusqueda = "";
        if (value instanceof Tramitacion) {
            Tramitacion tramita = (Tramitacion) value;
            cadenaBusqueda = tramita.getIdDocumento().getAsunto() + tramita.getIdDocumento().toShortString();
        } else if (value instanceof Documento) {
            Documento d = (Documento) value;
            cadenaBusqueda = d.getAsunto() + d.toShortString();
        } else if (value instanceof NotaSalida) {
            NotaSalida ns = (NotaSalida) value;
            cadenaBusqueda = ns.toNumeroString() + " " + ns.getAsunto() + " " + ns.getReferencia() + " " + ns.getRubro();
        } else {
            return false;
        }
        cadenaBusqueda = cadenaBusqueda.replaceAll("null", "");
        String nuevoFilter = "*" + filterText + "*";
        System.out.println("Nuevo Filter: "+nuevoFilter);
        try {
            Boolean match = JSFutil.strmatch(cadenaBusqueda.toLowerCase(), nuevoFilter);
            return match;
//            return JSFutil.strmatch(documentoFilter.getAsunto().toLowerCase(), nuevoFilter)
//                    || JSFutil.strmatch(documentoFilter.toShortString().toLowerCase(), nuevoFilter);
        } catch (Exception e) {
            return false;
        }
    }

    public void doVerMensaje(Tramitacion tram) {
        tram.setLeido(Boolean.TRUE);
        this.tramitacion = this.tramitacionFacade.find(tram.getIdTramitacion());
        this.tramitacion.setLeido(Boolean.TRUE);
        this.tramitacionFacade.edit(tramitacion);
        PrimeFaces.current().executeScript("PF('dlgMensaje').show();");
        PrimeFaces.current().ajax().update("formPopup");
        PrimeFaces.current().ajax().update("formMain:dataTablePendiente");
    }
}
