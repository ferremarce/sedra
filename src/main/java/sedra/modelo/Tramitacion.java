/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import sedra.util.Codigo;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "tramitacion")
@NamedQueries({
    @NamedQuery(name = "Tramitacion.findAll", query = "SELECT t FROM Tramitacion t")})
public class Tramitacion implements Serializable, Comparable<Tramitacion> {

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
    @Column(name = "fecha_hora_archivo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraArchivo;
    @JoinColumn(name = "id_documento", referencedColumnName = "id_documento")
    @ManyToOne
    private Documento idDocumento;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne
    private EstadoTramitacion idEstado;
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    @ManyToOne
    private Rol idRol;
    @JoinColumn(name = "id_creador", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idCreador;
    @JoinColumn(name = "id_usuario_confirmacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuarioConfirmacion;
    @JoinColumn(name = "id_usuario_remitente", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuarioRemitente;
    @JoinColumn(name = "id_usuario_archivo", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuarioArchivo;
    @OrderBy("idTramitacion")
    @OneToMany(mappedBy = "idTramitacionPadre")
    private List<Tramitacion> tramitacionList;
    @JoinColumn(name = "id_tramitacion_padre", referencedColumnName = "id_tramitacion")
    @ManyToOne
    private Tramitacion idTramitacionPadre;
    @Column(name = "leido")
    private Boolean leido;
    @JoinColumn(name = "id_prioridad", referencedColumnName = "id_prioridad")
    @ManyToOne
    private Prioridad idPrioridad;
    @OneToMany(mappedBy = "idTramitacion", cascade = CascadeType.REMOVE)
    private List<TramitacionAdjunto> tramitacionAdjuntoList;
    @Transient
    private Boolean flagBorrado;

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

    public Usuario getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(Usuario idCreador) {
        this.idCreador = idCreador;
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

    public List<Tramitacion> getTramitacionList() {
        return tramitacionList;
    }

    public void setTramitacionList(List<Tramitacion> tramitacionList) {
        this.tramitacionList = tramitacionList;
    }

    public Tramitacion getIdTramitacionPadre() {
        return idTramitacionPadre;
    }

    public void setIdTramitacionPadre(Tramitacion idTramitacionPadre) {
        this.idTramitacionPadre = idTramitacionPadre;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Prioridad getIdPrioridad() {
        return idPrioridad;
    }

    public void setIdPrioridad(Prioridad idPrioridad) {
        this.idPrioridad = idPrioridad;
    }

    public Boolean getFlagBorrado() {
        if (this.flagBorrado != null) {
            return flagBorrado;
        } else {
            return Boolean.FALSE;
        }
    }

    public void setFlagBorrado(Boolean flagBorrado) {
        this.flagBorrado = flagBorrado;
    }

    public Date getFechaHoraArchivo() {
        return fechaHoraArchivo;
    }

    public void setFechaHoraArchivo(Date fechaHoraArchivo) {
        this.fechaHoraArchivo = fechaHoraArchivo;
    }

    public Usuario getIdUsuarioArchivo() {
        return idUsuarioArchivo;
    }

    public void setIdUsuarioArchivo(Usuario idUsuarioArchivo) {
        this.idUsuarioArchivo = idUsuarioArchivo;
    }

    public List<TramitacionAdjunto> getTramitacionAdjuntoList() {
        return tramitacionAdjuntoList;
    }

    public void setTramitacionAdjuntoList(List<TramitacionAdjunto> tramitacionAdjuntoList) {
        this.tramitacionAdjuntoList = tramitacionAdjuntoList;
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
        return "sedra.modelo.Tramitacion[ idTramitacion=" + idTramitacion + " ]";
    }

    public String toAudita() {
        return "[Id=" + this.idTramitacion + "]"
                + "[NombreArchivo=" + this.nombreArchivo + "]"
                + "[Nota Breve=" + this.notaBreve + "]"
                + "[RemitidoA=" + this.remitidoA + "]"
                + "[RemitidoPor=" + this.remitidoPor + "]"
                + "[DerivadoEl=" + this.fechaDerivacion + "]"
                + "[Estado=" + this.idEstado + "]"
                + "[Rol/Oficina=" + this.idRol + "]"
                + "[Nro.Entrada=" + this.idDocumento.getIdDocumento() + "]";
    }

    @Override
    public int compareTo(Tramitacion t) {
        return t.getFechaDerivacion().compareTo(this.getFechaDerivacion());
    }

    public String doTramitadoPor() {
        String cadena = "";
        try {
            if (idTramitacionPadre != null) {
                cadena += idTramitacionPadre.idCreador.toInfoString();
            } else if (this.idEstado.getIdEstado().compareTo(Codigo.ESTADO_TRAMITE_INGRESADO) == 0) {
                cadena += this.idCreador.toInfoString();
            }
            cadena += this.remitidoPor == null ? "" : ("| " + this.remitidoPor);
        } catch (Exception ex) {
            cadena = "Error doTramitadoPor()";
        }
        return cadena;
    }

    public String doTramitadoA() {
        String cadena = "";
        try {
            cadena += this.idRol.toString();
            cadena += this.remitidoA != null ? "" : ("\n " + this.remitidoA);
        } catch (Exception ex) {
            cadena = "Error doTramitadoA()";
        }
        return cadena;
    }

    public Boolean esConfidencial() {
        return !(this.idEstado.getIdEstado().equals(Codigo.ESTADO_TRAMITE_PENDIENTE) || this.idEstado.getIdEstado().equals(Codigo.ESTADO_TRAMITE_RECHAZADO));
    }
}
