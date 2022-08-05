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
import sedra.fachada.DocumentoAdjuntoFacade;
import sedra.fachada.DocumentoFacade;
import sedra.fachada.TramitacionAdjuntoFacade;
import sedra.fachada.TramitacionFacade;
import sedra.modelo.Documento;
import sedra.modelo.DocumentoAdjunto;
import sedra.modelo.Tramitacion;
import sedra.modelo.TramitacionAdjunto;
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
    @Inject
    DocumentoAdjuntoFacade documentoAdjuntoFacade;
    @Inject
    TramitacionAdjuntoFacade tramitacionAdjuntoFacade;

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
            nroEntrada = doc.getNroEntrada().trim();
            try {
                nroEntradaArray = nroEntrada.split("-");
                nroExpe = Integer.parseInt(nroEntradaArray[0]);
            } catch (Exception ex) {
                cantidadError++;
                nroExpe = 0;
            }
            try {
                if (nroExpe == 0) {
                    nroExpe = Integer.parseInt(nroEntrada);
                }
            } catch (Exception ex) {
                cantidadError++;
                nroExpe = 0;
            }
            if (doc.getNumeroExpediente() == null) {
                doc.setNumeroExpediente(nroExpe);
                documentoFacade.edit(doc);
                cantidadUpdate++;
            }
            System.out.println("--Convirtiendo nroexpediente: " + doc.getIdDocumento());
        }
        JSFutil.addMessage("Registros actualizados: " + cantidadUpdate + "\n Errores: " + cantidadError, JSFutil.StatusMessage.INFORMATION);
    }
    
    public void doProcesarTramitePadre() {
        List<Documento> lista = documentoFacade.findAllOrdered();
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
            System.out.println("--Documento y tramite procesado: " + doc.getIdDocumento());
        }
        JSFutil.addMessage("Registros actualizados", JSFutil.StatusMessage.INFORMATION);
    }
    
    public void doProcesarDocumentoAdjunto() {
        List<Documento> lista = documentoFacade.findAllOrdered();
        lista.stream()
                .filter(doc -> doc.getNombreArchivo() != null)
                .forEach(doc -> this.saveDocumentoAdjunto(doc));
    }
    
    private void saveDocumentoAdjunto(Documento doc) {
        DocumentoAdjunto da = new DocumentoAdjunto();
        da.setTamanhoArchivo(doc.getTamanhoArchivo());
        da.setNombreArchivo(doc.getNombreArchivo());
        da.setIdDocumentoAnterior(doc.getIdDocumento());
        da.setTipoArchivoMime(doc.getTipoArchivo());
        da.setIdDocumento(doc);
        da.setDescripcion("Migrado");
        this.documentoAdjuntoFacade.create(da);
        doc.setNombreArchivo(null);
        this.documentoFacade.edit(doc);
        System.out.println("Documento adjunto creado: " + da.toPathFileSystem());
    }
    
    public void doProcesarTramiteAdjunto() {
        List<Tramitacion> lista = this.tramitacionFacade.findAllOrdered();
//        lista.stream()
//                .filter(tram -> tram.getNombreArchivo() != null)
//                .forEach(tram -> {
//                    this.saveTramiteAdjunto(tram);
//                    System.out.println("--Tramitacion para adjunto procesada: " + tram.getIdTramitacion());
//                }
//                );
        for (Tramitacion t : lista) {
            if (t.getNombreArchivo() != null) {
                this.saveTramiteAdjunto(t);
            }
        }
    }
    
    private void saveTramiteAdjunto(Tramitacion t) {
        TramitacionAdjunto ta = new TramitacionAdjunto();
        ta.setTamanhoArchivo(t.getTamanhoArchivo());
        ta.setNombreArchivo(t.getNombreArchivo());
        ta.setIdTramitacionAnterior(t.getIdTramitacion());
        ta.setTipoArchivoMime(t.getTipoArchivo());
        ta.setIdTramitacion(t);
        ta.setDescripcion("Migrado");
        this.tramitacionAdjuntoFacade.create(ta);
        t.setNombreArchivo(null);
        this.tramitacionFacade.edit(t);
        System.out.println("--Tramitacion adjunto creado: " + ta.toPathFileSystem());
    }
    
}
