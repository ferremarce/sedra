/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import sedra.fachada.PermisoFacade;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "PermisoController")
@SessionScoped
public class PermisoController implements Serializable {

    @Inject
    PermisoFacade permisoFacade;

    /**
     * Creates a new instance of PermisoController
     */
    public PermisoController() {
    }

    public PermisoFacade getPermisoFacade() {
        return permisoFacade;
    }

    public void setPermisoFacade(PermisoFacade permisoFacade) {
        this.permisoFacade = permisoFacade;
    }

    public SelectItem[] getPermisoSet() {
        return JSFutil.getSelectItems(permisoFacade.getAllPermiso(), Boolean.FALSE);
    }
}
