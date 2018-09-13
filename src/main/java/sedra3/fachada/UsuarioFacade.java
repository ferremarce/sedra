/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.fachada;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sedra3.modelo.Permiso;
import sedra3.modelo.Usuario;

/**
 *
 * @author jmferreira
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario getUsuario(String cuenta) {
        Query q = em.createQuery("SELECT a FROM Usuario a WHERE a.cuenta=:xCuenta");
        q.setParameter("xCuenta", cuenta);
        try {
            Usuario tr = (Usuario) q.getSingleResult();
            return tr;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Permiso> getPermisoUsuario(Usuario u) {
        Query q = em.createQuery("SELECT a FROM Permiso a WHERE a.idPermiso IN (SELECT b.idPermiso.idPermiso FROM PermisoRol b WHERE b.idRol.idRol=:xIdRol) ORDER BY a.nivel");
        q.setParameter("xIdRol", u.getIdRol().getIdRol());
        List<Permiso> tr = q.getResultList();
        return tr;
    }

}
