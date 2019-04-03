/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import sedra3.fachada.DocumentoFacade;
import sedra3.modelo.Documento;
import sedra3.reportes.FuenteReporte;
import sedra3.reportes.JasperManager;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "ReporteController")
@SessionScoped
public class ReporteController implements Serializable {

    @Inject
    DocumentoFacade documentoFacade;

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

    public void generarReporte(Integer id) throws IOException {
        JasperManager jm = new JasperManager();
        List<Documento> lista = new ArrayList<>();
        if (id == 0) {
            lista = documentoFacade.findAll();
        } else {
            lista.add(documentoFacade.find(id));
        }

        String tipoReporte = "PDF";
        String idFuenteReporte = "1";
        FuenteReporte fr = new FuenteReporte(Integer.valueOf(idFuenteReporte));
        String reportSource = jm.getPathweb() + "reportes/template/" + fr.getNombreReporte();
        jm.generarReporte(reportSource, tipoReporte, lista);
    }

    public void generarReporteFicha(List<?> dataList) {
        JasperManager jm = new JasperManager();
        String tipoReporte = "PDF";
        String idFuenteReporte = "1";
        FuenteReporte fr = new FuenteReporte(Integer.valueOf(idFuenteReporte));
        String reportSource = jm.getPathweb() + "reportes/template/" + fr.getNombreReporte();
        if (dataList.size() > 0) {
            jm.generarReporte(reportSource, tipoReporte, dataList);
        } else {
            JSFutil.addMessage("No hay datos para generar el reporte", JSFutil.StatusMessage.ERROR);
        }

    }
}
