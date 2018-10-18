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
import javax.inject.Inject;
import sedra3.fachada.ClasificadorFacade;
import sedra3.modelo.Clasificador;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "ClasificadorController")
@SessionScoped
public class ClasificadorController implements Serializable {

    private static final Logger LOG = Logger.getLogger(DocumentoController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    ClasificadorFacade clasificadorFacade;
    @Inject
    CommonController commonController;

    private Clasificador clasificador;
    private List<Clasificador> listaClasificador;
    private String criterio;

    /**
     * Creates a new instance of ClasificadorController
     */
    public ClasificadorController() {
    }

    public String obtenerRutaClasificador(Integer idClasificador) {
        String cadena = "";
        Integer padre;
        Clasificador c = clasificadorFacade.find(idClasificador);
        while (c != null) {
            padre = getPadre(c);
            cadena = c.getDenominacionClasificador() + "/" + cadena;
            if (padre.compareTo(0) == 0) {
                return cadena;
            } else {
                c = clasificadorFacade.find(padre);
            }
        }
        return cadena;
    }
    public Integer getPadre(Clasificador c) {
        for (Clasificador n : clasificadorFacade.findAll()) {
            if (n.getIdClasificador().compareTo(c.getIdClasificador()) == 0) {
                return n.getPadre();
            }
        }
        return 0;
    }
}
