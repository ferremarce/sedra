/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import sedra.fachada.PrioridadFacade;
import sedra.modelo.Prioridad;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "PrioridadController")
@SessionScoped
public class PrioridadController implements Serializable {

    private static final Logger LOG = Logger.getLogger(PrioridadController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    PrioridadFacade prioridadFacade;
    CommonController commonController;

    private Prioridad prioridad;
    private List<Prioridad> listaPrioridad;
    private String criterio;

    /**
     * Creates a new instance of PrioridadController
     */
    public PrioridadController() {
    }

    public PrioridadFacade getPrioridadFacade() {
        return prioridadFacade;
    }

    public void setPrioridadFacade(PrioridadFacade prioridadFacade) {
        this.prioridadFacade = prioridadFacade;
    }

    public SelectItem[] getPrioridadSet() {
        return JSFutil.getSelectItems(prioridadFacade.findAll(), Boolean.FALSE);
    }
}
