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
import sedra3.modelo.NotaSalida;

/**
 *
 * @author jmferreira
 */
@Stateless
public class NotaSalidaFacade extends AbstractFacade<NotaSalida> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotaSalidaFacade() {
        super(NotaSalida.class);
    }

    public List<NotaSalida> getAllNotaSalidaPlanArchivo(Integer clasificador) {
        Query q = em.createQuery("SELECT a FROM NotaSalida a WHERE a.idClasificador.idClasificador=:xCriterio");
        q.setParameter("xCriterio", clasificador);
        List<NotaSalida> tr = q.getResultList();
        return tr;
    }

    public List<NotaSalida> getAllNotaSalida(String criterio) {
        Query q = em.createQuery("SELECT a FROM NotaSalida a "
                + "WHERE (UPPER(a.numeroSalida) LIKE :xCriterio OR UPPER(a.numeroStr) LIKE :xCriterio) "
                + "OR a.idNota IN (SELECT d.idNota.idNota FROM DetalleNotaSalida d WHERE UPPER(d.idDocumento.asunto) LIKE :xCriterio OR UPPER(d.idDocumento.nroEntrada) LIKE :xCriterio) "
                + "OR a.referencia LIKE :xCriterio "
                + "ORDER BY a.numeroSalida,a.numeroStr");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<NotaSalida> tr = q.getResultList();
        return tr;
    }

    public List<NotaSalida> getAllNotaSalida(String criterio, Integer idTipo) {
        Query q = em.createQuery("SELECT a FROM NotaSalida a WHERE (UPPER(a.numeroSalida) LIKE :xCriterio OR UPPER(a.numeroStr) LIKE :xCriterio) AND a.idTipoNota.idTipoNota=:xIdTipo ORDER BY a.numeroSalida,a.numeroStr");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
            q.setParameter("xIdTipo", idTipo);
        } else {
            q.setParameter("xCriterio", "123456");
            q.setParameter("xIdTipo", idTipo);
        }
        List<NotaSalida> tr = q.getResultList();
        return tr;

    }

}
