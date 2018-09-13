/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Named(value = "CommonController")
@SessionScoped
public class CommonController implements Serializable {

    private static final Logger LOG = Logger.getLogger(CommonController.class.getName());

    /**
     * Creates a new instance of CommonController
     */
    public CommonController() {
    }

    public void initError() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String urlError = (String) request.getAttribute("javax.servlet.error.request_uri");

        LOG.log(Level.SEVERE, "!URL Error: {0} from IP: {1}", new Object[]{urlError, JSFutil.getClientIpAddr(request)});
    }
}
