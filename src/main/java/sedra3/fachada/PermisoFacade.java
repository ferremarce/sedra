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

/**
 *
 * @author jmferreira
 */
@Stateless
public class PermisoFacade extends AbstractFacade<Permiso> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoFacade() {
        super(Permiso.class);
    }

    public List<Permiso> getAllPermiso() {
        Query q = em.createQuery("SELECT a FROM Permiso a ORDER BY a.nivel");
        List<Permiso> tr = q.getResultList();
        return tr;

    }
}
