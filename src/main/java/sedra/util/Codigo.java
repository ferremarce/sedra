/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.util;

/**
 *
 * @author jmferreira
 */
public interface Codigo {

    public static final Integer ADMINISTRADOR = 1;

    public static final Integer TIPO_DOCUMENTO_DEFECTO = 2;

    public static final Integer ALERTA_DOCUMENTO_PENDIENTE = 100;

    public static final Integer ESTADO_TRAMITE_PENDIENTE = 1;
    public static final Integer ESTADO_TRAMITE_RECHAZADO = 2;
    public static final Integer ESTADO_TRAMITE_RECIBIDO = 3;
    public static final Integer ESTADO_TRAMITE_DERIVADO = 4;
    public static final Integer ESTADO_TRAMITE_INGRESADO = 5;
    public static final Integer ESTADO_TRAMITE_ARCHIVADO = 100;

    public static final Integer BANDEJA_PEDIENTES_CONFIRMACION = 1;
    public static final Integer BANDEJA_PENDIENTES_TRAMITACION = 2;
    public static final Integer BANDEJA_TRAMITADOS = 3;
    
    public static final Integer PRIORIDAD_ALTA = 1;
    public static final Integer PRIORIDAD_NORMAL = 2;

}
