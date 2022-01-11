/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.modelo;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "detalle_nota_salida")
@NamedQueries({
    @NamedQuery(name = "DetalleNotaSalida.findAll", query = "SELECT d FROM DetalleNotaSalida d")})
public class DetalleNotaSalida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iddetallenota_seq")
    @SequenceGenerator(allocationSize = 1, name = "iddetallenota_seq", sequenceName = "iddetallenota_seq")
    @Column(name = "id_detalle_nota")
    private Integer idDetalleNota;
    @Column(name = "fecha_enlace")
    @Temporal(TemporalType.DATE)
    private Date fechaEnlace;
    @JoinColumn(name = "id_documento", referencedColumnName = "id_documento")
    @ManyToOne
    private Documento idDocumento;
    @JoinColumn(name = "id_nota", referencedColumnName = "id_nota")
    @ManyToOne
    private NotaSalida idNota;

    public DetalleNotaSalida() {
    }

    public DetalleNotaSalida(Integer idDetalleNota) {
        this.idDetalleNota = idDetalleNota;
    }

    public Integer getIdDetalleNota() {
        return idDetalleNota;
    }

    public void setIdDetalleNota(Integer idDetalleNota) {
        this.idDetalleNota = idDetalleNota;
    }

    public Date getFechaEnlace() {
        return fechaEnlace;
    }

    public void setFechaEnlace(Date fechaEnlace) {
        this.fechaEnlace = fechaEnlace;
    }

    public Documento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Documento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public NotaSalida getIdNota() {
        return idNota;
    }

    public void setIdNota(NotaSalida idNota) {
        this.idNota = idNota;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleNota != null ? idDetalleNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleNotaSalida)) {
            return false;
        }
        DetalleNotaSalida other = (DetalleNotaSalida) object;
        if ((this.idDetalleNota == null && other.idDetalleNota != null) || (this.idDetalleNota != null && !this.idDetalleNota.equals(other.idDetalleNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra.modelo.DetalleNotaSalida[ idDetalleNota=" + idDetalleNota + " ]";
    }

    public String toAudita() {
        return "[Id=" + this.idDetalleNota + "]"
                + "[FechaEnlace=" + this.fechaEnlace + "]"
                + "[IdDoc/Entrada=" + this.idDocumento.getIdDocumento() + "/" + this.idDocumento.getNroEntrada() + "]"
                + "[IdNota/Nro=" + this.idNota + "/" + this.idNota.getNumeroSalida() + "]";
    }

}
