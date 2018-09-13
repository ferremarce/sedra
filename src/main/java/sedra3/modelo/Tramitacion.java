/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "tramitacion")
@NamedQueries({
    @NamedQuery(name = "Tramitacion.findAll", query = "SELECT t FROM Tramitacion t")})
public class Tramitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idtramitacion_seq")
    @SequenceGenerator(allocationSize = 1, name = "idtramitacion_seq", sequenceName = "idtramitacion_seq")
    @Column(name = "id_tramitacion")
    private Integer idTramitacion;
    @Lob
    @Column(name = "archivo")
    private byte[] archivo;
    @Column(name = "fecha_derivacion")
    @Temporal(TemporalType.DATE)
    private Date fechaDerivacion;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @Column(name = "hora_registro")
    @Temporal(TemporalType.TIME)
    private Date horaRegistro;
    @Column(name = "hora_salida")
    @Temporal(TemporalType.TIME)
    private Date horaSalida;
    @Size(max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Size(max = 255)
    @Column(name = "nota_breve")
    private String notaBreve;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "procesado_archivo")
    private Boolean procesadoArchivo;
    @Size(max = 255)
    @Column(name = "remitido_a")
    private String remitidoA;
    @Size(max = 255)
    @Column(name = "remitido_por")
    private String remitidoPor;
    @Column(name = "tamanho_archivo")
    private BigInteger tamanhoArchivo;
    @Size(max = 255)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Column(name = "fecha_confirmacion")
    @Temporal(TemporalType.DATE)
    private Date fechaConfirmacion;
    @Column(name = "hora_confirmacion")
    @Temporal(TemporalType.TIME)
    private Date horaConfirmacion;
    @JoinColumn(name = "id_documento", referencedColumnName = "id_documento")
    @ManyToOne
    private Documento idDocumento;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne
    private EstadoTramitacion idEstado;
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    @ManyToOne
    private Rol idRol;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;
    @JoinColumn(name = "id_usuario_confirmacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuarioConfirmacion;
    @JoinColumn(name = "id_usuario_remitente", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuarioRemitente;

    public Tramitacion() {
    }

    public Tramitacion(Integer idTramitacion) {
        this.idTramitacion = idTramitacion;
    }

    public Integer getIdTramitacion() {
        return idTramitacion;
    }

    public void setIdTramitacion(Integer idTramitacion) {
        this.idTramitacion = idTramitacion;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public Date getFechaDerivacion() {
        return fechaDerivacion;
    }

    public void setFechaDerivacion(Date fechaDerivacion) {
        this.fechaDerivacion = fechaDerivacion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(Date horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNotaBreve() {
        return notaBreve;
    }

    public void setNotaBreve(String notaBreve) {
        this.notaBreve = notaBreve;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getProcesadoArchivo() {
        return procesadoArchivo;
    }

    public void setProcesadoArchivo(Boolean procesadoArchivo) {
        this.procesadoArchivo = procesadoArchivo;
    }

    public String getRemitidoA() {
        return remitidoA;
    }

    public void setRemitidoA(String remitidoA) {
        this.remitidoA = remitidoA;
    }

    public String getRemitidoPor() {
        return remitidoPor;
    }

    public void setRemitidoPor(String remitidoPor) {
        this.remitidoPor = remitidoPor;
    }

    public BigInteger getTamanhoArchivo() {
        return tamanhoArchivo;
    }

    public void setTamanhoArchivo(BigInteger tamanhoArchivo) {
        this.tamanhoArchivo = tamanhoArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public Date getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(Date fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public Date getHoraConfirmacion() {
        return horaConfirmacion;
    }

    public void setHoraConfirmacion(Date horaConfirmacion) {
        this.horaConfirmacion = horaConfirmacion;
    }

    public Documento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Documento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public EstadoTramitacion getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(EstadoTramitacion idEstado) {
        this.idEstado = idEstado;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol idRol) {
        this.idRol = idRol;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getIdUsuarioConfirmacion() {
        return idUsuarioConfirmacion;
    }

    public void setIdUsuarioConfirmacion(Usuario idUsuarioConfirmacion) {
        this.idUsuarioConfirmacion = idUsuarioConfirmacion;
    }

    public Usuario getIdUsuarioRemitente() {
        return idUsuarioRemitente;
    }

    public void setIdUsuarioRemitente(Usuario idUsuarioRemitente) {
        this.idUsuarioRemitente = idUsuarioRemitente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTramitacion != null ? idTramitacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tramitacion)) {
            return false;
        }
        Tramitacion other = (Tramitacion) object;
        if ((this.idTramitacion == null && other.idTramitacion != null) || (this.idTramitacion != null && !this.idTramitacion.equals(other.idTramitacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra3.modelo.Tramitacion[ idTramitacion=" + idTramitacion + " ]";
    }
    
}
