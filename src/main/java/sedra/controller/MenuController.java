/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSeparator;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Separator;
import sedra.fachada.UsuarioFacade;
import sedra.modelo.Permiso;
import sedra.modelo.Usuario;
import sedra.util.JSFutil;

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

//    public void montarMenu() {
//        Usuario user = JSFutil.getUsuarioConectado();
//
//        String nivel;
//        model = new DefaultMenuModel();
//        DefaultSubMenu submenu = new DefaultSubMenu();
//        DefaultMenuItem item;
//
//        List<Permiso> tr = usuarioFacade.getPermisoUsuario(user);
//        for (Permiso x : tr) {
//            nivel = x.getNivel();
//            if (nivel.replaceAll("[^.]", "").length() == 0) { //cantidad de puntos que tiene la cadena
//                if (submenu.getLabel() != null) {
//                    model.addElement(submenu);
//                }
//                submenu = new DefaultSubMenu(x.getDescripcionPermiso());
//                submenu.setIcon(x.getUrlImagen());
//            } else {
//                /*Agregar un item*/
//                item = new DefaultMenuItem(x.getDescripcionPermiso());
//                item.setCommand(x.getTagMenu());
//                item.setIcon(x.getUrlImagen());
//                item.setAjax(false);
//                submenu.addElement(item);
//            }
//        }
//        if (submenu.getLabel() != null) {
//            model.addElement(submenu);
//        }
//
//    }
    public void montarMenu() {
        Usuario user = JSFutil.getUsuarioConectado();

        String nivel;
        model = new DefaultMenuModel();

        DefaultSubMenu submenu;
        DefaultMenuItem item;

//        submenu.getElements().add(DefaultSubMenu.builder().label("Otro submenu").build());
        item = DefaultMenuItem.builder().value("Inicio").build();
        item.setCommand("/index");
        item.setAjax(Boolean.FALSE);
        item.setIcon("fa fa-home");
        model.getElements().add(item);
        submenu = DefaultSubMenu.builder().label("solo inicial").build();

        List<Permiso> tr = usuarioFacade.getPermisoUsuario(user);
        for (Permiso x : tr) {
            nivel = x.getNivel();
            if (x.getTagMenu() == null) { //es un menu
                //submenu = new DefaultSubMenu(x.getDescripcionPermiso());
                submenu = DefaultSubMenu.builder().label(x.getDescripcionPermiso()).build();
                submenu.setIcon(x.getUrlImagen());
//                if(submenu.geti)
                model.getElements().add(submenu);
                //submenu.setIcon(x.getUrlImagen());
            } else {
                if (x.getConSeparador() != null && x.getConSeparador()) {
                    submenu.getElements().add(new DefaultSeparator());
                }
                /*Agregar un item*/
                //item = new DefaultMenuItem(x.getDescripcionPermiso());
                item = DefaultMenuItem.builder().value(x.getDescripcionPermiso()).build();
                item.setCommand(x.getTagMenu());
                item.setIcon(x.getUrlImagen());
                item.setAjax(false);
                item.setOnclick("PF('statusAjax').show();");
                submenu.getElements().add(item);
            }
        }
        item = DefaultMenuItem.builder().value("Ayuda").build();
        item.setUrl(JSFutil.getAbsoluteApplicationUrl() + "/help/workflow-sedra.pdf");
        item.setAjax(Boolean.FALSE);
        item.setTarget("_blank");
        item.setIcon("fa fa-info-circle");
        item.setTitle("Obtenga la información del manejo de flujo en el SEDRA");
        model.getElements().add(item);
//        if (submenu.getLabel() != null) {
//            model.getElements().add(submenu);
//        }

    }

}
