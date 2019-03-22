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
import java.util.List;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import static org.primefaces.component.chart.Chart.PropertyKeys.model;
import sedra3.fachada.DocumentoFacade;
import sedra3.fachada.NotaSalidaFacade;
import sedra3.modelo.Documento;
import sedra3.modelo.NotaSalida;
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

    private NotaSalida notaSalida;
    private Documento documento;
    private String criterioBusqueda = "";
    private List<NotaSalida> listaNotaSalida;

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
        this.listaNotaSalida=new ArrayList<>();
        this.criterioBusqueda = "";
    }

    public void buscarAllNotaSalida() {
        this.listaNotaSalida = notaSalidaFacade.getAllNotaSalida(criterioBusqueda);
        if (this.listaNotaSalida.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaNotaSalida.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
    }
}
