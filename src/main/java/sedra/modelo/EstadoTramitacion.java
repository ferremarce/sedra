/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import sedra.util.Codigo;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "estado_tramitacion")
@NamedQueries({
    @NamedQuery(name = "EstadoTramitacion.findAll", query = "SELECT e FROM EstadoTramitacion e")})
public class EstadoTramitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estado")
    private Integer idEstado;
    @Size(max = 255)
    @Column(name = "descripcion_estado")
    private String descripcionEstado;
    @Size(max = 255)
    @Column(name = "insignia")
    private String insignia;
    @Size(max = 255)
    @Column(name = "info_estado")
    private String infoEstado;
    @Column(name = "orden")
    private Integer orden;
    @OneToMany(mappedBy = "idEstado")
    private List<Tramitacion> tramitacionList;

    public EstadoTramitacion() {
    }

    public EstadoTramitacion(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public String getInsignia() {
        return insignia;
    }

    public void setInsignia(String insignia) {
        this.insignia = insignia;
    }

    public String getInfoEstado() {
        return infoEstado;
    }

    public void setInfoEstado(String infoEstado) {
        this.infoEstado = infoEstado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<Tramitacion> getTramitacionList() {
        return tramitacionList;
    }

    public void setTramitacionList(List<Tramitacion> tramitacionList) {
        this.tramitacionList = tramitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoTramitacion)) {
            return false;
        }
        EstadoTramitacion other = (EstadoTramitacion) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDescripcionEstado();
    }

    public Boolean esTramiteEntrada() {
        if (this.idEstado.compareTo(Codigo.ESTADO_TRAMITE_PENDIENTE) == 0
                || this.idEstado.compareTo(Codigo.ESTADO_TRAMITE_INGRESADO) == 0
                || this.idEstado.compareTo(Codigo.ESTADO_TRAMITE_RECIBIDO) == 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
