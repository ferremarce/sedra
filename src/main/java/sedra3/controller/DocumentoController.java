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
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sedra3.fachada.DocumentoFacade;
import sedra3.modelo.Documento;
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

    private Documento documento;
    private List<Documento> listaDocumento;
    private List<UploadedFile> adjuntoDocumento;
    private String criterio;

    /**
     * Creates a new instance of DocumentoController
     */
    public DocumentoController() {
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
        this.adjuntoDocumento = new ArrayList<>();
        return "/documento/CrearDocumento";
    }

    public String editSetup(Integer idDocumento) {
        this.documento = documentoFacade.find(idDocumento);
        return "/documento/CrearDocumento";
    }

    public String delete(Integer idDocumento) {
        try {
            Documento u = documentoFacade.find(idDocumento);
            String name = u.getNroEntrada();
            documentoFacade.remove(u);
            this.doRefrescar();
            JSFutil.addMessage("El Documento #" + name + "# fue eliminado.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "/documento/ListarDocumento";
    }

    public String create() {
        try {
            if (this.documento.getIdDocumento() == null) {
                documentoFacade.create(documento);
            } else {
                documentoFacade.edit(documento);
            }
            this.doRefrescar();
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

    public void handleFileUpload(FileUploadEvent event) {
        //LOG.log(Level.INFO, "Agregado el archivo {0}", event.getFile().getFileName());
        this.adjuntoDocumento.add(event.getFile());
    }

    public String doVerForm(Integer idExpediente) {
        this.documento = documentoFacade.find(idExpediente);
        return "/documento/VerDocumento";
    }

}
