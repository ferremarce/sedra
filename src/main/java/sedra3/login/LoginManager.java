/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.login;

import java.io.Serializable;
import java.util.TimeZone;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import sedra3.fachada.AuditaFacade;
import sedra3.fachada.UsuarioFacade;
import sedra3.modelo.Audita;
import sedra3.modelo.Usuario;
import sedra3.util.JSFutil;

/**
 *
 * @author root
 */
@Named(value = "LoginManager")
@SessionScoped
public class LoginManager implements Serializable {

    @Inject
    UsuarioFacade usuarioFacade;
    @Inject
    AuditaFacade auditaFacade;

    public static final String USER_SESSION_KEY = "user";
    private String cuenta;
    private String contrasenha;
    private String contrasenha2;
    private Usuario usuarioActual;

    /**
     * Creates a new instance of LoginManager
     */
    public LoginManager() {
    }

    

    /**
     * getter Cuenta
     *
     * @return
     */
    public String getCuenta() {
        return cuenta;
    }

    /**
     * setter Cuenta
     *
     * @param cuenta
     */
    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * getter Contrasenha
     *
     * @return
     */
    public String getContrasenha() {
        return contrasenha;
    }

    /**
     * setter Contrasenha
     *
     * @param contrasenha
     */
    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    /**
     * getter contrasenha2
     *
     * @return
     */
    public String getContrasenha2() {
        return contrasenha2;
    }

    /**
     * setter contrasenha2
     *
     * @param contrasenha2
     */
    public void setContrasenha2(String contrasenha2) {
        this.contrasenha2 = contrasenha2;
    }

    /**
     * Ejecutar la acción de login
     *
     * @return
     */
    public String doLogin() {
        try {
            Usuario user = usuarioFacade.getUsuario(cuenta);
            if (user != null) {
                if (!user.getContrasenha().equals(contrasenha)) {
                    JSFutil.addMessage("Acceso incorrecto!... El password ingresado es incorrecto.",JSFutil.StatusMessage.ERROR);
                    auditaFacade.create(new Audita("LOGIN", "Acceso incorrecto!... El password ingresado es incorrecto.", JSFutil.getFechaHoraActual(), user.getCuenta() + "/" + contrasenha, null));
                    return null;
                } else if (user.getActivo().compareTo("NO") == 0) {
                    JSFutil.addMessage("Acceso Incorrecto!... El usuario ha sido deshabilitado.",JSFutil.StatusMessage.ERROR);
                    auditaFacade.create(new Audita("LOGIN", "Acceso Incorrecto!... El usuario ha sido deshabilitado.", JSFutil.getFechaHoraActual(), user.getCuenta() + "/" + contrasenha, null));
                    return null;
                }
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
                this.usuarioActual = new Usuario();
                this.usuarioActual = user;
                auditaFacade.create(new Audita("LOGIN", "Acceso correcto.", JSFutil.getFechaHoraActual(), user.getCuenta(), null));
                JSFutil.putSessionVariable("tema", this.usuarioActual.getIdTheme());
                return "/index";

            } else {
                JSFutil.addMessage("Login Fallido!... Usuario '" + cuenta + "' no existe.",JSFutil.StatusMessage.ERROR);
                auditaFacade.create(new Audita("LOGIN", "Acceso Incorrecto!... Usuario no existe.", JSFutil.getFechaHoraActual(), cuenta + "/" + contrasenha, null));
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    
    /**
     * Invalidar la sesión y hacer logout
     *
     * @return
     */
    public String doLogout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "/login";
    }

    /**
     * getter del usuario conectado actualmente
     *
     * @return
     */
    public Usuario getUsuarioLogeado() {
        return JSFutil.getUsuarioConectado();
    }

    public String doCambiarContrasenhaForm() {
        this.contrasenha = "";
        this.contrasenha2 = "";
        return "/pages/CambiarContrasenha";
    }

    public String doCambiarContrasenha() {
        if (this.getContrasenha().length() < 8) {
            JSFutil.addMessage("Contraseña insegura. Debe proporcionar una contraseña de al menos 8 letras/numeros",JSFutil.StatusMessage.ERROR);
            return "";
        }
        if (this.getContrasenha().compareTo(this.contrasenha2) != 0) {
            JSFutil.addMessage("Las contraseñas no coinciden. Por favor verifique y vuelva a intentar",JSFutil.StatusMessage.ERROR);
            return "";
        }

        try {
            Usuario u = JSFutil.getUsuarioConectado();
            u.setContrasenha(JSFutil.getSecurePassword(this.contrasenha));
            usuarioFacade.edit(u);
            JSFutil.addMessage("Contraseña cambiada exitosamente.",JSFutil.StatusMessage.INFORMATION);
        } catch (Exception e) {
            JSFutil.addMessage("Ocurrió un error de persistencia.",JSFutil.StatusMessage.FATAL);
        }
        return "";
    }

    /**
     * getter timeZone
     *
     * @return
     */
    public TimeZone getMyTimeZone() {
        return JSFutil.getMyTimeZone();
    }
}
