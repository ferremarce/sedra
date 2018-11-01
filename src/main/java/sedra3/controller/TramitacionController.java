/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import sedra3.fachada.AuditaFacade;
import sedra3.fachada.ClasificadorFacade;
import sedra3.fachada.TramitacionFacade;
import sedra3.modelo.Audita;
import sedra3.modelo.EstadoTramitacion;
import sedra3.modelo.Tramitacion;
import sedra3.util.JSFutil;

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

    private Tramitacion tramitacion;
    private Tramitacion tramitacionRechazo;
    private List<Tramitacion> listaTramitacionPendiente;
    private List<Tramitacion> listaTramitacionConfirmado;
    private String criterioBusqueda = "%";
    private Tramitacion[] selectedTramitacion;

    /**
     * Creates a new instance of TramitacionController
     */
    public TramitacionController() {
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

    public String crearDocumentoFromClasificadorSetup() {
        this.documentoController.setClasificadorSeleccionado(clasificadorFacade.getFirstClasificador());
        this.clasificadorController.setSelectedNode(null);
        this.clasificadorController.cargarTree();
        return "/tramitacion/CrearDocumentoFromClasificador";
    }

    public String listPendientesSetup() {
        //this.criterioBusqueda = "";
        if (this.listaTramitacionPendiente == null) {
            this.buscarPendiente(1);
        }
        if (this.listaTramitacionConfirmado == null) {
            this.buscarPendiente(3);
        }
        return "/tramitacion/ListarDocumentoPendiente";
    }

    public void buscarPendiente(Integer estado) {
        if (estado.compareTo(1) == 0) {
            this.listaTramitacionPendiente = tramitacionFacade.getAllTramitacionPendientes(JSFutil.getUsuarioConectado().getIdRol().getIdRol(), this.criterioBusqueda, estado);
            if (this.listaTramitacionPendiente.isEmpty()) {
                JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
            } else {
                JSFutil.addMessage(this.listaTramitacionPendiente.size() + " pendientes no confirmados", JSFutil.StatusMessage.INFORMATION);
            }
        } else {
            this.listaTramitacionConfirmado = tramitacionFacade.getAllTramitacionPendientes(JSFutil.getUsuarioConectado().getIdRol().getIdRol(), this.criterioBusqueda, estado);
            if (this.listaTramitacionPendiente.isEmpty()) {
                JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
            } else {
                JSFutil.addMessage(this.listaTramitacionConfirmado.size() + " pendientes para tramitación", JSFutil.StatusMessage.INFORMATION);
            }
        }
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
            this.buscarPendiente(1);
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
            this.tramitacionRechazo.setObservacion(this.tramitacion.getObservacion());

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
            auditaFacade.create(new Audita("TRAMITACION", "Tramitacion creada exitosamente.", JSFutil.getFechaHoraActual(), this.tramitacionRechazo.toAudita(), JSFutil.getUsuarioConectado()));
            this.selectedTramitacion = null;

            //Sacar del pendiente
            this.tramitacion.setIdEstado(new EstadoTramitacion(2)); //Rechazado
            this.tramitacion.setIdUsuario(JSFutil.getUsuarioConectado());
            this.tramitacion.setFechaSalida(JSFutil.getFechaHoraActual());
            this.tramitacion.setHoraSalida(JSFutil.getFechaHoraActual());
            tramitacionFacade.edit(this.tramitacion);

            JSFutil.addMessage("Tramitacion creada exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            commonController.doExcepcion(ex);
        }
        this.buscarPendiente(1);
        return "/tramitacion/ListarDocumentoPendiente";
        //return null;
    }
}
