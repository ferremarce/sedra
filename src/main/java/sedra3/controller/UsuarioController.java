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
import sedra3.fachada.UsuarioFacade;
import sedra3.modelo.Usuario;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "UsuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    private static final Logger LOG = Logger.getLogger(UsuarioController.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());

    @Inject
    UsuarioFacade usuarioFacade;
    @Inject
    CommonController commonController;

    private Usuario usuario;
    private List<Usuario> listaUsuario;
    private String criterio;
    private boolean tmpActivo;
    private String contrasenhaNueva;
    private String contrasenhaRepetida;

    /**
     * Creates a new instance of UsuarioController
     */
    public UsuarioController() {
    }

    public String getContrasenhaNueva() {
        return contrasenhaNueva;
    }

    public void setContrasenhaNueva(String contrasenhaNueva) {
        this.contrasenhaNueva = contrasenhaNueva;
    }

    public String getContrasenhaRepetida() {
        return contrasenhaRepetida;
    }

    public void setContrasenhaRepetida(String contrasenhaRepetida) {
        this.contrasenhaRepetida = contrasenhaRepetida;
    }

    public UsuarioFacade getUsuarioFacade() {
        return usuarioFacade;
    }

    public boolean isTmpActivo() {
        return tmpActivo;
    }

    public void setTmpActivo(boolean tmpActivo) {
        this.tmpActivo = tmpActivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

///---------------------METODOS---------------------///
    public String listUsuarioSetup() {
        return "/usuario/ListarUsuario";
    }

    public String createSetup() {
        this.usuario = new Usuario();
        return "/usuario/CrearUsuario";
    }

    public String editSetup(Integer idUsuario) {
        this.usuario = usuarioFacade.find(idUsuario);
        this.tmpActivo = this.stringToBoolean(usuario.getActivo());
        return "/usuario/CrearUsuario";
    }

    public String delete(Integer idUsuario) {
        try {
            Usuario u = usuarioFacade.find(idUsuario);
            String name = u.getCuenta();
            usuarioFacade.remove(u);
            this.doRefrescar();
            JSFutil.addMessage("El Usuario #" + name + "# fue eliminado.", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "/usuario/ListarUsuario";
    }

    public String create() {
        if (usuario.getContrasenha().length() < 8) {
            JSFutil.addMessage("Contraseña insegura. Debe proporcionar una contraseña de al menos 8 letras/numeros", JSFutil.StatusMessage.ERROR);
            return "";
        }
        usuario.setActivo(this.booleanToString(tmpActivo));
        try {
            if (this.usuario.getIdUsuario() == null) {
                usuarioFacade.create(usuario);
            } else {
                usuarioFacade.edit(usuario);
            }
            this.doRefrescar();
            JSFutil.addMessage("Usuario creado exitosamente. ", JSFutil.StatusMessage.INFORMATION);
        } catch (Exception ex) {
            this.commonController.doExcepcion(ex);
        }
        return "/usuario/ListarUsuario";
    }

    private String booleanToString(boolean x) {
        if (x == true) {
            return "SI";
        } else {
            return "NO";
        }
    }

    private boolean stringToBoolean(String x) {
        return x.compareTo("SI") == 0;
    }

    public String doBuscar() {
        if (this.criterio.isEmpty()) {
            JSFutil.addMessage("No hay criterios para buscar...", JSFutil.StatusMessage.WARNING);
            return "";
        }
        this.listaUsuario = usuarioFacade.getAllUsuario(this.criterio);
        if (this.listaUsuario.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaUsuario.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }

    public String doRefrescar() {
        this.listaUsuario = usuarioFacade.getAllUsuario("%");
        if (this.listaUsuario.isEmpty()) {
            JSFutil.addMessage("No hay resultados...", JSFutil.StatusMessage.WARNING);
        } else {
            JSFutil.addMessage(this.listaUsuario.size() + " registros recuperados", JSFutil.StatusMessage.INFORMATION);
        }
        return "";
    }

}
