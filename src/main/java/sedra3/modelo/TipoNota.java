/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.modelo;

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
@Table(name = "tipo_nota")
@NamedQueries({
    @NamedQuery(name = "TipoNota.findAll", query = "SELECT t FROM TipoNota t")})
public class TipoNota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_nota")
    private Integer idTipoNota;
    @Size(max = 255)
    @Column(name = "descripcion_tipo_nota")
    private String descripcionTipoNota;
    @OneToMany(mappedBy = "idTipoNota")
    private List<NotaSalida> notaSalidaList;

    public TipoNota() {
    }

    public TipoNota(Integer idTipoNota) {
        this.idTipoNota = idTipoNota;
    }

    public Integer getIdTipoNota() {
        return idTipoNota;
    }

    public void setIdTipoNota(Integer idTipoNota) {
        this.idTipoNota = idTipoNota;
    }

    public String getDescripcionTipoNota() {
        return descripcionTipoNota;
    }

    public void setDescripcionTipoNota(String descripcionTipoNota) {
        this.descripcionTipoNota = descripcionTipoNota;
    }

    public List<NotaSalida> getNotaSalidaList() {
        return notaSalidaList;
    }

    public void setNotaSalidaList(List<NotaSalida> notaSalidaList) {
        this.notaSalidaList = notaSalidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoNota != null ? idTipoNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoNota)) {
            return false;
        }
        TipoNota other = (TipoNota) object;
        if ((this.idTipoNota == null && other.idTipoNota != null) || (this.idTipoNota != null && !this.idTipoNota.equals(other.idTipoNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDescripcionTipoNota();
    }
    
}
