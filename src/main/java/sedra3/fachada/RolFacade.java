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
import sedra3.modelo.Rol;

/**
 *
 * @author jmferreira
 */
@Stateless
public class RolFacade extends AbstractFacade<Rol> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    public List<Rol> getAllRol(String criterio) {
        Query q = em.createQuery("SELECT a FROM Rol a WHERE UPPER(a.descripcionRol) LIKE :xCriterio ORDER BY a.idRol");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Rol> tr = q.getResultList();
        return tr;
    }

    public List<Permiso> getPermisoRol(Rol u) {
        Query q = em.createQuery("SELECT a FROM Permiso a WHERE a.idPermiso IN (SELECT DISTINCT b.idPermiso.idPermiso FROM PermisoRol b WHERE b.idRol.idRol=:xIdRol) ORDER BY a.nivel");
        q.setParameter("xIdRol", u.getIdRol());
        List<Permiso> tr = q.getResultList();
        return tr;
    }

}
