/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sedra.controller;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sedra.modelo.Documento;

/**
 *
 * @author jmferreira
 */
@Named(value = "documentoHeader")
@SessionScoped
public class DocumentoHeader implements Serializable {

    @Getter
    @Setter
    private Documento documento;

}
