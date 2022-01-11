/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.exception;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author root
 */
@Named(value = "ExceptionController")
@RequestScoped
public class ExceptionController implements Serializable {

    private static final Logger LOG = Logger.getLogger(ExceptionController.class.getName());

    public ExceptionController() {
    }

    public String getStatusCode() {
        String val = String.valueOf((Integer) FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.status_code"));
        return val;
    }

    public String getMessage() {
        String val = (String) FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.message");
        return val;
    }

    public String getExceptionType() {
        try {
            String val = FacesContext.getCurrentInstance().getExternalContext().
                    getRequestMap().get("javax.servlet.error.exception_type").toString();
            return val;
        } catch (Exception e) {
            return "";
        }
    }

    public String getException() {
        try {
            String val = (String) ((Exception) FacesContext.getCurrentInstance().getExternalContext().
                    getRequestMap().get("javax.servlet.error.exception")).toString();
            return val;
        } catch (Exception e) {
            return "";
        }
    }

    public String getRequestURI() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.request_uri");
    }

    public String getServletName() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.servlet_name");
    }

    public String doStackTrace() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> map = context.getExternalContext().getRequestMap();
        Throwable throwable = (Throwable) map.get("javax.servlet.error.exception");
        if (throwable != null) {
            return Arrays.toString(throwable.getStackTrace());
        } else {
            LOG.log(Level.SEVERE, "No es posible obtener información de la excepción");
            return "No es posible obtener información de la excepción";
        }

    }
}
