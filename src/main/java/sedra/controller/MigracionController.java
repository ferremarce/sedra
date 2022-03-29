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
import sedra.fachada.TramitacionFacade;
import sedra.modelo.Documento;
import sedra.modelo.Tramitacion;
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
    @Inject
    TramitacionFacade tramitacionFacade;

    /**
     * Creates a new instance of MigracionController
     */
    public MigracionController() {
    }

    public void doConvertirNroEntrada() {
        List<Documento> lista = documentoFacade.findAll();
        String nroEntrada = "";
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

    public void doProcesarTramitePadre() {
        List<Documento> lista = documentoFacade.findAll();
        for (Documento doc : lista) {
            Tramitacion tramitaAnterior = null;
            for (Tramitacion tramita : doc.getTramitacionList()) {
                if (tramitaAnterior == null) {
                    tramitaAnterior = tramita;
                    continue;
                }
                if (tramita.getIdTramitacionPadre() == null) {
                    tramita.setIdTramitacionPadre(tramitaAnterior);
                    this.tramitacionFacade.edit(tramita);
                }
                tramitaAnterior = tramita;
            }
        }
        JSFutil.addMessage("Registros actualizados", JSFutil.StatusMessage.INFORMATION);
    }

}
