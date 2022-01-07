/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.reportes;

/**
 *
 * @author jmferreira
 */
public class FuenteReporte {

    int id;
    String nombreReporte;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public FuenteReporte(int id) {
        this.id = id;
        switch (id) {
            case 1:
                this.nombreReporte = "rptDelantal.jrxml";
                break;
            case 2:
                this.nombreReporte = "rptTicketDocumento.jrxml";
                break;
        }
    }
}
