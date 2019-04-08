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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import sedra3.fachada.EstadoTramitacionFacade;
import sedra3.modelo.EstadoTramitacion;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "EstadoTramitacionController")
@SessionScoped
public class EstadoTramitacionController implements Serializable {

    private static final Logger LOG = Logger.getLogger(EstadoTramitacionController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    EstadoTramitacionFacade estadoTramitacionFacade;
    CommonController commonController;

    private EstadoTramitacion estadoTramitacion;
    private List<EstadoTramitacion> listaEstadoTramitacion;
    private String criterio;

    /**
     * Creates a new instance of EstadoTramitacionController
     */
    public EstadoTramitacionController() {
    }

    public EstadoTramitacionFacade getEstadoTramitacionFacade() {
        return estadoTramitacionFacade;
    }

    public void setEstadoTramitacionFacade(EstadoTramitacionFacade estadoTramitacionFacade) {
        this.estadoTramitacionFacade = estadoTramitacionFacade;
    }

    public SelectItem[] getEstadoTramitacionSet() {
        return JSFutil.getSelectItems(estadoTramitacionFacade.getAllEstadoTramitacionNoTemporal(), Boolean.FALSE);
    }
}
