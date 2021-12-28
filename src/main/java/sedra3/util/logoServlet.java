/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sedra3.controller.CommonController;
import sedra3.fachada.ConfiguracionFacade;
import sedra3.modelo.Configuracion;

/**
 *
 * @author jmferreira
 */
@WebServlet(name = "logoServlet", urlPatterns = {"/logoServlet"})
public class logoServlet extends HttpServlet {

    @Inject
    ConfiguracionFacade configuracionFacade;
    @Inject
    CommonController commonController;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String root_path = getServletContext().getRealPath("/");
        root_path = root_path.replace('\\', '/');
        String imagenSource = root_path + "img/logo-new.png";
        File logoF = new File(imagenSource);
        StreamedContent content;

        Configuracion configuracion = configuracionFacade.find(1);

        if (configuracion.getLogoFileName() != null) {
            InputStream input = new ByteArrayInputStream(configuracion.getArchivoLogo());
            content = DefaultStreamedContent.builder().name(configuracion.getLogoFileName()).contentType(configuracion.getLogoFileType()).stream(() -> input).build();

        } else {
            InputStream input = new FileInputStream(logoF);
            content = DefaultStreamedContent.builder().name("logo-new.png").contentType(new MimetypesFileTypeMap().getContentType(logoF)).stream(() -> input).build();
        }
        response.setContentType(content.getContentType());
        response.setHeader("Content-disposition", "inline; filename=" + content.getName());

        byte[] buffer = new byte[2048];
        int length;

        InputStream inputStream = content.getStream().get();
        OutputStream output = response.getOutputStream();
        while ((length = (inputStream.read(buffer))) >= 0) {
            output.write(buffer, 0, length);
        }
        inputStream.close();
        output.flush();
        output.close();

        response.setStatus(200);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
