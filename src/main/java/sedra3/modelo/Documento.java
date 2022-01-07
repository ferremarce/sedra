/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.modelo;

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
import javax.validation.constraints.Size;
import sedra3.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "documento")
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d")})
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iddocumento_seq")
    @SequenceGenerator(allocationSize = 1, name = "iddocumento_seq", sequenceName = "iddocumento_seq")
    @Column(name = "id_documento")
    private Integer idDocumento;
    @Size(max = 255)
    @Column(name = "anexo")
    private String anexo;
    @Column(name = "anho")
    private Integer anho;
    @Lob
    @Column(name = "archivo")
    private byte[] archivo;
    @Size(max = 255)
    @Column(name = "asunto")
    private String asunto;
    @Column(name = "cerrado")
    private Boolean cerrado;
    @Size(max = 255)
    @Column(name = "comprobante_pago")
    private String comprobantePago;
    @Column(name = "fecha_documento")
    @Temporal(TemporalType.DATE)
    private Date fechaDocumento;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fecha_limite")
    @Temporal(TemporalType.DATE)
    private Date fechaLimite;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Column(name = "hora_registro")
    @Temporal(TemporalType.TIME)
    private Date horaRegistro;
    @Size(max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Size(max = 255)
    @Column(name = "nro_entrada")
    private String nroEntrada;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 255)
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "tamanho_archivo")
    private BigInteger tamanhoArchivo;
    @Size(max = 255)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @JoinColumn(name = "id_clasificador", referencedColumnName = "id_clasificador")
    @ManyToOne
    private Clasificador idClasificador;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne
    private TipoDocumento idTipoDocumento;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;
    @OneToMany(mappedBy = "idDocumento")
    private List<DetalleNotaSalida> detalleNotaSalidaList;
    @OneToMany(mappedBy = "idDocumento", cascade = CascadeType.REMOVE)
    @OrderBy("fechaDerivacion ASC, idTramitacion ASC")
    private List<Tramitacion> tramitacionList;
    @OneToMany(mappedBy = "idDocumento", cascade = CascadeType.REMOVE)
    private List<DocumentoAdjunto> documentoAdjuntoList;
    @Column(name = "numero_expediente")
    private Integer numeroExpediente;

    public Documento() {
    }

    public Documento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public Integer getAnho() {
        return anho;
    }

    public void setAnho(Integer anho) {
        this.anho = anho;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    public String getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(String comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(Date horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNroEntrada() {
        return nroEntrada;
    }

    public void setNroEntrada(String nroEntrada) {
        this.nroEntrada = nroEntrada;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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

    public Clasificador getIdClasificador() {
        return idClasificador;
    }

    public void setIdClasificador(Clasificador idClasificador) {
        this.idClasificador = idClasificador;
    }

    public TipoDocumento getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(TipoDocumento idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<DetalleNotaSalida> getDetalleNotaSalidaList() {
        return detalleNotaSalidaList;
    }

    public void setDetalleNotaSalidaList(List<DetalleNotaSalida> detalleNotaSalidaList) {
        this.detalleNotaSalidaList = detalleNotaSalidaList;
    }

    public List<Tramitacion> getTramitacionList() {
        return tramitacionList;
    }

    public void setTramitacionList(List<Tramitacion> tramitacionList) {
        this.tramitacionList = tramitacionList;
    }

    public List<DocumentoAdjunto> getDocumentoAdjuntoList() {
        return documentoAdjuntoList;
    }

    public void setDocumentoAdjuntoList(List<DocumentoAdjunto> documentoAdjuntoList) {
        this.documentoAdjuntoList = documentoAdjuntoList;
    }

    public Integer getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(Integer numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumento != null ? idDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.idDocumento == null && other.idDocumento != null) || (this.idDocumento != null && !this.idDocumento.equals(other.idDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + this.nroEntrada + "] " + this.asunto;
    }

    public String toAudita() {
        return "[Id=" + this.idDocumento + "] "
                + "[Nro.Entrada=" + this.nroEntrada + "] "
                + "[Asunto=" + this.asunto + "] "
                + "[CP=" + this.comprobantePago + "] "
                + "[Referencia=" + this.referencia + "] "
                + "[Archivo=" + this.nombreArchivo + "]";
    }

    public String toVerURL() {
        return JSFutil.getAbsoluteApplicationUrl() + "/faces/documento/VerDocumento.xhtml?id=" + this.getIdDocumento();
    }

}
