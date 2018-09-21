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
import sedra3.fachada.PermisoFacade;
import sedra3.fachada.PermisoRolFacade;
import sedra3.fachada.RolFacade;
import sedra3.modelo.Permiso;
import sedra3.modelo.PermisoRol;
import sedra3.modelo.Rol;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "RolController")
@SessionScoped
public class RolController implements Serializable {

    private static final Logger LOG = Logger.getLogger(RolController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    RolFacade rolFacade;
    @Inject
    PermisoFacade permisoFacade;
    @Inject
    PermisoRolFacade permisoRolFacade;
    @Inject
    CommonController commonController;

    private Rol rol;
    private List<Rol> listaRol;
    private String criterio;
    private Permiso[] arrayPermisos;

    /**
     * Creates a new instance of RolController
     */
    public RolController() {
    }

    public RolFacade getRolFacade() {
        return rolFacade;
    }

    public void setRolFacade(RolFacade rolFacade) {
        this.rolFacade = rolFacade;
    }

    public Permiso[] getArrayPermisos() {
        return arrayPermisos;
    }

    public void setArrayPermisos(Permiso[] arrayPermisos) {
        this.arrayPermisos = arrayPermisos;
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

    public String editPermisoSetup(Integer idRol) {
        this.rol = rolFacade.find(idRol);
        this.arrayPermisos = new Permiso[this.rol.getPermisoRolList().size()];
        int i = 0;
        for (PermisoRol pr : this.rol.getPermisoRolList()) {
            this.arrayPermisos[i] = pr.getIdPermiso();
            i++;
        }
        return "/rol/EditarPermiso";
    }

    public String delete(Integer idRol) {
        try {
            Rol u = rolFacade.find(idRol);
            String name = u.getDescripcionRol();
            rolFacade.remove(u);
            this.doRefrescar();
            JSFutil.addMessage("El Rol #" + name + "# fue eliminado.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
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
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
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

    public String updatePermiso() {
        try {
            for (PermisoRol pr : this.rol.getPermisoRolList()) {
                permisoRolFacade.remove(pr);
            }
            for (Permiso arrayPermiso : this.arrayPermisos) {
                PermisoRol pr = new PermisoRol();
                pr.setFechaAsignacion(JSFutil.getFechaHoraActual());
                pr.setIdPermiso(arrayPermiso);
                pr.setIdRol(rol);
                permisoRolFacade.create(pr);
            }
            JSFutil.addMessage("Permiso actualizado exitosamente.", JSFutil.StatusMessage.INFORMATION);
         } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "/rol/ListarRol";
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
