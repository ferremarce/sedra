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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sedra3.fachada.UsuarioFacade;
import sedra3.modelo.Permiso;
import sedra3.modelo.Usuario;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "MenuController")
@SessionScoped
public class MenuController implements Serializable {

    @Inject
    UsuarioFacade usuarioFacade;
    private DefaultMenuModel model;

    /**
     * Creates a new instance of MenuController
     */
    public MenuController() {
    }

    public MenuModel getModel() {
        return model;
    }

    @PostConstruct
    public void init() {
        this.montarMenu();
    }

    public void montarMenu() {
        Usuario user = JSFutil.getUsuarioConectado();

        String nivel;
        model = new DefaultMenuModel();
        DefaultSubMenu submenu = new DefaultSubMenu();
        DefaultMenuItem item;

        List<Permiso> tr = usuarioFacade.getPermisoUsuario(user);
        for (Permiso x : tr) {
            nivel = x.getNivel();
            if (nivel.replaceAll("[^.]", "").length() == 0) { //cantidad de puntos que tiene la cadena
                if (submenu.getLabel() != null) {
                    model.addElement(submenu);
                }
                submenu = new DefaultSubMenu(x.getDescripcionPermiso());
                submenu.setIcon(x.getUrlImagen());
            } else {
                /*Agregar un item*/
                item = new DefaultMenuItem(x.getDescripcionPermiso());
                item.setCommand(x.getTagMenu());
                item.setIcon(x.getUrlImagen());
                item.setAjax(false);
                submenu.addElement(item);
            }
        }
        if (submenu.getLabel() != null) {
            model.addElement(submenu);
        }

    }

}
