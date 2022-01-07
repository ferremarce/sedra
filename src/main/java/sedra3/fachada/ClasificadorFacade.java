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
import sedra3.modelo.Clasificador;

/**
 *
 * @author jmferreira
 */
@Stateless
public class ClasificadorFacade extends AbstractFacade<Clasificador> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClasificadorFacade() {
        super(Clasificador.class);
    }

    public List<Clasificador> getAllClasificador() {
        Query q = em.createQuery("SELECT a FROM Clasificador a ORDER BY a.denominacionClasificador");
        List<Clasificador> tr = q.getResultList();
        return tr;

    }

    public List<Clasificador> getAllClasificador(String valor) {
        Query q = em.createQuery("SELECT a FROM Clasificador a WHERE CONCAT(a.idClasificador,'') LIKE :idStr ORDER BY a.idClasificador ");
        q.setParameter("idStr", valor);
        List<Clasificador> tr = q.getResultList();
        return tr;

    }

    public List<Clasificador> getAllClasificadorPadres() {
        Query q = em.createQuery("SELECT a FROM Clasificador a WHERE a.padre=0 ORDER BY a.denominacionClasificador");
        List<Clasificador> tr = q.getResultList();
        return tr;

    }

    public List<Clasificador> getHijos(Integer idCla) {
        Query q = em.createQuery("SELECT a FROM Clasificador a WHERE a.padre=:xIdCla ORDER BY a.denominacionClasificador");
        q.setParameter("xIdCla", idCla);
        List<Clasificador> tr = q.getResultList();
        return tr;
    }

    public Clasificador getFirstClasificador() {
        Query q = em.createQuery("SELECT a FROM Clasificador a WHERE a.padre=0 ORDER BY a.denominacionClasificador");
        q.setMaxResults(1);
        List<Clasificador> tr = q.getResultList();
        if (tr.size() > 0) {
            return tr.get(0);
        } else {
            return null;
        }
    }
//    public Clasificador findDefaultClasificador(){
//        this.find(em)
//    }
}
