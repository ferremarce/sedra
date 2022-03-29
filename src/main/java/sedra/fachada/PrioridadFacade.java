/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sedra.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sedra.modelo.Prioridad;

/**
 *
 * @author jmferreira
 */
@Stateless
public class PrioridadFacade extends AbstractFacade<Prioridad> {

    @PersistenceContext(unitName = "sedra-3.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrioridadFacade() {
        super(Prioridad.class);
    }
    
}
