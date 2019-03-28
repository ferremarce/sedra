/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import org.primefaces.model.UploadedFile;
import sedra3.fachada.AuditaFacade;
import sedra3.fachada.DocumentoFacade;
import sedra3.fachada.NotaSalidaFacade;
import sedra3.fachada.TramitacionFacade;
import sedra3.modelo.Audita;
import sedra3.modelo.DetalleNotaSalida;
import sedra3.modelo.Documento;
import sedra3.modelo.EstadoTramitacion;
import sedra3.modelo.NotaSalida;
import sedra3.modelo.TipoNota;
import sedra3.modelo.Tramitacion;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "NotaSalidaController")
@SessionScoped
public class NotaSalidaController implements Serializable {

    @Inject
    NotaSalidaFacade notaSalidaFacade;
    @Inject
    DocumentoFacade documentoFacade;
    @Inject
    AuditaFacade auditaFacade;
    @Inject
    CommonController commonController;
    @Inject
    TramitacionFacade tramitacionFacade;

    private NotaSalida notaSalida;
    private Documento documento;
    private String criterioBusqueda = "";
    private List<NotaSalida> listaNotaSalida;
    private TipoNota tipoNota;
    private UploadedFile adjunto;
    private List<Documento> selectedDocumentos;

    /**
     * Creates a new instance of NotaSalidaController
     */
    public NotaSalidaController() {
    }

    public NotaSalida getNotaSalida() {
        return notaSalida;
    }

    public void setNotaSalida(NotaSalida notaSalida) {
        this.notaSalida = notaSalida;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    public List<NotaSalida> getListaNotaSalida() {
        return listaNotaSalida;
    }

    public void setListaNotaSalida(List<NotaSalida> listaNotaSalida) {
        this.listaNotaSalida = listaNotaSalida;
    }

    public TipoNota getTipoNota() {
        return tipoNota;
    }

    public void setTipoNota(TipoNota tipoNota) {
        this.tipoNota = tipoNota;
    }

    public String doVerForm(Integer idNota) {
        this.notaSalida = notaSalidaFacade.find(idNota);
        return "/notasalida/VerNotaSalida?faces-redirect=true";
    }

    public String listNotaSalidaSetup() {
//        if (this.tipoNota != null) {
//            this.buscarNotaSalida();
//        }
//        this.tipoNota = new TipoNota(1);
        return "/notasalida/ListarNotaSalida";
    }

    public void anexarDocAnotaExistente(Integer idDoc) {
        this.documento = documentoFacade.find(idDoc);
        this.listaNotaSalida = new ArrayList<>();
        this.criterioBusqueda = "";
    }

    public void buscarAllNotaSalida() {
        this.listaNotaSalida = notaSalidaFacade.getAllNotaSalida(criterioBusqueda, tipoNota.getIdTipoNota());
        if (this.listaNotaSalida.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaNotaSalida.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
    }

    public String createSetup(Integer idDoc) {
        Calendar c = Calendar.getInstance();
        c.setTime(JSFutil.getFechaHoraActual());
        this.notaSalida = new NotaSalida();
        this.notaSalida.setAnho(c.get(Calendar.YEAR));
        this.adjunto = null;
        this.selectedDocumentos = new ArrayList<>();
        if (idDoc != null) {
            Documento docu = documentoFacade.find(idDoc);
            this.selectedDocumentos.add(docu);
        }
        return "/notasalida/CrearNotaSalida";
    }

    public String editSetup(Integer idNotaSalida) {
        //int id = Integer.parseInt(idNotaSalida);
        this.notaSalida = notaSalidaFacade.find(idNotaSalida);
        this.adjunto = null;
        this.selectedDocumentos = new ArrayList<>();
        return "/notasalida/CrearNotaSalida";
    }

    public String delete(Integer idNotaSalida) {
        try {
            NotaSalida u = notaSalidaFacade.find(idNotaSalida);
            //Abrir de vuelta el documento que fue llaveado
            for (DetalleNotaSalida dns : u.getDetalleNotaSalidaList()) {
                Documento d = dns.getIdDocumento();
                d.setCerrado(Boolean.FALSE);
            }
            String name = u.getNumeroSalida();
            notaSalidaFacade.remove(u);
            JSFutil.addMessage("La NotaSalida/STR #" + name + "# fue eliminada y el documento de entrada fue desllaveado", JSFutil.StatusMessage.INFORMATION);
            auditaFacade.create(new Audita("NOTA_SALIDA", "NotaSalida eliminado exitosamente.", JSFutil.getFechaHoraActual(), u.toAudita(), JSFutil.getUsuarioConectado()));
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        this.buscarAllNotaSalida();
        return "/notasalida/ListarNotaSalida";
    }

    public void llavear(Integer idNota) {
        NotaSalida ns = notaSalidaFacade.find(idNota);
        NotaSalida tmpNota = ns;
        try {
            for (DetalleNotaSalida dns : tmpNota.getDetalleNotaSalidaList()) {
                Documento d = dns.getIdDocumento();
                for (Tramitacion t : d.getTramitacionList()) {
                    if (t.getIdEstado().getIdEstado() != 100) { //Existe todavia pendientes
                        t.setFechaSalida(JSFutil.getFechaHoraActual());
                        t.setHoraSalida(JSFutil.getFechaHoraActual());
                        t.setIdEstado(new EstadoTramitacion(100));
                        this.tramitacionFacade.edit(t);
                    }
                }
                d.setCerrado(Boolean.TRUE);
                documentoFacade.edit(d);
            }
            tmpNota.setCerrado(Boolean.TRUE);
            notaSalidaFacade.edit(tmpNota);
            auditaFacade.create(new Audita("NOTA_SALIDA", "El documento y todas sus tramitaciones fueron archivados exitosamente. ", JSFutil.getFechaHoraActual(), tmpNota.toAudita(), JSFutil.getUsuarioConectado()));
            JSFutil.addMessage("El documento y todas sus tramitaciones fueron archivados exitosamente.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        //this.criterioBusqueda = ns.getNumeroSalida() == null ? ns.getNumeroSTR() : ns.getNumeroSalida();
        this.buscarAllNotaSalida();
        //return "/notaSalida/ListarNotaSalida";
    }

}
