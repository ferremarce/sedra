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
import javax.validation.constraints.Size;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "audita")
@NamedQueries({
    @NamedQuery(name = "Audita.findAll", query = "SELECT a FROM Audita a")})
public class Audita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idaudita_seq")
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idaudita_seq", sequenceName = "idaudita_seq")
    @Column(name = "id_audita")
    private Integer idAudita;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Size(max = 3000)
    @Column(name = "operacion")
    private String operacion;
    @Size(max = 255)
    @Column(name = "tabla")
    private String tabla;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public Audita() {
    }

    public Audita(Integer idAudita) {
        this.idAudita = idAudita;
    }

    public Audita(String tabla, String descripcion, Date fechaHora, String operacion, Usuario idUsuario) {
        this.tabla = tabla;
        this.descripcion = descripcion;
        this.fecha = fechaHora;
        this.hora = fechaHora;
        this.operacion = operacion;
        this.idUsuario = idUsuario;
    }

    public Integer getIdAudita() {
        return idAudita;
    }

    public void setIdAudita(Integer idAudita) {
        this.idAudita = idAudita;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAudita != null ? idAudita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audita)) {
            return false;
        }
        Audita other = (Audita) object;
        if ((this.idAudita == null && other.idAudita != null) || (this.idAudita != null && !this.idAudita.equals(other.idAudita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra.modelo.Audita[ idAudita=" + idAudita + " ]";
    }

}
