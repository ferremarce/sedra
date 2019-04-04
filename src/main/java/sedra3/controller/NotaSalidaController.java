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
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.UploadedFile;
import sedra3.fachada.AuditaFacade;
import sedra3.fachada.DetalleNotaSalidaFacade;
import sedra3.fachada.DocumentoFacade;
import sedra3.fachada.NotaSalidaFacade;
import sedra3.fachada.TipoNotaFacade;
import sedra3.fachada.TramitacionFacade;
import sedra3.modelo.Audita;
import sedra3.modelo.Clasificador;
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
    DetalleNotaSalidaFacade detalleNotaSalidaFacade;
    @Inject
    DocumentoFacade documentoFacade;
    @Inject
    AuditaFacade auditaFacade;
    @Inject
    CommonController commonController;
    @Inject
    TramitacionFacade tramitacionFacade;
    @Inject
    ClasificadorController clasificadorController;
    @Inject
    TipoNotaFacade tipoNotaFacade;

    private NotaSalida notaSalida;
    private Documento documento;
    private String criterioBusqueda = "";
    private List<NotaSalida> listaNotaSalida;
    private TipoNota tipoNota;
    private UploadedFile adjunto;
    private List<Documento> selectedDocumentos;
    private String nroEntrada;

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

    public UploadedFile getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(UploadedFile adjunto) {
        this.adjunto = adjunto;
    }

    public String getNroEntrada() {
        return nroEntrada;
    }

    public void setNroEntrada(String nroEntrada) {
        this.nroEntrada = nroEntrada;
    }

    public List<Documento> getSelectedDocumentos() {
        return selectedDocumentos;
    }

    public void setSelectedDocumentos(List<Documento> selectedDocumentos) {
        this.selectedDocumentos = selectedDocumentos;
    }

    public String doVerForm(Integer idNota) {
        this.notaSalida = notaSalidaFacade.find(idNota);
        return "/notasalida/VerNotaSalida?faces-redirect=true";
    }

    public String listNotaSalidaSetup() {
        if (this.tipoNota == null) {
            //por defecto es el tipo Nota Salida
            this.tipoNota = tipoNotaFacade.find(1);
        }

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
        //Nota Salida por defecto
        this.notaSalida.setIdTipoNota(tipoNotaFacade.find(1));
        this.clasificadorController.cargarTree(Boolean.FALSE);
        return "/notasalida/CrearNotaSalida";
    }

    public String editSetup(Integer idNotaSalida) {
        this.notaSalida = notaSalidaFacade.find(idNotaSalida);
        this.adjunto = null;
        this.selectedDocumentos = new ArrayList<>();
        for (DetalleNotaSalida dns : this.notaSalida.getDetalleNotaSalidaList()) {
            this.selectedDocumentos.add(dns.getIdDocumento());
        }
        this.clasificadorController.cargarTree(Boolean.FALSE);
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

    public void onNodeSelect(NodeSelectEvent event) {
        Clasificador c = (Clasificador) event.getTreeNode().getData();
        System.out.println("Seleccionado: " + c.toString());
        this.notaSalida.setIdClasificador(c);
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.adjunto = event.getFile();
    }

    public void obtenerDocumentoByNroEntrada() {
        if (!this.nroEntrada.isEmpty()) {
            List<Documento> lista = documentoFacade.getDocumentoByNroEntrada(this.nroEntrada);
            if (!lista.isEmpty()) {
                for (Documento d : lista) {
                    if (d.getCerrado()) {
                        JSFutil.addMessage("El documento con entrada: " + this.nroEntrada + " del año " + d.getAnho() + " ya se encuentra llaveado", JSFutil.StatusMessage.WARNING);
                    } else {
                        this.selectedDocumentos.add(d);
                        this.nroEntrada = "";
                        break;
                    }
                }
            } else {
                JSFutil.addMessage("No existe nigun documento con entrada: " + this.nroEntrada, JSFutil.StatusMessage.WARNING);
            }
        } else {
            JSFutil.addMessage("No es posible localizar el documento con entrada: " + this.nroEntrada, JSFutil.StatusMessage.WARNING);
        }
    }

    public String doGuardar() {
        try {
            //Nuevo
            if (notaSalida.getIdNota() == null) {
                if (this.adjunto != null) {
                    notaSalida.setTipoArchivo(this.adjunto.getContentType());
                    notaSalida.setTamanhoArchivo(BigInteger.valueOf(this.adjunto.getSize()));
                    notaSalida.setNombreArchivo(this.adjunto.getFileName());
                }
                notaSalida.setCerrado(Boolean.FALSE);
                notaSalidaFacade.create(notaSalida);

                String doc = "[Entradas=";
                if (!this.selectedDocumentos.isEmpty()) {
                    DetalleNotaSalida dtn;
                    for (Documento d : this.selectedDocumentos) {
                        dtn = new DetalleNotaSalida();
                        dtn.setIdDocumento(d);
                        dtn.setIdNota(notaSalida);
                        dtn.setFechaEnlace(JSFutil.getFechaHoraActual());
                        detalleNotaSalidaFacade.create(dtn);
                        doc += d.getNroEntrada() + " ";
                    }
                }
                this.criterioBusqueda = notaSalida.getNumeroSalida() == null ? notaSalida.getNumeroStr() : notaSalida.getNumeroSalida();
                auditaFacade.create(new Audita("NOTA_SALIDA", "NotaSalida creado exitosamente.", JSFutil.getFechaHoraActual(), notaSalida.toAudita() + doc + "]", JSFutil.getUsuarioConectado()));
                JSFutil.addMessage("NotaSalida creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
                //Grabar el archivo a disco
                if (this.adjunto != null) {
                    int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(this.adjunto.getContents()), JSFutil.folderDocumento + notaSalida.getIdNota() + "-" + this.adjunto.getFileName());
                    if (resultado != 0) {
                        JSFutil.addMessage("Pero no se ha podido guardar el adjunto debido a un error interno en el procesamiento", JSFutil.StatusMessage.ERROR);
                    }
                }
            } else {
                if (this.adjunto != null) {
                    notaSalida.setTipoArchivo(this.adjunto.getContentType());
                    notaSalida.setTamanhoArchivo(BigInteger.valueOf(this.adjunto.getSize()));
                    notaSalida.setNombreArchivo(this.adjunto.getFileName());
                }
                notaSalidaFacade.edit(notaSalida);
                for (DetalleNotaSalida dns : notaSalida.getDetalleNotaSalidaList()) {
                    detalleNotaSalidaFacade.remove(dns);
                }
                String doc = "[Entradas=";
                if (!this.selectedDocumentos.isEmpty()) {
                    DetalleNotaSalida dtn;
                    for (Documento d : this.selectedDocumentos) {
                        dtn = new DetalleNotaSalida();
                        dtn.setIdDocumento(d);
                        dtn.setIdNota(notaSalida);
                        dtn.setFechaEnlace(JSFutil.getFechaHoraActual());
                        detalleNotaSalidaFacade.create(dtn);
                        doc += d.getNroEntrada() + " ";
                    }
                }
                this.criterioBusqueda = notaSalida.getNumeroSalida() == null ? notaSalida.getNumeroStr() : notaSalida.getNumeroSalida();
                auditaFacade.create(new Audita("NOTA_SALIDA", "NotaSalida actualizado exitosamente.", JSFutil.getFechaHoraActual(), notaSalida.toAudita() + doc + "]", JSFutil.getUsuarioConectado()));
                JSFutil.addMessage("NotaSalida actualizado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
                //Grabar el archivo a disco
                if (this.adjunto != null) {
                    int resultado = JSFutil.fileToDisk(new ByteArrayInputStream(this.adjunto.getContents()), JSFutil.folderDocumento + notaSalida.getIdNota() + "-" + this.adjunto.getFileName());
                    if (resultado != 0) {
                        JSFutil.addMessage("Pero no se ha podido guardar el adjunto debido a un error interno en el procesamiento", JSFutil.StatusMessage.ERROR);
                    }
                }
            }

        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
        this.tipoNota = notaSalida.getIdTipoNota();
        this.buscarAllNotaSalida();
        return "/notasalida/ListarNotaSalida";
        //return null;
    }

    public void doSacarDocumento(Documento d) {
        this.selectedDocumentos.remove(d);
        if (this.selectedDocumentos.isEmpty()) {
            this.selectedDocumentos = new ArrayList<>();
        }
    }

    public String doRefrescar() {
        this.listaNotaSalida = notaSalidaFacade.getAllNotaSalida("%");
        if (this.listaNotaSalida.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaNotaSalida.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }
}
