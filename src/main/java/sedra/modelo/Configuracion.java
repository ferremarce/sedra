/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "configuracion")
@NamedQueries({
    @NamedQuery(name = "Configuracion.findAll", query = "SELECT c FROM Configuracion c")})
public class Configuracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_configuracion")
    private Integer idConfiguracion;
    @Size(max = 255)
    @Column(name = "titulo_sistema")
    private String tituloSistema;
    @Size(max = 255)
    @Column(name = "sub_titulo_sistema")
    private String subTituloSistema;
    @Size(max = 500)
    @Column(name = "logo_file_name")
    private String logoFileName;
    @Column(name = "logo_file_type")
    private String logoFileType;
    //@Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "ARCHIVO_LOGO")
    private byte[] archivoLogo;

    public Configuracion() {
    }

    public Configuracion(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public Integer getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public String getTituloSistema() {
        return tituloSistema;
    }

    public void setTituloSistema(String tituloSistema) {
        this.tituloSistema = tituloSistema;
    }

    public String getSubTituloSistema() {
        return subTituloSistema;
    }

    public void setSubTituloSistema(String subTituloSistema) {
        this.subTituloSistema = subTituloSistema;
    }

    public String getLogoFileName() {
        return logoFileName;
    }

    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
    }

    public String getLogoFileType() {
        return logoFileType;
    }

    public void setLogoFileType(String logoFileType) {
        this.logoFileType = logoFileType;
    }

    public byte[] getArchivoLogo() {
        return archivoLogo;
    }

    public void setArchivoLogo(byte[] archivoLogo) {
        this.archivoLogo = archivoLogo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfiguracion != null ? idConfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuracion)) {
            return false;
        }
        Configuracion other = (Configuracion) object;
        if ((this.idConfiguracion == null && other.idConfiguracion != null) || (this.idConfiguracion != null && !this.idConfiguracion.equals(other.idConfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra.modelo.Configuracion[ idConfiguracion=" + idConfiguracion + " ]";
    }

}
