/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "permiso_rol")
@NamedQueries({
    @NamedQuery(name = "PermisoRol.findAll", query = "SELECT p FROM PermisoRol p")})
public class PermisoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idpermisorol_seq")
    @SequenceGenerator(initialValue = 200, allocationSize = 1, name = "idpermisorol_seq", sequenceName = "idpermisorol_seq")
    @Column(name = "id_permiso_rol")
    private Integer idPermisoRol;
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;
    @Size(max = 255)
    @Column(name = "habilitado")
    private String habilitado;
    @JoinColumn(name = "id_permiso", referencedColumnName = "id_permiso")
    @ManyToOne
    private Permiso idPermiso;
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    @ManyToOne
    private Rol idRol;

    public PermisoRol() {
    }

    public PermisoRol(Integer idPermisoRol) {
        this.idPermisoRol = idPermisoRol;
    }

    public Integer getIdPermisoRol() {
        return idPermisoRol;
    }

    public void setIdPermisoRol(Integer idPermisoRol) {
        this.idPermisoRol = idPermisoRol;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(String habilitado) {
        this.habilitado = habilitado;
    }

    public Permiso getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Permiso idPermiso) {
        this.idPermiso = idPermiso;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol idRol) {
        this.idRol = idRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermisoRol != null ? idPermisoRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoRol)) {
            return false;
        }
        PermisoRol other = (PermisoRol) object;
        if ((this.idPermisoRol == null && other.idPermisoRol != null) || (this.idPermisoRol != null && !this.idPermisoRol.equals(other.idPermisoRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra3.modelo.PermisoRol[ idPermisoRol=" + idPermisoRol + " ]";
    }

}
