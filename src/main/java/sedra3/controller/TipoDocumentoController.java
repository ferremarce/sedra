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
import sedra3.fachada.TipoDocumentoFacade;
import sedra3.modelo.TipoDocumento;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "TipoDocumentoController")
@SessionScoped
public class TipoDocumentoController implements Serializable {

    private static final Logger LOG = Logger.getLogger(TipoDocumentoController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    TipoDocumentoFacade tipoDocumentoFacade;
    CommonController commonController;

    private TipoDocumento tipoDocumento;
    private List<TipoDocumento> listaTipoDocumento;
    private String criterio;

    /**
     * Creates a new instance of TipoDocumentoController
     */
    public TipoDocumentoController() {
    }

    public TipoDocumentoFacade getTipoDocumentoFacade() {
        return tipoDocumentoFacade;
    }

    public void setTipoDocumentoFacade(TipoDocumentoFacade tipoDocumentoFacade) {
        this.tipoDocumentoFacade = tipoDocumentoFacade;
    }

    public SelectItem[] getTipoDocumentoSet() {
        return JSFutil.getSelectItems(tipoDocumentoFacade.findAll(), Boolean.TRUE);
    }
}
