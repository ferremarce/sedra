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
import javax.inject.Inject;
import sedra3.fachada.NotaSalidaFacade;
import sedra3.modelo.DetalleNotaSalida;
import sedra3.modelo.NotaSalida;

/**
 *
 * @author jmferreira
 */
@Named(value = "NotaSalidaController")
@SessionScoped
public class NotaSalidaController implements Serializable {

    @Inject
    NotaSalidaFacade notaSalidaFacade;

    /**
     * Creates a new instance of NotaSalidaController
     */
    public NotaSalidaController() {
    }

    public String doVerForm(Integer idNota) {
        //this.documento = documentoFacade.find(idExpediente);
        return "/documento/VerDocumento?faces-redirect=true";
    }
}
