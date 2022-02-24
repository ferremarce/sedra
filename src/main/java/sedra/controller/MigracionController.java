/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package sedra.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import sedra.fachada.DocumentoFacade;
import sedra.modelo.Documento;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "MigracionController")
@SessionScoped
public class MigracionController implements Serializable {

    @Inject
    DocumentoFacade documentoFacade;

    /**
     * Creates a new instance of MigracionController
     */
    public MigracionController() {
    }

    public void doConvertirNroEntrada() {
        List<Documento> lista = documentoFacade.findAll();
        String nroEntrada="";
        String[] nroEntradaArray;
        Integer nroExpe = 0;
        int cantidadError = 0;
        int cantidadUpdate = 0;
        for (Documento doc : lista) {
            nroEntrada = doc.getNroEntrada();
            try {
                nroEntradaArray = nroEntrada.split("-");
                nroExpe = Integer.parseInt(nroEntradaArray[0]);
            } catch (Exception ex) {
                cantidadError++;
                nroExpe = 0;
            }
            if (doc.getNumeroExpediente() == null) {
                doc.setNumeroExpediente(nroExpe);
                documentoFacade.edit(doc);
                cantidadUpdate++;
            }
        }
        JSFutil.addMessage("Registros actualizados: " + cantidadUpdate + "\n Errores: " + cantidadError, JSFutil.StatusMessage.INFORMATION);
    }

}
