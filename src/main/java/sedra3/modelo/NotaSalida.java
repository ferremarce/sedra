/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "nota_salida")
@NamedQueries({
    @NamedQuery(name = "NotaSalida.findAll", query = "SELECT n FROM NotaSalida n")})
public class NotaSalida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idnota_seq")
    @SequenceGenerator(allocationSize = 1, name = "idnota_seq", sequenceName = "idnota_seq")
    @Column(name = "id_nota")
    private Integer idNota;
    @Column(name = "anho")
    private Integer anho;
    @Lob
    @Column(name = "archivo")
    private byte[] archivo;
    @Column(name = "cerrado")
    private Boolean cerrado;
    @Size(max = 255)
    @Column(name = "fuente_financiamiento")
    private String fuenteFinanciamiento;
    @Size(max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Size(max = 255)
    @Column(name = "numero_str")
    private String numeroStr;
    @Size(max = 255)
    @Column(name = "numero_salida")
    private String numeroSalida;
    @Size(max = 255)
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "rubro")
    private Integer rubro;
    @Column(name = "tamanho_archivo")
    private BigInteger tamanhoArchivo;
    @Size(max = 255)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @OneToMany(mappedBy = "idNota")
    private List<DetalleNotaSalida> detalleNotaSalidaList;
    @JoinColumn(name = "id_clasificador", referencedColumnName = "id_clasificador")
    @ManyToOne
    private Clasificador idClasificador;
    @JoinColumn(name = "id_tipo_nota", referencedColumnName = "id_tipo_nota")
    @ManyToOne
    private TipoNota idTipoNota;

    public NotaSalida() {
    }

    public NotaSalida(Integer idNota) {
        this.idNota = idNota;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
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

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    public String getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(String fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNumeroStr() {
        return numeroStr;
    }

    public void setNumeroStr(String numeroStr) {
        this.numeroStr = numeroStr;
    }

    public String getNumeroSalida() {
        return numeroSalida;
    }

    public void setNumeroSalida(String numeroSalida) {
        this.numeroSalida = numeroSalida;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getRubro() {
        return rubro;
    }

    public void setRubro(Integer rubro) {
        this.rubro = rubro;
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

    public List<DetalleNotaSalida> getDetalleNotaSalidaList() {
        return detalleNotaSalidaList;
    }

    public void setDetalleNotaSalidaList(List<DetalleNotaSalida> detalleNotaSalidaList) {
        this.detalleNotaSalidaList = detalleNotaSalidaList;
    }

    public Clasificador getIdClasificador() {
        return idClasificador;
    }

    public void setIdClasificador(Clasificador idClasificador) {
        this.idClasificador = idClasificador;
    }

    public TipoNota getIdTipoNota() {
        return idTipoNota;
    }

    public void setIdTipoNota(TipoNota idTipoNota) {
        this.idTipoNota = idTipoNota;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNota != null ? idNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaSalida)) {
            return false;
        }
        NotaSalida other = (NotaSalida) object;
        if ((this.idNota == null && other.idNota != null) || (this.idNota != null && !this.idNota.equals(other.idNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra3.modelo.NotaSalida[ idNota=" + idNota + " ]";
    }

    public String toAudita() {
        String cadena = "[Id=" + this.idNota + "]"
                + "[NombreArchivo=" + this.nombreArchivo + "]"
                + "[STR=" + this.numeroStr + "]"
                + "[Nro.Sal/Memo=" + this.numeroSalida + "]"
                + "[Referencia=" + this.referencia + "]"
                + "[Anho=" + this.anho + "]"
                + "[Rubro=" + this.rubro + "]"
                + "[Clasificador=" + this.idClasificador + "]";
        return cadena;
    }
}
