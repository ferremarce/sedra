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
import sedra3.modelo.Documento;

/**
 *
 * @author jmferreira
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoFacade() {
        super(Documento.class);
    }
    public List<Documento> getAllDocumento(String criterio) {
        Query q = em.createQuery("SELECT a FROM Documento a "
                + "WHERE UPPER(a.asunto) LIKE :xCriterio OR UPPER(a.nroEntrada) LIKE :xCriterio "
//                + "OR a.idDocumento IN (SELECT d.idDocumento.idDocumento FROM DetalleNotaSalida d WHERE UPPER(d.idNota.numeroSalida) LIKE :xCriterio OR UPPER(d.idNota.numeroSTR) LIKE :xCriterio) "
                + "ORDER BY a.idDocumento");
        if (criterio.compareTo("") != 0) {
            q.setParameter("xCriterio", "%" + criterio.toUpperCase() + "%");
        } else {
            q.setParameter("xCriterio", "123456");
        }
        List<Documento> tr = q.getResultList();
        return tr;

    }
    
}
