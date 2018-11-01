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
import sedra3.modelo.Tramitacion;

/**
 *
 * @author jmferreira
 */
@Stateless
public class TramitacionFacade extends AbstractFacade<Tramitacion> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TramitacionFacade() {
        super(Tramitacion.class);
    }

    public List<Tramitacion> getAllTramitacionPendientes(Integer idRol, String criterio, Integer estado) {
        Query q = em.createQuery("SELECT a FROM Tramitacion a WHERE a.idRol.idRol=:xIdRol AND a.idEstado.idEstado=:xEstado AND a.idDocumento.cerrado=:xCerrado AND (UPPER(a.idDocumento.asunto) LIKE :xCriterio OR UPPER(a.idDocumento.nroEntrada) LIKE :xCriterio) ORDER BY a.idTramitacion");
        q.setParameter("xIdRol", idRol);
        q.setParameter("xCerrado", Boolean.FALSE);
        q.setParameter("xEstado", estado);
        //q.setMaxResults(100);
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Tramitacion> tr = q.getResultList();
        return tr;

    }
}
