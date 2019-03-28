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
import sedra3.fachada.TipoNotaFacade;
import sedra3.modelo.TipoNota;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "TipoNotaController")
@SessionScoped
public class TipoNotaController implements Serializable {

    private static final Logger LOG = Logger.getLogger(TipoNotaController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    TipoNotaFacade tipoNotaFacade;
    CommonController commonController;

    private TipoNota tipoNota;
    private List<TipoNota> listaTipoNota;
    private String criterio;

    /**
     * Creates a new instance of TipoNotaController
     */
    public TipoNotaController() {
    }

    public TipoNotaFacade getTipoNotaFacade() {
        return tipoNotaFacade;
    }

    public void setTipoNotaFacade(TipoNotaFacade tipoNotaFacade) {
        this.tipoNotaFacade = tipoNotaFacade;
    }

    public SelectItem[] getTipoNotaSet() {
        return JSFutil.getSelectItems(tipoNotaFacade.findAll(), Boolean.FALSE);
    }
}
