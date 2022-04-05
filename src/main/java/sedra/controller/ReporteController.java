/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import sedra.fachada.DocumentoFacade;
import sedra.fachada.TramitacionFacade;
import sedra.modelo.Documento;
import sedra.modelo.EstadoTramitacion;
import sedra.modelo.Rol;
import sedra.modelo.Tramitacion;
import sedra.reportes.FuenteReporte;
import sedra.reportes.JasperManager;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "ReporteController")
@SessionScoped
public class ReporteController implements Serializable {

    @Inject
    DocumentoFacade documentoFacade;
    @Inject
    TramitacionFacade tramitacionFacade;
    @Inject
    DocumentoController documentoController;
    @Inject
    CommonController commonController;
    @Inject
    ClasificadorController clasificadorController;

    private String destinoReporte = "PDF";
    private Date tmpFechaDesde = new Date();
    private Date tmpFechaHasta = new Date();
    private Rol tmpIdRol;
    private EstadoTramitacion tmpEstadoTramitacion;
    private Boolean disabled = Boolean.TRUE;
    List<Tramitacion> listaTramitacion;

    /**
     * Creates a new instance of ReporteController
     */
    public ReporteController() {
    }

    public String getDestinoReporte() {
        return destinoReporte;
    }

    public void setDestinoReporte(String destinoReporte) {
        this.destinoReporte = destinoReporte;
    }

    public Date getTmpFechaDesde() {
        return tmpFechaDesde;
    }

    public void setTmpFechaDesde(Date tmpFechaDesde) {
        this.tmpFechaDesde = tmpFechaDesde;
    }

    public Date getTmpFechaHasta() {
        return tmpFechaHasta;
    }

    public void setTmpFechaHasta(Date tmpFechaHasta) {
        this.tmpFechaHasta = tmpFechaHasta;
    }

    public Rol getTmpIdRol() {
        return tmpIdRol;
    }

    public void setTmpIdRol(Rol tmpIdRol) {
        this.tmpIdRol = tmpIdRol;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public EstadoTramitacion getTmpEstadoTramitacion() {
        return tmpEstadoTramitacion;
    }

    public void setTmpEstadoTramitacion(EstadoTramitacion tmpEstadoTramitacion) {
        this.tmpEstadoTramitacion = tmpEstadoTramitacion;
    }

    public List<Tramitacion> getListaTramitacion() {
        return listaTramitacion;
    }

    public void setListaTramitacion(List<Tramitacion> listaTramitacion) {
        this.listaTramitacion = listaTramitacion;
    }

    public String imprimirDelantalSetup() {
        this.documentoController.setCriterio("");
        this.documentoController.setListaDocumento(new ArrayList<Documento>());
        return "/reportes/rptDelantal";
    }

    public String listTramitacionOficinaSetup() {
        this.tmpIdRol = JSFutil.getUsuarioConectado().getIdRol();
        this.disabled = (this.tmpIdRol.getIdRol().compareTo(18) != 0 && this.tmpIdRol.getIdRol().compareTo(1) != 0); //Es archivo
        return "/reportes/ListadoTramitacionOficina";
    }

    public void generarReporte(Integer id) throws IOException {
        try {
            JasperManager jm = new JasperManager();
            List<Documento> lista = new ArrayList<>();
            if (id == 0) {
                lista = documentoFacade.findAll();
            } else {
                lista.add(documentoFacade.find(id));
            }
            for (int i = 0; i < lista.size(); i++) {
                lista.get(i).setAnexo(clasificadorController.obtenerRutaClasificador(lista.get(i).getIdClasificador().getIdClasificador()));
            }
            String idFuenteReporte = "1";
            FuenteReporte fr = new FuenteReporte(Integer.valueOf(idFuenteReporte));
            String reportSource = jm.getPathweb() + "reportes/template/" + fr.getNombreReporte();
            jm.generarReporte(reportSource, this.destinoReporte, lista);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
    }

    public void generarTicket(Integer id) throws IOException {
        try {
            JasperManager jm = new JasperManager();
            List<Documento> lista = new ArrayList<>();
            if (id == 0) {
                lista = documentoFacade.findAll();
            } else {
                lista.add(documentoFacade.find(id));
            }

            String idFuenteReporte = "2";
            FuenteReporte fr = new FuenteReporte(Integer.valueOf(idFuenteReporte));
            String reportSource = jm.getPathweb() + "reportes/template/" + fr.getNombreReporte();
            jm.generarReporte(reportSource, this.destinoReporte, lista);
        } catch (Exception e) {
            this.commonController.doExcepcion(e);
        }
    }

    public void buscarDocumento() {
        Integer idRolTmp = null;
        if (this.tmpIdRol != null) {
            idRolTmp = this.tmpIdRol.getIdRol();
        }
        this.listaTramitacion = tramitacionFacade.getAllTramitacion(idRolTmp, tmpEstadoTramitacion.getIdEstado(), tmpFechaDesde, tmpFechaHasta);
        if (this.listaTramitacion.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaTramitacion.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
    }
}
