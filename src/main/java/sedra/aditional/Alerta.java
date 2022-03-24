/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sedra.aditional;

import sedra.util.Codigo;

/**
 *
 * @author jmferreira
 */
public class Alerta {

    private Integer codigoMensaje;
    private Integer cantidad;
    private String mensaje;

    public Alerta(Integer codigoMensaje, Integer cantidad, String mensaje) {
        this.codigoMensaje = codigoMensaje;
        this.cantidad = cantidad;
        this.mensaje = mensaje;
    }

    public Integer getCodigoMensaje() {
        return codigoMensaje;
    }

    public void setCodigoMensaje(Integer codigoMensaje) {
        this.codigoMensaje = codigoMensaje;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return cantidad + " " + mensaje;
    }

    public String toAction() {
        if (this.codigoMensaje.equals(Codigo.ALERTA_DOCUMENTO_PENDIENTE)) {
            return "/tramitacion/ListarDocumentoPendiente.xhtml??faces-redirect=true&alerta=" + codigoMensaje;
        }
        return "";
    }

}
