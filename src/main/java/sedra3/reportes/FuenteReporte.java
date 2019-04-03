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
                this.nombreReporte = "rptFicha.jrxml";
                break;
            case 2:
                this.nombreReporte = "rptCheques.jrxml";
                break;
            case 3:
                this.nombreReporte = "rptDescuentosPorSTR.jrxml";
                break;
            case 4:
                this.nombreReporte = "rptDescuentosPorALAORDEN.jrxml";
                break;
            case 5:
                this.nombreReporte = "rptChequesAnulados.jrxml";
                break;
            case 6:
                this.nombreReporte = "rptDescuentosAgrupados.jrxml";
                break;
            case 7:
                this.nombreReporte = "rptRangoDescuentos.jrxml";
                break;
            case 8:
                this.nombreReporte = "rptChequesNoCobrados.jrxml";
                break;
            case 9:
                this.nombreReporte = "rptLibroBanco.jrxml";
                break;
            case 10:
                this.nombreReporte = "rptDescuentosGremio.jrxml";
                break;
            case 11:
                this.nombreReporte = "rptViaticos.jrxml";
                break;
            case 12:
                this.nombreReporte = "rptViaticosInterior.jrxml";
                break;
            case 13:
                this.nombreReporte = "rptFacturas.jrxml";
                break;
            case 14:
                this.nombreReporte = "rptFacturasContabilidad.jrxml";
                break;
            case 15:
                this.nombreReporte = "rptFacturasFinanciera.jrxml";
                break;
            case 16:
                this.nombreReporte = "rptFacturasRendicionCta.jrxml";
                break;
            case 17:
                this.nombreReporte = "rptDescuentosXanio.jrxml";
                break;
            case 18:
                this.nombreReporte = "rptDescuentosAgrupadosXanio.jrxml";
                break;
            case 19:
                this.nombreReporte = "rptCheques2.jrxml";
                break;

        }
    }
}
