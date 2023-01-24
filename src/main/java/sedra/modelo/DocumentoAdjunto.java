/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.modelo;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "documento_adjunto")
@NamedQueries({
    @NamedQuery(name = "DocumentoAdjunto.findAll", query = "SELECT e FROM DocumentoAdjunto e")})
public class DocumentoAdjunto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_documento_adjunto")
    private Integer idDocumentoAdjunto;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Size(max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Lob
    @Column(name = "archivo")
    private byte[] archivo;
    @Column(name = "tamanho_archivo")
    private BigInteger tamanhoArchivo;
    @Size(max = 255)
    @Column(name = "tipo_adjunto")
    private String tipoAdjunto;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "tipo_archivo_mime")
    private String tipoArchivoMime;
    @Column(name = "id_documento_anterior")
    private Integer idDocumentoAnterior;
    @JoinColumn(name = "id_documento", referencedColumnName = "id_documento")
    @ManyToOne
    private Documento idDocumento;
    @Size(max = 255)
    @Column(name = "checksum_esperado")
    private String checksumEsperado;
    @Size(max = 255)
    @Column(name = "checksum_calculado")
    private String checksumCalculado;

    public DocumentoAdjunto() {
    }

    public DocumentoAdjunto(Integer idDocumentoAdjunto) {
        this.idDocumentoAdjunto = idDocumentoAdjunto;
    }

    public Integer getIdDocumentoAdjunto() {
        return idDocumentoAdjunto;
    }

    public void setIdDocumentoAdjunto(Integer idDocumentoAdjunto) {
        this.idDocumentoAdjunto = idDocumentoAdjunto;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public BigInteger getTamanhoArchivo() {
        return tamanhoArchivo;
    }

    public void setTamanhoArchivo(BigInteger tamanhoArchivo) {
        this.tamanhoArchivo = tamanhoArchivo;
    }

    public String getTipoAdjunto() {
        return tipoAdjunto;
    }

    public void setTipoAdjunto(String tipoAdjunto) {
        this.tipoAdjunto = tipoAdjunto;
    }

    public String getTipoArchivoMime() {
        return tipoArchivoMime;
    }

    public void setTipoArchivoMime(String tipoArchivoMime) {
        this.tipoArchivoMime = tipoArchivoMime;
    }

    public Documento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Documento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdDocumentoAnterior() {
        return idDocumentoAnterior;
    }

    public void setIdDocumentoAnterior(Integer idDocumentoAnterior) {
        this.idDocumentoAnterior = idDocumentoAnterior;
    }

    public String getChecksumEsperado() {
        return checksumEsperado;
    }

    public void setChecksumEsperado(String checksumEsperado) {
        this.checksumEsperado = checksumEsperado;
    }

    public String getChecksumCalculado() {
        return checksumCalculado;
    }

    public void setChecksumCalculado(String checksumCalculado) {
        this.checksumCalculado = checksumCalculado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoAdjunto != null ? idDocumentoAdjunto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoAdjunto)) {
            return false;
        }
        DocumentoAdjunto other = (DocumentoAdjunto) object;
        if ((this.idDocumentoAdjunto == null && other.idDocumentoAdjunto != null) || (this.idDocumentoAdjunto != null && !this.idDocumentoAdjunto.equals(other.idDocumentoAdjunto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "segment.modelo.DocumentoAdjunto[ idDocumentoAdjunto=" + idDocumentoAdjunto + " ]";
    }

    public String toNameDownload() {
        String extension = "";
        int i = this.getNombreArchivo().lastIndexOf('.');
        if (i > 0) {
            extension = this.getNombreArchivo().substring(i + 1);
        }
        if (extension.length() <= 0) {
            extension = "doc";
        }
        //return this.getTipoAdjunto() + "-" + this.idProyecto.getDocumentoCamara() + "." + extension;
        return this.getNombreArchivo();
    }

    public String toURLDownload() {
        return "documento-" + this.idDocumentoAdjunto;
    }

    public String toTamanho() {
        if (this.tamanhoArchivo.intValue() < 1024) {
            return tamanhoArchivo + "B";
        } else if (this.tamanhoArchivo.intValue() < 1048576) {
            return (tamanhoArchivo.intValue() / 1024) + "KB";
        } else {
            return (tamanhoArchivo.intValue() / 1048576) + "MB";
        }
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String toPathFileSystem() {
        return JSFutil.folderDocumento + this.getIdDocumentoAdjunto() + "-" + this.getNombreArchivo();
    }

    public Boolean validFile() {
        try {
            return this.checksumEsperado.equals(this.checksumCalculado);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
