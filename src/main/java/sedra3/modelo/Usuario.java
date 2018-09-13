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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idusuario_seq")
    @SequenceGenerator(initialValue = 10, allocationSize = 1, name = "idusuario_seq", sequenceName = "idusuario_seq")
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Size(max = 255)
    @Column(name = "activo")
    private String activo;
    @Size(max = 255)
    @Column(name = "contrasenha")
    private String contrasenha;
    @Size(max = 255)
    @Column(name = "cuenta")
    private String cuenta;
    @Size(max = 255)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 255)
    @Column(name = "id_theme")
    private String idTheme;
    @OneToMany(mappedBy = "idUsuario")
    private List<Audita> auditaList;
    @OneToMany(mappedBy = "idUsuario")
    private List<Documento> documentoList;
    @OneToMany(mappedBy = "idUsuario")
    private List<Tramitacion> tramitacionList;
    @OneToMany(mappedBy = "idUsuarioConfirmacion")
    private List<Tramitacion> tramitacionList1;
    @OneToMany(mappedBy = "idUsuarioRemitente")
    private List<Tramitacion> tramitacionList2;
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    @ManyToOne
    private Rol idRol;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(String idTheme) {
        this.idTheme = idTheme;
    }

    public List<Audita> getAuditaList() {
        return auditaList;
    }

    public void setAuditaList(List<Audita> auditaList) {
        this.auditaList = auditaList;
    }

    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    public List<Tramitacion> getTramitacionList() {
        return tramitacionList;
    }

    public void setTramitacionList(List<Tramitacion> tramitacionList) {
        this.tramitacionList = tramitacionList;
    }

    public List<Tramitacion> getTramitacionList1() {
        return tramitacionList1;
    }

    public void setTramitacionList1(List<Tramitacion> tramitacionList1) {
        this.tramitacionList1 = tramitacionList1;
    }

    public List<Tramitacion> getTramitacionList2() {
        return tramitacionList2;
    }

    public void setTramitacionList2(List<Tramitacion> tramitacionList2) {
        this.tramitacionList2 = tramitacionList2;
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
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sedra3.modelo.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
