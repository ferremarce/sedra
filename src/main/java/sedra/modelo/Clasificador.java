/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author jmferreira
 */
@Entity
@Table(name = "clasificador")
@NamedQueries({
    @NamedQuery(name = "Clasificador.findAll", query = "SELECT c FROM Clasificador c")})
public class Clasificador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idclasificador_seq")
    @SequenceGenerator(initialValue=200, allocationSize = 1, name = "idclasificador_seq", sequenceName = "idclasificador_seq")
    @Column(name = "id_clasificador")
    private Integer idClasificador;
    @Size(max = 255)
    @Column(name = "denominacion_clasificador")
    private String denominacionClasificador;
    @Size(max = 255)
    @Column(name = "nivel_clasificador")
    private String nivelClasificador;
    @Column(name = "padre")
    private Integer padre;
    @OneToMany(mappedBy = "idClasificador")
    private List<Documento> documentoList;
    @OneToMany(mappedBy = "idClasificador")
    private List<NotaSalida> notaSalidaList;

    public Clasificador() {
    }

    public Clasificador(Integer idClasificador) {
        this.idClasificador = idClasificador;
    }

    public Integer getIdClasificador() {
        return idClasificador;
    }

    public void setIdClasificador(Integer idClasificador) {
        this.idClasificador = idClasificador;
    }

    public String getDenominacionClasificador() {
        return denominacionClasificador;
    }

    public void setDenominacionClasificador(String denominacionClasificador) {
        this.denominacionClasificador = denominacionClasificador;
    }

    public String getNivelClasificador() {
        return nivelClasificador;
    }

    public void setNivelClasificador(String nivelClasificador) {
        this.nivelClasificador = nivelClasificador;
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
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
        hash += (idClasificador != null ? idClasificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clasificador)) {
            return false;
        }
        Clasificador other = (Clasificador) object;
        if ((this.idClasificador == null && other.idClasificador != null) || (this.idClasificador != null && !this.idClasificador.equals(other.idClasificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDenominacionClasificador()+" [id="+this.getIdClasificador()+"]";
    }
    
}
