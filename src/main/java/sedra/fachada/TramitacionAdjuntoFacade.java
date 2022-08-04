/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sedra.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sedra.modelo.TramitacionAdjunto;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Stateless
public class TramitacionAdjuntoFacade extends AbstractFacade<TramitacionAdjunto> {

    @PersistenceContext(unitName = "sedra-3.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TramitacionAdjuntoFacade() {
        super(TramitacionAdjunto.class);
    }

    public void remove(TramitacionAdjunto ta) {
        String file = ta.toPathFileSystem();
        this.em.remove(this.em.merge(ta));
        JSFutil.deleteFileFromDisk(file);
    }
}
