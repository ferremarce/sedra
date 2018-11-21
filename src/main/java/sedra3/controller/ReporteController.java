/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jmferreira
 */
@Named(value = "ReporteController")
@SessionScoped
public class ReporteController implements Serializable {

    /**
     * Creates a new instance of ReporteController
     */
    public ReporteController() {
    }

    public String imprimirDelantalSetup() {
        //this.model = null;
        return "/reportes/rptDelantal";
    }
    public String listTramitacionOficinaSetup() {
//        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//        Usuario user = (Usuario) session.getAttribute("user");
//        this.tmpIdRol = user.getIdRol();
//        if (user.getIdRol().getIdRol().compareTo(18) == 0) {//Es archivo
//            this.disabled = false;
//        } else {
//            this.disabled = true;
//        }
        return "/reportes/ListadoTramitacionOficina";
    }
}
