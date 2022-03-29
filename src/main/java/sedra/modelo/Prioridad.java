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

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "prioridad")
@NamedQueries({
    @NamedQuery(name = "Prioridad.findAll", query = "SELECT t FROM Prioridad t")})
public class Prioridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_prioridad")
    private Integer idPrioridad;
    @Size(max = 255)
    @Column(name = "descripcion_prioridad")
    private String descripcionPrioridad;
    @Size(max = 255)
    @Column(name = "insignia")
    private String insignia;
    @Column(name = "orden")
    private Integer orden;
    @OneToMany(mappedBy = "idPrioridad")
    private List<Tramitacion> tramitacionList;

    public Prioridad() {
    }

    public Integer getIdPrioridad() {
        return idPrioridad;
    }

    public void setIdPrioridad(Integer idPrioridad) {
        this.idPrioridad = idPrioridad;
    }

    public String getDescripcionPrioridad() {
        return descripcionPrioridad;
    }

    public void setDescripcionPrioridad(String descripcionPrioridad) {
        this.descripcionPrioridad = descripcionPrioridad;
    }

    public String getInsignia() {
        return insignia;
    }

    public void setInsignia(String insignia) {
        this.insignia = insignia;
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
        hash += (idPrioridad != null ? idPrioridad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prioridad)) {
            return false;
        }
        Prioridad other = (Prioridad) object;
        if ((this.idPrioridad == null && other.idPrioridad != null) || (this.idPrioridad != null && !this.idPrioridad.equals(other.idPrioridad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDescripcionPrioridad();
    }

}
