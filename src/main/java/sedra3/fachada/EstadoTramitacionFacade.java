/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra3.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sedra3.modelo.EstadoTramitacion;

/**
 *
 * @author jmferreira
 */
@Stateless
public class EstadoTramitacionFacade extends AbstractFacade<EstadoTramitacion> {

    @PersistenceContext(unitName = "sedra3.org_sedra3_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoTramitacionFacade() {
        super(EstadoTramitacion.class);
    }
    
}
