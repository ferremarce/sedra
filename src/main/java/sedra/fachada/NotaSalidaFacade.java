/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.fachada;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sedra.modelo.NotaSalida;

/**
 *
 * @author jmferreira
 */
@Stateless
public class NotaSalidaFacade extends AbstractFacade<NotaSalida> {

    @PersistenceContext(unitName = "sedra-3.0-SNAPSHOTPU")
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
                //                + "OR a.idNota IN (SELECT d.idNota.idNota FROM DetalleNotaSalida d WHERE UPPER(d.idDocumento.asunto) LIKE :xCriterio OR UPPER(d.idDocumento.numeroExpediente) LIKE :xCriterio) "
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

    public List<NotaSalida> getAllNotaSalida(String criterio, Integer idTipo, Date fechaDesde, Date fechahasta) {
        Query q;
        if (fechaDesde != null && fechahasta != null) {
            q = em.createQuery("SELECT a FROM NotaSalida a WHERE a.idTipoNota.idTipoNota=:xIdTipo "
                    + "AND (UPPER(a.numeroSalida) LIKE :xCriterio OR UPPER(a.numeroStr) LIKE :xCriterio) "
                    + "AND a IN (SELECT DISTINCT b.idNota FROM DetalleNotaSalida b WHERE b.idDocumento.fechaDocumento BETWEEN :xFechaInicio AND :xFechaFin) "
                    + "ORDER BY a.numeroSalida, a.numeroStr");
            q.setParameter("xFechaInicio", fechaDesde);
            q.setParameter("xFechaFin", fechahasta);
        } else {
            q = em.createQuery("SELECT a FROM NotaSalida a WHERE a.idTipoNota.idTipoNota=:xIdTipo "
                    + "AND (UPPER(a.numeroSalida) LIKE :xCriterio OR UPPER(a.numeroStr) LIKE :xCriterio) "
                    + "ORDER BY a.numeroSalida, a.numeroStr");
        }
        if (criterio.isBlank()) {
            q.setParameter("xCriterio", "xxxxxxxxxxxxxxxxxxxxx");
        } else {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        }
        q.setParameter("xIdTipo", idTipo);

        List<NotaSalida> tr = q.getResultList();
        return tr;

    }

}
