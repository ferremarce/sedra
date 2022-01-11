/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sedra.modelo.DocumentoAdjunto;

/**
 *
 * @author jmferreira
 */
@Stateless
public class DocumentoAdjuntoFacade extends AbstractFacade<DocumentoAdjunto> {

    @PersistenceContext(unitName = "sedra-3.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoAdjuntoFacade() {
        super(DocumentoAdjunto.class);
    }
    
}
