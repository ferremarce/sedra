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
import javax.inject.Inject;
import sedra3.fachada.RolFacade;
import sedra3.modelo.Rol;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "RolController")
@SessionScoped
public class RolController implements Serializable {

    @Inject
    RolFacade rolFacade;

    private Rol rol;
    private List<Rol> listaRol;
    private String criterio;

    /**
     * Creates a new instance of RolController
     */
    public RolController() {
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<Rol> listaRol) {
        this.listaRol = listaRol;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

///---------------------METODOS---------------------///
    public String listRolSetup() {
        return "/rol/ListarRol";
    }

    public String createSetup() {
        this.rol = new Rol();
        return "/rol/CrearRol";
    }

    public String editSetup(Integer idRol) {
        this.rol = rolFacade.find(idRol);
        return "/rol/CrearRol";
    }

    public String delete(Integer idRol) {
        try {
            Rol u = rolFacade.find(idRol);
            String name = u.getDescripcionRol();
            rolFacade.remove(u);
            this.doRefrescar();
            JSFutil.addMessage("El Rol #" + name + "# fue eliminado.", JSFutil.StatusMessage.INFORMATION);
        } catch (NumberFormatException ne) {
            JSFutil.addMessage(ne.getMessage(), JSFutil.StatusMessage.ERROR);
        } catch (Exception e) {
            JSFutil.addMessage(e.getMessage(), JSFutil.StatusMessage.ERROR);

        }
        return "/rol/ListarRol";
    }

    public String create() {
        try {
            if (this.rol.getIdRol() == null) {
                rolFacade.create(rol);
            } else {
                rolFacade.edit(rol);
            }
            this.doRefrescar();
            JSFutil.addMessage("Rol creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            //e.printStackTrace();
            JSFutil.addMessage("Ocurrió un error de persistencia.", JSFutil.StatusMessage.ERROR);
        }
        return "/rol/ListarRol";
    }

    public String doBuscar() {
        if (this.criterio.isEmpty()) {
            JSFutil.addMessage("No hay criterios para buscar...", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listaRol = rolFacade.getAllRol(this.criterio);
        if (this.listaRol.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaRol.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }

    public String doRefrescar() {
        this.listaRol = rolFacade.getAllRol("%");
        if (this.listaRol.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaRol.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }

}
