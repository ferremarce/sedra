/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.primefaces.model.TreeNode;
import sedra3.fachada.ClasificadorFacade;
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

    /**
     * Creates a new instance of TramitacionController
     */
    public TramitacionController() {
    }

    public String crearDocumentoFromClasificadorSetup() {
        this.documentoController.setClasificadorSeleccionado(clasificadorFacade.getFirstClasificador());
        this.clasificadorController.setSelectedNode(null);
        this.clasificadorController.cargarTree();
        return "/tramitacion/CrearDocumentoFromClasificador";
    }
}
